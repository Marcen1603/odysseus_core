package de.uniol.inf.is.odysseus.peer.console;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class WaitForBroadcastAnswerThread extends RepeatingJobThread {
	
	private final DatagramSocket socket;
	private final CommandInterpreter ci;
	private final String peerGroupFilter;
	
	private final List<InetAddress> alreadyReceived = Lists.newArrayList();
	
	public WaitForBroadcastAnswerThread( DatagramSocket socket, CommandInterpreter ci, String peerGroupFilter ) {
		super(0, "Wait for broadcast answer thread");
		
		Preconditions.checkNotNull(socket, "socket must not be null!");
		Preconditions.checkNotNull(ci, "CommandInterpreter must not be null!");
		
		this.socket = socket;
		this.ci = ci;
		this.peerGroupFilter = peerGroupFilter;
	}
	
	@Override
	public void doJob() {
		try {
			byte[] buffer = new byte[1024];
			DatagramPacket p = new DatagramPacket(buffer, buffer.length);
			socket.receive(p);

			InetAddress address = p.getAddress();
			if( !address.equals(InetAddress.getLocalHost()) && !alreadyReceived.contains(address)) {
				alreadyReceived.add(address);
				
				try {
					JSONObject obj = new JSONObject(new String(p.getData()));
					String peerName = (String)obj.get("PeerName");
					String peerGroup = (String)obj.get("GroupName");
					String addressStr = address.getHostAddress().toString();
					if( Strings.isNullOrEmpty(peerGroupFilter) || peerGroup.contains(peerGroupFilter)) {
						ci.println(addressStr + " = " + peerName + " { " + peerGroup + " }");
					}
					
				} catch (JSONException e) {
					ci.println("Could not parse answer from " + address.getHostAddress().toString() + ": " + e.getMessage());
				}							
			}

		} catch (SocketTimeoutException e) {
			// ignore
		} catch( SocketException e ) {
			if( !e.getMessage().equals("socket closed")) {
				ci.println("Could not receive answer package of crying: " + e.getMessage());
			} else {
				stopRunning();
			}
		} catch (IOException e) {
			ci.println("Could not receive answer package of crying: " + e.getMessage());
		}
	}
}
