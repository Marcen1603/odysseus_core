package de.uniol.inf.is.odysseus.peer.broadcast.impl;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class BroadcastRequestListener implements IBroadcastListener {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestListener.class);

	private final IP2PNetworkManager networkManager;
	
	private DatagramSocket socket;
	private boolean isRunning = true;
	private int port = 65212;

	public BroadcastRequestListener(IP2PNetworkManager networkManager) {
		Preconditions.checkNotNull(networkManager, "NetworkManager must not be null!");
		
		this.networkManager = networkManager;
	}

	@Override
	public void run() {
		try {
			socket = new DatagramSocket(port);
			LOG.info("Begin waiting for udp broadcast messages on port {}", port);

			while (isRunning) {

				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet); // blocking

				String message = new String(packet.getData()).trim();

				if (message.equalsIgnoreCase("REQUEST_INFORMATION")) {
					LOG.debug("Received udp broadcast message from address {}:{} at own port {}", new Object[] { packet.getAddress(), packet.getPort(), port });
					
					if( networkManager.isStarted() ) {
						try {
							JSONObject json = new JSONObject();
							json.put("PeerID", networkManager.getLocalPeerID().toString());
							json.put("PeerName", networkManager.getLocalPeerName());
							json.put("GroupName", networkManager.getLocalPeerGroupName());
							json.put("GroupID", networkManager.getLocalPeerGroupID().toString());
							json.put("Port", networkManager.getPort());
	
							byte[] sendData = json.toString().getBytes();
							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
							socket.send(sendPacket);
							LOG.debug("Answer " + json.toString() + " send to {}:{}", packet.getAddress(), packet.getPort());
	
						} catch (JSONException e) {
							LOG.error("Could not create JSON object for udp broadcast answer: {}", e.getMessage());
						}
					}
				}
			}

		} catch (BindException ex) {
			port++;
			run();
		} catch (IOException ex) {
			if (!(ex instanceof SocketException)) {
				LOG.error("Could not wait for broadcasts", ex);
			}
		} finally {
			LOG.debug("Waiting for broadcast messages ends here now");
		}
	}

	@Override
	public void stopListener() {
		this.isRunning = false;
		this.socket.close();
	}

}
