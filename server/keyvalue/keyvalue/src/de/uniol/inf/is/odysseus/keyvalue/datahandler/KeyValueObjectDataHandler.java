package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
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
//		System.out.println("readData(ByteBuffer)");
		try {
			CharBuffer decoded = Charset.forName("UTF-8").newDecoder().decode(buffer);
//			System.out.println("buffer: " + buffer.toString());
//			System.out.println("readData(ByteBuffer): " + decoded.toString());
			return new KeyValueObject<>(jsonStringToMap(decoded.toString()));
		} catch (CharacterCodingException e) {
//			System.out.println("buffer: " + buffer.toString());
//			System.out.println("Could not decode data with KVO handler" + e.getMessage());
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
//		System.out.println("message.length: " + message.length);
		if(message.length == 1) {
			return new KeyValueObject<>(jsonStringToMap(message[0]));			
		} else {
			return null;
		}
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		try {
			//BSON vs. JSON...
		    BsonFactory factory = new BsonFactory();
//		    JsonFactory factory = new JsonFactory();
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    JsonGenerator gen = factory.createJsonGenerator(baos);
		    ObjectMapper mapper = new ObjectMapper(new BsonFactory());
		    mapper.writeValue(gen, data);
//		    dataHandlers[i],writeData(baos, data);
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	
	@Override
	public void writeJSONData(StringBuilder string, Object data) {
		if(data instanceof KeyValueObject<?>) {
			try {
			    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
			    string.append(mapper.writeValueAsString(((KeyValueObject<?>) data).getAttributes()));
//			    System.out.println("writeJSONData: " + string);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		} else {
			//?
		}
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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		try {
			JsonNode rootNode = mapper.readValue(json, JsonNode.class);
			if(rootNode.isObject()) {
				return mapper.treeToValue(rootNode, Map.class);
			} else {
				//Was hier?
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}
		return null;
	}
}
