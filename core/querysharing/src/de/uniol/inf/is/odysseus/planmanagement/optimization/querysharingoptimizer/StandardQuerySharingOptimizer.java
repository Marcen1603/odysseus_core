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
package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

@SuppressWarnings({"unchecked","rawtypes"})
public class StandardQuerySharingOptimizer implements IQuerySharingOptimizer {
	
	public synchronized void applyQuerySharing(List<IPhysicalQuery> plan, List<IPhysicalQuery> newQueries, OptimizationConfiguration conf) {
		boolean restructuringAllowed;
		if(conf.getParameterAllowRestructuringOfCurrentPlan() != null) {
			restructuringAllowed = conf.getParameterAllowRestructuringOfCurrentPlan().getValue();
		} else {
			restructuringAllowed = false;
		}
		//Weder neue Queries vorhanden, noch die Erlaubnis den alten Plan umzustrukturieren 
		if(newQueries == null && !restructuringAllowed) {
			return;
		}

		List<IPhysicalOperator> newOps = new CopyOnWriteArrayList<IPhysicalOperator>();
		List<IPhysicalOperator> ipos =  new CopyOnWriteArrayList<IPhysicalOperator>();

		// Sammeln aller im alten Plan enthaltenen physischen Operatoren

		for(IPhysicalQuery q : plan) {
			for(IPhysicalOperator ipo : q.getPhysicalChilds()) {
				if(!ipos.contains(ipo)) {
					ipos.add(ipo);
				}
			}
		}

		// Sammeln aller in den NEUEN Queries enthaltenen physischen Operatoren
		if(newQueries != null) {
			for(IPhysicalQuery q : newQueries) {	
				for(IPhysicalOperator ipo : q.getPhysicalChilds()) {
					if(!ipos.contains(ipo)) {
						ipos.add(ipo);
					}
					if(!newOps.contains(ipo)) {
						newOps.add(ipo);
					}
				}
			}
		}
		boolean parameterShareSimilarOperators;
		if(conf.getParameterShareSimilarOperators() != null) {
			parameterShareSimilarOperators = conf.getParameterShareSimilarOperators().getValue();
		} else {
			parameterShareSimilarOperators = false;
		}
		while(removeIdenticalOperators(ipos, newOps, restructuringAllowed)
				|| (parameterShareSimilarOperators && reconnectSimilarOperators(ipos,newOps,restructuringAllowed)));
//		while((parameterShareSimilarOperators && reconnectSimilarOperators(ipos,newOps,restructuringAllowed))
//				|| removeIdenticalOperators(ipos, newOps, restructuringAllowed));
	}

	public void applyQuerySharing(List<IPhysicalQuery> plan, OptimizationConfiguration conf) {
		//Neustrukturierung eines bestehenden Plans ist erlaubt
		if(conf.getParameterAllowRestructuringOfCurrentPlan().getValue()) {
			applyQuerySharing(plan, null, conf);
		}
	}

	private boolean removeIdenticalOperators(List<IPhysicalOperator> ipos, List<IPhysicalOperator> newOps, boolean restructuringAllowed) {
		int size = ipos.size();
		for(int i = 0; i<size-1; i++) {
			for(int j = i+1; j<size; j++) {
				if(ipos.size() <= 1){
					return false;
				}
				IPhysicalOperator op1 = ipos.get(i);
				IPhysicalOperator op2 = ipos.get(j);
				//Einer der Operatoren ist zum Austausch berechtigt und es ist nicht ein und derselbe
				if((newOps.contains(op1) || newOps.contains(op2) || restructuringAllowed)
						&& !op1.getName().equals(op2.getName())) {


					//Schritt 1: Entfernen von identischen Operatoren

					// Operatoren sind semantisch gleiche Pipes
					if(op1 instanceof IPipe
							&& op1.isSemanticallyEqual(op2)) {
						// Der erste Operator ist nicht neu, der zweite allerdings schon und eine Umstrukturierung des alten Plans ist untersagt
						if (!restructuringAllowed && !newOps.contains(op1)) {
							IPhysicalOperator temp = op1;
							op1 = op2;
							op2 = temp;
						}

						this.replaceOperator(op1, op2);
						//Entfernen des ersetzten Operators aus der Liste
						ipos.remove(op1);
						newOps.remove(op1);

						// Reiteration (m�glicherweise neue identische Operatoren)
						return true;
						//break;

					}
				}
			}
		}
		return false;
	}

