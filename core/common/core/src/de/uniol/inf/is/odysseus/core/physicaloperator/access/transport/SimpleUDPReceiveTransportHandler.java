package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class SimpleUDPReceiveTransportHandler extends AbstractPushTransportHandler {

	private static class ReceiveThread extends Thread {

		private final byte[] receiveData = new byte[1024];
		private DatagramSocket serverSocket;
		private final SimpleUDPReceiveTransportHandler handler;

		public ReceiveThread(SimpleUDPReceiveTransportHandler hdl, int port) throws SocketException {
			setName("UDP ETW Receive Thread");
			setDaemon(true);

			serverSocket = new DatagramSocket(port);
			handler = hdl;
		}

		@Override
		public void run() {
			try {
				while (true) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);

					byte[] data = new byte[receivePacket.getLength()];
					System.arraycopy(receivePacket.getData(), 0, data, 0, data.length);
					
					ByteBuffer buffer = ByteBuffer.wrap(data);
					buffer.position(buffer.limit());
					handler.fireProcess(buffer);
				}
			} catch (IOException e) {
				if (serverSocket != null)
					LOG.error("Could not receive data from ETW", e);
			}
		}

		public void stopRunning() {
			serverSocket.close();
			serverSocket = null;
			
			try {
				super.join();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(SimpleUDPReceiveTransportHandler.class);
	private static final String NAME = "SimpleUDPReceive";

	private int port = -1;
	private ReceiveThread receiveThread;

	public SimpleUDPReceiveTransportHandler() {
		super();
	}
	
	public SimpleUDPReceiveTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}
	
	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		
		SimpleUDPReceiveTransportHandler handler = new SimpleUDPReceiveTransportHandler(protocolHandler, options);
		if( options.containsKey("port")) {
			handler.setPort(Integer.valueOf(options.get("port")));
		}
		
		return handler;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		if( port == -1 ) {
			throw new IOException("option 'port' is mandatory to receive udp packets");
		}
		
		receiveThread = new ReceiveThread(this, port);
		receiveThread.start();
		
		fireOnConnect();
	}

	@Override
	public void processOutOpen() throws IOException {
        throw new UnsupportedOperationException();
	}

	@Override
	public void processInClose() throws IOException {
		
		fireOnDisconnect();
		
		receiveThread.stopRunning();
	}

	@Override
	public void processOutClose() throws IOException {
        throw new UnsupportedOperationException();
	}

	@Override
	public void send(byte[] message) throws IOException {
        throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		if (!(other instanceof SimpleUDPReceiveTransportHandler)) 
			return false;
		
		SimpleUDPReceiveTransportHandler otherUDP = (SimpleUDPReceiveTransportHandler) other;
		
		return port == otherUDP.port;
	}

}
