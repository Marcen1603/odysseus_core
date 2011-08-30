/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.listener;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;
import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.listener.IP2PPOEventListener;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;

public class P2PPOEventListenerJxtaImpl implements IP2PPOEventListener {

	private String queryId;

	private PipeAdvertisement pipeAdvertisement;

	private JxtaSocket socket;

	private ObjectOutputStream oout;

	private ILogListener log;

	public P2PPOEventListenerJxtaImpl(String queryId, PipeAdvertisement pipeAdv, OperatorPeerJxtaImpl peer) {
		this.queryId = queryId;
		this.pipeAdvertisement = pipeAdv;
		this.log = peer.getLog();

		try {
			socket = new JxtaSocket(peer.getNetPeerGroup(), null, pipeAdv, 8000, true);
			socket.setSoTimeout(0);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		OutputStream out = null;
		try {
			out = socket.getOutputStream();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		try {
			oout = new ObjectOutputStream(new BufferedOutputStream(out));
		} catch (IOException e2) {
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
	public void eventOccured(IEvent<?,?> poEvent, long eventNanoTime) {
		log.logEvent(queryId, "Event aufgetreten: "
				+ poEvent);
		sendEvent((POEvent)poEvent);
	}
 
	@Override
	public void sendEvent(POEvent poEvent) {
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
		messageElements.put("event", poEvent.getEventType().toString());
		Message msg = MessageTool.createOdysseusMessage(OdysseusMessageType.Event, messageElements);

		try {
			oout.writeObject(msg);
			oout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
