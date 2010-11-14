package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.executor.datastructure.Plan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.*;

public class StandardQuerySharingOptimizer implements IQuerySharingOptimizer {
	@Override
	public IPlan applyQuerySharing(IPlanOptimizable sender, IPlan oldPlan) {

		// Holen der Queries
		List<IQuery> queries = oldPlan.getQueries();

		// Für den Spezialfall der komplett identischen Anfragen könnte hier
		// bereits eine Minimierung des Plans erfolgen

		// Konstruktion des globalen Graphen durch
		// Hinzufügen der einzelnen Anfragen

		QSGraph global = new QSGraph();
		for (IQuery currentQuery : queries) {
			// getPhysicalChilds oder getRoots?
			for (IPhysicalOperator po : currentQuery.getPhysicalChilds()) {
				global.addVertice(po);
			}
			// Kanten des Graphens anhand der Subscriptions der physischen
			// Operatoren einfügen
			for (IPhysicalOperator po : currentQuery.getPhysicalChilds()) {
				if (po instanceof ISink) {
					for (PhysicalSubscription a : ((ISink<IPhysicalOperator>) po)
							.getSubscribedToSource()) {
						if (global.addConnection((IPhysicalOperator) a.getTarget(), po)) {
							System.out.println("Added Connection from "
									+ a.getTarget().toString() + " to " + po.toString());
						}
					}
				}
				if (po instanceof ISource) {
					for (PhysicalSubscription b : ((ISource<IPhysicalOperator>) po)
							.getSubscriptions()) {
						if (global.addConnection(po, (IPhysicalOperator) b.getTarget())) {
							System.out.println("Added Connection from "
									+ po.toString() + " to " + b.getTarget().toString());
						}
					}
				}
			}

		}

		// TODO: Optimierbare Teilgraphen ausfindig machen
		// und in eine Liste einfügen
		List<QSGraph> graphlist = new ArrayList<QSGraph>();

		// Interleaved Execution Algorithmus auf die Teilgraphen anwenden
		for (QSGraph g : graphlist) {
			g = interleavedExecutionAlgorithm(g);
		}

		// Anwenden der Änderungen auf den alten Plan

		return oldPlan;
	}

	
	public IPlan eliminateIdenticalOperators(IPlan oldPlan, List<IQuery> newQueries) {
		List<IPhysicalOperator> newOps = new ArrayList<IPhysicalOperator>();
		List<IPhysicalOperator> ipos =  new ArrayList<IPhysicalOperator>();

		// Sammeln aller im Plan enthaltenen physischen Operatoren
		//System.out.println("ALLE Queries:");
		for(IQuery q : oldPlan.getQueries()) {
			//System.out.println(q.getQueryText());
			for(IPhysicalOperator ipo : q.getPhysicalChilds()) {
				if(!ipos.contains(ipo)) {
					ipos.add(ipo);
				}
			}
		}
		
		// Sammeln aller in den NEUEN Queries enthaltenen physischen Operatoren
		//System.out.println("NEUE Queries:");
		for(IQuery q : newQueries) {
			//System.out.println(q.getQueryText());
			for(IPhysicalOperator ipo : q.getPhysicalChilds()) {
				if(!newOps.contains(ipo)) {
					newOps.add(ipo);
				}
			}
		}
		
		int size = ipos.size();
		
		// Vergleich der physischen Operatoren auf semantische Äquivalenz bzw. Austauschbarkeit der Quellen
		for(int i = 0; i<size-1; i++) {
			for(int j = i+1; j<size; j++) {
				IPhysicalOperator op1 = ipos.get(i);
				IPhysicalOperator op2 = ipos.get(j);
				//Einer der Operatoren ist zum Austausch berechtigt un es ist nicht und derselbe
				if((newOps.contains(op1) || newOps.contains(op2))
						&& !op1.getName().equals(op2.getName())) {


					//Schritt 1: Entfernen von identischen Operatoren

					// Operatoren sind semantisch gleiche Pipes
					if(op1 instanceof IPipe
							&& op1.isSemanticallyEqual(op2)) {
						// Der erste Operator ist nicht neu, der zweite allerdings schon
						if (!newOps.contains(op1)) {
							IPhysicalOperator temp = op1;
							op1 = op2;
							op2 = temp;
						}
						System.out.println("Found match!!!");
						System.out.println(op1.toString());
						System.out.println(op2.toString());

						this.replaceOperator(op1, op2);
						//Entfernen des ersetzten Operators aus der Liste
						ipos.remove(op1);
						newOps.remove(op1);

						// Reiteration (möglicherweise neue identische Operatoren)
						size--;
						i=-1;
						//break;

					}

					// Schritt 2: Tauschen von Eingangsquellen bei Operatoren, deren Ergebnis
					// im Ergebnis anderer Operatoren enthalten ist

					// op1 ist in op2 enthalten 
					if(op1 instanceof AbstractPipe && op2 instanceof IPipe
							&& ((AbstractPipe<?,?>)op1).isContainedIn((IPipe)op2) && newOps.contains(op1)) {
						this.replaceInput(op1, op2);
						// Reiteration (möglicherweise neue identische Operatoren)
						i=-1;
						break;
						// op2 ist in op1 enthalten 
					} else if(op1 instanceof AbstractPipe && op2 instanceof IPipe
							&& ((AbstractPipe<?,?>)op2).isContainedIn((IPipe)op1) && newOps.contains(op2)) {
						this.replaceInput(op2, op1);
						// Reiteration (möglicherweise neue identische Operatoren)
						i=-1;
						break;

					}

				}

			}
		}


		return oldPlan;
	}
	
