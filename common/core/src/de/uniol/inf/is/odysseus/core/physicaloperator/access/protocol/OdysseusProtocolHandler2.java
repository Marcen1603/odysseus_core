package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.objecthandler.PunctAwareByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class OdysseusProtocolHandler2<T extends IStreamObject<? extends IMetaAttribute>> extends MarkerByteBufferHandler<T> {

	final static byte OBJECT = 0;
	final static byte PUNCT = 1;


	public OdysseusProtocolHandler2(){
	}

	public OdysseusProtocolHandler2(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		objectHandler = new PunctAwareByteBufferHandler<T>(dataHandler);
	}


	protected ByteBuffer prepareObject(IPunctuation punctuation) {
		ByteBuffer buffer;
		int puncNumber = punctuation.getNumber();
		// TODO: Move to registry
		switch(puncNumber){
		case 1:
		case 2:
			buffer = ByteBuffer.allocate(1024);
			buffer.put(punctuation.getNumber());
			PunctAwareByteBufferHandler.dataHandlerList.get(puncNumber-1).writeData(buffer, punctuation.getValue());
			break;
		default:
			byte[] data = ObjectByteConverter.objectToBytes(punctuation);
			buffer = ByteBuffer.allocate(data.length+4);
			buffer.put(punctuation.getNumber());
			buffer.putInt(data.length);
			buffer.put(data);
		}
		buffer.flip();
		return buffer;
	}

	@Override
	protected ByteBuffer prepareObject(T object) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		// add type info OBJECT
		buffer.put(OBJECT);
		getDataHandler().writeData(buffer, object);
		buffer.flip();
		return buffer;
	}




	@Override
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
		OdysseusProtocolHandler2<T> instance = new OdysseusProtocolHandler2<T>(
				direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public String getName() {
		return "OdysseusMarker";
	}

}
