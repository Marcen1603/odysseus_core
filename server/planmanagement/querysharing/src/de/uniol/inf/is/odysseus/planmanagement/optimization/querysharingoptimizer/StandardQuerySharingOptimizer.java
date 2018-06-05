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
package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StandardQuerySharingOptimizer implements IQuerySharingOptimizer {

	@Override
	public synchronized void applyQuerySharing(Collection<IPhysicalQuery> plan,
			List<IPhysicalQuery> newQueries, OptimizationConfiguration conf,
			IDataDictionaryWritable dd) {
		boolean restructuringAllowed;
		if (conf.getParameterAllowRestructuringOfCurrentPlan() != null) {
			restructuringAllowed = conf
					.getParameterAllowRestructuringOfCurrentPlan().getValue();
		} else {
			restructuringAllowed = false;
		}
		//restructuringAllowed = false;
		// Weder neue Queries vorhanden, noch die Erlaubnis den alten Plan
		// umzustrukturieren
		if (newQueries == null && !restructuringAllowed) {
			return;
		}

		List<IPhysicalOperator> newOps = new CopyOnWriteArrayList<IPhysicalOperator>();
		List<IPhysicalOperator> ipos = new CopyOnWriteArrayList<IPhysicalOperator>();

		// Sammeln aller im alten Plan enthaltenen physischen Operatoren

		for (IPhysicalQuery q : plan) {
			for (IPhysicalOperator ipo : q.getPhysicalChilds()) {
				if (!ipos.contains(ipo)) {
					ipos.add(ipo);
				}
			}
		}

		// Sammeln aller in den NEUEN Queries enthaltenen physischen Operatoren
		if (newQueries != null) {
			for (IPhysicalQuery q : newQueries) {
				for (IPhysicalOperator ipo : q.getPhysicalChilds()) {
					if (!ipos.contains(ipo)) {
						ipos.add(ipo);
					}
					if (!newOps.contains(ipo)) {
						newOps.add(ipo);
					}
				}
			}
		}
		boolean parameterShareSimilarOperators;
		if (conf.getParameterShareSimilarOperators() != null) {
			parameterShareSimilarOperators = conf
					.getParameterShareSimilarOperators().getValue();
		} else {
			parameterShareSimilarOperators = false;
		}
		while (removeIdenticalOperators(ipos, newOps, restructuringAllowed, dd)
				|| (parameterShareSimilarOperators && reconnectSimilarOperators(
						ipos, newOps, restructuringAllowed))) {

		}
		// while((parameterShareSimilarOperators &&
		// reconnectSimilarOperators(ipos,newOps,restructuringAllowed))
		// || removeIdenticalOperators(ipos, newOps, restructuringAllowed));
	}

	@Override
	public void applyQuerySharing(Collection<IPhysicalQuery> plan,
			OptimizationConfiguration conf, IDataDictionaryWritable dd) {
		// Neustrukturierung eines bestehenden Plans ist erlaubt
		if (conf.getParameterAllowRestructuringOfCurrentPlan().getValue()) {
			applyQuerySharing(plan, null, conf, dd);
		}
	}

	private boolean removeIdenticalOperators(List<IPhysicalOperator> ipos,
			List<IPhysicalOperator> newOps, boolean restructuringAllowed,
			IDataDictionaryWritable dd) {
		int size = ipos.size();
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (ipos.size() <= 1) {
					return false;
				}
				IPhysicalOperator op1 = ipos.get(i);
				IPhysicalOperator op2 = ipos.get(j);
				// Einer der Operatoren ist zum Austausch berechtigt und es ist
				// nicht ein und derselbe
				if ((newOps.contains(op1) || newOps.contains(op2) || restructuringAllowed)) {

					if (op1.equals(op2)) {
						continue;
					}

					// Schritt 1: Entfernen von identischen Operatoren

					// Operatoren sind semantisch gleiche Pipes, die identische Quellen haben
					if (op1 instanceof IPipe && 
							((AbstractPipe)op1).hasSameSources(op2) &&  
							haveSameNames(op1,op2) 
							&& op1.isSemanticallyEqual(op2)) {
						// Der erste Operator ist nicht neu, der zweite
						// allerdings schon und eine Umstrukturierung des alten
						// Plans ist untersagt
						// Do not replace, when operator is running!
						if ((!restructuringAllowed && !newOps.contains(op1)) || op1.isOpen()) {
							IPhysicalOperator temp = op1;
							op1 = op2;
							op2 = temp;
						}

						replaceOperator(op1, op2, dd);
						// Entfernen des ersetzten Operators aus der Liste
						ipos.remove(op1);
						newOps.remove(op1);

						// Reiteration (m�glicherweise neue identische
						// Operatoren)
						return true;
						// break;

					}
				}
			}
		}
		return false;
	}

	private boolean haveSameNames(IPhysicalOperator op1, IPhysicalOperator op2) {
		if (op1.getName() == null && op2.getName()==null){
			return true;
		}
		return (op1.getName().equals(op2.getName()));
	}

	private boolean reconnectSimilarOperators(List<IPhysicalOperator> ipos,
			List<IPhysicalOperator> newOps, boolean restructuringAllowed) {
		int size = ipos.size();
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (ipos.size() <= 1) {
					return false;
				}
				IPhysicalOperator op1 = ipos.get(i);
				IPhysicalOperator op2 = ipos.get(j);
				if ((newOps.contains(op1) || newOps.contains(op2) || restructuringAllowed)
						&& !op1.getName().equals(op2.getName())) {
					// Schritt 2: Tauschen von Eingangsquellen bei Operatoren,
					// deren Ergebnis
					// im Ergebnis anderer Operatoren enthalten ist

					// op1 und op2 haben identische Quellen und op1 ist in op2 enthalten
					if (op1 instanceof AbstractPipe
							&& op2 instanceof IPipe
							&& ((AbstractPipe<?, ?>)op1).hasSameSources(op2)
							&& ((AbstractPipe<?, ?>) op1)
									.isContainedIn((IPipe) op2)
							&& (newOps.contains(op1) || restructuringAllowed)) {
						replaceInput(op1, op2);
						// Reiteration (m�glicherweise neue identische
						// Operatoren)
						return true;
						// break;
						// op1 und op2 haben identische Quellen und op2 ist in op1 enthalten
					} else if (op1 instanceof AbstractPipe
							&& op2 instanceof IPipe
							&& ((AbstractPipe<?, ?>)op2).hasSameSources(op1)
							&& ((AbstractPipe<?, ?>) op2)
									.isContainedIn((IPipe) op1)
							&& (newOps.contains(op2) || restructuringAllowed)) {
						replaceInput(op2, op1);
						// Reiteration (m�glicherweise neue identische
						// Operatoren)
						return true;
						// break;

					}
				}
			}
		}
		return false;
	}

	private void replaceOperator(IPhysicalOperator toReplace,
			IPhysicalOperator replacement, IDataDictionaryWritable dd) {
		// Austausch von Operatoren

		// Holen s�mtlicher Subscriptions von bei dem zu ersetzenden Operator
		// angemeldeten sinks
		Collection<ISubscription> sinks = new ArrayList(
				((IPipe) toReplace).getSubscriptions());

		// Ersetzen des Eingangsoperators bei allen angeschlossenen sinks
		for (ISubscription sub : sinks) {
			ISink s = (ISink) sub.getSink();

			// debug
			// System.out.println("S-Name: " + s.getName());
			// System.out.println("op1-Name: " + op1.getName());

			// Schema- und Portinformationen der alten Verbindung in Erfahrung
			// bringen
			SDFSchema schema = sub.getSchema();
			int sinkInPort = sub.getSinkInPort();
			int sourceOutPort = sub.getSourceOutPort();

			boolean isActive = ((AbstractSource) toReplace)
					.isActive((AbstractPhysicalSubscription) sub);
			int oc = ((AbstractPhysicalSubscription)sub).getOpenCalls();
			// Subscription l�schen
			((IPipe) toReplace).unsubscribeSink(sub);

			// System.out.println(s.getName() + " unsubscribed from " +
			// op1.getName());

			// mit den Informationen der alten Subscription den Ersatzoperator
			// beim Sink anmelden
			// s.subscribeToSource(op2, sinkInPort, sourceOutPort, schema);
			((ISource) replacement).subscribeSink(s, sinkInPort, sourceOutPort,
					schema, isActive, oc);

		}

		// Holen der Quellen, bei denen der zu ersetzende Operator bislang
		// angemeldet war
		// (Sind die gleichen wie von op2, ansonsten h�tte
		// op2.isSemanticallyEqual(op2) false ergeben)
		Collection<ISubscription<?,?>> sources = new ArrayList<ISubscription<?,?>>(
				((IPipe) toReplace).getSubscribedToSource());
		for (ISubscription<?,?> sub : sources) {
			ISource s = (ISource) sub.getSource();
			s.unsubscribeSink(sub);
			((ISink) toReplace).unsubscribeFromSource(sub);

		}

		// Ersetzen des Operators in den einzelnen Queries, die als Besitzer
		// eingetragen waren
		List<IOperatorOwner> owner = new ArrayList(toReplace.getOwner());
		int noOwners = owner.size();
		for (int k = 0; k < noOwners; k++) {
			IOperatorOwner oo = owner.get(k);
			((IPhysicalQuery) oo).replaceOperator(toReplace, replacement);
			((IPhysicalQuery) oo).replaceRoot(toReplace, replacement);
		}

		// Update the data dictionary
		List<IOperatorOwner> toRemove = new ArrayList<>();
		for (Entry<IOperatorOwner, Resource> id : toReplace.getUniqueIds()
				.entrySet()) {
			//dd.getOperator(id.getValue());
			dd.removeOperator(id.getValue());
			toRemove.add(id.getKey());
			replacement.addUniqueId(id.getKey(), id.getValue());
			dd.setOperator(id.getValue(), replacement);
		}
		for (IOperatorOwner id : toRemove) {
			toReplace.removeUniqueId(id);
		}
		// System.out.println(op1.getName() + "has been replaced by" +
		// op2.getName());

	}

	/**
	 * Ersetzt die bisherige Quelle von op1 mit op2
	 * 
	 * @param op1
	 * @param op2
	 */
	private void replaceInput(IPhysicalOperator op1, IPhysicalOperator op2) {
		// Ersetzen der Quelle von op1 mit op2
		Collection<ISubscription> sources = new ArrayList(
				((IPipe) op1).getSubscribedToSource());
		for (ISubscription sub : sources) {
			ISource s = (ISource) sub.getSource();
			s.unsubscribeSink(sub);
			((ISink) op1).unsubscribeFromSource(sub);
			((IPipe) op2).subscribeSink(op1, sub.getSinkInPort(),
					sub.getSourceOutPort(), sub.getSchema());
		}

		// Hinzuf�gen des op2-Operators in den einzelnen Queries, die als
		// Besitzer von op1 eingetragen sind
		// (Denn dieser Operator ist nun Teil der Query, da er als Eingang f�r
		// op1 dient.
		List<IOperatorOwner> owner = new ArrayList<IOperatorOwner>(
				op1.getOwner());
		int noOwners = owner.size();
		for (int k = 0; k < noOwners; k++) {
			IOperatorOwner oo = owner.get(k);
			((IPhysicalQuery) oo).addChild(op2);

		}
	}
}
