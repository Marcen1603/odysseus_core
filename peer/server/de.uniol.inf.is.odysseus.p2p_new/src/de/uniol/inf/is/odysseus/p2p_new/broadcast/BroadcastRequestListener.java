package de.uniol.inf.is.odysseus.p2p_new.broadcast;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;

public class BroadcastRequestListener implements IBroadcastListener {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestListener.class);

	private DatagramSocket socket;
	private boolean isRunning = true;
	private int port = 65212;

	public BroadcastRequestListener() {
	}

	@Override
	public void run() {
		P2PNetworkManager.waitForStart();

		try {
			socket = new DatagramSocket(port);
			LOG.info("Begin waiting for udp broadcast messages on port {}", port);

			while (isRunning) {

				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);

				String message = new String(packet.getData()).trim();

				if (message.equalsIgnoreCase("REQUEST_INFORMATION")) {
					LOG.debug("Received udp broadcast message from address {}:{} at own port {}", new Object[] { packet.getAddress(), packet.getPort(), port });
					
					if( P2PNetworkManager.isActivated() ) {
						try {
							JSONObject json = new JSONObject();
							json.put("PeerID", P2PNetworkManager.getInstance().getLocalPeerID().toString());
							json.put("PeerName", P2PNetworkManager.getInstance().getLocalPeerName());
							json.put("GroupName", P2PNetworkManager.getInstance().getLocalPeerGroupName());
							json.put("GroupID", P2PNetworkManager.getInstance().getLocalPeerGroupID().toString());
							json.put("Port", P2PNetworkManager.getInstance().getPort());
	
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
