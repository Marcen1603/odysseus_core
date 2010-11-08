package de.uniol.inf.is.odysseus.priority_interval;

import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class NonPriorityPredicate<T extends IMetaAttributeContainer<? extends IPriority>> implements IPredicate<T> {

	private static final long serialVersionUID = 3293073689856493084L;

	@Override
	public boolean evaluate(T input) {
		return input.getMetadata().getPriority() == 0;
	}

	@Override
	public boolean evaluate(T left, T right) {
		return left.getMetadata().getPriority() == 0;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated) {
		
	}
	
	@Override
	public NonPriorityPredicate<T> clone() {
		return new NonPriorityPredicate<T>();
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		return pred instanceof NonPriorityPredicate;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}
