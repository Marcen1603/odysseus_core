package de.uniol.inf.is.odysseus.spatial.geom;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class GeometryDeserializer extends StdDeserializer<GeometryWrapper> {

	private static final long serialVersionUID = 8221707006275531024L;

	protected GeometryDeserializer(Class<GeometryWrapper> vc) {
		super(vc);
	}

	@Override
	public GeometryWrapper deserialize(JsonParser arg0, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		return null;
	}

}
