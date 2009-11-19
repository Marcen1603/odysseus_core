package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.Query.Status;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.logging.Log;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.*;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;

public class SocketServerListenerJxtaImpl implements ISocketServerListener {

	private class ConnectionHandler extends Thread {

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
				OutputStream out = socket.getOutputStream();
				InputStream in = socket.getInputStream();
				ObjectInputStream oin = new ObjectInputStream(in);
				Message msg = null;
				msg = (Message) oin.readObject();
				Iterator<String> it = msg.getMessageNamespaces();
				String namespace="";
				while (it.hasNext()) {
					namespace = it.next();
					//AdminPeer erhält die Auswertung für sein Gebot vom ThinPeer
					if (namespace.equals("BiddingResult")) {
						AdministrationPeerJxtaImpl.getInstance().getQueryResultHandler().handleQueryResult(msg, "BiddingResult");
					}
					//Thin-Peer bestimmt den Admin-Peer,
					//der eine Anfrage ausführen soll.
					if (namespace.equals("DoQuery")){
						String queryId = MessageTool.getMessageElementAsString(
								"DoQuery", "queryId", msg);
						String query = MessageTool.getMessageElementAsString(
								"DoQuery", "query", msg);
						String language = MessageTool.getMessageElementAsString(
								"DoQuery", "language", msg);
						PipeAdvertisement adv = MessageTool.createPipeAdvertisementFromXml(MessageTool.getMessageElementAsString(
								"DoQuery", "customized_advertisement", msg));
						QueryJxtaImpl q = new QueryJxtaImpl();
						q.setLanguage(language);
						q.setQuery(query);
						q.setId(queryId);
						q.setResponseSocketThinPeer(adv);
						q.setStatus(Status.BIDDING);
						synchronized(AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries()){
							AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().put(queryId, q);
							Log.addQuery(queryId);
							Log.logAction(queryId, "Anfrage direkt empfangen !");
						}
						AdministrationPeerJxtaImpl.getInstance().getQueryResultHandler().handleQueryResult(msg, "DoQuery");
					}
					
					if (namespace.equals("ExecutionBid")) {
						// Gebot für die Ausführung einer Anfrage bzw. eines
						// Teils der Anfrage von einem OperatorPeer erhalten.
						String queryId = MessageTool.getMessageElementAsString(
								"ExecutionBid", "queryId", msg);
						
						PipeAdvertisement pipeAdv = MessageTool
								.getResponsePipe("ExecutionBid", msg, 0);
						
						String peerId = MessageTool.getMessageElementAsString(
								"ExecutionBid", "peerId", msg);
						//TODO: SubplanId in Nachricht einbauen
						String subplanId = MessageTool.getMessageElementAsString(
								"ExecutionBid", "subplanId", msg);
						Log.logAction(queryId, "Gebot von einem Operator-Peer eingegangen.");
						//Füge das Gebot der Query hinzu
						((QueryJxtaImpl)AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().get(queryId)).addBidding(pipeAdv, peerId, subplanId);
						Log.addBid(queryId, AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().get(queryId).getSubPlans().get(subplanId).getBiddings().size());
					}
					if (namespace.equals("Event")) {
						String queryId = MessageTool.getMessageElementAsString("Event", "queryId", msg);
						String event = MessageTool.getMessageElementAsString("Event", "event", msg);
						AdministrationPeerJxtaImpl.getInstance().getGui().addEvent(queryId, event);
					}
					// ....
				}
				
				oin.close();
				out.close();
				in.close();

				socket.close();
				

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private PipeAdvertisement serverPipeAdvertisement;
	
	

	public void setServerPipeAdvertisement(PipeAdvertisement serverPipeAdvertisement) {
		this.serverPipeAdvertisement = serverPipeAdvertisement;
	}

	public SocketServerListenerJxtaImpl() {
		serverPipeAdvertisement = AdvertisementTools.getServerPipeAdvertisement(AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup());
	}

	public void run() {
		JxtaServerSocket serverSocket = null;
		try {
			serverSocket = new JxtaServerSocket(AdministrationPeerJxtaImpl
					.getInstance().getNetPeerGroup(), serverPipeAdvertisement,
					10);
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

	public PipeAdvertisement getServerPipeAdvertisement() {
		return serverPipeAdvertisement;
	}
	
	

}
