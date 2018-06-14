package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NewFilenamePunctuation;

@SuppressWarnings("deprecation")
public class PunctAwareByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>>
		extends ByteBufferHandler<T> {

	static public final List<IStreamObjectDataHandler<?>> dataHandlerList = new ArrayList<IStreamObjectDataHandler<?>>();
	{
		// fill with know punctuations --> move to other place sometimes
		dataHandlerList.add(DataHandlerRegistry.instance.getStreamObjectDataHandler("tuple", Heartbeat.schema));
		dataHandlerList.add(DataHandlerRegistry.instance.getStreamObjectDataHandler("tuple", NewFilenamePunctuation.schema));
	}
	
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
				Tuple<?> t;
				// TODO: Move to another place
				switch (type){
				case 1:
					t = (Tuple<?>) dataHandlerList.get(type-1).readData(byteBuffer, false);
					punctuation = Heartbeat.createNewInstance(t);
					break;
				case 2:
					t = (Tuple<?>) dataHandlerList.get(type-1).readData(byteBuffer, false);
					punctuation = NewFilenamePunctuation.createNewInstance(t);
					break;
				default:
					int size = byteBuffer.getInt();		
					byte[] puncBytes = new byte[size];
					byteBuffer.get(puncBytes);
					punctuation = (IPunctuation) ObjectByteConverter.bytesToObject(puncBytes);
				}
				
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
