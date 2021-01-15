package com.ecom.web.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecom.domain.listings.Listing;
import com.ecom.domain.listings.ListingService;
import com.ecom.domain.messages.Message;
import com.ecom.domain.messages.MessageService;
import com.ecom.domain.users.User;
import com.ecom.domain.users.UserService;
import com.ecom.web.dto.FromToUser;
import com.ecom.web.dto.MessageDto;
import com.ecom.web.dto.MessageDtoReq;

import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/messages")
public class MessageController {

  private MessageService messageService;
  private UserService userService;
  private ListingService listingService;
  private ModelMapper modelMapper;

  public MessageController(
      MessageService messageService,
      UserService userService,
      ListingService listingService,
      ModelMapper modelMapper) {
    this.messageService = messageService;
    this.userService = userService;
    this.listingService = listingService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public Collection<MessageDto> findAll(Authentication authentication) {
    if (authentication.getPrincipal() == null)
      throw new DataRetrievalFailureException("User not found.");
    User user = (User) authentication.getPrincipal();
    List<Message> messages = messageService.findByToUserId(user.getId(), Sort.by("dateTime"));
    return messages.stream().map(this::asMessageDto).collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}")
  public MessageDto findOne(@PathVariable String id) {
    Message message = getMessage(id);
    return entityToDto(message);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(Authentication authentication, @Valid @RequestBody MessageDtoReq newMessage) {
    if (authentication.getPrincipal() == null)
      throw new DataRetrievalFailureException("User not found.");
    User user = (User) authentication.getPrincipal();
    User targetUser = getUser(getListing(newMessage.getListingId()).getUserId());
    Message message = getNewMessage(newMessage, user, targetUser);
    messageService.save(message);
    sendPushNotifications(newMessage.getMessage(), targetUser);
  }

  @PutMapping("/{id}")
  public MessageDto updateMessage(
      @PathVariable("id") String id, @Valid @RequestBody MessageDto updatedMessage) {
    Message message = getMessage(id);
    message.setListingId(updatedMessage.getListingId());
    return entityToDto(messageService.save(message));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteMessage(@PathVariable("id") String id) {
    getMessage(id);
    messageService.delete(id);
  }

  private MessageDto asMessageDto(Message message) {
    User userFrom = getUser(message.getFromUserId());
    User userTo = getUser(message.getToUserId());
    FromToUser fromUser = new FromToUser();
    FromToUser toUser = new FromToUser();
    fromUser.setId(userFrom.getId());
    fromUser.setName(userFrom.getName());
    toUser.setId(userTo.getId());
    toUser.setName(userTo.getName());
    MessageDto messageDto = new MessageDto();
    messageDto.setId(message.getId());
    messageDto.setListingId(message.getListingId());
    messageDto.setDateTime(message.getDateTime());
    messageDto.setContent(message.getContent());
    messageDto.setFromUser(fromUser);
    messageDto.setToUser(toUser);
    return messageDto;
  }

  private Message getMessage(String id) {
    return messageService
        .findById(id)
        .orElseThrow(
            () ->
                new DataRetrievalFailureException("The message with the given Id was not found."));
  }

  private User getUser(String id) {
    return userService
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  private Listing getListing(String id) {
    return listingService
        .findById(id)
        .orElseThrow(
            () ->
                new DataRetrievalFailureException(
                    "The listing with the given listingId was not found."));
  }

  private Message getNewMessage(MessageDtoReq newMessage, User user, User targetUser) {
    Message message = new Message();
    message.setFromUserId(user.getId());
    message.setToUserId(targetUser.getId());
    message.setListingId(newMessage.getListingId());
    message.setContent(newMessage.getMessage());
    message.setDateTime(new Date().getTime());
    return message;
  }

  private void sendPushNotifications(String newMessage, User targetUser) {
    String receipient = getReceipient(targetUser);
    ExpoPushMessage epm = new ExpoPushMessage(receipient);
    epm.body = newMessage;
    PushClient client = new PushClient();
    List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(Arrays.asList(epm));
    List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();
    for (List<ExpoPushMessage> chunk : chunks) {
      messageRepliesFutures.add(client.sendPushNotificationsAsync(chunk));
    }
    List<ExpoPushTicket> allTickets = new ArrayList<>();
    for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
      try {
        for (ExpoPushTicket ticket : messageReplyFuture.get()) {
          allTickets.add(ticket);
        }
      } catch (InterruptedException e) {
        log.error(e.getMessage());
      } catch (ExecutionException e) {
        log.error(e.getMessage());
      }
    }
    allTickets.forEach(ept -> log.info("status: " + ept.getStatus() + ", id: " + ept.id));
  }

  private String getReceipient(User targetUser) {
    String receipient = targetUser.getExpoPushToken();
    if (!PushClient.isExponentPushToken(receipient))
      throw new DataRetrievalFailureException("Token: " + receipient + " is not a valid token.");
    return receipient;
  }

  protected MessageDto entityToDto(Message entity) {
    return modelMapper.map(entity, MessageDto.class);
  }
}
