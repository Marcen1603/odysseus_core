package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;

public interface ISourceExecutor {

	public void start();

	public void interrupt();	

	public boolean hasSource(IIterableSource<?> s);

	public Collection<IIterableSource<?>> getSources();

}
