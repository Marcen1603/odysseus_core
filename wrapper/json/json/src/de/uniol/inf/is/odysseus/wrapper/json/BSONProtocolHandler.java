package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.undercouch.bson4jackson.BsonFactory;
import de.uniol.inf.is.odysseus.keyvalue.datatype.IBSONWriter;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class BSONProtocolHandler<T extends KeyValueObject<IMetaAttribute>> extends AbstractJSONProtocolHandler<T> {

	public static final String NAME = "BSON";

	public BSONProtocolHandler() {
		this.init_internal();
	}


	public BSONProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,OptionMap optionsMap) {
		super(direction,access,dataHandler,optionsMap);
		if (! (dataHandler instanceof IBSONWriter)){
			throw new IllegalArgumentException("Datahandler "+dataHandler+" cannot be used together with BSONProtocolHandler");
		}
		this.init_internal();
	}

	private void init_internal() {
		mapper = new ObjectMapper(new BsonFactory());
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		this.name = "BSON";
	}

	@Override
	public IProtocolHandler<T> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		BSONProtocolHandler<T> instance = new BSONProtocolHandler<T>(direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public void process(long callerId, ByteBuffer buffer) {
		// TODO: check if callerId is relevant
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
		this.getTransportHandler().send(((IBSONWriter)this.getDataHandler()).writeBSONData(kvObject));
	}
}
