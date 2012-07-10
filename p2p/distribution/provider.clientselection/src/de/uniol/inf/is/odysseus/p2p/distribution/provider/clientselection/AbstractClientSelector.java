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
package de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection;

import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;



public abstract class AbstractClientSelector<C> implements IClientSelector {

	
	private C callback;

	public C getCallback() {
		return this.callback;
	}

	public void setCallback(C callback) {
		this.callback = callback;
	}

	private int time;
	private P2PQuery query;
	final protected ILogListener log;

	public AbstractClientSelector(int time, P2PQuery query, C callback, ILogListener log) {
		this.query = query;
		this.time = time;
		this.callback = callback;
		this.log = log;
	}
	
	public AbstractClientSelector(int time, P2PQuery query, ILogListener log) {
		this.query = query;
		this.time = time;
		this.callback = null;
		this.log = log;
	}
	
	@Override
	public void setTimetoWait(int time) {
		this.time = time;
	}

	@Override
	public abstract void run();

	@Override
	public P2PQuery getQuery() {
		return query;
	}
	
	public int getTime() {
		return time;
	}
}
