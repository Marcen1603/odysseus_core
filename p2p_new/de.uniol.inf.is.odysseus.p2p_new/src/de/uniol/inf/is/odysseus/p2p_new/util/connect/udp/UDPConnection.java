package de.uniol.inf.is.odysseus.p2p_new.util.connect.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.AbstractJxtaConnection;

public class UDPConnection extends AbstractJxtaConnection {

	public static final byte DATA_BYTE = 0;
	public static final byte CONNECT_BYTE = 1;
	public static final byte ACK_BYTE = 2;
	
	private static final Logger LOG = LoggerFactory.getLogger(UDPConnection.class);
	private static final int BUFFER_SIZE = 4096;

	private final InetAddress address;
	private final int port;
	private final DatagramSocket socket;
	private final boolean isServer;
	
	private RepeatingJobThread receiverThread;
	private RepeatingJobThread connectThread;
	
	// server-version
	UDPConnection( DatagramSocket socket, InetAddress address, int port ) {
		Preconditions.checkNotNull(socket, "Socket for udp connection must not be null!");
		Preconditions.checkNotNull(address, "Address for udp connection must not be null!");
		Preconditions.checkArgument(port > 0 , "Port for udp connection must be positive!");
		
		this.address = address;
		this.port = port;
		this.socket = socket;
		this.isServer = true;
	}
	
	// client-version
	public UDPConnection(String address, int port) throws SocketException, UnknownHostException {
		Preconditions.checkNotNull(address, "Address for udp connection must not be null!");
		Preconditions.checkArgument(port > 0 , "Port for udp connection must be positive!");
		
		this.address = InetAddress.getByName(address);
		this.port = port;
		this.socket = new DatagramSocket();
		this.isServer = false;

		receiverThread = new RepeatingJobThread() {
			@Override
			public void doJob() {
				
				try {
					byte[] buffer = new byte[BUFFER_SIZE];
					DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
					socket.receive(packet);
					recieve(packet.getData());
				} catch (IOException e) {
					LOG.error("Could not receive datagram packet", e);
					disconnect();
					stopRunning();
				}
			};
		};
		receiverThread.start();
	}
	
	@Override
	public void connect() throws IOException {
		if( !isServer ) {
			connectThread = new RepeatingJobThread(1000) {
				@Override
				public void doJob() {
					try {
						byte[] connectPacket = new byte[1];
						connectPacket[0] = CONNECT_BYTE;
						
						DatagramPacket packet = new DatagramPacket(connectPacket, 1, address, port);
						socket.send(packet);
						
					} catch (IOException e) {
						LOG.error("Could not send connect message", e);
						setConnectFail();
						stopRunning();
					}
				}
			};
			connectThread.start();
		} else {
			super.connect();
		}
	}
	
	@Override
	public void send(byte[] data) throws IOException {
		waitForConnect();
		
		byte[] rawData = new byte[data.length + 1 + 4];
		rawData[0] = DATA_BYTE;
		insertInt(rawData, 1, data.length);
		System.arraycopy(data, 0, rawData, 5, data.length);
		
		DatagramPacket packet = new DatagramPacket(rawData, rawData.length, address, port);
		socket.send(packet);
	}
	
	public void recieve(byte[] data) {
		if (data[0] == ACK_BYTE) {
			if (connectThread != null) {
				// connection established
				connectThread.stopRunning();
				connectThread = null;
				try {
					super.connect();
				} catch (IOException ex) {
					LOG.error("Could not set connected to true", ex);
				}
			}
			return;
		} else if( data[0] == DATA_BYTE ) {
			
			int size = toInt(data, 1);
			
			byte[] rawData = new byte[size];
			System.arraycopy(data, 5, rawData, 0, size);
			fireMessageReceiveEvent(rawData);
		}
	}
	
	@Override
	public void disconnect() {
		if( receiverThread != null ) {
			receiverThread.stopRunning();
			receiverThread.interrupt();
		}
		
		super.disconnect();
	}
	
	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}
	
	private static int toInt(byte[] bytes, int offset) {
		int ch1 = bytes[offset];
		int ch2 = bytes[offset + 1];
		int ch3 = bytes[offset + 2];
		int ch4 = bytes[offset + 3];
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
}
