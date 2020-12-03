package com.ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.jackson.JsonNodeValueReader;

import com.ecommerce.model.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DtoMapperTest {

	private ModelMapper modelMapper = new ModelMapper();

	@Test
	void when_given_location_OK() throws IOException {
		modelMapper.getConfiguration().addValueReader(new JsonNodeValueReader());
		JsonNode orderNode = new ObjectMapper().readTree("{\"latitude\":37.33233141,\"longitude\":-122.0312186}");
		Location expected = new Location();
		expected.setLatitude(37.33233141F);
		expected.setLongitude(-122.0312186F);
		assertEquals(expected, modelMapper.map(orderNode, Location.class));
	}
}
