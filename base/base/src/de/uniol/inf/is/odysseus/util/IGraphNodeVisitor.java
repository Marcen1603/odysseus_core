package de.uniol.inf.is.odysseus.util;

public interface IGraphNodeVisitor<T,R> {
	public void nodeAction(T node);
	
	public void beforeFromSinkToSourceAction(T sink, T source);
	public void afterFromSinkToSourceAction(T sink, T source);
	
	public void beforeFromSourceToSinkAction(T source, T sink);
	public void afterFromSourceToSinkAction(T source, T sink);
	
	public R getResult();
}
