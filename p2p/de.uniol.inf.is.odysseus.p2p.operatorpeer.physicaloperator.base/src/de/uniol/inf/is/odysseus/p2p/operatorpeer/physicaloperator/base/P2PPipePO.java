package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.base.IMetaAttribute; //import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool; //import de.uniol.inf.is.odysseus.queryexecution.po.base.object.ILatency;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription; //import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.ISink;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.ISource;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.Subscription;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

//public class P2PPipePO<M extends ILatency> extends
public class P2PPipePO<M extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<M>, RelationalTuple<M>> {

	private String queryId;

	private PipeAdvertisement adv;

	public ArrayList<StreamHandler> subscribe = new ArrayList<StreamHandler>();

	private PeerGroup peerGroup;

	@Override
	protected void process_open() throws OpenFailedException {
		System.out.println("Starte Pipe Thread vor der Blockierung");
		JxtaServerSocket server = null;
		try {
			server = new JxtaServerSocket(getPeerGroup(), adv, 1000);
			System.out.println("Jxta ServerSocket Adv: " + adv.toString());
			server.setSoTimeout(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		while (true) {
		//TODO: mehrere Verbindungen zur Pipe ermöglichen

			Socket socket = null;
			try {
				System.out.println("immernoch vor");
				socket = server.accept();
				System.out.println("danach Blockierung");
				if (socket != null) {
					synchronized (subscribe) {
						System.out.println("Subscriber gefunden für Pipe: "
								+ getAdv().toString());
						System.out.println("Server Pipe Advertisement"
								+ server.getPipeAdv());
						StreamHandler temp = new StreamHandler(socket);

						subscribe.add(temp);
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
	}
	
//	class ConnectionListener extends Thread {
//
//		public void run() {
//			System.out.println("Starte Pipe Thread");
//			JxtaServerSocket server = null;
//			try {
//				server = new JxtaServerSocket(getPeerGroup(), adv, 1000);
//				System.out.println("Jxta ServerSocket Adv: " + adv.toString());
//				server.setSoTimeout(0);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			while (true) {
//
//				Socket socket = null;
//				try {
//					socket = server.accept();
//
//					if (socket != null) {
//						synchronized (subscribe) {
//							System.out.println("Subscriber gefunden für Pipe: "
//									+ getAdv().toString());
//							System.out.println("Server Pipe Advertisement"
//									+ server.getPipeAdv());
//							StreamHandler temp = new StreamHandler(socket);
//
//							subscribe.add(temp);
//						}
//
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	class StreamHandler {
		InputStream in;
		ObjectInputStream oin;
		ObjectOutputStream oout;
		OutputStream out;
		Socket socket;

		public StreamHandler(Socket socket) {
			this.socket = socket;
			try {
				out = socket.getOutputStream();
				in = socket.getInputStream();
				oout = new ObjectOutputStream(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (getSubscribedTo().get(0) == null) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (PhysicalSubscription subscription : getSubscribedTo()) {
				System.out.println("Setze alle Subscriber auf connected: "
						+ getSubscribedTo().get(0).getTarget().toString());
				setConnectedToPipe((ISource) subscription.getTarget(), 0);
			}

			// setConnectedToPipe(getSubscribedTo().get(0).getTarget(), 0);
		}

		public void transfer(Object o) {
			System.out.println("Pipe transferiert " + o);
			try {
				oout.writeObject(o);
				oout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// TODO: GUI AUSGABE durch Registrierung?!?!
			// if (queryId!=null &&
			// OperatorPeerJxtaImpl.getInstance().isGuiEnabled()){
			// OperatorPeerJxtaImpl.getInstance().getGui().addOperation(queryId,
			// o.toString());
			// }
		}

		public void done() {
			try {
				// Signalisieren, dass dieser Peer fertig ist.
				oout.writeObject(new Integer(0));
				oout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
				oout.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public P2PPipePO(String adv, PeerGroup peerGroup) {
		super();
		this.peerGroup = peerGroup;
		this.adv = MessageTool.createPipeAdvertisementFromXml(adv);
		System.out.println("Initialisiere P2PPipePO: " + this.adv.toString());
//		ConnectionListener listener = new ConnectionListener();
//		listener.start();
	}

	protected void process_next(RelationalTuple<M> object, int port) {
		System.out.println("PipePO process next");
		synchronized (subscribe) {
			for (StreamHandler sh : subscribe) {
				sh.transfer(object);
			}
		}
	}


	private static void setConnectedToPipe(ISink<?> mySink, int depth) {
		for (PhysicalSubscription<? extends ISource<?>> source : mySink
				.getSubscribedTo()) {
			setConnectedToPipe(source.getTarget(), depth + 1);
		}
	}

	private static void setConnectedToPipe(ISource<?> source, int depth) {
		if (source instanceof ISink<?>) {
			setConnectedToPipe((ISink<?>) source, depth);
		} else {
			if (IP2PInputPO.class.isAssignableFrom(source.getClass())) {
				((IP2PInputPO) source).setConnectedToPipe(true);

			}

		}
	}

	protected void process_done() {
		synchronized (subscribe) {
			for (StreamHandler sh : subscribe) {
				sh.done();
			}
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public void setAdv(PipeAdvertisement adv) {
		this.adv = adv;
	}

	public PipeAdvertisement getAdv() {
		return adv;
	}

	public PeerGroup getPeerGroup() {
		return peerGroup;
	}

	public void setPeerGroup(PeerGroup peerGroup) {
		this.peerGroup = peerGroup;
	}

}
