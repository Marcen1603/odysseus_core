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
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class SplittingExecutionHandler<F extends AbstractSplittingStrategy>
		extends AbstractExecutionHandler<F> implements IExecutionHandler<F> {

	public SplittingExecutionHandler() {
		super(Lifecycle.SPLIT);
	}

	public SplittingExecutionHandler(
			SplittingExecutionHandler<F> other) {
		super(other);
	}

	@Override
	public void run() {
		if (getExecutionListenerCallback() != null && getPeer() != null) {
			P2PQuery query = getExecutionListenerCallback().getQuery();
			getFunction().setCallback(getExecutionListenerCallback());
			int subplanID = 0;
			for (ILogicalOperator fullPlan : query.getLogicalOperatorplan()) {
				ArrayList<ILogicalOperator> plan = getFunction().splitPlan(
						fullPlan);
				if (plan == null || plan.size() == 0) {
					getExecutionListenerCallback()
							.changeState(Lifecycle.FAILED);
					return;
				}
                for (int i = 0; i < plan.size(); i++) {
                	getExecutionListenerCallback().getQuery().addSubPlan(
                			new Subplan(query.getId() + "_"+(subplanID++),
                					plan.get(i)), i == 0);
                }
			}
			getExecutionListenerCallback().changeState(
					Lifecycle.SUCCESS);

		}
	}

	@Override
	public IExecutionHandler<F> clone() {
		return new SplittingExecutionHandler<F>(this);
	}
}
