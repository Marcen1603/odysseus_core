package de.uniol.inf.is.odysseus.util;


public interface INodeVisitor<T,R> {
	public void ascendAction(T to);
	public void descendAction(T to);
	public void nodeAction(T op);
	public R getResult();
}
