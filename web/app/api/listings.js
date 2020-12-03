import client from './client';

const endpoint = '/listings';

const getListings = () => client.get(endpoint);

const getMyListings = () => client.get(endpoint + '/mylistings');

const addListing = (listing, onUploadProgress) => {
  const data = new FormData();
  data.append('title', listing.title);
  data.append('price', listing.price);
  data.append('categoryId', listing.category.value);
  data.append('description', listing.description);
  listing.images.forEach((image) =>
    data.append('images', {
      name: 'image' + (Math.floor(Math.random() * 1000000) + 1),
      type: 'image/jpeg',
      uri: image,
    })
  );
  if (listing.location)
    data.append('location', JSON.stringify(listing.location));

  console.log(data);

  return client.post(endpoint, data, {
    onUploadProgress: (progress) =>
      onUploadProgress(progress.loaded / progress.total),
  });
};

const deleteListing = (listingId) => client.delete(endpoint + '/' + listingId);

export default {
  addListing,
  getListings,
  getMyListings,
  deleteListing,
};
