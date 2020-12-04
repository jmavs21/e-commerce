import React, { useEffect, useState } from 'react';
import { FlatList, StyleSheet } from 'react-native';
import { Alert } from 'react-native';

import Card from '../components/Card';
import defaultStyles from '../config/styles';
import listingsApi from '../api/listings';
import Screen from '../components/Screen';
import AppText from '../components/Text';
import Button from '../components/Button';
import ActivityIndicator from '../components/ActivityIndicator';
import useApi from '../hooks/useApi';

function MyListingsScreen({ navigation }) {
  const {
    data: listings,
    setData: setListings,
    error,
    loading,
    request: loadListings,
  } = useApi(listingsApi.getMyListings);

  const deleteListing = useApi(listingsApi.deleteListing);

  useEffect(() => {
    loadListings();
  }, []);

  const deleteMyListing = (listing) => {
    Alert.alert('Delete', 'Do you want to delete this listing?', [
      { text: 'No' },
      {
        text: 'Yes',
        onPress: async () => {
          const originalListings = listings;
          setListings(listings.filter((item) => item.id !== listing.id));
          const result = await deleteListing.request(listing.id);
          if (!result.ok) {
            setListings(originalListings);
            return Alert.alert(
              'Error',
              'Could not delete listing, please retry later.'
            );
          }
        },
      },
    ]);
  };

  return (
    <>
      <ActivityIndicator visible={loading} />
      <Screen style={styles.screen}>
        {error && (
          <>
            <AppText>Couldn't retrieve your listings.</AppText>
            <Button title="Retry" onPress={loadListings} />
          </>
        )}
        <FlatList
          data={listings}
          keyExtractor={(listings) => listings.id.toString()}
          renderItem={({ item }) => (
            <Card
              title={item.title}
              subTitle={'$' + item.price}
              imageUrl={item.images[0].url}
              onPress={() => deleteMyListing(item)}
              thumbnailUrl={item.images[0].thumbnailUrl}
            />
          )}
          refreshing={loading}
          onRefresh={loadListings}
        />
      </Screen>
    </>
  );
}

const styles = StyleSheet.create({
  screen: {
    padding: 20,
    backgroundColor: defaultStyles.colors.light,
  },
});

export default MyListingsScreen;
