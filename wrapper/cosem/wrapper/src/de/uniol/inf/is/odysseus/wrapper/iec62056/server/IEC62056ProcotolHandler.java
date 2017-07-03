package de.uniol.inf.is.odysseus.wrapper.iec62056.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

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
import de.uniol.inf.is.odysseus.wrapper.iec62056.parser.XMLCOSEMParser;

public class IEC62056ProcotolHandler extends AbstractProtocolHandler<IStreamObject<? extends IMetaAttribute>> {

	private String type;
	private AbstractCOSEMParser<?> parser;
	protected boolean isDone;

	private final String XML_TYPE = "xml";
	private final String JSON_TYPE = "json";

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

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Pullbased
	 */
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		InputStreamReader reader = new InputStreamReader(getTransportHandler().getInputStream());
		if (parser == null) {
			switch (type.toLowerCase()) {
			case JSON_TYPE:
				parser = new JSONCOSEMParser(reader, getSchema());
				break;
			case XML_TYPE:
			default:
				parser = new XMLCOSEMParser(reader, getSchema());
			}
		}
		isDone = false;
	}

	@Override
	public void close() throws IOException {
		if (parser != null) {
			parser.close();
			parser = null;
		}
		super.close();
	}

	@Override
	public boolean hasNext() throws IOException {
		if(this.parser != null) {
			return parser.isDone() ? false : true;
		}
		return false;
	}

	@Override
	public IStreamObject<? extends IMetaAttribute> getNext() throws IOException {
		return hasNext() ? getDataHandler().readData(parser.parse()): null;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	/*
	 * Pushbased
	 */
	@Override
	public void onConnect(ITransportHandler caller) {
		super.onConnect(caller);
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub
		super.onDisonnect(caller);
	}

	@Override
	public void process(InputStream message) {
		// TODO Auto-generated method stub
		super.process(message);
	}

	@Override
	public void process(String[] message) {
		// TODO Auto-generated method stub
		super.process(message);
	}

	@Override
	public void process(IStreamObject<? extends IMetaAttribute> message) {
		// TODO Auto-generated method stub
		super.process(message);
	}

}
