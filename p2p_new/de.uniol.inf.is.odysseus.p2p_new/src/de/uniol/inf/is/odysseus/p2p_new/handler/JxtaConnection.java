package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.util.StoppableThread;

public class JxtaConnection extends StoppableThread {

	private static final int BUFFER_SIZE_BYTES = 1024;
	private static final Logger LOG = LoggerFactory.getLogger(JxtaConnection.class);

	private final Socket socket;
	private final JxtaTransportHandler transportHandler;

	private OutputStream outputStream;

	public JxtaConnection(Socket socket, JxtaTransportHandler transportHandler) {
		this.socket = Preconditions.checkNotNull(socket, "Socket for jxta-connection must not be null!");
		this.transportHandler = Preconditions.checkNotNull(transportHandler, "TransportHandler for jxta-connection must not be null!");
	}

	@Override
	public void run() {
		DataInputStream dis = null;
		try {
			outputStream = socket.getOutputStream();
			dis = new DataInputStream(socket.getInputStream());
			while (isRunning()) {
				byte[] buffer = new byte[BUFFER_SIZE_BYTES];
				int readByteCount = dis.read(buffer);
				byte[] msgBytes = new byte[readByteCount];
				System.arraycopy(buffer, 0, msgBytes, 0, readByteCount);

				transportHandler.receive(msgBytes);
			}

		} catch (IOException ex) {
			LOG.error("Could not process connection", ex);
		} finally {
			tryClose(dis);
			tryClose(outputStream);
		}
	}

	public final Socket getSocket() {
		return socket;
	}

	public final void send( byte[] data) throws IOException {
		if( outputStream != null ) {
			outputStream.write(data);
			outputStream.flush();
		}
	}

	private static void tryClose(InputStream dis) {
		if (dis != null) {
			try {
				dis.close();
			} catch (IOException ex) {
			}
		}
	}

	private static void tryClose(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException ex) {
			}
		}
	}
}