	private boolean reconnectSimilarOperators(List<IPhysicalOperator> ipos, List<IPhysicalOperator> newOps, boolean restructuringAllowed) {
		int size = ipos.size();
		for(int i = 0; i<size-1; i++) {
			for(int j = i+1; j<size; j++) {
				if(ipos.size() <= 1){
					return false;
				}
				IPhysicalOperator op1 = ipos.get(i);
				IPhysicalOperator op2 = ipos.get(j);
				if((newOps.contains(op1) || newOps.contains(op2) || restructuringAllowed)
						&& !op1.getName().equals(op2.getName())) {
					// Schritt 2: Tauschen von Eingangsquellen bei Operatoren, deren Ergebnis
					// im Ergebnis anderer Operatoren enthalten ist

					// op1 ist in op2 enthalten 
					if(op1 instanceof AbstractPipe && op2 instanceof IPipe
							&& ((AbstractPipe<?,?>)op1).isContainedIn((IPipe)op2) && (newOps.contains(op1) || restructuringAllowed)) {
						this.replaceInput(op1, op2);
						// Reiteration (m�glicherweise neue identische Operatoren)
						return true;
						//break;
						// op2 ist in op1 enthalten 
					} else if(op1 instanceof AbstractPipe && op2 instanceof IPipe
							&& ((AbstractPipe<?,?>)op2).isContainedIn((IPipe)op1) && (newOps.contains(op2) || restructuringAllowed)) {
						this.replaceInput(op2, op1);
						// Reiteration (m�glicherweise neue identische Operatoren)
						return true;
						//break;

					}
				}
			}
		}
		return false;
	}

	private void replaceOperator(IPhysicalOperator op1, IPhysicalOperator op2) {
		// Austausch von Operatoren

		// Holen s�mtlicher Subscriptions von bei dem zu ersetzenden Operator angemeldeten sinks
		Collection<ISubscription> sinks = new ArrayList(((IPipe)op1).getSubscriptions());

		// Ersetzen des Eingangsoperators bei allen angeschlossenen sinks
		for(ISubscription sub : sinks) {
			ISink s = (ISink)sub.getTarget();

			// debug
			//System.out.println("S-Name: " + s.getName());
			//System.out.println("op1-Name: " + op1.getName());

			// Schema- und Portinformationen der alten Verbindung in Erfahrung bringen
			SDFSchema schema = sub.getSchema();
			int sinkInPort = sub.getSinkInPort();
			int sourceOutPort = sub.getSourceOutPort();

			
			// Subscription l�schen
			((IPipe)op1).unsubscribeSink(sub);
			//System.out.println(s.getName() + " unsubscribed from " + op1.getName());

			// mit den Informationen der alten Subscription den Ersatzoperator beim Sink anmelden
			//s.subscribeToSource(op2, sinkInPort, sourceOutPort, schema);
			((ISource)op2).subscribeSink(s, sinkInPort, sourceOutPort, schema);


		}

		// Holen der Quellen, bei denen der zu ersetzende Operator bislang angemeldet war
		// (Sind die gleichen wie von op2, ansonsten h�tte op2.isSemanticallyEqual(op2) false ergeben)
		Collection<ISubscription<IPhysicalOperator>> sources = new ArrayList<ISubscription<IPhysicalOperator>>(((IPipe)op1).getSubscribedToSource());
		for(ISubscription<?> sub : sources) {
			ISource s = (ISource)sub.getTarget();
			s.unsubscribeSink(sub);
			((ISink)op1).unsubscribeFromSource(sub);

		}

		// Ersetzen des Operators in den einzelnen Queries, die als Besitzer eingetragen waren
		List<IOperatorOwner> owner = new ArrayList(op1.getOwner());
		int noOwners = owner.size();
		for(int k = 0; k<noOwners; k++) {
			IOperatorOwner oo = owner.get(k);
			((IPhysicalQuery)oo).replaceOperator(op1, op2);
			((IPhysicalQuery)oo).replaceRoot(op1, op2);
		}

		//System.out.println(op1.getName() + "has been replaced by" + op2.getName());

	}

	/**
	 * Ersetzt die bisherige Quelle von op1 mit op2
	 * @param op1
	 * @param op2
	 */
	private void replaceInput(IPhysicalOperator op1, IPhysicalOperator op2) {
		// Ersetzen der Quelle von op1 mit op2
		Collection<ISubscription> sources = new ArrayList(((IPipe)op1).getSubscribedToSource());
		for(ISubscription sub : sources) {
			ISource s = (ISource)sub.getTarget();
			s.unsubscribeSink(sub);
			((ISink)op1).unsubscribeFromSource(sub);
			((IPipe)op2).subscribeSink(op1,sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
		}

		// Hinzuf�gen des op2-Operators in den einzelnen Queries, die als Besitzer von op1 eingetragen sind
		// (Denn dieser Operator ist nun Teil der Query, da er als Eingang f�r op1 dient.
		List<IOperatorOwner> owner = new ArrayList<IOperatorOwner>(op1.getOwner());
		int noOwners = owner.size();
		for(int k = 0; k<noOwners; k++) {
			IOperatorOwner oo = owner.get(k);
			((IPhysicalQuery)oo).addChild(op2);

		}						
	}
}
