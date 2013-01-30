package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticString;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticStringHandler extends
		AbstractDataHandler<ProbabilisticString> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticStringHandler.types.add("ProbabilisticString");
	}
	private static Charset charset = Charset.forName("UTF-8");
	private static CharsetEncoder encoder = charset.newEncoder();
	private static CharsetDecoder decoder = charset.newDecoder();

	@Override
	public IDataHandler<ProbabilisticString> getInstance(final SDFSchema schema) {
		return new ProbabilisticStringHandler();
	}

	public ProbabilisticStringHandler() {
		super();
	}

	@Override
	public ProbabilisticString readData(final ObjectInputStream inputStream)
			throws IOException {
		final int length = inputStream.readInt();
		final Map<String, Double> values = new HashMap<String, Double>();
		for (int i = 0; i < length; i++) {
			try {
				String value = (String) inputStream.readObject();
				final Double probability = inputStream.readDouble();
				values.put(value, probability);
			} catch (ClassNotFoundException e) {
				throw new IOException(e);
			}
		}
		return new ProbabilisticString(values);
	}

	@Override
	public ProbabilisticString readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<String, Double> values = new HashMap<String, Double>();
		for (int i = 0; i < discreteValues.length; i++) {
			final String[] discreteValue = discreteValues[i].split(":");
			values.put(discreteValue[0], Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticString(values);
	}

	@Override
	public ProbabilisticString readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<String, Double> values = new HashMap<String, Double>();
		for (int i = 0; i < length; i++) {
			try {
				int stringLength = buffer.getInt();
				int limit = buffer.limit();
				buffer.limit(buffer.position() + stringLength);
				String value = decoder.decode(buffer).toString();
				buffer.limit(limit);
				final Double probability = buffer.getDouble();
				values.put(value, probability);
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
		}
		return new ProbabilisticString(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticString values = (ProbabilisticString) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<String, Double> value : values.getValues().entrySet()) {
			try {
				ByteBuffer encodedValue = encoder.encode(CharBuffer.wrap(value
						.getKey()));
				buffer.putInt(encodedValue.remaining());
				buffer.put(encodedValue);
				buffer.putDouble(value.getValue());
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticDoubleHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		int size = 0;
		final ProbabilisticString values = (ProbabilisticString) attribute;
		for (final Entry<String, Double> value : values.getValues().entrySet()) {
			try {
				ByteBuffer encodedValue = encoder.encode(CharBuffer.wrap(value
						.getKey()));
				size += encodedValue.remaining();
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
		}
		return (((ProbabilisticString) attribute).getValues().size()
				* Double.SIZE + size) / 8;
	}

}