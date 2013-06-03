package de.uniol.inf.is.odysseus.p2p_new.util.connect.direct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.AbstractJxtaConnection;

class SocketConnection extends AbstractJxtaConnection {

	private static final Logger LOG = LoggerFactory.getLogger(SocketConnection.class);
	private static final int BUFFER_SIZE = 1024;
	
	private final byte[] buffer = new byte[BUFFER_SIZE];
	
	private Socket socket;
	private OutputStream outStream;
	private InputStream inStream;
	private RepeatingJobThread receiverThread;
	
	public SocketConnection( Socket socket ) {
		setSocket(socket);
	}
	
	public SocketConnection() {
		// socket not set here
	}

	@Override
	public void connect() throws IOException {
		
		outStream = socket.getOutputStream();
		inStream = socket.getInputStream();
		
		receiverThread = new RepeatingJobThread() {
				@Override
				public void doJob() {
					try {
						final int bytesRead = inStream.read(buffer);
						if (bytesRead == -1) {
							disconnect();
							stopRunning();
						} else if (bytesRead > 0) {
							final byte[] msg = new byte[bytesRead];
							System.arraycopy(buffer, 0, msg, 0, bytesRead);
							fireMessageReceiveEvent(msg);
						}
					} catch (IOException e) {
						LOG.error("Could not read from input stream of socket", e);
						
						stopRunning();
						disconnect();
					}
				}
		};
		receiverThread.start();
		
		super.connect();
	}

	@Override
	public void send(byte[] data) throws IOException {
		waitForConnect();
		
		outStream.write(data);
		outStream.flush();
	}

	@Override
	public void disconnect() {
		if( receiverThread != null ) {
			receiverThread.stopRunning();
			receiverThread = null;
		}
		
		tryClose(socket);
		super.disconnect();
	}

	protected final void setSocket(Socket socket) {
		Preconditions.checkNotNull(socket, "Socket for connection must not be null!");
		Preconditions.checkState(this.socket == null, "Socket is already set!");

		this.socket = socket;
	}

	private static void tryClose(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			LOG.error("Could not close socket", e);
		}
	}
}
