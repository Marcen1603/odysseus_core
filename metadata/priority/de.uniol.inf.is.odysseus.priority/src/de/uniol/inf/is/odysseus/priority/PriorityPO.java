package de.uniol.inf.is.odysseus.priority;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;

/**
 * @author Jonas Jacobi
 */
public class PriorityPO<T extends IMetaAttributeContainer<? extends IPriority>> extends
		AbstractPipe<T, T> {

	private final Map<Byte, IPredicate<? super T>> priorites;
	private final byte defaultPriority;
	
	public PriorityPO(PriorityAO<T> priorityAO) {
		super();
		this.priorites = priorityAO.getPriorities();
		this.defaultPriority = priorityAO.getDefaultPriority();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	protected void process_next(T next, int port) {
		for (Map.Entry<Byte, IPredicate<? super T>> curPriority : this.priorites
				.entrySet()) {
			if (curPriority.getValue().evaluate(next)) {
				next.getMetadata().setPriority(curPriority.getKey());
				transfer(next);
				ITimeInterval time = (ITimeInterval) next.getMetadata();
				sendPunctuation(time.getStart());
				return;
			}
		}
		next.getMetadata().setPriority(this.defaultPriority);
		transfer(next);
		return;
	}
	
	@Override
	public void sendPunctuation(PointInTime punctuation) {
		synchronized (this.subscriptions) {
			for (Subscription<ISource<? extends T>> sub : delegateSink.getSubscribedTo()) {
				if(sub.target.isSink()) {
					ISink<?> sink = (ISink<?>) sub.target;
					sink.processPunctuation(punctuation);
				} 
			}
			super.sendPunctuation(punctuation);
		}
	}	
	

}
