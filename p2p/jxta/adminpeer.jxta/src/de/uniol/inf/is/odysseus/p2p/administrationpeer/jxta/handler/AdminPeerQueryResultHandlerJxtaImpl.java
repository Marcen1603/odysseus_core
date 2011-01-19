package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class AdminPeerQueryResultHandlerJxtaImpl extends AbstractMessageHandler {

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public AdminPeerQueryResultHandlerJxtaImpl(AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		setInterestedNamespace("BiddingResult");
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
	}

	@Override
	public void handleMessage(Object _msg, String _namespace) {

		Message msg = (Message) _msg;
		String namespace = (String) _namespace;
		String queryId = MessageTool.getMessageElementAsString(namespace,
				"queryId", msg);
		String queryResult = MessageTool.getMessageElementAsString(namespace,
				"result", msg);

		// AdminPeer hat Zuspruch fuer eine Anfrage bekommen.
		if (queryResult.equals("granted")) {
			Log.logAction(queryId,
					"Zusage fuer die Verwaltung der Anfrage bekommen.");
			for (Query q : administrationPeerJxtaImpl
					.getQueries().keySet()) {
				if (q.getId().equals(queryId)) {
					// Einstieg in die Ausfuehrungsumgebung
					q.setStatus(Lifecycle.NEW);
					administrationPeerJxtaImpl.getQueries()
							.get(q).startListener();
				}
			}

		} else if (queryResult.equals("denied")) {
			for (Query q : administrationPeerJxtaImpl
					.getQueries().keySet()) {
				if (q.getId() == queryId) {
					administrationPeerJxtaImpl.getQueries()
							.remove(q);
				}
			}
		}

	}

}
