package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
		return getTransportHandler().getInputStream().available() > 0;
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		// ReadInput
		JSONObject object = null;
		KeyValueObject<IMetaAttribute> out = new KeyValueObject<>();
		try {
			object = new JSONObject(new JSONTokener(reader));
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

	static public void convert(JSONObject object,
			KeyValueObject<IMetaAttribute> out,
			String path) throws IOException, JSONException {
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
				JSONArray elements = (JSONArray)value;
				convert(elements, out, key, path);
			} else {
				out.setAttribute(path + key, value);
			}
		}
	}
	
	static public void convert(JSONArray elements, KeyValueObject<IMetaAttribute> out, String key, String path) throws IOException, JSONException{
		for (int i=0;i<elements.length();i++){
			Object arrayElement = elements.get(i);
			StringBuffer newPath = new StringBuffer(path);
			newPath.append(key).append("[").append(i).append("]").append(".");
			if (arrayElement instanceof JSONObject){
				convert((JSONObject) arrayElement,out,newPath.toString());
			}else if (arrayElement instanceof JSONArray){
				convert((JSONArray) arrayElement, out, key, newPath.toString());
			}else{
				out.setAttribute(newPath.toString(), arrayElement);
			}
		}
	}

	@Override
	public void write(byte[] message) throws IOException {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			Map<String, String> options, ITransportHandler transportHandler,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler,
			ITransferHandler<KeyValueObject<? extends IMetaAttribute>> transfer) {
		JSONProtocolHandler instance = new JSONProtocolHandler();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);

		instance.setTransportHandler(transportHandler);
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
		convert(object, out,  "");
		System.out.println(out);
	}
}
