package de.uniol.inf.is.odysseus.wrapper.mosaik;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

public class MosaikProtocolHandler<T extends KeyValueObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {

	static Logger LOG = LoggerFactory.getLogger(MosaikProtocolHandler.class);
	
    static final int REQ = 0;
    static final int SUCCESS = 1;
    static final int ERROR = 2;
    protected OdysseusSimulator sim;  
    
    protected boolean dividedMsg = false;
    protected ByteArrayOutputStream dividedMsgByteArray;
    protected int dividedSize = 0;
    protected int alreadyRead = 0;
	
    static final JSONParser parser = new JSONParser();
	
	public MosaikProtocolHandler() {
		super();
	}

	public MosaikProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		this.sim = new OdysseusSimulator(this, "Odysseus");
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IDataHandler<T> dataHandler) {
		return new MosaikProtocolHandler<>(direction, access, dataHandler, options);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
		super.close();
	}

	@Override
	public void process(String[] message) {
		getTransfer().transfer(getDataHandler().readData(message));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
		try {     
			int size;
			if(!dividedMsg) {
				//read in header
				ByteBuffer header = ByteBuffer.allocate(4);
				header.order(ByteOrder.BIG_ENDIAN);
		        message.get(header.array(), 0, 4);
		        size = header.getInt(0);	
			} else {
				size = dividedSize;
			}
			
			//read in message
	        int read = 0;  
			int remaining = message.remaining();      
			byte[] messageByteArray = new byte[remaining];
			if(size > remaining) {
				byte[] tmpMessageByteArray = new byte[remaining];
				message.get(tmpMessageByteArray, 0, remaining);
				if(!dividedMsg) {
					dividedMsg = true;
					alreadyRead = 0;
					dividedSize = size;
					dividedMsgByteArray = new ByteArrayOutputStream();
				}
				dividedMsgByteArray.write(tmpMessageByteArray);
				alreadyRead += remaining;
				if(alreadyRead == size) {
					dividedMsg = false;
					messageByteArray = dividedMsgByteArray.toByteArray();
				}
			} else {
		        while (read < size) {
		            message.get(messageByteArray, read, size - read);
		            if (messageByteArray.length < 0) {
		                throw new IOException("Unexpected end of stream");
		            }
		            read += messageByteArray.length;
		        }
			}
			if(messageByteArray.length == size) {
				//handle complete message
				final String messageSring = new String(messageByteArray, "UTF-8");
				final JSONArray payload = (JSONArray) parser.parse(messageSring);    
		        
				// Expand payload
				final int msgType = ((Number) payload.get(0)).intValue();
				if (msgType != REQ) {
					throw new IOException("Expected message type 0, got " + msgType);
				}
				int msgId = ((Number) payload.get(1)).intValue();
				final JSONArray call = (JSONArray) payload.get(2);

				// Set request data
				String method = (String) call.get(0);
				JSONArray args = (JSONArray) call.get(1);
				JSONObject kwargs = (JSONObject) call.get(2);

				Object result;	  
				boolean stop = false;
				switch (method) {
				case "init":
					final String sid = (String) args.get(0);
					result = this.sim.init(sid, kwargs);
					break;
				case "create":
					final int num = ((Number) args.get(0)).intValue();
					final String model = (String) args.get(1);
					result = this.sim.create(num, model, kwargs);
					break;
				case "step":
					final long time = ((Number) args.get(0)).longValue();
					final JSONObject inputs = (JSONObject) args.get(1);
					result = this.sim.step(time, inputs);
					break;
				case "get_data":
					final JSONObject outputs = (JSONObject) args.get(0);
					result = this.sim.getData(outputs);
					break;
				case "stop":
					stop = true;
					result = null;
					this.close();
					getTransfer().transfer(null);
					break;
				default:
					throw new RuntimeException("Unkown method: " + method);
				}

				if(!stop) {
					//send response
					final JSONArray reply = new JSONArray();
					reply.add(SUCCESS);
					reply.add(msgId);
					reply.add(result);
					ByteArrayOutputStream sendData = new ByteArrayOutputStream();
					ByteBuffer sendHeader = ByteBuffer.allocate(4);
					sendHeader.putInt(0, reply.toString().length());
					sendData.write(sendHeader.array());
					sendData.write(reply.toString().getBytes());
					getTransportHandler().send(sendData.toByteArray());
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug(e.getMessage());
		}
	}

	@Override
	public String getName() {
		return "Mosaik";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof MosaikProtocolHandler)) {
			return false;
		}
		// the datahandler was already checked in the
		// isSemanticallyEqual-Method of AbstracProtocolHandler
		return true;
	}
}