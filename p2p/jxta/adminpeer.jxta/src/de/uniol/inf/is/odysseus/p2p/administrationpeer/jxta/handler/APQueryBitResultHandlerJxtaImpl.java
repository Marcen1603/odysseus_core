package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

/**
 * This class reacts on the answer for a bit to process a query
 * 
 * @author Marco Grawunder
 * 
 */

public class APQueryBitResultHandlerJxtaImpl extends AbstractJxtaMessageHandler {

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public APQueryBitResultHandlerJxtaImpl(
			AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		setInterestedNamespace("BiddingResult");
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
	}

	@Override
	public void handleMessage(Object _msg, String _namespace) {

		Message msg = (Message) _msg;
		String namespace = (String) _namespace;
		String queryId = meas(namespace, "queryId", msg);
		String queryResult = meas(namespace, "result", msg);

		// AdminPeer hat Zuspruch fuer eine Anfrage bekommen.
		if (queryResult.equals("granted")) {
			logAction(queryId,
					"Zusage fuer die Verwaltung der Anfrage bekommen.");
			P2PQuery q = administrationPeerJxtaImpl.getQuery(queryId);
			if (q != null) {
				q.setStatus(Lifecycle.NEW);
				IExecutionListener l = administrationPeerJxtaImpl
						.getListenerForQuery(queryId);
				l.startListener();
			} else {
				logAction(queryId,
						"Gebot fuer nicht bekannte Anfrage bekommen");
			}

		} else if (queryResult.equals("denied")) {
			logAction(queryId,
					"Zusage fuer die Verwaltung der Anfrage NICHT bekommen. Verwerfe Anfrage");
			administrationPeerJxtaImpl.removeQuery(queryId);
		}

	}

}
