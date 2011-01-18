package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IStreamHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class StreamHandlerJxtaImpl implements IStreamHandler {

	static Logger logger = LoggerFactory.getLogger(StreamHandlerJxtaImpl.class);
	
	private static final int TIMEOUT = 8000;

	private PipeAdvertisement adv;

	private JxtaSocket socket = null;

	private ObjectInputStream iStream;

	private InputStream in;

	private String queryId;

	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public StreamHandlerJxtaImpl(PipeAdvertisement adv, String queryId, ThinPeerJxtaImpl thinPeerJxtaImpl) {
		super();
		this.adv = adv;
		this.queryId = queryId;
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
		logger.debug("Initialisiere StreamHandler auf dem Thin-Peer: "+this.adv.toString());
	}

	@Override
	public void run() {
		logger.debug("Bin im StreamHandler Thread");
		while (socket == null) {
			try {
				logger.debug("Bauen Socket auf mit diesem Adv: "+adv.toString());
				socket = new JxtaSocket(thinPeerJxtaImpl
						.getNetPeerGroup(), null, adv, TIMEOUT, true);
				break;
			} catch (IOException e2) {
				socket = null;
				e2.printStackTrace();
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
			logger.debug("Lesen des Objektes");
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
				e.printStackTrace();
			}
			// Empfangene Daten der Gui hinzufuegen
			if(!thinPeerJxtaImpl.getGui().isEnabled())
				thinPeerJxtaImpl.getGui().setEnabled(true);
			Log.addResult(queryId, o);
		}

	}

}
