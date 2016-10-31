package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.PunctAwareByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class OdysseusProtocolHandler2<T extends IStreamObject<? extends IMetaAttribute>> extends MarkerByteBufferHandler<T> {

	static final Logger LOG = LoggerFactory.getLogger(OdysseusProtocolHandler2.class);

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
	protected ByteBuffer prepareObject(T object) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		// add type info OBJECT
		buffer.put(OBJECT);
		getDataHandler().writeData(buffer, object);
		buffer.flip();
		return buffer;
	}




	@Override
	protected synchronized void processObject() throws IOException, ClassNotFoundException {
		T so = null;

		try{
			so = objectHandler.create();
		}catch(Exception e){
			LOG.warn("Error reading input. Clearing input buffer ... ");
			objectHandler.clear();
		}
		if (so != null){
			//System.err.println(so);
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
