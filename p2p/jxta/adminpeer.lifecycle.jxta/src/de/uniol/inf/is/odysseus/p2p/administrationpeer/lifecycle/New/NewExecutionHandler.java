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
package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.New;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class NewExecutionHandler extends AbstractExecutionHandler<ICompiler> {

	public NewExecutionHandler() {
		super(Lifecycle.NEW);
	}

	public NewExecutionHandler(NewExecutionHandler newExecutionHandler) {
		super(newExecutionHandler);
	}

	@Override
	public void run() {
		if (getFunction() != null && getPeer() != null
				&& getExecutionListenerCallback() != null) {
			List<IQuery> plan = null;
			P2PQuery query = getExecutionListenerCallback().getQuery();
			try {
				// Translate query
				plan = getFunction().translateQuery(
						query.getDeclarativeQuery(), query.getLanguage(),
						query.getUser(), query.getDataDictionary());
			} catch (QueryParseException e3) {
				e3.printStackTrace();
				log.logAction(
						query.getId(),
						"Error Compiling Query: " + e3.getMessage());
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
				return;
			} catch (Throwable e2) {
				e2.printStackTrace();
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
				return;
			}
			try {
				// Restruct Query
				for (IQuery q : plan) {
					ILogicalOperator restructPlan = getFunction().rewritePlan(
							q.getLogicalPlan(), null);
					query.addLogicalOperatorplan(restructPlan);
				}
			} catch (Exception e) {
				e.printStackTrace();
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
				return;
			}
		} else {
			getExecutionListenerCallback().changeState(Lifecycle.FAILED);
			return;
		}
		getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
	}

	@Override
	public void setPeer(IOdysseusPeer peer) {
		super.setPeer(peer);
		Method[] methods = peer.getClass().getMethods();
		for (Method m : methods) {
			// Find method returning ICompiler
			if (m.getReturnType() == ICompiler.class) {
				try {
					setFunction((ICompiler) m.invoke(peer, (Object[]) null));
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public IExecutionHandler<ICompiler> clone() {
		return new NewExecutionHandler(this);
	}

}
