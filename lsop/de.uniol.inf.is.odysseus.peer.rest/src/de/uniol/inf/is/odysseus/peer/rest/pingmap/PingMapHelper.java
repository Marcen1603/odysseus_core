package de.uniol.inf.is.odysseus.peer.rest.pingmap;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;






import java.util.List;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.collect.ImmutableCollection;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapListener;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;

public class PingMapHelper implements IPingMapListener {

	private static final int SINK_MIN_PORT = 10000;
	private static final int SINK_MAX_PORT = 20000;

	
	private static PingMapHelper instance;
	private IPingMap pingMap;
	private List<IPingMapNode> pingMapNodes = Collections.synchronizedList(new ArrayList<IPingMapNode>());
	private Collection<SocketThread> socketThreads = Collections.synchronizedSet(new HashSet<SocketThread>());

	
	
	public static PingMapHelper getInstance() {
		if (instance == null) {
			instance = new PingMapHelper();
		}
		return instance;
	}
	
	private PingMapHelper() {
		pingMap = PingMapServiceBinding.getPingMap();
	}
	
	public int createServerSocket() {
		int randomPort = SINK_MIN_PORT + (int) (Math.random() * ((SINK_MAX_PORT - SINK_MIN_PORT) + 1));
		SocketThread thread = new SocketThread(randomPort);
		thread.start();
		synchronized (socketThreads) {
			socketThreads.add(thread);
			if (socketThreads.size() == 1) {
				PingMapServiceBinding.getPingMap().addListener(this);
			}
		}				
		return randomPort;
	}


	@Override
	public void pingMapChanged() {
		ImmutableCollection<PeerID> peers = PingMapServiceBinding.getPeerDictionary().getRemotePeerIDs();
		synchronized (pingMapNodes) {
			pingMapNodes.clear();
			pingMapNodes.add(pingMap.getNode(PingMapServiceBinding.getP2PNetworkManager().getLocalPeerID()).orNull());
			for (PeerID peer : peers) {
				IPingMapNode node = pingMap.getNode(peer).orNull();
				if (node != null) {
					pingMapNodes.add(node);
				}
			}
		}		
	}

	
	private class SocketThread extends Thread {
		private int port;
		
		public SocketThread(int port) {
			this.port = port;
			this.setDaemon(true);
			this.setName("RestService: PingMap SocketThread");
		}
		
		
		@Override		
		public void run() {
			try(ServerSocket serverSocket = new ServerSocket(port)) {
				Socket socket = serverSocket.accept();
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				while (true) {
					synchronized (pingMapNodes) {
						out.writeInt(pingMapNodes.size());
						for (IPingMapNode node : pingMapNodes) {
							String peerId = node.getPeerID().toString();
							String peerName = PingMapServiceBinding.getPeerDictionary().getRemotePeerName(peerId);
							out.writeUTF(peerId);
							out.writeUTF(peerName);
							Vector3D pos = node.getPosition();
							out.writeDouble(pos.getX());
							out.writeDouble(pos.getY());
							out.writeDouble(pos.getZ());
							out.writeDouble(pingMap.getPing(node.getPeerID()).or(0d));
						}
					}				
					Thread.sleep(3000);
				}
			} catch (SocketException e) {
				synchronized (socketThreads) {
					socketThreads.remove(this);
					if (socketThreads.size() == 0) {
						PingMapServiceBinding.getPingMap().removeListener(PingMapHelper.this);
					}
				}	
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
