package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class BSONProtocolHandler<T extends KeyValueObject<?>> extends AbstractJSONProtocolHandler<T> {
	
	public static final String NAME = "BSON";
	
	public BSONProtocolHandler() {
		this.init();
	}
	

	public BSONProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IDataHandler<T> dataHandler) {
		super(direction,access,dataHandler);
		this.init();
	}
	
	private void init() {
		mapper = new ObjectMapper(new BsonFactory());
		this.name = "BSON";
	}

	@Override
	public IProtocolHandler<T> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options,
			IDataHandler<T> dataHandler) {
		BSONProtocolHandler<T> instance = new BSONProtocolHandler<T>(direction, access, dataHandler);
		instance.setOptionsMap(options);
		return instance;
	}
	
	@Override
	public void process(ByteBuffer buffer) {
		ArrayList<T> objects = new ArrayList<T>();
		try {
			byte[] message = null;
			if(buffer.hasArray()) {
				message = buffer.array();
			} else {
				message = new byte[buffer.remaining()];
				int i = 0;
				while(buffer.remaining() > 0) {
					message[i++] = buffer.get();
				}
			}
			if(message != null && message.length > 0) {
				JsonNode rootNode = mapper.readValue(message, JsonNode.class);
				if(rootNode.isArray()) {
					for(JsonNode node: rootNode) {
						objects.add(getDataHandler().readData(node.toString()));
					}
				} else if(rootNode.isObject()) {
					objects.add(getDataHandler().readData(rootNode.toString()));
				}
				super.process(objects);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(T kvObject)
			throws IOException {
		this.getTransportHandler().send(this.getDataHandler().writeBSONData(kvObject));
	}
}
