package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.objecthandler.PunctAwareByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public abstract class AbstractObjectHandlerByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>> extends AbstractByteBufferHandler<T> {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractObjectHandlerByteBufferHandler.class);

	protected ByteBufferHandler<T> objectHandler;

	public AbstractObjectHandlerByteBufferHandler() {
		super();
	}

	public AbstractObjectHandlerByteBufferHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		objectHandler = new ByteBufferHandler<T>(dataHandler);
	}

	@Override
	public boolean hasNext() throws IOException {
		if (getTransportHandler().getInputStream() != null) {
			return getTransportHandler().getInputStream().available() > 0;
		} else {
			return false;
		}
	}

	protected ByteBuffer prepareObject(IPunctuation punctuation) {
		return convertPunctuation(punctuation);
	}

	public static ByteBuffer convertPunctuation(IPunctuation punctuation) {
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
			buffer.putInt(data.length);
			buffer.put(data);
		}
		buffer.flip();
		return buffer;
	}

	protected ByteBuffer prepareObject(T object) {
		return convertObject(object, getDataHandler());
	}

	public static ByteBuffer convertObject(Object object, IDataHandler dataHandler) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		dataHandler.writeData(buffer, object);
		buffer.flip();
		return buffer;
	}


	protected void processObject() throws IOException, ClassNotFoundException {
		T object = objectHandler.create();
		if (object != null) {
			getTransfer().transfer(object);
		} else {
			LOG.error("Empty object");
		}
	}

}
