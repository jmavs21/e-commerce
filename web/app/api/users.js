import client from './client';

const endpoint = '/users';

const register = (userInfo) => client.post(endpoint, userInfo);

const getUserById = (userId) => client.get(endpoint + '/' + userId);

export default { register, getUserById };
