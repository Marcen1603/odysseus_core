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
package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;

public abstract class AbstractSplittingStrategy implements ISplittingStrategy {

	private IExecutionListenerCallback callback;
	private AbstractOdysseusPeer peer;

	public IExecutionListenerCallback getCallback() {
		return this.callback;
	}

	public void setCallback(IExecutionListenerCallback callback) {
		this.callback = callback;
	}
	
	@Override
	public abstract ArrayList<ILogicalOperator> splitPlan(
			ILogicalOperator ao);

	@Override
	public abstract String getName();
	
	@Override
	public void setPeer(IOdysseusPeer peer) {
		this.peer = (AbstractOdysseusPeer) peer;
	}
	protected AbstractOdysseusPeer getPeer() {
		return this.peer;
	}
	
	@Override
	public void finalizeService() {
	}

	@Override
	public void startService() {
	}

}
