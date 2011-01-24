package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class AdminPeerQueryResultHandlerJxtaImpl extends AbstractMessageHandler {

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public AdminPeerQueryResultHandlerJxtaImpl(
			AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
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
			P2PQuery q = administrationPeerJxtaImpl.getQuery(queryId);
			if (q != null){
				q.setStatus(Lifecycle.NEW);
				IExecutionListener l = administrationPeerJxtaImpl.getListenerForQuery(queryId);
				l.startListener();
			}else{
				Log.logAction(queryId, "Gebot fuer nicht bekannte Anfrage bekommen");
			}

		} else if (queryResult.equals("denied")) {
			Log.logAction(queryId,
					"Zusage fuer die Verwaltung der Anfrage NICHT bekommen. Verwerfe Anfrage");
			administrationPeerJxtaImpl.removeQuery(queryId);
		}

	}

}
