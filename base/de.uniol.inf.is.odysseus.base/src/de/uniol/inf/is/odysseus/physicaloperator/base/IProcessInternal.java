package de.uniol.inf.is.odysseus.physicaloperator.base;

public interface IProcessInternal<R> {
	public void process_internal(R event, int port);
}
