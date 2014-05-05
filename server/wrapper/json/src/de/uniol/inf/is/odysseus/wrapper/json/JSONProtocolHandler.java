package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class JSONProtocolHandler extends AbstractJSONProtocolHandler {
	
	public JSONProtocolHandler() {
		this.init();
	}
	

	public JSONProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		super(direction,access,dataHandler);
		this.init();
	}
	
	private void init() {
		this.mapper = new ObjectMapper(new JsonFactory());
		this.name = "JSON";
	}
	
	@Override
	public void process(ByteBuffer message) {
		ArrayList<KeyValueObject<? extends IMetaAttribute>> objects = new ArrayList<KeyValueObject<? extends IMetaAttribute>>();
		try {
			objects.add(getDataHandler().readData(message));
			super.process(objects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> kvObject)
			throws IOException {
		StringBuilder string = new StringBuilder();
		this.getDataHandler().writeJSONData(string, kvObject);
		this.getTransportHandler().send(Charset.forName("UTF-8").encode(CharBuffer.wrap(string.toString().toCharArray())).array());
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
}
