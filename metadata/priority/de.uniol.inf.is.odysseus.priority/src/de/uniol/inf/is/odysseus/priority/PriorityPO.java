package de.uniol.inf.is.odysseus.priority;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

/**
 * @author Jonas Jacobi
 */
public class PriorityPO<T extends IMetaAttribute<? extends IPriority>> extends
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
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T next, int port) {
		for (Map.Entry<Byte, IPredicate<? super T>> curPriority : this.priorites
				.entrySet()) {
			if (curPriority.getValue().evaluate(next)) {
				next.getMetadata().setPriority(curPriority.getKey());
				transfer(next);
				return;
			}
		}
		next.getMetadata().setPriority(this.defaultPriority);
		transfer(next);
		return;
	}

}
