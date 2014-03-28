package de.uniol.inf.is.odysseus.p2p_new.util.connect.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.AbstractJxtaServerConnection;

public class UDPServerConnection extends AbstractJxtaServerConnection {

	private static final Logger LOG = LoggerFactory.getLogger(UDPServerConnection.class);

	private final Map<ConnectionData, IJxtaConnection> activeConnectionsMap = Maps.newHashMap();

	private RepeatingJobThread receiverThread;
	private DatagramSocket socket;

	@Override
	public void start() throws IOException {
		socket = new DatagramSocket();

		receiverThread = new RepeatingJobThread(0, "Udp server connection accept") {
			@Override
			public void doJob() {

				try {
					byte[] buffer = new byte[P2PNewPlugIn.TRANSPORT_BUFFER_SIZE];
					DatagramPacket packet = new DatagramPacket(buffer, P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);

					socket.receive(packet);

					byte[] data = packet.getData();
					ConnectionData d = new ConnectionData(packet.getAddress(), packet.getPort());

					if (data[0] == UDPConnection.CONNECT_BYTE) {
						if (!activeConnectionsMap.containsKey(d)) {
							UDPConnection connection = new UDPConnection(socket, packet.getAddress(), packet.getPort());
							connection.connect();
							
							activeConnectionsMap.put(d, connection);
							fireConnectionAddEvent(connection);
						}
						
						sendAckPacket(packet.getAddress(), packet.getPort());
					} else {
						UDPConnection connection = (UDPConnection) activeConnectionsMap.get(d);
						connection.recieve(packet.getData());
					}

				} catch (IOException e) {
					LOG.error("Could not receive datagram packet", e);
				}
			}

		};
		receiverThread.start();
	}

	@Override
	public void stop() {
		if (receiverThread != null) {
			receiverThread.stopRunning();
			receiverThread = null;
		}
	}

	@Override
	public boolean isStarted() {
		return receiverThread != null && receiverThread.isAlive();
	}

	@Override
	public ImmutableList<IJxtaConnection> getConnections() {
		return ImmutableList.copyOf(activeConnectionsMap.values());
	}

	@Override
	public PipeAdvertisement getPipeAdvertisement() {
		throw new UnsupportedOperationException();
	}

	public int getLocalPort() {
		return socket.getLocalPort();
	}

	private void sendAckPacket(InetAddress address, int port) throws IOException {
		byte[] ackData = new byte[1];
		ackData[0] = UDPConnection.ACK_BYTE;
		DatagramPacket p = new DatagramPacket(ackData, ackData.length, address, port);
		socket.send(p);
	};

}
