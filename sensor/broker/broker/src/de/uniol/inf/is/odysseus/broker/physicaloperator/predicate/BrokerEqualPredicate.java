/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.broker.physicaloperator.predicate;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

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
	 * @see de.uniol.inf.is.odysseus.core.server.predicate.IPredicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(T input) {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.predicate.IPredicate#evaluate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(T left, T right) {
		if (this.position == -1) {
			return left.equals(right);
		}
        Tuple<ITimeInterval> leftTuple = (Tuple<ITimeInterval>) left;
        Tuple<ITimeInterval> rightTuple = (Tuple<ITimeInterval>) right;	
        if( leftTuple.getMetadata().getStart().equals(rightTuple.getMetadata().getStart()) ) {
//			if (leftTuple.getAttribute(this.position).equals(rightTuple.getAttribute(this.position))) {				
        	return true;
        }
        return false;
	}
	
	@Override
	public BrokerEqualPredicate<T> clone() {
		return new BrokerEqualPredicate<T>(this);
	}


}