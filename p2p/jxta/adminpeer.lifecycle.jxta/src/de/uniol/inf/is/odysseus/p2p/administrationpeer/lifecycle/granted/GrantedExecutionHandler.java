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
package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import java.util.HashMap;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class GrantedExecutionHandler extends AbstractExecutionHandler<JxtaMessageSender> {

	public GrantedExecutionHandler() {
		super(Lifecycle.GRANTED);
	}
	
	public GrantedExecutionHandler(
			GrantedExecutionHandler grantedExecutionHandler) {
		super(grantedExecutionHandler);
	}

	@Override
	public IExecutionHandler<JxtaMessageSender> clone()  {
		return new GrantedExecutionHandler(this);
	}


	@Override
	public void run() {
		IExecutionListenerCallback cb = getExecutionListenerCallback();
		// Erzeuge Message Handler for this query
		GrantedMessageHandler handler = new GrantedMessageHandler(cb.getQuery(), cb, getPeer(), "Granted"+cb.getQuery().getId());
		getPeer().registerMessageHandler(handler);
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		//Sende Anfrage an die bestaetigten Peers
		messageElements.put("queryId", cb.getQuery().getId());
		messageElements.put("pipeAdvertisement", getPeer().getServerResponseAddress());
		for(Subplan s : cb.getQuery().getSubPlans().values()) {
			if(s.getBiddings().size() > 0 && s.getBiddings().get(0) instanceof BidJxtaImpl) {
				messageElements.put("subplanId", s.getId());
				getFunction().sendMessage(PeerGroupTool.getPeerGroup(), MessageTool
						.createSimpleMessage("Granted"+ s.getId(), messageElements), ((BidJxtaImpl)s.getBiddings().get(0)).getResponseSocket(),10);
			}
			
		}
	}

	@Override
	public void setPeer(IOdysseusPeer peer) {
		super.setPeer(peer);
		setFunction((JxtaMessageSender) getPeer().getMessageSender());
	}
	
}
