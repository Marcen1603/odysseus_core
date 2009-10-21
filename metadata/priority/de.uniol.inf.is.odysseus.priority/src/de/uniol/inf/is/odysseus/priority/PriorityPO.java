package de.uniol.inf.is.odysseus.priority;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

/**
 * @author Jonas Jacobi, Jan Steinke
 */
public class PriorityPO<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPipe<T, T> {

	private final Map<Byte, IPredicate<? super T>> priorites;
	private final byte defaultPriority;
	private boolean isPunctuationActive;

	private boolean isActive = true;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public PriorityPO(PriorityAO<T> priorityAO) {
		super();
		this.priorites = priorityAO.getPriorities();
		this.defaultPriority = priorityAO.getDefaultPriority();
		this.isPunctuationActive = priorityAO.isPunctuationActive();
		this.isActive = priorityAO.isActive();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T next, int port) {
		if (isActive) {
			for (Map.Entry<Byte, IPredicate<? super T>> curPriority : this.priorites
					.entrySet()) {
				if (curPriority.getValue().evaluate(next)) {
					next.getMetadata().setPriority(curPriority.getKey());
					transfer(next);
					ITimeInterval time = (ITimeInterval) next.getMetadata();
					if (curPriority.getKey() != 0 && isPunctuationActive) {
						sendPunctuation(time.getStart());
					}
					return;
				}
			}
			next.getMetadata().setPriority(this.defaultPriority);
			transfer(next);
		} else {
			transfer(next);
		}
		return;
	}

	@Override
	public void processPunctuation(PointInTime timestamp) {
		sendPunctuation(timestamp);
	}

	public boolean isPunctuationActive() {
		return isPunctuationActive;
	}

	public void setPunctuationActive(boolean isPunctuationActive) {
		this.isPunctuationActive = isPunctuationActive;
	}

}
