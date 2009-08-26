package de.uniol.inf.is.odysseus.util;


public interface INodeVisitor<T,R> {
	public void ascend(T to);
	public void descend(T to);
	public void node(T op);
	public R getResult();
}
