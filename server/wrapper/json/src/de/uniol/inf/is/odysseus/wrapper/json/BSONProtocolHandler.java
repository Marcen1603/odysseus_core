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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class BSONProtocolHandler extends AbstractJSONProtocolHandler {
	
	public static final String NAME = "BSON";
	
	public BSONProtocolHandler() {
		this.init();
	}
	

	public BSONProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		super(direction,access,dataHandler);
		this.init();
	}
	
	private void init() {
		mapper = new ObjectMapper(new BsonFactory());
		this.name = "BSON";
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
	public void process(ByteBuffer message) {
		ArrayList<KeyValueObject<? extends IMetaAttribute>> objects = new ArrayList<KeyValueObject<? extends IMetaAttribute>>();
		try {
			JsonNode rootNode = mapper.readValue(message.array(), JsonNode.class);
			if(rootNode.isArray()) {
				for(JsonNode node: rootNode) {
					objects.add(getDataHandler().readData(node.toString()));
				}
			} else if(rootNode.isObject()) {
				objects.add(getDataHandler().readData(rootNode.toString()));
			}
			super.process(objects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> kvObject)
			throws IOException {
		this.getTransportHandler().send(this.getDataHandler().writeBSONData(kvObject));
	}
}
