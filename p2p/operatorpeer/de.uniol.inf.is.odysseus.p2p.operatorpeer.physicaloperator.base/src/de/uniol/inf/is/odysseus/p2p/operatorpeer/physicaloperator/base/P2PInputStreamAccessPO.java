package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.access.AbstractInputStreamAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.access.IDataTransformation;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class P2PInputStreamAccessPO<In, Out extends IMetaAttributeContainer<?>>
		extends AbstractInputStreamAccessPO<In, Out> {

	private PipeAdvertisement adv;
	private JxtaSocket socket;

	private ConnectionHandler con;
	private PeerGroup peerGroup;

	class ConnectionHandler extends Thread {

		@Override
		public void run() {
			while (socket == null) {
				try {
					socket = new JxtaSocket(getPeerGroup(), null, adv, 15000,
							true);
					
					System.out.println("JxtaSocket erzeugt " + adv.toString());
					break;
				} catch (IOException e2) {
					// Timeout passiert weil ein anderer Peer seinen
					// P2PPipePOfalse
					// noch nicht geöffnet hat, daher wird es solange probiert
					// bis der P2PPipePO
					// geöffnet ist. Hier noch sinnvolles Abruchskriterium
					// nötig.
					socket = null;
					e2.printStackTrace();
				}
			}
		}

	}

	public P2PInputStreamAccessPO(IDataTransformation<In, Out> transformation,
			String adv, PeerGroup peergroup) {
		super(transformation);
		this.adv = MessageTool.createPipeAdvertisementFromXml(adv);
		this.peerGroup = peergroup;
		System.out.println("Initialisiere P2PInputStreamAccessPO: "
				+ this.adv.toString());

	}

	@Override
	protected void process_open() throws OpenFailedException {

		done = false;
		con = new ConnectionHandler();
		con.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean hasNext() {
		if (socket == null) {
			if (buffer != null) {
				return true;
			}
			return false;
		}
		if (buffer != null) {
			return true;
		}
		try {
			if (this.iStream == null) {
				InputStream in = socket.getInputStream();
				this.iStream = new ObjectInputStream(in);
			}
			Object o = this.iStream.readObject();
			if ((o instanceof Integer) && (((Integer) o).equals(0))) {
				propagateDone();
				return false;
			} else if (o instanceof RelationalTuple) {
				System.out.println("tuple received "+adv.getID().toString() +" "+o);
			}

			In inElem = (In) o;

			if (inElem == null) {
				throw new Exception("null element from socket");
			}
			buffer = this.transformation.transform(inElem);
			return true;
		} catch (EOFException e) {
			e.printStackTrace();
			propagateDone();
			return false;
		} catch (IOException e) {
			// Hier müsste er dann auf einen HotPeer wechseln
			e.printStackTrace();
			return false;
		} catch (Exception e) {
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
	protected void process_done() {
		done = true;
		try {
			this.iStream.close();
			socket.close();
		} catch (IOException e) {
			// we are done, we don't care anymore for exceptions
		}
	}


	public PeerGroup getPeerGroup() {
		return peerGroup;
	}
	
	@Override
	public P2PInputStreamAccessPO<In, Out> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}


}
