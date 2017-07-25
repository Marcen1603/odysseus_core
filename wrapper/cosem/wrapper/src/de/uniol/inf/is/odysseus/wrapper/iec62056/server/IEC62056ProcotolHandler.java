package de.uniol.inf.is.odysseus.wrapper.iec62056.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.iec62056.parser.AbstractCOSEMParser;
import de.uniol.inf.is.odysseus.wrapper.iec62056.parser.JSONCOSEMParser;

/**
 * 
 * @author Jens Plümer
 *
 */
public class IEC62056ProcotolHandler extends AbstractProtocolHandler<IStreamObject<? extends IMetaAttribute>> {

	private static final Logger logger = LoggerFactory.getLogger(IEC62056ProcotolHandler.class.getSimpleName());
	
	private final String XML_TYPE = "xml";
	private final String JSON_TYPE = "json";
	
	private AbstractCOSEMParser parser;
	private boolean isDone;
	private String type;
	private String smgwDeviceName;
	private String[] tokens;
	
	public IEC62056ProcotolHandler() {
		super();
	}

	public IEC62056ProcotolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		String type = options.getValue("type");
		if (type != null) {
			this.type = type;
		} else {
			this.type = "xml";
		}
		@SuppressWarnings("unchecked")
		List<String> tokens = ((List<String>) options.getValue("tokens"));
		switch (type) {
		case (JSON_TYPE):
			smgwDeviceName = options.getValue("smgwDeviceName");
			if (smgwDeviceName != null) {
				if (tokens != null) {
					if (getSchema().getAttributes().stream()
							.anyMatch(e -> e.getAttributeName().equals(smgwDeviceName))) {
						tokens.add(smgwDeviceName);
					} else {
						throw new IllegalArgumentException("missing " + smgwDeviceName + " in schema definition");
					}
				}
			} else {
				throw new IllegalArgumentException("missing option from access operator: smgwDeviceName");
			}
		case (XML_TYPE):
			break;
		}
		if(tokens != null) {
			this.tokens = tokens.toArray(new String[tokens.size()]);
		}
		logger.info("Initialized " + IEC62056ProcotolHandler.class.getSimpleName() + " with a " + this.type + " parser");
	}

	@Override
	public IProtocolHandler<IStreamObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		return new IEC62056ProcotolHandler(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return "DLMS/COSEM";
	}
	
	private void initParser(byte[] message) {
		InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(message));
		if (parser == null) {
			switch (type.toLowerCase()) {
			case JSON_TYPE:
				if(tokens == null) {
					List<String> tokenList = new ArrayList<String>();
					if (!getSchema().getAttributes().stream()
							.anyMatch(e -> e.getAttributeName().equals(smgwDeviceName))) {
						throw new IllegalArgumentException("missing " + smgwDeviceName + " in schema definition");
					}
					tokenList.add("logical_name");
					tokenList.add("objects");
					tokenList.add(smgwDeviceName);
					tokens = tokenList.toArray(new String[tokenList.size()]);
				}
				parser = new JSONCOSEMParser(reader, getStringBuilder(), tokens);
				break;
			case XML_TYPE:
			default:
				throw new NotImplementedException("the corresponding XMLCOSEMParser has no implementation yet");
//				parser = new XMLCOSEMParser(reader, getStringBuilder(), tokens);
			}
		}
	}
	
	private StringBuilder getStringBuilder() {
		StringBuilder builder = new StringBuilder("[" + getSchema().getAttributes().get(0).getAttributeName());
		for(int i = 1; i < getSchema().getAttributes().size(); i++) {
			builder.append("|");
			builder.append(getSchema().getAttributes().get(i).getAttributeName());
		}
		builder.append("]");
		return builder;
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
		try {
			InputStream initialStream = getTransportHandler().getInputStream();
			byte[] targetArray = new byte[initialStream.available()];
			initialStream.read(targetArray);
			initParser(targetArray);
		} catch (IllegalArgumentException e) {
			logger.info("Given transport handler has no input stream");
		}
		isDone = false;
		logger.info("connection opened");
	}

	@Override
	public void close() throws IOException {
		terminateParser();
		super.close();
		logger.info("connection closed");
	}

	@Override
	public boolean hasNext() throws IOException {
		if(this.parser != null) {
			return parser.isDone() ? false : true;
		}
		return false;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		terminateParser();
		super.onDisonnect(caller);
		logger.info("disconnected");
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * pull-based processing
	 */
	@Override
	public IStreamObject<? extends IMetaAttribute> getNext() throws IOException {
		return hasNext() ? getDataHandler().readData(parser.parse()): null;
	}
	
	/*
	 * push-based processing
	 */
	@Override
	public void process(String[] message) {
		try {
		initParser(String.join("", message).getBytes());
		getTransfer().transfer(getDataHandler().readData(parser.parse()));
		terminateParser();
		} catch (IllegalArgumentException e) {
			if (!isDone) {
				logger.error("error occurred: " + e.getMessage());
				try {
					close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
}