	private void replaceOperator(IPhysicalOperator op1, IPhysicalOperator op2) {
		// Austausch von Operatoren
		
		// Holen sämtlicher Subscriptions von bei dem zu ersetzenden Operator angemeldeten sinks
		Collection<ISubscription> sinks = new ArrayList(((IPipe)op1).getSubscriptions());
		
		// Ersetzen des Eingangsoperators bei allen angeschlossenen sinks
		for(ISubscription sub : sinks) {
			ISink s = (ISink)sub.getTarget();

			// debug
			System.out.println("S-Name: " + s.getName());
			System.out.println("op1-Name: " + op1.getName());
			
			// Schema- und Portinformationen der alten Verbindung in Erfahrung bringen
			SDFAttributeList schema = sub.getSchema();
			int sinkInPort = sub.getSinkInPort();
			int sourceOutPort = sub.getSourceOutPort();

			// Subscription löschen
			((IPipe)op1).unsubscribeSink(sub);
			System.out.println(s.getName() + " unsubscribed from " + op1.getName());
			
			// mit den Informationen der alten Subscription den Ersatzoperator beim Sink anmelden
			//s.subscribeToSource(op2, sinkInPort, sourceOutPort, schema);
			((ISource)op2).subscribeSink(s, sinkInPort, sourceOutPort, schema);


		}
		
		// Holen der Quellen, bei denen der zu ersetzende Operator bislang angemeldet war
		// (Sind die gleichen wie von op2, ansonsten hätte op2.isSemanticallyEqual(op2) false ergeben)
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
			((Query)oo).replaceOperator(op1, op2);
			((Query)oo).replaceRoot(op1, op2);
		}
		
		System.out.println(op1.getName() + "has been replaced by" + op2.getName());
		
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
		
		// Hinzufügen des op2-Operators in den einzelnen Queries, die als Besitzer von op1 eingetragen sind
		// (Denn dieser Operator ist nun Teil der Query, da er als Eingang für op1 dient.
		List<IOperatorOwner> owner = new ArrayList<IOperatorOwner>(op1.getOwner());
		int noOwners = owner.size();
		for(int k = 0; k<noOwners; k++) {
			IOperatorOwner oo = owner.get(k);
			((Query)oo).addChild(op2);

		}						
	}
	

	private QSGraph interleavedExecutionAlgorithm(QSGraph g) {
		List<Vertice> vertices = g.getVertices();
		QSGraph res = g;

		// Schritt 1
		// TODO: Sobald die Graph-Datenstruktur fertig ist, wird natürlich von
		// den Quellen aus angefangen
		for (Vertice vi : vertices) {
			ArrayList<Vertice> temp = new ArrayList<Vertice>();
			for (Vertice vj : vertices) {
				if (vi.equals(vj)) {
					break;
				}
				if (vi.implies(vj)) {
					temp.add(vj);
				}
			}
			// Ersetzen der bisherigen Eingangskante mit einer neuen Kante
			if (!temp.isEmpty()) {
				res.replaceInputConnection(vi, res.getInput(vi),
						selectMostRestrictive(temp));
			}
		}

		// Schritt 2
		for (Vertice vi : vertices) {
			ArrayList<Vertice> temp = new ArrayList<Vertice>();
			for (Vertice vj : vertices) {
				if (vi.equals(vj)) {
					break;
				}
				if (vi.implies(vj) && vj.implies(vi)) {
					temp.add(vj);
				}
			}
			if (!temp.isEmpty()) {
				for (Vertice vj : temp) {
					for (Vertice vk : res.getOutput(vi)) {
						res.replaceInputConnection(vk, vj, vi);
					}
					res.removeVertice(vj);
				}
			}
		}
		return res;
	}

	private Vertice selectMostRestrictive(List<Vertice> vertices) {
		// TODO: Knoten mit dem "restriktivsten" Prädikat ermitteln
		// Beispiel: v1 mit R1.A<100 ist restriktiver als v2 mit R1.A<110
		return vertices.get(0);
	}

}
