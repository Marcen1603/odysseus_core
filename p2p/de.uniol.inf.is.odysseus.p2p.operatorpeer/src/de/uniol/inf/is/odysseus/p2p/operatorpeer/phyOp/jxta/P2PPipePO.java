package de.uniol.inf.is.odysseus.p2p.operatorpeer.phyOp.jxta;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.ILatency;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.ISink;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.ISource;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.Subscription;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

//public class P2PPipePO<M extends ILatency> extends
public class P2PPipePO<M extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<M>, RelationalTuple<M>> {
	
	private String queryId;
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	class ConnectionListener extends Thread {

		public void run() {
			JxtaServerSocket server = null;
			try {
				server = new JxtaServerSocket(OperatorPeerJxtaImpl
						.getInstance().getNetPeerGroup(), adv, 1000);
				server.setSoTimeout(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {

				Socket socket = null;
				try {
					socket = server.accept();

					if (socket != null) {
						synchronized (subscribe) {
							StreamHandler temp = new StreamHandler(socket);
							subscribe.add(temp);
						}

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

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
			while(getSubscribedTo().get(0)==null){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			setConnectToPipe(getSubscribedTo().get(0).getTarget(), 0);
		}

		public void transfer(Object o) {
			try {
				oout.writeObject(o);
				oout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (queryId!=null && OperatorPeerJxtaImpl.getInstance().isGuiEnabled()){
				OperatorPeerJxtaImpl.getInstance().getGui().addOperation(queryId, o.toString());
			}
		}
		
		public void done() {
			try {
				//Signalisieren, dass dieser Peer fertig ist.
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

	private PipeAdvertisement adv;

	public ArrayList<StreamHandler> subscribe = new ArrayList<StreamHandler>();

	public P2PPipePO(String adv) {
		super();
		this.adv = MessageTool.createPipeAdvertisementFromXml(adv);
		ConnectionListener listener = new ConnectionListener();
		listener.start();
	}

	public PipeAdvertisement getAdv() {
		return adv;
	}

	protected void process_next(RelationalTuple<M> object, int port) {

		synchronized (subscribe) {
			for (StreamHandler sh : subscribe) {
				sh.transfer(object);
			}
		}
	}
	
	public void setAdv(PipeAdvertisement adv) {
		this.adv = adv;
	}

	private static void setConnectToPipe(ISink<?> mySink, int depth) {
		for (PhysicalSubscription<? extends ISource<?>> source : mySink
				.getSubscribedTo()) {
			setConnectToPipe(source.getTarget(), depth + 1);
		}
	}

	private static void setConnectToPipe(ISource<?> source, int depth) {
		if (source instanceof ISink<?>) {
			setConnectToPipe((ISink<?>) source, depth);
		} else {
			if (IP2PInputPO.class.isAssignableFrom(source.getClass())){
				((IP2PInputPO) source).setConnectToPipe(true);
				
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
	

}
