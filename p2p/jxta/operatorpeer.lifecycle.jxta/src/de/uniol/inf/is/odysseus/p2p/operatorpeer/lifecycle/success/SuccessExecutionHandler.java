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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.success;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class SuccessExecutionHandler<F> extends AbstractExecutionHandler<F> {

	static Logger logger = LoggerFactory
			.getLogger(SuccessExecutionHandler.class);
	
	static private Map<Lifecycle, Lifecycle> followingCycle = new HashMap<Lifecycle, Lifecycle>();
	{
		followingCycle.put(Lifecycle.GRANTED, Lifecycle.RUNNING);
		followingCycle.put(Lifecycle.RUNNING, Lifecycle.TERMINATED);		
	}
	
	public SuccessExecutionHandler() {
		super(Lifecycle.SUCCESS);
	}

	public SuccessExecutionHandler(
			SuccessExecutionHandler<F> successExecutionHandler) {
		super(successExecutionHandler);
	}

	
	@Override
	public IExecutionHandler<F> clone()  {
		return new SuccessExecutionHandler<F>(this);
	}

	@Override
	public void run() {
		P2PQuery q = getExecutionListenerCallback().getQuery();
		Lifecycle oldState = q.getHistory().get(getExecutionListenerCallback().getQuery().getHistory().size()-2);
		Lifecycle newState = followingCycle.get(oldState);
		logger.debug("Changing state from "+oldState+" to "+newState);
		getExecutionListenerCallback().changeState(newState);
	}
	

}
