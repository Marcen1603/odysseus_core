package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.ErrorPopup;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.handler.StreamHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.queryAdministration.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;

public class SocketServerListenerJxtaImpl implements ISocketServerListener {

	private class ConnectionHandler extends Thread {

		private final Socket socket;

		ConnectionHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			sendAndReceiveData(socket);
		}

		private void sendAndReceiveData(Socket socket) {
			try {

				// get the socket output stream
				OutputStream out = socket.getOutputStream();
				// get the socket input stream
				InputStream in = socket.getInputStream();
				ObjectInputStream oin = new ObjectInputStream(in);

				Message msg = null;

				Object o = oin.readObject();

				msg = (Message) o;

				Iterator<String> it = msg.getMessageNamespaces();

				while (it.hasNext()) {
					String namespace = it.next();
					// Gebot von einem Verwaltungs-Peer empfangen
					if (namespace.equals("Bidding")) {
						String query = MessageTool.getMessageElementAsString(
								"Bidding", "query", msg);
						PipeAdvertisement pipeAdv = MessageTool
								.getResponsePipe("Bidding", msg, 0);
						PeerAdvertisement peerAdv = MessageTool
								.getPeerAdvertisement("Bidding", msg);
						((QueryJxtaImpl) ThinPeerJxtaImpl.getInstance()
								.getQueries().get(query)).addBidding(pipeAdv,
								peerAdv);
					}
					if (namespace.equals("UnknownSource")) {
						String queryId = MessageTool.getMessageElementAsString(
								"UnknownSource", "query", msg);
						ThinPeerJxtaImpl.getInstance().getQueries().remove(
								queryId);
						ThinPeerJxtaImpl.getInstance().getGui().removeTab(
								queryId);
						new ErrorPopup(
								"Fehler bei der Übersetzung der Anfrage.");
					}
					if (namespace.equals("ResultStreaming")) {
						StreamHandlerJxtaImpl shandler = new StreamHandlerJxtaImpl(
								MessageTool.getResponsePipe("ResultStreaming",
										msg, 0), MessageTool
										.getMessageElementAsString(
												"ResultStreaming", "queryId",
												msg));
						Thread t = new Thread(shandler);
						t.start();
					}
					if (namespace.equals("QueryFailed")) {
						String queryId = MessageTool.getMessageElementAsString(
								"QueryFailed", "queryId", msg);
						ThinPeerJxtaImpl.getInstance().getQueries().remove(
								queryId);
						ThinPeerJxtaImpl.getInstance().getGui().removeTab(
								queryId);
						new ErrorPopup(
								"<html>Anfrage konnte nicht verteilt werden.<br />Zu wenig Angebote.</html>");
					}
					// ....
				}

				oin.close();
				out.close();
				in.close();

				socket.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private PipeAdvertisement serverPipeAdvertisement;

	private ID pipeId;

	public SocketServerListenerJxtaImpl() {
		this.pipeId = IDFactory.newPipeID(ThinPeerJxtaImpl.getInstance()
				.getNetPeerGroup().getPeerGroupID());
		serverPipeAdvertisement = this.getServerPipeAdvertisement();

	}

	public void run() {
		JxtaServerSocket serverSocket = null;
		try {
			serverSocket = new JxtaServerSocket(ThinPeerJxtaImpl.getInstance()
					.getNetPeerGroup(), serverPipeAdvertisement, 10);
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
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(this.pipeId);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("serverPipe");
		return advertisement;
	}

}
