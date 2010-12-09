package de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

/**
 * The BrokerEqualPredicate evaluates to elements of type <T>.
 * Two elements are equal if either a given attribute (if a position were given) 
 * or the whole element is equal to another element.
 * 
 * @author Dennis Geesen
 *
 * @param <T> the type of an element
 */
public class BrokerEqualPredicate<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPredicate<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3464913151655062309L;

	/** The position of the attribute. */
	private int position;

	/**
	 * Instantiates a new predicate which will evaluate the whole element.
	 */
	public BrokerEqualPredicate() {
		this.position = -1;
	}

	/**
	 * Instantiates a new predicate which will only evaluate the attribute of a given position.
	 *
	 * @param position the position
	 */
	public BrokerEqualPredicate(int position) {
		this.position = position;
	}

	public BrokerEqualPredicate(BrokerEqualPredicate<T> brokerEqualPredicate) {
		this.position = brokerEqualPredicate.position;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.predicate.IPredicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(T input) {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.predicate.IPredicate#evaluate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(T left, T right) {
		return true;
//		if (this.position == -1) {
//			return left.equals(right);
//		} else {
//			RelationalTuple<ITimeInterval> leftTuple = (RelationalTuple<ITimeInterval>) left;
//			RelationalTuple<ITimeInterval> rightTuple = (RelationalTuple<ITimeInterval>) right;			
//			if (leftTuple.getAttribute(this.position).equals(rightTuple.getAttribute(this.position))) {				
//				return true;
//			} else {				
//				return false;
//			}
//		}
	}
	
	@Override
	public BrokerEqualPredicate<T> clone() {
		return new BrokerEqualPredicate<T>(this);
	}


}