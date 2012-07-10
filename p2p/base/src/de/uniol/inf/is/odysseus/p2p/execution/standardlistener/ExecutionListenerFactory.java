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
package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class ExecutionListenerFactory implements IExecutionListenerFactory {

	@Override
	public IExecutionListener getNewInstance(P2PQuery query, List<IExecutionHandler<?>> handler) {
		IExecutionListener listener = new ExecutionListener(query);
		listener.registerHandler(handler);
		return listener;
	}

	@Override
	public String getName() {
		return "ExecutionListenerFactory";
	}
}
