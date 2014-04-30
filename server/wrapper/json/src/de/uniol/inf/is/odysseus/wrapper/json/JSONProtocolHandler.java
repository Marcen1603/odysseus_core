package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class JSONProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	protected BufferedReader reader;
	
	protected ObjectMapper mapper;
	protected ArrayList<String> jsonArray;
	
	private boolean isDone = false;
	
	public JSONProtocolHandler() {
	}
	

	public JSONProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		super(direction,access,dataHandler);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if(this.getDirection() == ITransportDirection.IN) {
			if(this.getAccess() == IAccessPattern.PULL) {
				reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
			} else {
				//?
			}
		}
	}

	@Override
	public void close() throws IOException {
		if(reader != null) {
			reader.close();
		}
		super.close();
	}

	@Override
	public boolean hasNext() throws IOException {
//		System.out.println("JSON .- hasNext");
		if(jsonArray != null && jsonArray.size() > 0) {
			return true;
		} else {
			try {
				if (!reader.ready()) {
					this.isDone = true;
					return false;
				}
				if(jsonArray == null) {
					jsonArray = new ArrayList<String>();
				}	
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
				JsonNode rootNode = mapper.readValue(reader, JsonNode.class);

				if(rootNode.isArray()) {
					for(JsonNode node: rootNode) {
						jsonArray.add(node.toString());
					}
					return true;
				} else if(rootNode.isObject()) {
					jsonArray.add(rootNode.toString());
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				this.isDone = true;
				return false;
			}
		}
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
//		System.out.println("JSON .- getNext");
		if(jsonArray != null && jsonArray.size() > 0) {
			return getDataHandler().readData(jsonArray.remove(0));
		} else {
			return null;
		}
	}
	
	@Override
	public void process(String[] message) {
//		System.out.println("JSON .- process - String[]");
		getTransfer().transfer(getDataHandler().readData(message));
	}
	
	@Override
	public void process(ByteBuffer message) {
//		System.out.println("JSON .- process - ByteBuffer");
		KeyValueObject<? extends IMetaAttribute> object = getDataHandler().readData(message);
		if(object != null) {
			getTransfer().transfer(object);
		} else {
//			System.out.println("getDataHandler().readData(message) = null");
		}
	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> kvObject)
			throws IOException {
		StringBuilder string = new StringBuilder();
		this.getDataHandler().writeJSONData(string, kvObject);
		Charset charset = Charset.forName("UTF-8");
		this.getTransportHandler().send(charset.encode(CharBuffer.wrap(string.toString().toCharArray())).array());
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		JSONProtocolHandler instance = new JSONProtocolHandler(direction, access, dataHandler);
		instance.setOptionsMap(options);
		return instance;
	}

	@Override
	public String getName() {
		return "JSON";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof JSONProtocolHandler)) {
			return false;
		} else {
			// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
			return true;
		}
	}

    @Override
    public ITransportExchangePattern getExchangePattern() {
        if(this.getDirection() == ITransportDirection.IN) {
        	return ITransportExchangePattern.InOnly;
        } else {
        	return ITransportExchangePattern.OutOnly;
        }
    }
    
    @Override
    public boolean isDone() {
    	return isDone;
    }
}
