import React from 'react';
import { StyleSheet } from 'react-native';

import Text from '../Text';
import defaultStyles from '../../config/styles';

function ErrorMessage({ error, visible }) {
  if (!visible || !error) return null;
  return <Text style={styles.error}>{error}</Text>;
}

const styles = StyleSheet.create({
  error: {
    color: defaultStyles.colors.danger,
  },
});

export default ErrorMessage;
