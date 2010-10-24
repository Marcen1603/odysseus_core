package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

public class GraphConnection {
	private Vertice source;
	private Vertice target;
	
	protected GraphConnection(Vertice source, Vertice target) {
		this.source = source;
		this.target  = target;
	}

	public Vertice getSource() {
		return source;
	}

	public void setSource(Vertice source) {
		this.source = source;
	}

	public Vertice getTarget() {
		return target;
	}

	public void setTarget(Vertice target) {
		this.target = target;
	}

	public boolean equals(GraphConnection gc) {
		if(this.source.equals(gc.getSource()) && this.target.equals(gc.getTarget())) {
			return true;
		}
		return false;
	}	
}
