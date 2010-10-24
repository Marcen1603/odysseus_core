package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class StandardQuerySharingOptimizer implements IQuerySharingOptimizer {
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

		// Spielplatz

		// for(IQuery currentQuery : queries) {
		// for (IPhysicalOperator po : currentQuery.getPhysicalChilds()) {
		// if(po instanceof SelectPO) {
		// System.out.println(po.getName());
		// SelectPO<?> spo = (SelectPO<?>) po;
		// IPredicate ip = spo.getPredicate();
		// System.out.println("----------------" + "\n" + ip.toString() + "\n" +
		// "--------------");
		// //ip.contains(ip);
		// }
		// }
		// }
		return oldPlan;
	}

	public List<IQuery> eliminateIdenticalQueries(List<IQuery> newQueries,
			IPlan oldPlan) {
		List<IQuery> registeredQueries = oldPlan.getQueries();
		for (IQuery currentQuery : newQueries) {
			for (IQuery currentRegisteredQuery : registeredQueries) {
				if (isEqualQuery(currentQuery, currentRegisteredQuery)) {
					// neue query mit den gleichen physischen ops wie die alte
					// registrieren
				}
			}
		}
		return newQueries;
	}

	private boolean isEqualQuery(IQuery q1, IQuery q2) {
		boolean res = true;

		List<IPhysicalOperator> q1pos = q1.getPhysicalChilds();
		List<IPhysicalOperator> q2pos = q2.getPhysicalChilds();
		if (q1pos.size() != q2pos.size()) {
			return false;
		}
		for (IPhysicalOperator curq1 : q1pos) {
			boolean foundmatch = false;
			for (IPhysicalOperator curq2 : q2pos) {
				if(curq1.isSemanticallyEqual(curq2)) {
					foundmatch = true;
					if(curq1 instanceof IPipe) {
						Collection<ISubscription> sinks = ((IPipe)curq1).getSubscriptions();
						for(ISubscription sub : sinks) {
							ISink s = (ISink)sub.getTarget();
							SDFAttributeList schema = sub.getSchema();
							int sinkInPort = sub.getSinkInPort();
							//Eigentlich sollte man den Sourceport von dem anderen operator herausfinden anstatt aus der zu ersetztenden subscpription...
							int sourceOutPort = sub.getSourceOutPort();
							
							s.unsubscribeFromSource(sub);
							s.subscribeToSource(curq2, sinkInPort, sourceOutPort, schema);
						}
						Collection<ISubscription> sources = ((IPipe)curq1).getSubscribedToSource();
						for(ISubscription sub : sources) {
							ISource s = (ISource)sub.getTarget();
							s.unsubscribeSink(sub);
						}
						curq1.removeOwner(q1);
						curq2.addOwner(q1);
						((Query)q1).replaceOperator(curq1, curq2);
					}
					
					System.out.println("Found match!!!");
					System.out.println(curq1.toString());
					System.out.println(curq2.toString());
					break;
				}
				
				SDFAttributeList attlist1 = curq1.getOutputSchema();
				SDFAttributeList attlist2 = curq2.getOutputSchema();

//				if (curq1.getClass().equals(curq2.getClass())
//						&& attlist1.compareTo(attlist2) == 0) {
//					System.out.println("Found match!!!");
//					System.out.println("PO1:");
//					for (SDFAttribute a : attlist1) {
//						System.out.println(a.toString());
//					}
//					System.out.println("PO2:");
//					for (SDFAttribute b : attlist2) {
//						System.out.println(b.toString());
//					}
//
//					foundmatch = true;
//
//					System.out.println(curq1.toString());
//					System.out.println(curq2.toString());
//					break;
//				}
			}
			if (!foundmatch) {
				return false;
			}
		}

		return res;
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
