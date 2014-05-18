package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jan Sören Schwarz
 * 
 */
public class KeyValueObjectDataHandler extends AbstractDataHandler<KeyValueObject<?>> {

	static protected List<String> types = new ArrayList<String>();
	static private ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
	static private ObjectMapper bsonMapper = new ObjectMapper(new BsonFactory());

	protected static final Logger LOG = LoggerFactory.getLogger(KeyValueObjectDataHandler.class);
	
	static {
		types.add(SDFDatatype.KEYVALUEOBJECT.getURI());
	}
	
	public KeyValueObjectDataHandler() {
	}
	
	@Override
	public IDataHandler<KeyValueObject<?>> getInstance(SDFSchema schema) {
		return new KeyValueObjectDataHandler();
	}
	
	@Override
	public KeyValueObject<?> readData(ByteBuffer buffer) {
//		parseJSON(buffer);
		try {
			CharBuffer decoded = Charset.forName("UTF-8").newDecoder().decode(buffer);
			return new KeyValueObject<>(jsonStringToMap(decoded.toString()));
		} catch (CharacterCodingException e) {
		} 
		// return null or empty KeyValueObject?
		return new KeyValueObject<>();
	}

	@Override
	public KeyValueObject<?> readData(ObjectInputStream inputStream)
			throws IOException {
		return null;
	}

	@Override
	public KeyValueObject<?> readData(String message) {
		return new KeyValueObject<>(jsonStringToMap(message));
	}
	
	@Override
	public KeyValueObject<?> readData(String[] message) {
		if(message.length == 1) {
			return new KeyValueObject<>(jsonStringToMap(message[0]));			
		} else {
			return null;
		}
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new IllegalArgumentException("writeData() is not implemented in this DataHandler! writeJSONData should be used instead.");
	}
	
	@Override
	public void writeJSONData(StringBuilder string, Object data) {
		try {
			if(data instanceof KeyValueObject<?>) {
				string.append(jsonMapper.writer().writeValueAsString(((KeyValueObject<?>) data).getAttributes()));
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] writeBSONData(Object data) {
		try {
			if(data instanceof KeyValueObject<?>) {
			    return bsonMapper.writer().writeValueAsBytes(((KeyValueObject<?>) data).getAttributes());
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int memSize(Object attribute) {
		return 0;
	}

	@Override
	public Class<?> createsType() {
		return KeyValueObject.class;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> jsonStringToMap(String json) {
		try {
//			LOG.debug("jsonStringToMap: " + json);
			JsonNode rootNode = jsonMapper.reader().readTree(json);		
			if(!rootNode.isObject()) {
				//könnte das wirklich vorkommen?
				rootNode = rootNode.get(0);
			} 
			Map<String, Object> map = jsonMapper.reader().treeToValue(rootNode, Map.class);
			return map;
		} catch (IOException e) {
			LOG.debug(e.getMessage());
			return null;
		}
	}
}
