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
package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public abstract class AbstractExecutionHandler<F> implements IExecutionHandler<F>
{
	
	final private Lifecycle providedLifecycle;
	private IExecutionListenerCallback executionListenerCallback = null;
	protected IOdysseusPeer peer;
	private F function;
	protected ILogListener log;
	
	public AbstractExecutionHandler(AbstractExecutionHandler<F> other) {
		setFunction(other.getFunction());
		setPeer(other.getPeer());
		setExecutionListenerCallback(other.getExecutionListenerCallback());
		this.providedLifecycle = other.providedLifecycle;
	}
	
	public AbstractExecutionHandler(Lifecycle lifecycle, F function, IOdysseusPeer peer) {
		this.providedLifecycle = lifecycle;
		this.function = function;
		this.peer = peer;
	}
	
	public AbstractExecutionHandler(Lifecycle lifecycle) {
		this.providedLifecycle = lifecycle;
	}

	@Override
	public IExecutionListenerCallback getExecutionListenerCallback() {
		return this.executionListenerCallback;
	}
	
	@Override
	public void setExecutionListenerCallback(
			IExecutionListenerCallback callbackExecutionListener) {
		this.executionListenerCallback = callbackExecutionListener;
	}

	@Override
	public Lifecycle getProvidedLifecycle() {
		return this.providedLifecycle;
	}

	@Override
	public IOdysseusPeer getPeer() {
		return this.peer;
	}

	@Override
	public void setPeer(IOdysseusPeer peer) {
		this.peer = peer;
		this.log = peer.getLog();
	}

	@Override
	public F getFunction() {
		return function;
	}

	@Override
	public void setFunction(F function) {
		this.function = function;
	}	
	
	@Override
	final public String getName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public Thread startHandler() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}
	
	@Override
	public abstract void run();

	@Override
	public abstract IExecutionHandler<F> clone() ;

}
