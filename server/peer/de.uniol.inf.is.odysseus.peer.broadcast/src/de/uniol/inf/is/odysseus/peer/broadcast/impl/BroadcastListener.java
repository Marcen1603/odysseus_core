package de.uniol.inf.is.odysseus.peer.broadcast.impl;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.broadcast.IBroadcastListener;

public class BroadcastListener implements IBroadcastListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(BroadcastListener.class);
	
	private static IP2PNetworkManager p2pManager;
	
	private DatagramSocket socket;
	private boolean isRunning = true;
	private int port = 65212;

	public static void bindP2PNetworkManager(IP2PNetworkManager manager) {
		p2pManager = manager;
	}

	public static void unbindP2PNetworkManager(IP2PNetworkManager manager) {
		if (manager == p2pManager) {
			p2pManager = null;
		}
	}

	@Override
	public void run() {
		try {
			// Keep a socket open to listen to all the UDP traffic that is
			// destined for this port
			socket = new DatagramSocket(port);
			LOG.debug("Begin waiting for broadcast message for peer identification");
			
			while (isRunning) {

				// Receive a packet if Network is Started
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);
				// See if the packet holds the right command (message)
				String message = new String(packet.getData()).trim();
				
				LOG.debug("Received message with text '{}' from address {}:{}", new Object[] {message, packet.getAddress(), packet.getPort()});
				if (message.equalsIgnoreCase("REQUEST_INFORMATION")) {
					LOG.debug("Received message is for us! Creating answer...");
					JSONObject json = new JSONObject();
					try {
						json.put("Port", p2pManager.getPort());
						json.put("PeerName", p2pManager.getLocalPeerName());
						json.put("GroupName", p2pManager.getLocalPeerGroupName());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					byte[] sendData = json.toString().getBytes();

					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
					socket.send(sendPacket);
					LOG.debug("Answer send to {}:{}", packet.getAddress(), packet.getPort());
					
				} else {
					LOG.debug("Received message is not for us");
				}
			}
			
		} catch (BindException ex) {
			port++;
			run();
		} catch (IOException ex) {
			LOG.error("Could not wait for broadcasts", ex);
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
