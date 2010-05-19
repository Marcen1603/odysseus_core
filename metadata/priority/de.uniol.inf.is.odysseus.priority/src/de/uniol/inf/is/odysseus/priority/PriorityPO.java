package de.uniol.inf.is.odysseus.priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

/**
 * @author Jonas Jacobi, Jan Steinke
 */
public class PriorityPO<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPipe<T, T> {

	private final Map<Byte, IPredicate<? super T>> priorites;

	protected List<IPostPriorisationPipe<?>> copartners;

	public void setCopartners(List<IPostPriorisationPipe<?>> copartners) {
		this.copartners = copartners;
	}

	public List<IPostPriorisationPipe<?>> getCopartners() {
		return copartners;
	}

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
		this.copartners = new ArrayList<IPostPriorisationPipe<?>>();
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
				if (curPriority.getKey() != 0 && isPunctuationActive) {
					sendPunctuation(time.getStart());
				}

				if (copartners != null) {
					for (IPostPriorisationPipe<?> each : copartners) {
						each.addTimeInterval((IMetaAttributeContainer<?>) next);
					}
				}

				return;
			}
		}
		next.getMetadata().setPriority(this.defaultPriority);
		transfer(next);

		return;
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
	public PriorityPO<T> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


}
