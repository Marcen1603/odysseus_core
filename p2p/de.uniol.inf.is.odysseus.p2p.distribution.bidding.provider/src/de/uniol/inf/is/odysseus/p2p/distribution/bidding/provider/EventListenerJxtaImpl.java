package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Iterator;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;

class ConnectionHandler extends Thread {

	private final Socket socket;

	ConnectionHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		sendAndReceiveData(socket);
	}

	private void sendAndReceiveData(Socket socket) {
		try {
			InputStream in = socket.getInputStream();
			ObjectInputStream oin = new ObjectInputStream(in);
			while (true) {
				
				Message msg = null;
				msg = (Message) oin.readObject();
				Iterator<String> it = msg.getMessageNamespaces();
				String namespace = "";
				while (it.hasNext()) {
					namespace = it.next();
					// AdminPeer erhält die Auswertung für sein Gebot vom
					// ThinPeer
					if (namespace.equals("Event")) {
						String queryId = MessageTool.getMessageElementAsString(
								"Event", "queryId", msg);
						String event = MessageTool.getMessageElementAsString(
								"Event", "event", msg);
						System.out.println("Event, welches nicht ausgegben wird:" +queryId+" "+event);
//						AdministrationPeerJxtaImpl.getInstance().getGui()
//								.addEvent(queryId, event);
					}

				}
			}
			// oin.close();
			// out.close();
			// in.close();
			//
			// socket.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

public class EventListenerJxtaImpl implements IEventListener {

	PipeAdvertisement pipeAdv = null;

	public void setPipeAdv(PipeAdvertisement pipeAdv) {
		this.pipeAdv = pipeAdv;
	}


	public EventListenerJxtaImpl() {
	}

	
	public void run() {
		JxtaServerSocket serverSocket = null;
		try {
			serverSocket = new JxtaServerSocket(PeerGroupTool.getPeerGroup(), pipeAdv, 1000);
			serverSocket.setSoTimeout(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				if (socket != null) {
					ConnectionHandler temp = new ConnectionHandler(socket);
					temp.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public PipeAdvertisement getPipeAdv() {
		return pipeAdv;
	}

}
