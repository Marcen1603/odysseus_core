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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.granted;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class GrantedExecutionHandler extends AbstractExecutionHandler<JxtaMessageSender>{

	static Logger logger = LoggerFactory.getLogger(GrantedExecutionHandler.class);
		
	final private List<GrantedMessageHandler> handlerList;
	
	public GrantedExecutionHandler() {
		super(Lifecycle.GRANTED);
		handlerList = new ArrayList<GrantedMessageHandler>();
	}

	public GrantedExecutionHandler(
			GrantedExecutionHandler grantedExecutionHandler) {
		super(grantedExecutionHandler);
		handlerList = new ArrayList<GrantedMessageHandler>(grantedExecutionHandler.handlerList);
	}

	@Override
	public IExecutionHandler<JxtaMessageSender> clone()  {
		return new GrantedExecutionHandler(this);
	}

	@Override
	public void run() {
		for(String sub : getExecutionListenerCallback().getQuery().getSubPlans().keySet()) {
							logger.debug("Erzeuge GrantedMessageHandler " +"Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+sub);
				GrantedMessageHandler handl = new GrantedMessageHandler(getExecutionListenerCallback(), "Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+sub, getFunction(), log);
				this.handlerList.add(handl);
		}
		
		getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
	}
	
	@Override
	public void setPeer(IOdysseusPeer peer) {
		super.setPeer(peer);
		setFunction((JxtaMessageSender) getPeer().getMessageSender());
		this.log = getPeer().getLog();
	}

}
