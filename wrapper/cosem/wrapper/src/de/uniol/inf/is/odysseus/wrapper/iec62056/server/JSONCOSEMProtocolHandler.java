package de.uniol.inf.is.odysseus.wrapper.iec62056.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.shared.model.ProcessedData;
import de.uniol.inf.is.odysseus.shared.model.Result;
import de.uniol.inf.is.odysseus.wrapper.iec62056.parser.AbstractCOSEMParser;
import de.uniol.inf.is.odysseus.wrapper.iec62056.parser.JSONCOSEMParser;

public class JSONCOSEMProtocolHandler extends AbstractProtocolHandler<IStreamObject<? extends IMetaAttribute>> {

	private static final Logger log = LoggerFactory.getLogger(JSONCOSEMProtocolHandler.class.getSimpleName());

	private AbstractCOSEMParser<IStreamObject<? extends IMetaAttribute>> parser;
	private String rootNode;
	private String objectType;
	private boolean isDone;
	private boolean isTranportHandlerOpen;

	public JSONCOSEMProtocolHandler() {
		super();
	}
	
	public JSONCOSEMProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		// ACCESS
		if (getDirection().equals(ITransportDirection.IN)) {
			String node = getOptionsMap().get("rootNode");
			if(node != null) {
				rootNode = node;
			} else {
				rootNode = "objects";
			}
		// SENDER	
		} else {
			objectType = getOptionsMap().get("objectType");
			
		}
		
		log.info("Initialized " + JSONCOSEMProtocolHandler.class.getSimpleName());
	}

	@Override
	public IProtocolHandler<IStreamObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		return new JSONCOSEMProtocolHandler(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return "JSON_COSEM";
	}

	private void terminateParser() {
		if (parser != null) {
			parser.close();
			parser = null;
		}
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			try {
				InputStream initialStream = getTransportHandler().getInputStream();
				byte[] targetArray = new byte[initialStream.available()];
				initialStream.read(targetArray);
				initParser(targetArray);
			} catch (IllegalArgumentException e) {
				log.info("Given transport handler has no input stream");
			}
			isDone = false;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initParser(byte[] message) {
		InputStream reader = new ByteArrayInputStream(message);
		parser = new JSONCOSEMParser(reader, getSchema(), rootNode);		
	}

	@Override
	public void close() throws IOException {
		terminateParser();
		super.close();
		log.info("connection closed");
	}

	@Override
	public boolean hasNext() throws IOException {
		if (this.parser != null) {
			return parser.isDone() ? false : true;
		}
		return false;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		super.onConnect(caller);
		log.info("connected");
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		super.onDisonnect(caller);
		terminateParser();
		if (getTransportHandler() != null) {
			try {
				if (getDirection().equals(ITransportDirection.OUT)) {
					getTransportHandler().processOutClose();
				} else if (getDirection().equals(ITransportDirection.IN)) {
//					getTransportHandler().processInClose();
				} else {
					getTransportHandler().processInClose();
					getTransportHandler().processOutClose();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info("disconnected");
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	/*
	 * pull-based processing
	 */
	@Override
	public IStreamObject<? extends IMetaAttribute> getNext() throws IOException {
		return hasNext() ? getDataHandler().readData(parser.parse()) : null;
	}

	/*
	 * push-based processing
	 */
	@Override
	public void process(String[] message) {
		try {
			initParser(String.join("", message).getBytes());
			Tuple<?> tuple = (Tuple<?>) parser.parse();
			
			if(tuple != null) {
				log.info("tuple=" + tuple.toString());
				getTransfer().transfer(getDataHandler().readData(tuple));
			}
			
			terminateParser();
		} catch (IllegalArgumentException | NullPointerException e) {
			log.warn("last message before error: " + Arrays.toString(message));
			log.error("exception occurred: " + e);
			if (!isDone) {
				try {
					close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			throw e;
		}
	}

	@Override
	public void write(IStreamObject<? extends IMetaAttribute> object) throws IOException {
		if (!isTranportHandlerOpen) {
			getTransportHandler().processOutOpen();
			isTranportHandlerOpen = true;
		}
		
		// get the json schema from options
		if (objectType == null) {
			throw new IllegalArgumentException("no valid output schema:" + objectType);
		}
		Result data = ProcessedData.getResultType(objectType);
		if (data == null) {
			close();
			throw new IllegalArgumentException("given output schema is unknown:" + objectType);
		}
		
		long timeintervalStart = -1;
		long timeintervalEnd = -1;
		Long minlStart = null;
		Long maxlStart = null;
		Long lend = null;
		Long latency = null;

		IMetaAttribute meta = object.getMetadata();
		// collect meta data
		if (meta instanceof ITimeInterval) {
			timeintervalStart = ((ITimeInterval) meta).getStart().getMainPoint();
			timeintervalEnd = ((ITimeInterval) meta).getEnd().getMainPoint();
		}
		if (meta instanceof ILatency) {
			minlStart = ((ILatency) meta).getLatencyStart();
			maxlStart = ((ILatency) meta).getMaxLatencyStart();
			lend = ((ILatency) meta).getLatencyEnd();
			latency = ((ILatency) meta).getLatency();
		}

		// set meta data
		data.setTimeintervalStart(timeintervalStart);
		data.setTimeintervalEnd(timeintervalEnd);
		data.setMinlStart(minlStart != null ? minlStart : -1);
		data.setMaxlStart(maxlStart != null ? maxlStart : -1);
		data.setLend(lend != null ? lend : -1);
		data.setLatency(latency != null ? latency : -1);
		// data.setDatarate(datarates);
		Map<String, Object> values = new HashMap<String, Object>();
		for (int i = 0; i < getSchema().size(); i++) {
			values.put(getSchema().get(i).getAttributeName(), ((Tuple<?>) object).getAttribute(i));
		}
		
		// set specific tuple values
		data.setValues(values);
		ProcessedData processed = new ProcessedData();
		processed.setResult(data);
		
		// write data out
		String json = new ObjectMapper().writeValueAsString(processed);
		CharBuffer charBuffer = CharBuffer.wrap(json);
		ByteBuffer bBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] encodedBytesTmp = bBuffer.array();
		byte[] encodedBytes = new byte[charBuffer.limit()];
		System.arraycopy(encodedBytesTmp, 0, encodedBytes, 0, charBuffer.limit());
		this.getTransportHandler().send(encodedBytes);
	}
	
}
