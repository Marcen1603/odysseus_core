package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.handler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IStreamHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;

public class StreamHandlerJxtaImpl implements IStreamHandler {

	private PipeAdvertisement adv;

	private JxtaSocket socket = null;

	private ObjectInputStream iStream;

	private InputStream in;

	private String queryId;

	public StreamHandlerJxtaImpl(PipeAdvertisement adv, String queryId) {
		super();
		this.adv = adv;
		this.queryId = queryId;
		System.out.println("Initialisiere StreamHandler auf dem Thin-Peer: "+this.adv.toString());
	}

	@Override
	public void run() {
		System.out.println("Bin im StreamHandler Thread");
		while (socket == null) {
			try {
				System.out.println("Bauen Socket auf mit diesem Adv: "+adv.toString());
				socket = new JxtaSocket(ThinPeerJxtaImpl.getInstance()
						.getNetPeerGroup(), null, adv, 8000, true);
				break;
			} catch (IOException e2) {
				socket = null;
			}
		}

		try {
			in = socket.getInputStream();
			this.iStream = new ObjectInputStream(new BufferedInputStream(in));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object o = null;
		while (true) {
			System.out.println("Lesen des Objektes");
			try {
				o = iStream.readObject();
				if ((o instanceof Integer) && (((Integer) o).equals(0))){
					iStream.close();
					socket.close();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Empfangene Daten der Gui hinzufuegen
			if(!ThinPeerJxtaImpl.getInstance().getGui().isEnabled())
				ThinPeerJxtaImpl.getInstance().getGui().setEnabled(true);
			ThinPeerJxtaImpl.getInstance().getGui().addResult(queryId, o);
		}

	}

}
