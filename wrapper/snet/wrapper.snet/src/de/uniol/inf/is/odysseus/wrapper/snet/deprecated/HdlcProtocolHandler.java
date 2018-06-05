package de.uniol.inf.is.odysseus.wrapper.snet.deprecated;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.snet.communication.util.ByteUtils;

@SuppressWarnings("all")
public class HdlcProtocolHandler <T extends IStreamObject<? extends IMetaAttribute>>  extends AbstractProtocolHandler <T> {

	protected BufferedInputStream reader;
	
	//definition of the headers to be removed.
	private final byte FB = 0x7e;
	private final byte ADDR = 0x30;
	private final byte FE = 0x7e;
	private final byte CRTL_CRC = 0x00;
	private final byte CRTL_CHECKSUM = 0x01;
	private final byte CRTL_NONE = 0x01;
	
	List<String> hdlcAsStringData = new ArrayList<String>();
	
	//used to store the incoming payload of WSN messages
	byte[] wsnPayload;

	//used to store the extracted payload of hdlc messages
	byte[] hdlcPayload;
	
	byte frameBegin;
	byte frameEnd;
	byte addr;
	byte ctrl;
	byte[] dc = new byte [2];

	
	//used to store the length of the received payload
	int payloadLength;
	int payLoadSize;
	
	public HdlcProtocolHandler() {}
	
	public HdlcProtocolHandler(
			ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,OptionMap optionsMap) {
		super(direction, access,dataHandler,optionsMap);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		HdlcProtocolHandler <T> handler = new HdlcProtocolHandler<T> (direction, access, dataHandler,options);
		return handler;
	}

	@Override
	public String getName() {
		return "HDLC";
	}

	@Override
    public boolean hasNext() throws IOException {
		if (hdlcAsStringData.size() > 0) {
			return true;
		} 
		inter_open();
		return false;

    }
	
	@Override
	public void open() throws java.net.UnknownHostException ,IOException {
		inter_open();
		
		}
	
	
	private void inter_open() throws java.net.UnknownHostException ,IOException{
		getTransportHandler().open();
		 
		 reader = new BufferedInputStream(getTransportHandler()
				.getInputStream());
		 
		try {
			while(reader.available() == 0){
				// We shouldn't poll here anyways ;-)
			}
		payLoadSize =  reader.available();
		wsnPayload = new byte[payLoadSize];
		payloadLength = reader.read(wsnPayload);
				
				if (payloadLength == payLoadSize){
					  				
						String payload = new String(wsnPayload, "UTF-8");
						System.err.println("\n" + payload + "\n");
						// hdlcPayload = getHdlcPayLoad(wsnPayload);
						// String str = new String(hdlcPayload, "UTF-8");
						// hdlcAsStringData.add(payload);   
				
				}
					
				
			

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private byte[] getHdlcPayLoad(byte[] payload) {
		
		frameBegin = payload[0];
		frameEnd =  payload[payload.length-1];
		addr = payload[1];
		ctrl = payload[2];
		if (ctrl == CRTL_CRC || ctrl == CRTL_CHECKSUM ){
			hdlcPayload = ByteUtils.subbytes(payload, 3, payload.length-3);
			dc =          ByteUtils.subbytes(payload, payload.length-3, payload.length-1);
		}else if (ctrl == CRTL_NONE){
			
			hdlcPayload = ByteUtils.subbytes(payload, 3, payload.length-1);	
		}
		if (checkPayload(hdlcPayload,ctrl,dc))
			return hdlcPayload;
		else
			return null; 
	}

		
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public T getNext(){
		String hdlcData = hdlcAsStringData.get(0);
		hdlcAsStringData.remove(0);
		return getDataHandler().readData(hdlcData);

	}
	
	
	public boolean checkPayload (byte[] payloadToCheck,byte checkMethod,byte[] controlSequence){
		if(checkMethod == CRTL_CRC)
		return checkSumComputation(payloadToCheck,controlSequence);
		else if (checkMethod == CRTL_CHECKSUM)
		return crcComputation(payloadToCheck,controlSequence);
		else
			return false;
	}
	
	public boolean checkSumComputation (byte[] payloadToCheck, byte[] controlSequence){
		return false;
	}

	public boolean crcComputation (byte[] payloadToCheck, byte[] controlSequence){
		return false;
	}

	
	
	

	
}
