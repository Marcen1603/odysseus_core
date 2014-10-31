package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.undercouch.bson4jackson.BsonFactory;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;

public abstract class AbstractKeyValueObjectDataHandler<T extends KeyValueObject<?>>
		extends AbstractDataHandler<T> {

	static protected ObjectMapper jsonMapper = new ObjectMapper(
			new JsonFactory());
	static protected ObjectMapper bsonMapper = new ObjectMapper(
			new BsonFactory());
	static {
		jsonMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,
				true);
		bsonMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,
				true);
	}

	public AbstractKeyValueObjectDataHandler() {
		super(null);
	}

	@Override
	public int memSize(Object attribute) {
		return 0;
	}

	@Override
	public T readData(ByteBuffer buffer) {
		try {
			if (buffer.remaining() > 0) {
				CharBuffer decoded = Charset.forName("UTF-8").newDecoder()
						.decode(buffer);
				return jsonStringToKVO(decoded.toString());
			}
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public T readData(String message) {
		return jsonStringToKVO(message);
	}

	@Override
	public T readData(String[] message) {
		return jsonStringToKVO(message[0]);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new IllegalArgumentException(
				"writeData() is not implemented in this DataHandler! writeJSONData should be used instead.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeJSONData(StringBuilder string, Object data) {
		try {
			if (data instanceof NestedKeyValueObject<?>) {
				string.append(jsonMapper.writer().writeValueAsString(
						((T) data).getAttributes()));
			} else if (data instanceof KeyValueObject<?>) {
				string.append(jsonMapper.writer().writeValueAsString(
						((T) data).getAttributesAsNestedMap()));
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
			if (data instanceof NestedKeyValueObject) {
				byte[] tmp = bsonMapper.writer().writeValueAsBytes(
						((T) data).getAttributes());
				// System.out.println("writeBSONData: " + tmp);
				return tmp;
			} else if (data instanceof KeyValueObject<?>) {
				return bsonMapper.writer().writeValueAsBytes(
						((T) data).getAttributes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private T jsonStringToKVO(String json) {
		if (this instanceof KeyValueObjectDataHandler) {
			return (T) ((KeyValueObjectDataHandler) this).jsonStringToKVO(json);
		} else if (this instanceof NestedKeyValueObjectDataHandler) {
			return (T) ((NestedKeyValueObjectDataHandler) this)
					.jsonStringToKVO(json);
		}
		return null;
	}
}
