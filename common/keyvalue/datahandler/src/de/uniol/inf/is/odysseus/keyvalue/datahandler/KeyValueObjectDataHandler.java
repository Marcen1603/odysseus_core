package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.keyvalue.datatype.IBSONWriter;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;

/**
 *
 * @author Jan-Sören Schwarz
 * @author Marco Grawunder
 *
 */

public class KeyValueObjectDataHandler extends AbstractStreamObjectDataHandler<KeyValueObject<?>> implements IBSONWriter {

	protected static List<String> types = new ArrayList<String>();
	protected static final Logger LOG = LoggerFactory.getLogger(KeyValueObjectDataHandler.class);

	static {
		types.add(SDFKeyValueDatatype.KEYVALUEOBJECT.getURI());
	}

	@Override
	public int memSize(Object attribute, boolean handleMetaData) {
		StringBuilder builder = new StringBuilder();
		writeJSONData(builder, attribute, handleMetaData);
		ByteBuffer charBuffer;
		try {
			charBuffer = conversionOptions.getEncoder().encode(CharBuffer.wrap(builder.toString()));
			return charBuffer.limit();
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return 0;
	}

	@Override
	public void writeData(ByteBuffer buffer, KeyValueObject<?> object, boolean handleMetaData) {
		writeData(buffer, (Object)object, handleMetaData);
	}

	@Override
	public void writeData(List<String> output, Object data, boolean handleMetaData, WriteOptions options) {
		StringBuilder string = new StringBuilder();
		writeJSONData(string, data, handleMetaData);
		output.add(string.toString());
	}

	@Override
	public void writeData(StringBuilder string, Object data, boolean handleMetaData) {
		writeJSONData(string, data, handleMetaData);
	}

	@Override
	public KeyValueObject<?> readData(ByteBuffer buffer, boolean handleMetaData) {
		// TODO: Find a way to handle metadata in key value
		try {
			if (buffer.remaining() > 0) {
				CharBuffer decoded = conversionOptions.getDecoder().decode(buffer);
				return (KeyValueObject<?>) KeyValueObject.createInstance(decoded.toString());
			}
		} catch (IOException e) {
			LOG.error("Could not decode data with KeyValueObject handler", e);
		}
		return null;
	}

	@Override
	public KeyValueObject<?> readData(InputStream inputStream, boolean handleMetaData) {
		// TODO: Find a way to handle metadata in key value
		try {
			byte[] buffer = new byte[inputStream.available()];
			for (int i = 0; inputStream.available() > 0; i++) {
				buffer[i] = (byte) inputStream.read();
			}

			CharBuffer decoded = conversionOptions.getDecoder().decode(ByteBuffer.wrap(buffer));
			return (KeyValueObject<?>) KeyValueObject.createInstance(decoded.toString());
		} catch (IOException e) {
			LOG.error("Could not decode data with KeyValueObject handler", e);
		}
		return null;
	}

	@Override
	public KeyValueObject<?> readData(Iterator<String> message, boolean handleMetaData) {
		// TODO: Find a way to handle metadata in key value
		return (KeyValueObject<?>) KeyValueObject.createInstance(message.next());
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data, boolean handleMetaData) {
		StringBuilder builder = new StringBuilder();
		writeJSONData(builder, data, handleMetaData);
		ByteBuffer charBuffer;
		try {
			charBuffer = conversionOptions.getEncoder().encode(CharBuffer.wrap(builder.toString()));
			buffer.put(charBuffer);
			return;
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}

		// TODO: Find a way to handle metadata in key value
		throw new IllegalArgumentException(
				"writeData() is not implemented in this DataHandler! writeJSONData should be used instead.");
	}

	private void writeJSONData(StringBuilder string, Object data, boolean handleMetaData) {
		if (data instanceof KeyValueObject<?>) {
			string.append((((KeyValueObject<?>) data).toString(handleMetaData)));
		}
	}

	@Override
	public byte[] writeBSONData(KeyValueObject<?> kvObject) {
		return kvObject.getAsBSON();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public IDataHandler<KeyValueObject<?>> getInstance(SDFSchema schema) {
		return new KeyValueObjectDataHandler();
	}

	@Override
	public Class<?> createsType() {
		return KeyValueObject.class;
	}

}
