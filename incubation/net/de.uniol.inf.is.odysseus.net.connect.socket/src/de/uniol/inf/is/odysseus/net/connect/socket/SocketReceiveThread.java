package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class SocketReceiveThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(SocketReceiveThread.class);
	private static final int BUFFER_SIZE_BYTES = 1024 * 4; 
	
	private final Socket clientSocket;
	private final InputStream inFromSocket;
	private final OutputStream outToSocket;
	private final byte[] READING_BUFFER = new byte[BUFFER_SIZE_BYTES];
	private final ISocketReceiveThreadListener listener;
	
	private boolean running;
	
	public SocketReceiveThread(Socket clientSocket, ISocketReceiveThreadListener listener) throws IOException {
		Preconditions.checkNotNull(clientSocket, "clientSocket must not be null!");
		Preconditions.checkNotNull(listener, "listener must not be null!");

		this.listener = listener;
		this.clientSocket = clientSocket;
		this.inFromSocket = clientSocket.getInputStream();
		this.outToSocket = clientSocket.getOutputStream();
		
		setName("Client socket receive: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
		setDaemon(true);
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}
	
	@Override
	public void run() {
		LOG.info("Starting client socket receive thread for {}:", clientSocket.getInetAddress(), clientSocket.getPort());
		MessageByteBuffer mb = new MessageByteBuffer();
		
		running = true;
		try {
			while( running ) {
				int bytesRead = inFromSocket.read(READING_BUFFER);

				if (bytesRead == -1) {
					LOG.debug("Reached end of data stream. Socket closed...");
					running = false;
					listener.socketDisconnected();
					
				} else if (bytesRead > 0) {
					byte[] msg = new byte[bytesRead];
					System.arraycopy(READING_BUFFER, 0, msg, 0, bytesRead);
					
					mb.put(msg);

					List<byte[]> packets = mb.getPackets();
					for (byte[] packet : packets) {
						listener.bytesReceived(packet);
					}
				}
			}
		} catch( IOException e ) {
			LOG.warn("Could not read from inputstream from socket {}", clientSocket, e);
			listener.socketDisconnected();
		}
		
		LOG.info("Stopped client socket receive thread for {}:{}", clientSocket.getInetAddress(), clientSocket.getPort());
	}
	
	public void write( byte[] data ) throws OdysseusNetConnectionException {
		try {
			outToSocket.write(data);
			outToSocket.flush();
		} catch( IOException e ) {
			listener.socketDisconnected();
			
			throw new OdysseusNetConnectionException("Could not write data", e);
		}
	}
	
	public void stopRunning() {
		running = false;
	}
}
