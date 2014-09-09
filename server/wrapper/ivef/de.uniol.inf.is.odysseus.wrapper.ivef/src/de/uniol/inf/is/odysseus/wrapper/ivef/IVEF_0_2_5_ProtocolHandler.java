/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.ivef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.MSG_IVEF;
import de.uniol.inf.is.odysseus.wrapper.ivef.parser.IVEF_0_2_5_Parser;

/**
 * @author msalous
 *
 */
public class IVEF_0_2_5_ProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {
	
	/** Logger for this class. */
	private final Logger LOG = LoggerFactory.getLogger(IVEF_0_2_5_ProtocolHandler.class);
	/** Input stream as BufferedReader (Only in GenericPull: for example: file). */
	protected BufferedReader reader;
	/** IVEF Parser*/
	private IVEF_0_2_5_Parser parser;
	/** IVEF Message */
	private MSG_IVEF ivef;
	/** IVEF object as key-value to be returned for GenericPull. */
	private KeyValueObject<? extends IMetaAttribute> next = null;
	/** Delay on GenericPull. */
	private long delay = 0;
	
	//Constructors
	public IVEF_0_2_5_ProtocolHandler() {
		this.parser = new IVEF_0_2_5_Parser();
		this.ivef = new MSG_IVEF();
	}

	public IVEF_0_2_5_ProtocolHandler(ITransportDirection direction,
			IAccessPattern access,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler, OptionMap options) {
		super(direction, access, dataHandler,options);
		this.parser = new IVEF_0_2_5_Parser();
		this.ivef = new MSG_IVEF();
		if (options.containsKey("delay")){ 
			delay = options.getInt("delay",0);
		}
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		IVEF_0_2_5_ProtocolHandler instance = new IVEF_0_2_5_ProtocolHandler(direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public String getName() {
		return "IVEF_0_2_5";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return (other instanceof IVEF_0_2_5_ProtocolHandler);
	}
	
	//GenericPull stuff:
	//hasNext, getNext open and close methods are used by genericPull...files
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if ( (this.getAccessPattern().equals(IAccessPattern.PULL)) || 
			 (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL) ) )
			 this.reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream() ) );
	}

	@Override
	public void close() throws IOException {
		try {
			if (this.reader != null)
				this.reader.close();
		} 
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		getTransportHandler().close();
	}
	
	@Override
	public boolean hasNext() throws IOException {
		if (!reader.ready()) {
			return false;
		}
		String ivefLine = null;
		while((ivefLine=this.reader.readLine()) != null) {
			if (!(this.parser.parseXMLString(ivefLine, true) ) )
				return false;
			if (this.parser.IVEFPresent()) {
				this.ivef = this.parser.getIVEF();
				this.next = this.ivef.toMap(); 
				this.next.setMetadata("object", ivef);
				this.parser.resetIVEFPresent();
				return true;
			}
		}
		return false;
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
		return next;
	}

	//GenericPush stuff:
	//process is used by genericPush...UDP 
	@Override
	public void process(ByteBuffer message) {
		byte[] m = new byte[message.limit()];
		message.get(m);
		String ivefStr = (new String(m));//.trim();
		if (!(this.parser.parseXMLString(ivefStr, true) ) )
			return;
		if(!this.parser.IVEFPresent())
			return;
		this.parser.resetIVEFPresent();
		this.ivef = this.parser.getIVEF();
		KeyValueObject<? extends IMetaAttribute> map = ivef.toMap();
		map.setMetadata("object", ivef);
		getTransfer().transfer(map);
	}
	
	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object)
			throws IOException {
		try {
			Object obj = object.getMetadata("object");
			if (!(obj instanceof MSG_IVEF)) {
				return;
			}
			this.ivef = (MSG_IVEF) obj;
			getTransportHandler().send(this.ivef.toXML().getBytes());
		} 
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
