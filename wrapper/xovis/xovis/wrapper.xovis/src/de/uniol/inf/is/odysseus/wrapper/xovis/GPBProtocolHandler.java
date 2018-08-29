package de.uniol.inf.is.odysseus.wrapper.xovis;

import java.io.IOException;
import java.util.ArrayList;

import com.google.protobuf.CodedInputStream;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.TrackingObj.ObjectPositions;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.XovisEventObj.XovisEvent;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.util.AXovisStreamListener;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.util.XovisEventStreamListener;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.util.XovisObjectStreamListener;
import de.uniol.inf.is.odysseus.wrapper.xovis.physicaloperator.access.XovisTransportHandler;

public class GPBProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	public enum GPBSTREAMTYPE {
		EVENTSTREAM, OBJECTSTREAM
	}

	private AXovisStreamListener listener;

	public static final String name = "GPB";
	// The incoming gpb stream type
	public static final String STREAMTYPE = "streamtype";
	private String streamType = "event";
	// The incoming xovis stream type enum
	private GPBSTREAMTYPE type;
	
	private boolean opened = false;
	
	public GPBProtocolHandler(){
		
	}

	public GPBProtocolHandler(ITransportDirection direction,
			IAccessPattern access,
			IStreamObjectDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler,
			OptionMap options) {
		super(direction, access, dataHandler, options);
		if (!options.containsKey(STREAMTYPE)) {
			streamType = "event";
		} else if (options.containsKey(STREAMTYPE)) {
			streamType = options.get(STREAMTYPE);
		}

		switch (streamType) {
		case "event":
			setType(GPBSTREAMTYPE.EVENTSTREAM);
			break;
		case "object":
			setType(GPBSTREAMTYPE.OBJECTSTREAM);
		}
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options,
			IStreamObjectDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {

		GPBProtocolHandler instance = new GPBProtocolHandler(direction, access,
				dataHandler, options);
		return instance;
	}

	@Override
	public String getName() {
		return "GPB";
	}

	@Override
	public boolean hasNext() throws IOException {
		if (listener.getkVPairs().size() > 0) {
			return true;
		}
		intern_open();
		return false;
	}

	@Override
	public void open() throws java.net.UnknownHostException, IOException {
		intern_open();
	}

	private void intern_open() throws java.net.UnknownHostException,
			IOException {
		if(!opened){
			opened = true;
			switch (getType()) {
			case EVENTSTREAM:
				getTransportHandler().open();
				CodedInputStream cis = CodedInputStream.newInstance(getTransportHandler().getInputStream());
				listener = new XovisEventStreamListener(cis, new ArrayList<XovisEvent>());
				break;
			case OBJECTSTREAM:
				getTransportHandler().open();
				listener = new XovisObjectStreamListener(((XovisTransportHandler)getTransportHandler()).getInputSocket(), new ArrayList<ObjectPositions>());
				break;
			}
			
			listener.start();
			System.err.println("Opened stream");
		} else {
			listener.parseStream();
		}
	}


	private void intern_close() throws IOException {
		try {
			if (listener != null) {
				listener.close();
			}
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			getTransportHandler().close();
		}
		opened= false;
		System.err.println("Closed stream");
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		@SuppressWarnings("unchecked")
		KeyValueObject<IMetaAttribute> out = listener.getkVPairs().get(0);
		// System.out.println(out.toString());
		listener.getkVPairs().remove(0);
		return out;
	}

	@Override
	public void close() throws IOException {
		intern_close();
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		boolean equal = true;
		if(!(other instanceof GPBProtocolHandler) || !this.getName().equals(other.getName()) || getType() != ((GPBProtocolHandler)other).getType()){
			equal = false;
		}
		return equal;
	}

	public GPBSTREAMTYPE getType() {
		return type;
	}

	private void setType(GPBSTREAMTYPE type) {
		this.type = type;
	}
}
