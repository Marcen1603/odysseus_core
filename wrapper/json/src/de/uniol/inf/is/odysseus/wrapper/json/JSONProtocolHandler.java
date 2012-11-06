package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class JSONProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	protected BufferedReader reader;

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		reader = new BufferedReader(new InputStreamReader(getTransportHandler()
				.getInputStream()));
	}

	@Override
	public void close() throws IOException {
		reader.close();
		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		return true;
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		// ReadInput
		KeyValueObject<IMetaAttribute> out = null;
		JSONObject object = null;
		String nextObject = readNextObject(reader);
		out = new KeyValueObject<>();
		try {
			object = new JSONObject(new JSONTokener(nextObject));
		} catch (JSONException e) {
			throw new IOException(e);
		}
		// KeyValueObject Creation
		try {
			convert(object, out, "");
		} catch (JSONException e) {
			throw new IOException(e);
		}
		return out;
	}

	static public String readNextObject(BufferedReader reader) {
		StringBuffer object = new StringBuffer();
		int openBrackets = 0;
		boolean foundObject = false;
		try {
			String input = null;
			while ((input = reader.readLine()) != null) {
				char[] line = input.toCharArray();
				object.append(line);
				for (char c : line) {
					if (c == '{') {
						openBrackets++;
						foundObject = true;
					} else if (c == '}') {
						openBrackets--;
					}
				}
				if (openBrackets == 0 && foundObject) {
					return object.toString();
				}
			}
		} catch (IOException e) {

		}
		return null;
	}

	static public void convert(JSONObject object,
			KeyValueObject<IMetaAttribute> out, String path)
			throws IOException, JSONException {
		@SuppressWarnings("unchecked")
		Iterator<String> names = object.keys();
		while (names.hasNext()) {
			String key = names.next();
			Object value = null;
			value = object.get(key);
			if (value instanceof JSONObject) {
				StringBuffer newPath = new StringBuffer(path);
				newPath.append(key).append(".");
				convert((JSONObject) value, out, newPath.toString());
			} else if (value instanceof JSONArray) {
				JSONArray elements = (JSONArray) value;
				convert(elements, out, key, path);
			} else {
				out.setAttribute(path + key, value);
			}
		}
	}

	static public void convert(JSONArray elements,
			KeyValueObject<IMetaAttribute> out, String key, String path)
			throws IOException, JSONException {
		for (int i = 0; i < elements.length(); i++) {
			Object arrayElement = elements.get(i);
			StringBuffer newPath = new StringBuffer(path);
			newPath.append(key).append("[").append(i).append("]").append(".");
			if (arrayElement instanceof JSONObject) {
				convert((JSONObject) arrayElement, out, newPath.toString());
			} else if (arrayElement instanceof JSONArray) {
				convert((JSONArray) arrayElement, out, key, newPath.toString());
			} else {
				out.setAttribute(newPath.toString(), arrayElement);
			}
		}
	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object) throws IOException {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
    public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction,
            IAccessPattern access, Map<String, String> options,
            IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler,
            ITransferHandler<KeyValueObject<? extends IMetaAttribute>> transfer) {
		JSONProtocolHandler instance = new JSONProtocolHandler();
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		return instance;
	}

	@Override
	public String getName() {
		return "JSON";
	}

	public static void main(String[] args) throws JSONException, IOException {
		String test = "{" + "\"firstName\": \"John\","
				+ "\"lastName\": \"Smith\"," + "\"age\": 25,"
				+ "\"address\": {" + "\"streetAddress\": \"21 2nd Street\","
				+ "\"city\": \"New York\"," + "\"state\": \"NY\","
				+ "\"postalCode\": \"10021\"" + "}," + "\"phoneNumber\": ["
				+ "{" + "\"type\": \"home\"," + "\"number\": \"212 555-1234\""
				+ "}," + "{" + "\"type\": \"fax\","
				+ "\"number\": \"646 555-4567\"" + "}" + "]" + "}";
		JSONObject object = new JSONObject(test);
		System.out.println(object);
		KeyValueObject<IMetaAttribute> out = new KeyValueObject<>();
		convert(object, out, "");
		System.out.println(out);
	}




    @Override
    public void onConnect(ITransportHandler caller) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void process(ByteBuffer message) {
        // TODO Auto-generated method stub
        
    }
}
