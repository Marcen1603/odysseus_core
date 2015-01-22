package de.uniol.inf.is.odysseus.p2p_new.broadcast;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.PeerReachabilityInfo;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class BroadcastRequestSender extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestSender.class);
	private static final int ANSWER_PORT = 65200;
	private static final int REQUEST_PORT = 65212;
	private static final String REQUEST_TEXT = "REQUEST_INFORMATION";
	private static final byte[] SEND_DATA = REQUEST_TEXT.getBytes(); 
	
	private final Collection<InetAddress> broadcastAddresses = Lists.newArrayList();
	
	private DatagramSocket socket;
	private BroadcastAnswerReceiver receiver;
	
	public BroadcastRequestSender( long intervalMillis ) {
		super(intervalMillis, "UDP Find Peers thread");
	}		
	
	@Override
	public void beforeJob() {
		try {
			broadcastAddresses.addAll(getBroadcastableAddresses());

			int curPort = ANSWER_PORT;
			while( true ) {
				try {
					socket = new DatagramSocket(curPort, InetAddress.getByName("0.0.0.0"));
					break;
				} catch( BindException e ) {
					curPort++;
				}
			}
			socket.setBroadcast(true);
			LOG.info("Created udp socket at port {}", curPort);
			
			receiver = new BroadcastAnswerReceiver(socket);
			receiver.start();
			
		} catch (SocketException | UnknownHostException e) {
			LOG.error("Could not create udp socket for broadcasting", e);
			socket = null;
		}
	}
	
	private static Collection<InetAddress> getBroadcastableAddresses() {
		List<InetAddress> result = Lists.newArrayList();

		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || !networkInterface.isUp() || networkInterface.isVirtual()) {
					continue; 
				}

				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast != null) {
						result.add(broadcast);
					}
				}
			}
		} catch (SocketException e) {
			// ignore
		}

		return result;
	}	
	
	@Override
	public void doJob() {
		if( socket != null ) {
			LOG.debug("Sending udp broadcast to port {} to {}", REQUEST_PORT, REQUEST_PORT + 4);
			receiver.clearReachabilityMap();
			
			try {
				for( int i = REQUEST_PORT; i < REQUEST_PORT + 5; i++ ) {
					DatagramPacket packet = new DatagramPacket(SEND_DATA, SEND_DATA.length, InetAddress.getByName("255.255.255.255"), i);
					socket.send(packet);
					
					for (InetAddress broadcast : broadcastAddresses) {
						try {
							DatagramPacket sendPacket = new DatagramPacket(SEND_DATA, SEND_DATA.length, broadcast, i);
							socket.send(sendPacket);
						} catch (Exception e) {
							// ignore
						}
					}
				}
				
				wait3Seconds();
				
				Map<PeerID, PeerReachabilityInfo> reachabilityMap = receiver.getReachabilityMap();
				if( PeerReachabilityService.isActivated() ) {
					PeerReachabilityService.getInstance().setReachabilityMap(reachabilityMap);
				}
				
			} catch (IOException e) {
				LOG.debug("Could not send udp broadcast packets", e);
			}
			
		} else {
			stopRunning();
		}
	}
	
	private static void wait3Seconds() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void afterJob() {
		receiver.stopRunning();
		socket.close();
	}
}
