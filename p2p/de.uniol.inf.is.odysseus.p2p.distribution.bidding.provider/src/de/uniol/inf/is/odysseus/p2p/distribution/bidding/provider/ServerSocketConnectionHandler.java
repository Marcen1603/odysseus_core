package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.Log;
import de.uniol.inf.is.odysseus.p2p.Query.Status;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.IServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;

public class ServerSocketConnectionHandler implements IServerSocketConnectionHandler{

		private Socket socket;
		private BiddingProvider distributionProvider;

		public ServerSocketConnectionHandler(BiddingProvider distributionProvider) {
			this.distributionProvider = distributionProvider;
		}
		
		public BiddingProvider getDistributionProvider() {
			return distributionProvider;
		}

		public Socket getSocket() {
			return socket;
		}


		public void setSocket(Socket socket) {
			this.socket = socket;
		}


		public ServerSocketConnectionHandler(Socket socket) {
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

						getDistributionProvider().getQueryResultHandler().handleQueryResult(msg, "BiddingResult");
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
						synchronized(getDistributionProvider().getManagedQueries()){
							getDistributionProvider().getManagedQueries().put(queryId, q);
							Log.addQuery(queryId);
							Log.logAction(queryId, "Anfrage direkt empfangen !");
						}
						getDistributionProvider().getQueryResultHandler().handleQueryResult(msg, "DoQuery");
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
						((QueryJxtaImpl)getDistributionProvider().getManagedQueries().get(queryId)).addBidding(pipeAdv, peerId, subplanId);
						Log.addBid(queryId, getDistributionProvider().getManagedQueries().get(queryId).getSubPlans().get(subplanId).getBiddings().size());
					}
					if (namespace.equals("Event")) {
						String queryId = MessageTool.getMessageElementAsString("Event", "queryId", msg);
						String event = MessageTool.getMessageElementAsString("Event", "event", msg);
						Log.getWindow().addEvent(queryId, event);
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
