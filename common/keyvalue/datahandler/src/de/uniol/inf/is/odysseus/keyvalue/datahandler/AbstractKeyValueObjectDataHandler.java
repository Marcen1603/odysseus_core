package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.undercouch.bson4jackson.BsonFactory;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public abstract class AbstractKeyValueObjectDataHandler<T extends KeyValueObject<? extends IMetaAttribute>>
		extends AbstractStreamObjectDataHandler<T> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractKeyValueObjectDataHandler.class);

	private Charset charset = Charset.forName("UTF-8");
	// private CharsetDecoder decoder = charset.newDecoder();
	private CharsetEncoder encoder = charset.newEncoder();

	static protected ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
	static protected ObjectMapper bsonMapper = new ObjectMapper(new BsonFactory());

	static {
		jsonMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		bsonMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		jsonMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		bsonMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	}

	public AbstractKeyValueObjectDataHandler() {
		super(null);
	}

	@Override
	public int memSize(Object attribute, boolean handleMetaData) {
		StringBuilder builder = new StringBuilder();
		writeJSONData(builder, attribute);
		ByteBuffer charBuffer;
		try {
			charBuffer = encoder.encode(CharBuffer.wrap(builder.toString()));
			return charBuffer.limit();
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return 0;
	}

	@Override
	public void writeData(ByteBuffer buffer, T object, boolean handleMetaData) {
		writeData(buffer, (Object)object, handleMetaData);
	}

	@Override
	public void writeData(List<String> output, Object data, boolean handleMetaData, WriteOptions options) {
		StringBuilder string = new StringBuilder();
		writeJSONData(string, data);
		output.add(string.toString());
	}

	@Override
	public void writeData(StringBuilder string, Object data, boolean handleMetaData) {
		writeJSONData(string, data);
	}

	@Override
	public T readData(ByteBuffer buffer, boolean handleMetaData) {
		// TODO: Find a way to handle metadata in key value
		try {
			if (buffer.remaining() > 0) {
				CharBuffer decoded = Charset.forName("UTF-8").newDecoder().decode(buffer);
				return jsonStringToKVO(decoded.toString());
			}
		} catch (CharacterCodingException e) {
			LOG.error("Could not decode data with KeyValueObject handler", e);
		}
		return null;
	}

	@Override
	public T readData(InputStream inputStream, boolean handleMetaData) {
		// TODO: Find a way to handle metadata in key value
		try {
			byte[] buffer = new byte[inputStream.available()];
			for (int i = 0; inputStream.available() > 0; i++) {
				buffer[i] = (byte) inputStream.read();
			}

			CharBuffer decoded = Charset.forName("UTF-8").newDecoder().decode(ByteBuffer.wrap(buffer));
			return jsonStringToKVO(decoded.toString());
		} catch (IOException e) {
			LOG.error("Could not decode data with KeyValueObject handler", e);
		}
		return null;
	}

	@Override
	public T readData(Iterator<String> message, boolean handleMetaData) {
		// TODO: Find a way to handle metadata in key value
		return jsonStringToKVO(message.next());
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data, boolean handleMetaData) {
		StringBuilder builder = new StringBuilder();
		writeJSONData(builder, data);
		ByteBuffer charBuffer;
		try {
			charBuffer = encoder.encode(CharBuffer.wrap(builder.toString()));
			buffer.put(charBuffer);
			return;
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}

		// TODO: Find a way to handle metadata in key value
		throw new IllegalArgumentException(
				"writeData() is not implemented in this DataHandler! writeJSONData should be used instead.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeJSONData(StringBuilder string, Object data) {
		try {
			if (data instanceof KeyValueObject<?>) {
				string.append(jsonMapper.writer().writeValueAsString(((T) data).getAttributesAsNestedMap()));
			}
			// System.out.println("writeJSONData: " + string.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] writeBSONData(Object data) {
		try {
			return bsonMapper.writer().writeValueAsBytes(((T) data).getAttributes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private T jsonStringToKVO(String json) {
		return (T) ((KeyValueObjectDataHandler) this).jsonStringToKVO(json);
	}
}
