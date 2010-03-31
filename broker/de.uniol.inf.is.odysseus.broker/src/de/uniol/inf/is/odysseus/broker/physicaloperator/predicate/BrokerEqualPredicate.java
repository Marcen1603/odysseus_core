package de.uniol.inf.is.odysseus.broker.physicaloperator.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class BrokerEqualPredicate<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPredicate<T> {

	private static final long serialVersionUID = -3464913151655062309L;

	private int position;

	public BrokerEqualPredicate() {
		this.position = -1;
	}

	public BrokerEqualPredicate(int position) {
		this.position = position;
	}

	@Override
	public boolean evaluate(T input) {
		return false;
	}

	@Override
	public boolean evaluate(T left, T right) {
		if (this.position == -1) {
			return left.equals(right);
		} else {
			RelationalTuple<ITimeInterval> leftTuple = (RelationalTuple<ITimeInterval>) left;
			RelationalTuple<ITimeInterval> rightTuple = (RelationalTuple<ITimeInterval>) right;			
			if (leftTuple.getAttribute(this.position).equals(rightTuple.getAttribute(this.position))) {				
				return true;
			} else {				
				return false;
			}
		}
	}

}