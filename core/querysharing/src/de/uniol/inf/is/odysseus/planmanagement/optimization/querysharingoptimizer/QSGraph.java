package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import java.util.List;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class QSGraph {
	private List<Vertice> vertices = new ArrayList<Vertice>();
	private List<GraphConnection> connections = new ArrayList<GraphConnection>();
	
	protected QSGraph(){
	}
	
	protected QSGraph(List<Vertice> vertices, List<GraphConnection> connections) {
		this.vertices = vertices;
		this.connections = connections;		
	}
	
	protected void addVertice(IPhysicalOperator po) {
		// TODO: Ordnungsgem‰ﬂge Beschriftung des Operators ermitteln
		// Irgendwas der Form "Bezeichner <-- expression + input"
		String label = po.getName();
		if(po instanceof SelectPO) {
			IPredicate<?> pred = ((SelectPO<?>)po).getPredicate();
			if(pred instanceof IRelationalPredicate) {
				List<SDFAttribute> attlist = ((IRelationalPredicate)pred).getAttributes();
				System.out.println(((IRelationalPredicate)pred).toString());
				for(SDFAttribute a : attlist) {
					System.out.println(a.toString());
				}
			}
		}
		vertices.add(new Vertice(po, label));
	}
	
	protected boolean removeVertice(Vertice v) {
		if (hasConnections(v)) {
			return false;
		}
		vertices.remove(v);
		return true;		
	}
	
	protected boolean addConnection(Vertice source, Vertice target) {
		if(!vertices.contains(source) || !vertices.contains(target) || existsConnection(source, target)) {
			return false;
		}
		connections.add(new GraphConnection(source, target));
		return true;
	}
	
	protected boolean addConnection(IPhysicalOperator source, IPhysicalOperator target) {
		Vertice sourcev = getVertice(source);
		Vertice targetv = getVertice(target);
		return addConnection(sourcev,targetv);			
	}
	
	protected boolean removeConnection(Vertice source, Vertice target) {
		for(GraphConnection gc : connections) {
			if(source.equals(gc.getSource()) && target.equals(gc.getTarget())) {
				connections.remove(gc);
				return true;
			}
		}
		return false;
	}
	
	protected void replaceInputConnection(Vertice v, Vertice oldSource, Vertice newSource) {
		removeConnection(oldSource, v);
		addConnection(newSource, v);
		//TODO: Beschriftung des Knotens in Abh‰ngigkeit von der neuen Quelle anpassen
		String newLabel = "";
		v.setLabel(newLabel);
	}
	
	protected boolean hasConnections(Vertice v) {
		for(GraphConnection gc : connections) {
			if(gc.getSource().equals(v) || gc.getTarget().equals(v)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean existsConnection(Vertice source, Vertice target) {
		for(GraphConnection gc : connections) {
			if(gc.getSource().equals(source) && gc.getTarget().equals(target)) {
				return true;
			}
		}
		return false;
	}

	public List<Vertice> getVertices() {
		return vertices;
	}
	
	public Vertice getVertice(IPhysicalOperator ipo) {
		for(Vertice v : vertices) {
			if(v.getPo().equals(ipo)) {
				return v;
			}
		}
		return null;
	}

	public List<GraphConnection> getConnections() {
		return connections;
	}
	
	public Vertice getInput(Vertice v) {
		if(!vertices.contains(v)) return null;
		for(GraphConnection gc : connections) {
			if(gc.getTarget().equals(v)) {
				return gc.getSource();
			}
		}
		return null;
	}
	
	public List<Vertice> getOutput(Vertice v) {
		if(!vertices.contains(v)) return null;
		ArrayList<Vertice> res = new ArrayList<Vertice>();
		for(GraphConnection gc : connections) {
			if(gc.getSource().equals(v)) {
				res.add(gc.getTarget());
			}
		}
		return res;
	}
	
}
