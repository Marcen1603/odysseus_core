/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

/**
 * Hilfsklasse, um einen logischen Operatorplan systematisch von Quelle bis
 * Senke zu durchlaufen.
 * 
 * @author Timo Michelsen
 * 
 */
public class LogicalGraphWalker {

	private final List<ILogicalOperator> operators;

	/**
	 * Konstruktor. Erstellt eine neue {@link LogicalGraphWalker}-Instanz. Ihr
	 * wird eine Liste aller zu besuchender Operatoren mitgegeben.
	 * 
	 * @param operators
	 */
	public LogicalGraphWalker(List<ILogicalOperator> operators) {
		this.operators = operators;
	}

	public LogicalGraphWalker(ILogicalOperator logicalPlan) {
		this.operators = new ArrayList<ILogicalOperator>();
		this.operators.add(logicalPlan);
	}

	/**
	 * Durchläuft alle im Konstruktor gegebenen physischen Operatoren. Zu jedem
	 * Operator wird die gegebene {@link IOperatorWalker}-Instanz aufgerufen.
	 * 
	 * @param visitor
	 *            Visitor, welcher bei jedem physischen Operator aufgerufen wird
	 */
	public void walk(IOperatorWalker<ILogicalOperator> visitor) {

		// find sources
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator op : operators) {
			if (op.getSubscribedToSource().size() == 0)
				sources.add(op);
		}

		List<ILogicalOperator> operatorsToVisit = new ArrayList<ILogicalOperator>();
		List<ILogicalOperator> operatorsVisited = new ArrayList<ILogicalOperator>();

		operatorsToVisit.addAll(sources);

		while (!operatorsToVisit.isEmpty()) {

			ILogicalOperator operator = operatorsToVisit.remove(operatorsToVisit.size() - 1);
			visitor.walk(operator);

			operatorsVisited.add(operator);

			Collection<LogicalSubscription> subscriptions = operator.getSubscriptions();

			for (LogicalSubscription subscription : subscriptions) {
				ILogicalOperator sink = subscription.getSink();
				if (!operatorsToVisit.contains(sink) && !operatorsVisited.contains(sink) && operators.contains(sink)) {

					// check
					if (sink.getSubscribedToSource().size() > 1) {

						boolean ok = true;
						for (LogicalSubscription sourceSubscription : sink.getSubscribedToSource()) {
							if (!operatorsVisited.contains(sourceSubscription.getSource())) {
								ok = false;
								break;
							}
						}

						if (ok)
							operatorsToVisit.add(sink);

					} else {
						operatorsToVisit.add(sink);
					}
				}
			}
		}
	}

}
