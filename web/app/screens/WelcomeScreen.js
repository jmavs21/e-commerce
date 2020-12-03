import React from 'react';
import { ImageBackground, StyleSheet, View, Image, Text } from 'react-native';

import Button from '../components/Button';
import routes from '../navigation/routes';

import colors from '../config/colors';

function WelcomeScreen({ navigation }) {
  return (
    <ImageBackground
      blurRadius={10}
      style={styles.background}
      source={require('../assets/background.jpg')}
    >
      <View style={styles.logoContainer}>
        <Text style={styles.tagline}>E-commerce</Text>
      </View>
      <View style={styles.buttonsContainer}>
        <Button
          title="Login"
          onPress={() => navigation.navigate(routes.LOGIN)}
        />
        <Button
          title="Register"
          color="secondary"
          onPress={() => navigation.navigate(routes.REGISTER)}
        />
      </View>
    </ImageBackground>
  );
}

const styles = StyleSheet.create({
  background: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  buttonsContainer: {
    padding: 20,
    width: '100%',
  },
  logo: { width: 100, height: 100 },
  logoContainer: { position: 'absolute', top: 70, alignItems: 'center' },
  tagline: {
    color: colors.primary,
    fontSize: 55,
    fontWeight: '600',
    paddingVertical: 20,
  },
});

export default WelcomeScreen;
