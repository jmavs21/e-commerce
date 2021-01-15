package com.ecom.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDtoReq {

  @NotBlank
  @Size(min = 2, max = 50)
  private String name;

  @NotBlank
  @Email
  @Size(min = 5, max = 255)
  private String email;

  @NotBlank
  @Size(min = 3, max = 1024)
  private String password;

  private Boolean isAdmin = false;

  private String expoPushToken = "";

  public UserDtoReq() {}
}
