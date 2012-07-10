/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;

public class EventMessageHandler extends AbstractJxtaMessageHandler {

	public EventMessageHandler(ILogListener log) {
		super(log,"ProviderEvents");
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String event = meas(namespace, "event", (Message) msg);
		String queryId = meas(namespace, "queryId", (Message) msg);
		log.addEvent(queryId, event);
	}

}
