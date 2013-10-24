package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph;

import de.uniol.inf.is.odysseus.core.collection.IPair;

public class GraphNodePair<T,S> implements IPair<T, S> {
	private T e1;
	private S e2;
	
	public GraphNodePair() {
		
	}
	
	public GraphNodePair(T gn1, S gn2) {
		this.e1 = gn1;
		this.e2 = gn2;
	}
	@Override
	public T getE1() {
		return e1;
	}

	@Override
	public void setE1(T e1) {
		this.e1 = e1;
	}

	@Override
	public S getE2() {
		return e2;
	}

	@Override
	public void setE2(S e2) {
		this.e2 = e2;
	}


}
