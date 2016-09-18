package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
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

	@Override
	public void write(T object) throws IOException {
		ByteBuffer buffer = prepareObject(object);
		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 8 + 5]; // 8 for markers, 5 for type info
		insertInt(rawBytes, 0, start);
		insertInt(rawBytes, 4, messageSizeBytes + 1);
		rawBytes[8] = OBJECT;
		insertInt(rawBytes, messageSizeBytes + 9, end);
		getTransportHandler().send(rawBytes);
	}

	@Override
	public void writePunctuation(IPunctuation punctuation) throws IOException {
		ByteBuffer buffer = prepareObject(punctuation);
		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 8 + 5]; // 8 for markers, 5 for type info
		insertInt(rawBytes, 0, start);
		insertInt(rawBytes, 4, messageSizeBytes + 1);
		rawBytes[8] = PUNCT;
		insertInt(rawBytes, messageSizeBytes + 9, end);
		getTransportHandler().send(rawBytes);
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
