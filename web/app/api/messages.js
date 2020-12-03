import client from './client';

const endpoint = '/messages';

const getMessages = () => client.get(endpoint);

const deleteMessage = (messageId) => client.delete(endpoint + '/' + messageId);

const sendMessage = (message, listingId) =>
  client.post(endpoint, { message, listingId });

export default { getMessages, sendMessage, deleteMessage };
