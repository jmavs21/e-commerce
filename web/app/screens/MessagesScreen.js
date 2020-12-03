import React, { useState, useEffect } from 'react';
import { FlatList, StyleSheet, Alert } from 'react-native';

import ActivityIndicator from '../components/ActivityIndicator';
import AppText from '../components/Text';
import Button from '../components/Button';
import ListItem from '../components/lists/ListItem';
import Screen from '../components/Screen';
import ListItemSeparator from '../components/lists/ListItemSeparator';
import ListItemDeleteAction from '../components/lists/ListItemDeleteAction';
import useApi from '../hooks/useApi';
import messagesApi from '../api/messages';

function MessagesScreen(props) {
  const {
    data: messages,
    error,
    loading,
    request: loadMessages,
    setData: setMessages,
  } = useApi(messagesApi.getMessages);

  useEffect(() => {
    loadMessages();
  }, []);

  const [refreshing, setRefreshing] = useState(false);

  const handleDelete = async (message) => {
    const originalMessages = messages;
    setMessages(messages.filter((m) => m.id !== message.id));
    const result = await messagesApi.deleteMessage(message.id);
    if (!result.ok) {
      setMessages(originalMessages);
      return Alert.alert(
        'Error',
        'Could not delete the message, please retry later.'
      );
    }
  };

  return (
    <>
      <ActivityIndicator visible={loading} />
      <Screen>
        {error && (
          <>
            <AppText>Couldn't retrieve the messages.</AppText>
            <Button title="Retry" onPress={loadMessages} />
          </>
        )}
        <FlatList
          data={messages}
          keyExtractor={(message) => message.id.toString()}
          renderItem={({ item }) => (
            <ListItem
              title={item.fromUser.name}
              subTitle={item.content}
              onPress={() => console.log('Message selected=', item)}
              renderRightActions={() => (
                <ListItemDeleteAction onPress={() => handleDelete(item)} />
              )}
            />
          )}
          ItemSeparatorComponent={ListItemSeparator}
          refreshing={refreshing}
          onRefresh={loadMessages}
        />
      </Screen>
    </>
  );
}

const styles = StyleSheet.create({});

export default MessagesScreen;
