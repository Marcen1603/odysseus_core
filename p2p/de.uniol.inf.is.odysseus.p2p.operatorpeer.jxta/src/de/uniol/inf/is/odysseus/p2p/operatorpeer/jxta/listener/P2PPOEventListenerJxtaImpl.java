package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.listener;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.listener.IP2PPOEventListener;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.logging.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;

public class P2PPOEventListenerJxtaImpl implements IP2PPOEventListener {

	private String queryId;

	private PipeAdvertisement pipeAdvertisement;

	private JxtaSocket socket;

	private ObjectOutputStream oout;

	public P2PPOEventListenerJxtaImpl(String queryId, PipeAdvertisement pipeAdv) {
		this.queryId = queryId;
		this.pipeAdvertisement = pipeAdv;

		try {
			socket = new JxtaSocket(OperatorPeerJxtaImpl.getInstance()
					.getNetPeerGroup(), null, pipeAdv, 8000, true);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		OutputStream out = null;
		try {
			out = socket.getOutputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			oout = new ObjectOutputStream(new BufferedOutputStream(out));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public String getQueryId() {
		return queryId;
	}

	public PipeAdvertisement getPipeAdvertisement() {
		return pipeAdvertisement;
	}

	public void setPipeAdvertisement(PipeAdvertisement pipeAdvertisement) {
		this.pipeAdvertisement = pipeAdvertisement;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	@Override
	public void poEventOccured(POEvent poEvent) {
		Log.logEvent(queryId, "Event aufgetreten: "
				+ poEvent.getPOEventType().toString());
		sendEvent(poEvent);
	}

	@Override
	public void sendEvent(POEvent poEvent) {
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
		messageElements.put("event", poEvent.getPOEventType().toString());
		Message msg = MessageTool.createSimpleMessage("Event", messageElements);

		try {
			oout.writeObject(msg);
			oout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
