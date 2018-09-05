package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class JSONProtocolHandler<T extends KeyValueObject<IMetaAttribute>>
		extends AbstractJSONProtocolHandler<T> {

	private static final String WRITE_METADATA = "json.write.metadata";

	private boolean writemetadata = false;

	private static final String WRITE_STARTTIMESTAMP_AS = "json.write.starttimestamp";
	private static final String WRITE_ENDTIMESTAMP_AS = "json.write.endtimestamp";

	private String startTimestampKey = null;
	private String endTimestampKey = null;

	protected static final Logger LOG = LoggerFactory
			.getLogger(JSONProtocolHandler.class);

	public JSONProtocolHandler() {
		super();
		init_internal();
	}

	public JSONProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,
			OptionMap options) {
		super(direction, access, dataHandler, options);
		this.init_internal();
	}

	private void init_internal() {
		if (optionsMap != null) {
			if (optionsMap.containsKey(WRITE_METADATA)) {
				writemetadata = Boolean.parseBoolean(optionsMap
						.get(WRITE_METADATA));
			}
			if (optionsMap.containsKey(WRITE_STARTTIMESTAMP_AS)) {
				startTimestampKey = optionsMap.get(WRITE_STARTTIMESTAMP_AS);
			}
			if (optionsMap.containsKey(WRITE_ENDTIMESTAMP_AS)) {
				endTimestampKey = optionsMap.get(WRITE_ENDTIMESTAMP_AS);
			}
		}
		this.mapper = new ObjectMapper(new JsonFactory());
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		this.name = "JSON";
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
		ArrayList<T> objects = new ArrayList<T>();
		try {
			// LOG.debug("process(ByteBuffer message): " + message);
			// Was soll hier passieren falls der ByteBuffer mehrere JSON-Objekte
			// beinhaltet???
			objects.add(getDataHandler().readData(message));
			super.process(objects);
		} catch (Exception e) {
			// e.printStackTrace();
			LOG.debug(e.getMessage());
		}
	}

	@Override
	public void write(T kvObject) throws IOException {
		StringBuilder string = new StringBuilder();
		if (startTimestampKey != null) {
			if (!kvObject.containsKey(startTimestampKey)) {
				kvObject.setAttribute(startTimestampKey,
						((ITimeInterval) kvObject.getMetadata()).getStart()
								.getMainPoint());
			} else {
				LOG.debug("The key "
						+ startTimestampKey
						+ " is already in use and so the starttimestamp can not be stored.");
			}
		}
		if (endTimestampKey != null) {
			if (!kvObject.containsKey(endTimestampKey)) {
				kvObject.setAttribute(endTimestampKey,
						((ITimeInterval) kvObject.getMetadata()).getEnd()
								.getMainPoint());
			} else {
				LOG.debug("The key "
						+ endTimestampKey
						+ " is already in use and so the endtimestamp can not be stored.");
			}
		}

		this.getDataHandler().writeData(string, kvObject, writemetadata);

//		string.append(System.getProperty("line.separator"));
//		CharBuffer charBuffer = CharBuffer.wrap(string);
//		ByteBuffer bBuffer = getCharset().encode(charBuffer);
//		byte[] encodedBytesTmp = bBuffer.array();
//		byte[] encodedBytes = new byte[charBuffer.limit()];
//		System.arraycopy(encodedBytesTmp, 0, encodedBytes, 0,
//				charBuffer.limit());
		this.getTransportHandler().send(string.toString(), true);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		return new JSONProtocolHandler<T>(direction, access, dataHandler,
				options);
	}
}
