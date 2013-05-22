package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

final class JxtaPOUtil {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaPOUtil.class);

	public static final byte NONE_BYTE = 0;
	public static final byte PUNCTUATION_BYTE = 1;
	public static final byte DATA_BYTE = 2;
	public static final byte CONTROL_BYTE = 3;

	public static final byte OPEN_SUBBYTE = 0;
	public static final byte CLOSE_SUBBYTE = 1;
	public static final byte DONE_SUBBYTE = 2;
	public static final byte PING_SUBBYTE = 3;

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

	public static byte[] generateControlPacket(byte type) {
		final byte[] packet = new byte[2];
		packet[0] = CONTROL_BYTE;
		packet[1] = type;
		return packet;
	}

	public static void tryConnectAsync(final IJxtaConnection connection) {
		LOG.debug("Trying to connect");
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					connection.connect();
				} catch (final IOException ex) {
					LOG.error("Could not connect", ex);
				}
			}
		});
		t.setName("Connect thread");
		t.setDaemon(true);
		t.start();
	}

	public static void tryDisconnectAsync(final IJxtaConnection connection) {
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				connection.disconnect();
			}
		});
		t.setName("Discconnect thread");
		t.setDaemon(true);
		t.start();
	}
}
