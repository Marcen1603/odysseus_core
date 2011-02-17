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
package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.AbstractExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class ExecutionListener extends AbstractExecutionListener {

	static Logger logger = LoggerFactory.getLogger(ExecutionListener.class);

	ExecutorService execService = Executors.newFixedThreadPool(1);
	Future<?> f = null;

	public ExecutionListener(P2PQuery query) {
		super(query);
		setCallback(new ExecutionListenerCallback());
	}

	@Override
	protected synchronized void execute(IExecutionHandler<?> executionHandler) {
		logger.debug("Executing " + executionHandler);
		boolean useExecThreads = false;
		if (useExecThreads) {
			if (f != null && !f.isDone()) {
				f.cancel(true);
			}
			f = execService.submit(executionHandler);
		} else {
			executionHandler.run();
		}
	}

	@Override
	public void run() {

		while (getQuery().getStatus() != Lifecycle.TERMINATED) {
			Lifecycle currentQueryState = getQuery().getStatus();
			IExecutionHandler<?> nextHandler = getHandler().get(
					currentQueryState);
			logger.debug("Current lifecycle: " + currentQueryState);
			if (nextHandler != null) {
				execute(nextHandler);
			}
			// Sonst hängen wir ewig in einer Schleife fest, falls es keinen
			// passenden ExecutionHandler gibt.
			else {
				logger.warn("no Handler for Lifecycle " + currentQueryState
						+ " found!");
				getCallback().changeState(Lifecycle.FAILED);
				return;

			}
			synchronized (this) {
				while (currentQueryState == getQuery().getStatus()) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}
