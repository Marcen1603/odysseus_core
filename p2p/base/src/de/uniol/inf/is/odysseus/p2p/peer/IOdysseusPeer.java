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
package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerFactory;

public interface IOdysseusPeer extends IPeer, IQueryProvider {
	void registerMessageHandler(IMessageHandler messageHandler);
	Object getServerResponseAddress();
	void bindExecutionListenerFactory(IExecutionListenerFactory factory);
	void unbindExecutionListenerFactory(IExecutionListenerFactory factory);
	void bindExecutionHandler(IExecutionHandler<?> handler);
	void unbindExecutionHandler(IExecutionHandler<?> handler);
	void initLocalMessageHandler();
	void initMessageSender();
	void initLocalExecutionHandler();
	void registerMessageHandler(List<IMessageHandler> messageHandler);
	void deregisterMessageHandler(IMessageHandler messageHandler);
	IMessageSender<?,?,?> getMessageSender();
	ILogListener getLog();

}
