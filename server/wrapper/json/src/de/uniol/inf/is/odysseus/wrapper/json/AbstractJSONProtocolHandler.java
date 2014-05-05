package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

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


abstract public class AbstractJSONProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {
	
	protected String name;
	
	protected BufferedReader reader;
	protected ArrayList<String> jsonArray;
	protected ObjectMapper mapper;
	
	protected boolean isDone = false;
	
	public AbstractJSONProtocolHandler() {
	}

	public AbstractJSONProtocolHandler(
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
		if(jsonArray != null && jsonArray.size() > 0) {
			return getDataHandler().readData(jsonArray.remove(0));
		} else {
			return null;
		}
	}
	
	@Override
	public void process(String[] message) {
		getTransfer().transfer(getDataHandler().readData(message));
	}
	
	public void process(ArrayList<KeyValueObject<? extends IMetaAttribute>> objects) {
		if(objects.size() > 0) {
			for(KeyValueObject<? extends IMetaAttribute> object: objects) {
				getTransfer().transfer(object);
			}
		} 
	}

	@Override
	public String getName() {
		return name;
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
