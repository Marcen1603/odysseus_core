package de.uniol.inf.is.odysseus.client.communication.transformStrategies;

import java.io.DataInputStream;
import java.io.IOException;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class SpatialGeometryTransformStrategy implements ITransformStrategy {

	@Override
	public Object transformBytesToObject(DataInputStream inputStream) throws IOException {

		WKBReader wkbReader = new WKBReader();

		int length = inputStream.readInt();
		byte[] binData = new byte[length];
		inputStream.read(binData);
		try {
			return wkbReader.read(binData);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}