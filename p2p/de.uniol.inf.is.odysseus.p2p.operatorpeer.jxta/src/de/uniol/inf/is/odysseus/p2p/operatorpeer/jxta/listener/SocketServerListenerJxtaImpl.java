package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.listener;

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
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.AbstractOperatorPeer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.listener.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.logging.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.Query;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;

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

		private synchronized void sendAndReceiveData(Socket socket) {
			try {
				OutputStream out = socket.getOutputStream();
				InputStream in = socket.getInputStream();

				ObjectInputStream oin = new ObjectInputStream(in);

				Message msg = null;

				Object o = oin.readObject();

				msg = (Message) o;

				Iterator<String> it = msg.getMessageNamespaces();

				while (it.hasNext()) {
					String namespace = it.next();

					if (namespace.equals("QueryResult")) {
						OperatorPeerJxtaImpl.getInstance()
								.getQueryResultHandler().handleQueryResult(msg,
										"QueryResult");
					} else if (namespace.equals("DoQuery")) {
						String queryId = MessageTool.getMessageElementAsString(
								"DoQuery", "queryId", msg);
						QueryJxtaImpl q = new QueryJxtaImpl();
						q.setId(queryId);

						String language = MessageTool
								.getMessageElementAsString("DoQuery",
										"language", msg);

						String pipeAdv = MessageTool.getMessageElementAsString(
								"DoQuery", "adminPipeAdvertisement", msg);

						q.setAdminPeerPipe(MessageTool
								.createPipeAdvertisementFromXml(pipeAdv));

						q.setLanguage(language);
						if (OperatorPeerJxtaImpl.getInstance().getQueries()
								.get(queryId) == null) {
							OperatorPeerJxtaImpl.getInstance().getQueries()
									.put(queryId, q);
						}
						OperatorPeerJxtaImpl.getInstance()
								.getQueryResultHandler().handleQueryResult(msg,
										"DoQuery");
					} else if (namespace.equals("HotQuery")) {
						ILogicalOperator ao = (ILogicalOperator) MessageTool
								.getObjectFromMessage(msg, 0);
//						RelationalPipesTransform trafo = new RelationalPipesTransform();
//						trafo.init();
//						ISource<?> source = null;
//						if (OperatorPeerJxtaImpl.getTrafoMode().equals("PNID")) {
//							
//							try {
//								source = trafo.transformPNID(ao,
//										PriorityMode.NO_PRIORITY);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						} else if ((OperatorPeerJxtaImpl.getTrafoMode()
//								.equals("TI"))) {
//							source = trafo.transformTI(ao,
//									PriorityMode.NO_PRIORITY);
//						}
						// TODO: Im Moment zu Testzwecken hart gecoded. ändern!!!
						IIterableSource<?> source = (IIterableSource<?>) getoPeer().getTrafo().transform(ao, new TransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"));
//						OperatorPeerJxtaImpl.getInstance().getScheduler().getSources().add(source);
//						OperatorPeerJxtaImpl.getInstance().getScheduler()
//								.addPlan(source, Thread.NORM_PRIORITY);
						String queryId = MessageTool.getMessageElementAsString(
								"HotQuery", "queryId", msg);

						if (OperatorPeerJxtaImpl.getInstance().getQueries()
								.get(queryId) == null) {
							Log.addQuery(queryId);
							QueryJxtaImpl q = new QueryJxtaImpl();
							q.setId(queryId);
							OperatorPeerJxtaImpl.getInstance().getQueries()
									.put(queryId, q);
						}
						OperatorPeerJxtaImpl.getInstance().getQueries().get(
								queryId).setStatus(Query.Status.HOTQUERY);
						Log
								.logAction(queryId,
										"Habe eine Aufforderung bekommen, als HotPeer für diese Anfrage zu agieren.");
						//TODO: Executor hier einbauen
//						Log.setScheduler(queryId, OperatorPeerJxtaImpl
//								.getInstance().getScheduler().getClass()
//								.getSimpleName());
//						Log.setSchedulerStrategy(queryId, OperatorPeerJxtaImpl
//								.getInstance().getSchedulerStrategy()
//								.getClass().getSimpleName());
						try {
							source.open();
						} catch (OpenFailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (!OperatorPeerJxtaImpl.getInstance().getExecutor().isRunning()) {
							OperatorPeerJxtaImpl.getInstance().getExecutor().startExecution();
//							OperatorPeerJxtaImpl.getInstance().getScheduler()
//									.startScheduling();
						}
					}
				}

				oin.close();
				out.close();
				in.close();

				socket.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private PipeAdvertisement serverPipeAdvertisement;

	private ID pipeId;

	private AbstractOperatorPeer oPeer;


	public SocketServerListenerJxtaImpl(AbstractOperatorPeer aPeer) {
		this.pipeId = IDFactory.newPipeID(OperatorPeerJxtaImpl.getInstance()
				.getNetPeerGroup().getPeerGroupID());
		serverPipeAdvertisement = this.getServerPipeAdvertisement();
		this.oPeer = aPeer;
	}

	public PipeAdvertisement getServerPipeAdvertisement() {
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(this.pipeId);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("serverPipe");
		return advertisement;
	}

	@Override
	public void run() {
		JxtaServerSocket serverSocket = null;
		try {
			serverSocket = new JxtaServerSocket(OperatorPeerJxtaImpl
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
	
	public AbstractOperatorPeer getoPeer() {
		return oPeer;
	}
}
