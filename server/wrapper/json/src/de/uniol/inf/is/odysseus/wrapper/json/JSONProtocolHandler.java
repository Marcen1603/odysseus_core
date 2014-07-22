package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class JSONProtocolHandler<T extends KeyValueObject<?>> extends AbstractJSONProtocolHandler<T> {

	protected static final Logger LOG = LoggerFactory.getLogger(JSONProtocolHandler.class);
	
	public JSONProtocolHandler() {
		this.init();
	}	

	public JSONProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IDataHandler<T> dataHandler) {
		super(direction,access,dataHandler);
		this.init();
	}
	
	private void init() {
		this.mapper = new ObjectMapper(new JsonFactory());
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		this.name = "JSON";
	}
	
	@Override
	public void process(ByteBuffer message) {
		ArrayList<T> objects = new ArrayList<T>();
		try {
//			LOG.debug("process(ByteBuffer message): " + message);
			//Was soll hier passieren falls der ByteBuffer mehrere JSON-Objekte beinhaltet???
			objects.add(getDataHandler().readData(message));
			super.process(objects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(T kvObject) throws IOException {
		StringBuilder string = new StringBuilder();
		this.getDataHandler().writeJSONData(string, kvObject);
		string.append(System.getProperty("line.separator"));
		CharBuffer charBuffer = CharBuffer.wrap(string);
		ByteBuffer bBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] encodedBytesTmp = bBuffer.array();
		byte[] encodedBytes = new byte[charBuffer.limit()]; 
		System.arraycopy(encodedBytesTmp, 0, encodedBytes, 0, charBuffer.limit());
		this.getTransportHandler().send(encodedBytes);
	}

	@Override
	public IProtocolHandler<T> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options,
			IDataHandler<T> dataHandler) {
		JSONProtocolHandler<T> instance = new JSONProtocolHandler<T>(direction, access, dataHandler);
		instance.setOptionsMap(options);
		return instance;
	}
}
