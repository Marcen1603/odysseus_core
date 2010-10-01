package de.uniol.inf.is.odysseus.priority;

import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * @author Jonas Jacobi, Jan Steinke
 */
public class PriorityPO<K extends IPriority & ITimeInterval, T extends IMetaAttributeContainer<K>>
		extends AbstractPipe<T, T> {

	private final Map<Byte, IPredicate<? super T>> priorites;

	private final byte defaultPriority;

	public byte getDefaultPriority() {
		return defaultPriority;
	}

	private boolean isPunctuationActive;

	public PriorityPO(PriorityAO<T> priorityAO) {
		super();
		this.priorites = priorityAO.getPriorities();
		this.defaultPriority = priorityAO.getDefaultPriority();
		this.isPunctuationActive = priorityAO.isPunctuationActive();
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
				Byte priority = curPriority.getKey();
				next.getMetadata().setPriority(priority);
				transfer(next, 0);

				if (priority != 0) {
					processPrioritizedElement(next);
				}
				return;
			}
		}
		next.getMetadata().setPriority(this.defaultPriority);
		if (this.defaultPriority != 0) {
			processPrioritizedElement(next);
		}
		transfer(next);

		return;
	}

	private void processPrioritizedElement(T next) {
		transfer(next, 1);
		if (isPunctuationActive) {
			ITimeInterval time = (ITimeInterval) next.getMetadata();
			sendPunctuation(time.getStart().clone());
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	public boolean isPunctuationActive() {
		return isPunctuationActive;
	}

	public void setPunctuationActive(boolean isPunctuationActive) {
		this.isPunctuationActive = isPunctuationActive;
	}

	@Override
	public PriorityPO<K, T> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

}
