package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.objecthandler.PunctAwareByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NewFilenamePunctuation;

public class OdysseusProtocolHandler<T extends IStreamObject<? extends IMetaAttribute>> extends SizeByteBufferHandler<T> {
	
	final static byte OBJECT = 0;
	final static byte PUNCT = 1;
	
	
	public OdysseusProtocolHandler(){
	}

	public OdysseusProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		objectHandler = new PunctAwareByteBufferHandler<T>(dataHandler);
	}
	
	@Override
	public void write(T object) throws IOException {
		ByteBuffer buffer = prepareObject(object);
		sendWithTypeInfo(buffer, OBJECT);
	}

	@Override
	public void writePunctuation(IPunctuation punctuation) throws IOException {
		ByteBuffer buffer = prepareObject(punctuation);
		sendWithTypeInfo(buffer,punctuation.getNumber());
	}
	
	private ByteBuffer prepareObject(IPunctuation punctuation) {
		ByteBuffer buffer;
		int puncNumber = punctuation.getNumber();
		// TODO: Move to registry
		switch(puncNumber){
		case 1:
		case 2:
			buffer = ByteBuffer.allocate(1024);
			PunctAwareByteBufferHandler.dataHandlerList.get(puncNumber-1).writeData(buffer, punctuation.getValue());
			break;
		default:
			byte[] data = ObjectByteConverter.objectToBytes(punctuation);
			buffer = ByteBuffer.allocate(data.length+4);
			buffer.putInt(data.length);
			buffer.put(data);
		}
		buffer.flip();
		return buffer;
	}

	private void sendWithTypeInfo(ByteBuffer buffer, byte typeInfo) throws IOException {
		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 5]; // sizeinfo = 5, typeinfo = 1
		insertInt(rawBytes, 0, messageSizeBytes+1); // typeInfo is part of object!
		rawBytes[4]= typeInfo;
		
		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 5, messageSizeBytes);
		getTransportHandler().send(rawBytes);
	}
	
	protected void processObject() throws IOException, ClassNotFoundException {
		T so = objectHandler.create();
		if (so != null){
			getTransfer().transfer(so);
		}else{
			IPunctuation punc = ((PunctAwareByteBufferHandler<T>)objectHandler).getPunctuation();
			if (punc != null){
				getTransfer().sendPunctuation(punc);
			}
		}
	}
	

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		OdysseusProtocolHandler<T> instance = new OdysseusProtocolHandler<T>(
				direction, access, dataHandler, options);
		return instance;
	}
	
	@Override
	public String getName() {
		return "Odysseus";
	}
	
}
