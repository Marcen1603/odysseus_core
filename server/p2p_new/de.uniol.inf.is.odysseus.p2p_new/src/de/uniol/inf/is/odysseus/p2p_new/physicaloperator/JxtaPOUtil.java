package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

final class JxtaPOUtil {

	private JxtaPOUtil() {
	}

	public static IPunctuation createPunctuation(ByteBuffer messageBuffer) {
		final byte[] rawData = new byte[messageBuffer.remaining()];
		messageBuffer.get(rawData, 0, rawData.length);

		return (IPunctuation) ObjectByteConverter.bytesToObject(rawData);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends IStreamObject> T createStreamObject(ByteBuffer byteBuffer, TupleDataHandler dataHandler) throws BufferUnderflowException {
		T retval = null;
		try {
			retval = (T) dataHandler.readData(byteBuffer);

			if (byteBuffer.remaining() > 0) {
				final byte[] metadataBytes = new byte[byteBuffer.remaining()];
				byteBuffer.get(metadataBytes);

				final IMetaAttribute metadata = (IMetaAttribute) ObjectByteConverter.bytesToObject(metadataBytes);
				retval.setMetadata(metadata);
			}
		} finally {
			byteBuffer.clear();
		}

		return retval;
	}
}
