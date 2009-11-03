package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
//import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IDataTransformation;

public class P2PInputStreamAccessPO<In, Out extends IMetaAttributeContainer<?>> extends
		AbstractIterableSource<Out> implements IP2PInputPO {

	private PipeAdvertisement adv;

	private JxtaSocket socket;

	private Out buffer;

	private ObjectInputStream iStream;

	private boolean done = false;

	private InputStream in;

	final private IDataTransformation<In, Out> transformation;

	private ConnectionHandler con;
	
	private boolean connectToPipe = false;

	private PeerGroup peerGroup;

	class ConnectionHandler extends Thread {

		public ConnectionHandler() {

		}

		public void run() {
			while (!connectToPipe){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (socket == null) {
				try {
					socket = new JxtaSocket(getPeerGroup(), null, adv, 8000, true);
					System.out.println("JxtaSocket erzeugt");
					break;
				} catch (IOException e2) {
					// Timeout passiert weil ein anderer Peer seinen
					// P2PPipePOfalse
					// noch nicht geöffnet hat, daher wird es solange probiert
					// bis der P2PPipePO
					// geöffnet ist. Hier noch sinnvolles Abruchskriterium
					// nötig.
					socket  = null;
					e2.printStackTrace();
				}
			}
		}

	}

	public P2PInputStreamAccessPO(
			IDataTransformation<In, Out> transformation, String adv, PeerGroup peergroup) {
		super();
		this.transformation = transformation;
		this.adv = MessageTool.createPipeAdvertisementFromXml(adv);
		this.peerGroup = peergroup;
		System.out.println("Initialisiere P2PInputStreamAccessPO: "+this.adv.toString());
		
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		con = new ConnectionHandler();
		con.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean hasNext() {
		if (socket == null) {
//			if (con == null || !con.isAlive()) {
//				con = new ConnectionHandler();
//				con.start();
//			}
			if (buffer != null){
				return true;
			}
			return false;
		}
		if (buffer != null) {
			return true;
		}
		try {
			if (this.iStream == null) {
				in = socket.getInputStream();
				this.iStream = new ObjectInputStream(in);
			}

			Object o = this.iStream.readObject();
			if ((o instanceof Integer) && (((Integer) o).equals(0))){
				propagateDone();
				return false;
			}
			
			In inElem = (In) o;

			if (inElem == null) {
				throw new Exception("null element from socket");
			}
			System.out.println("Bekomme Element aus dem Datenstrom");
			buffer = this.transformation.transform(inElem);
			return true;
		} catch (EOFException e) {
			e.printStackTrace();
			propagateDone();
			return false;
		} catch (IOException e) {
			//Hier müsste er dann auf einen HotPeer wechseln
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			// TODO fehlerzustand irgendwie signalisieren?
			propagateDone();
			return false;
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public synchronized void transferNext() {
		if (buffer != null){
			transfer(buffer);
			
		}
		buffer = null;

	}

	@Override
	protected void process_done() {
		done = true;
		try {
			this.iStream.close();
			socket.close();
		} catch (IOException e) {
			// we are done, we don't care anymore for exceptions
		}
	}

	public boolean isConnectedToPipe() {
		return connectToPipe;
	}

	public void setConnectedToPipe(boolean connectToPipe) {
		if(connectToPipe) {
			System.out.println("ConnectToPipe auf true gesetzt bei Adv " +adv.toString());
		}
		this.connectToPipe = connectToPipe;
	}

	@Override
	public void setP2P(boolean p2p) {
		// TODO Auto-generated method stub
		
	}
	
	public PeerGroup getPeerGroup() {
		return peerGroup;
	}


}
