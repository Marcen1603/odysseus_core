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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.running;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.user.P2PUserContext;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class RunningExecutionHandler extends
		AbstractExecutionHandler<IServerExecutor> {

	static Logger logger = LoggerFactory
			.getLogger(RunningExecutionHandler.class);

	public RunningExecutionHandler() {
		super(Lifecycle.RUNNING);
	}

	public RunningExecutionHandler(
			RunningExecutionHandler runningExecutionHandler) {
		super(runningExecutionHandler);
	}

	@Override
	public IExecutionHandler<IServerExecutor> clone() {
		return new RunningExecutionHandler(this);
	}

	@Override
	public void run() {
		logger.debug("running wird ausgefuehrt");
		try {
			for (Subplan s : getExecutionListenerCallback().getQuery()
					.getSubPlans().values()) {
				if (s.getStatus() == Lifecycle.GRANTED) {
					log.logAction(s.getId(), "Running Plan");
					logger.debug("Adding plan: "
							+ AbstractTreeWalker.prefixWalk(s.getAo(),
									new AlgebraPlanToStringVisitor()));
					ISession user = P2PUserContext.getActiveSession("");
					getFunction().startQuery(s.getQuery().getID(), user);
//					System.err.println("Query " + s.getQuery() + " started");
					
				}
			}

			if (!getFunction().isRunning()) {
				getFunction().startExecution();
			}
		} catch (PlanManagementException e2) {
			e2.printStackTrace();
		}

	}

	@Override
	public void setPeer(IOdysseusPeer peer) {
		super.setPeer(peer);
		Method[] methods = peer.getClass().getMethods();
		for (Method m : methods) {
			if (m.getReturnType() == IExecutor.class) {
				try {
					setFunction((IServerExecutor) m.invoke(peer, (Object[]) null));
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

}
