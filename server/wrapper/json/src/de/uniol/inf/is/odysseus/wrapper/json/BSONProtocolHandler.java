package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class BSONProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	protected BufferedReader reader;
	
	protected ObjectMapper mapper;
	protected ArrayList<KeyValueObject<IMetaAttribute>> jsonArray;
	
	private ITransportDirection direction;
	
	public BSONProtocolHandler() {
	}
	

	public BSONProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		super(direction,access,dataHandler);
		this.direction = direction;
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if(this.direction == ITransportDirection.IN) {
			reader = new BufferedReader(new InputStreamReader(getTransportHandler()
					.getInputStream()));
		}
	}

	@Override
	public void close() throws IOException {
		if(reader != null) {
			reader.close();
		}
		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		if(jsonArray != null && jsonArray.size() > 0) {
			KeyValueObject<IMetaAttribute> out = jsonArray.get(0);
			jsonArray.remove(out);
			return out;
		} else {
			if(jsonArray == null) {
				jsonArray = new ArrayList<KeyValueObject<IMetaAttribute>>();
			}
			KeyValueObject<IMetaAttribute> keyValueObject = new KeyValueObject<>();
	
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readValue(reader, JsonNode.class);
				if(rootNode.isArray()) {
					for(JsonNode node: rootNode) {
						keyValueObject = new KeyValueObject<IMetaAttribute>(mapper.treeToValue(node, Map.class));
						jsonArray.add(keyValueObject);
					}
					KeyValueObject<IMetaAttribute> out = jsonArray.get(0);
					jsonArray.remove(out);
					return out;
				} else if(rootNode.isObject()) {
					keyValueObject = new KeyValueObject<IMetaAttribute>(mapper.treeToValue(rootNode, Map.class));
					return keyValueObject;
				} else {
					return null;
				}
			} catch (IOException e) {
//				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object)
			throws IOException {
		ITransportHandler tHandler = this.getTransportHandler();
		OutputStream outputStream = tHandler.getOutputStream();
	    ObjectMapper mapper = new ObjectMapper(new BsonFactory());
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
	    mapper.writeValue(outputStream, object);
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		BSONProtocolHandler instance = new BSONProtocolHandler(direction, access, dataHandler);
		instance.setOptionsMap(options);
		return instance;
	}

	@Override
	public String getName() {
		return "BSON";
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
        if(direction == ITransportDirection.IN) {
        	return ITransportExchangePattern.InOnly;
        } else {
        	return ITransportExchangePattern.OutOnly;
        }
    }
}
