package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IStreamHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class StreamHandlerJxtaImpl implements IStreamHandler {

	static Logger logger = LoggerFactory.getLogger(StreamHandlerJxtaImpl.class);

	private PipeAdvertisement adv;

	private String queryId;
	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	private ILogListener log;

	public StreamHandlerJxtaImpl(PipeAdvertisement adv, String queryId,
			ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.adv = adv;
		this.queryId = queryId;
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
		this.log = thinPeerJxtaImpl.getLog();
		logger.debug("Initialisiere StreamHandler auf dem Thin-Peer: "
				+ this.adv.toString());
	}

	@Override
	public void run() {
		JxtaSocket socket = null;
		ObjectInputStream iStream = null;
		InputStream in = null;
		while (socket == null) {
			try {
				socket = new JxtaSocket(thinPeerJxtaImpl.getNetPeerGroup(), adv);
				socket.setSoTimeout(0);
				break;
			} catch (IOException e2) {
				socket = null;
				// e2.printStackTrace();
			}
		}
	//	logger.debug("Connected to "+adv);
		try {
			in = socket.getInputStream();
			iStream = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object o = null;
		while (iStream != null) {
			try {
				o = iStream.readObject();
				if (o != null && (o instanceof Integer) && (((Integer) o).equals(0))) {
					iStream.close();
					socket.close();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				// } catch (ClassNotFoundException e) {
				// e.printStackTrace();
			}
			// Empfangene Daten der Gui hinzufuegen
			if (!thinPeerJxtaImpl.getGui().isEnabled())
				thinPeerJxtaImpl.getGui().setEnabled(true);
			log.addResult(queryId, o);
		}

	}

}
