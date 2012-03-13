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
package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.failed;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class FailedExecutionHandler<F> extends AbstractExecutionHandler<F>{

	public FailedExecutionHandler() {
		super(Lifecycle.FAILED);
	}

	public FailedExecutionHandler(
			FailedExecutionHandler<F> failedExecutionHandler) {
		super(failedExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone() {
		return new FailedExecutionHandler<F>(this);
	}

	@Override
	public void run() {
		IExecutionListenerCallback cb = getExecutionListenerCallback();
		List<Lifecycle> history = cb.getQuery()
				.getHistory();

		// Jeder Schritt darf nur einmailg wiederholt werden
		Lifecycle priorLifecycle = cb
				.getQuery()
				.getHistory()
				.get(cb.getQuery().getHistory()
						.size() - 2);
		// Falls Failed direkt nach Failed kommt, dann exisitert der geforderte
		// Handler nicht und wir brechen hier komplett ab
		if (occurence(history,
				Arrays.asList(Lifecycle.FAILED, Lifecycle.FAILED)) > 0) {
			cb.changeState(Lifecycle.TERMINATED);
			return;
		}
        if (priorLifecycle == Lifecycle.NEW) {
        	if (occurence(history,
        			Arrays.asList(priorLifecycle, Lifecycle.FAILED)) > 0) {
        		cb.changeState(
        				Lifecycle.TERMINATED);
        	}
        } else if (priorLifecycle == Lifecycle.SPLIT) {
        	if (occurence(history,
        			Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
        		cb.changeState(Lifecycle.SPLIT);
        	} else {
        		cb.changeState(
        				Lifecycle.TERMINATED);
        	}

        } else if (priorLifecycle == Lifecycle.DISTRIBUTION) {
        	if (occurence(history,
        			Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
        		cb.changeState(
        				Lifecycle.DISTRIBUTION);
        	} else {
        		cb.changeState(
        				Lifecycle.TERMINATED);
        	}

        } else if (priorLifecycle == Lifecycle.GRANTED) {
        	if (occurence(history,
        			Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
        		cb.changeState(
        				Lifecycle.DISTRIBUTION);
        	} else {
        		cb.changeState(
        				Lifecycle.TERMINATED);
        	}
        } else if (priorLifecycle == Lifecycle.RUNNING) {
        	if (occurence(history,
        			Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
        		cb.changeState(
        				Lifecycle.DISTRIBUTION);
        	} else {
        		cb.changeState(
        				Lifecycle.TERMINATED);
        	}
        }

	}

	private int occurence(List<Lifecycle> list, List<Lifecycle> occurence) {
		int occ = 0;
		if (occurence.size() > 0) {
			for (int counter = 0; counter + occurence.size() < list.size(); counter++) {
				boolean iterate = true;
				List<Lifecycle> tempList = list.subList(counter, counter
						+ occurence.size());
				for (int i = 0; i < tempList.size(); i++) {
					if (!(tempList.get(i) == occurence.get(i))) {
						iterate = false;
						break;
					}
				}
				if (iterate) {
					occ++;
				}
				// }
			}
		}
		return occ;
	}

}
