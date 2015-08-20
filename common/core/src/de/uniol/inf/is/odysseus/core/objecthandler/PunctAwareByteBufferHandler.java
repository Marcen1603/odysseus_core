package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;

import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public class PunctAwareByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>>
		extends ByteBufferHandler<T> {

	IPunctuation punctuation = null;
	
	public PunctAwareByteBufferHandler(IStreamObjectDataHandler<T> dataHandler) {
		super(dataHandler);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public synchronized T create() throws IOException, ClassNotFoundException, BufferUnderflowException {
		T retval = null;
		synchronized (byteBuffer) {
			byteBuffer.flip();
			// read type:
			byte type = byteBuffer.get();
			if (type == 0) {
				retval = (T) this.dataHandler.readData(byteBuffer);
				byteBuffer.clear();
			} else {
				// TODO: Better punctuation handling...
				int size = byteBuffer.getInt();		
				byte[] puncBytes = new byte[size];
				byteBuffer.get(puncBytes);
				punctuation = (IPunctuation) ObjectByteConverter.bytesToObject(puncBytes);
				byteBuffer.clear();
				retval = null;
			}
		}
		return retval;
	}

	public IPunctuation getPunctuation() {
		return punctuation;
	}
	
}
