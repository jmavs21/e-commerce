import React, { useEffect, useState } from 'react';
import { FlatList, StyleSheet } from 'react-native';
import Card from '../components/Card';
import defaultStyles from '../config/styles';
import listingsApi from '../api/listings';
import routes from '../navigation/routes';
import Screen from '../components/Screen';
import AppText from '../components/Text';
import Button from '../components/Button';
import ActivityIndicator from '../components/ActivityIndicator';
import useApi from '../hooks/useApi';
import useAuth from '../auth/useAuth';

function ListingsScreen({ navigation }) {
  const { user, logOut } = useAuth();
  const { data: listings, error, loading, request: loadListings } = useApi(
    listingsApi.getListings
  );

  useEffect(() => {
    loadListings();
  }, []);

  if (error && listings?.status === 401) logOut();

  const [refreshing, setRefreshing] = useState(false);

  return (
    <>
      <ActivityIndicator visible={loading} />
      <Screen style={styles.screen}>
        {error && (
          <>
            <AppText>Couldn't retrieve the listings.</AppText>
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
              onPress={() => navigation.navigate(routes.LISTING_DETAILS, item)}
              thumbnailUrl={item.images[0].thumbnailUrl}
            />
          )}
          refreshing={refreshing}
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

export default ListingsScreen;
