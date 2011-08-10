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
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.queryselection;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class QuerySelectionStrategy implements IQuerySelectionStrategy {
	private static int MAXQUERIES = 10;

	@Override
	public boolean handleQuery(Subplan subplan, IOdysseusPeer peer) {

		if (peer.getQueryCount() == MAXQUERIES) {
			return false;
		}
		// Pruefen, ob adressierte Quellen ueberhaupt verwaltet werden
		List<AccessAO> sources = new ArrayList<AccessAO>();
		collectSourcesFromPlan(subplan.getAo(), sources);
		if (!sources.isEmpty()) {
			for (AccessAO ao : sources) {
				if (!GlobalState.getActiveDatadictionary()
						.containsViewOrStream(ao.getSource().getURI(),
								GlobalState.getActiveUser(""))) {
					return false;
				}
			}
			return true;
		}
		// keine Quellen werden verwaltet
		if (GlobalState.getActiveDatadictionary().emptySourceTypeMap()) {
			return true;
		} else {
			return false;
		}
	}

	private void collectSourcesFromPlan(ILogicalOperator op,
			List<AccessAO> sources) {
		if ((op instanceof AccessAO) && !(op instanceof P2PAO)) {
			sources.add((AccessAO) op);
		} else {
			for (LogicalSubscription subscription : op.getSubscribedToSource()) {
				collectSourcesFromPlan(subscription.getTarget(), sources);
			}
		}

	}

}
