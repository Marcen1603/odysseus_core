/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;

/**
 * The BrokerQueryPredicate represents the query predicate for a SweepArea.
 * In this case it uses a {@link BrokerEqualPredicate} to evaluate to elements.
 *
 * @author Dennis Geesen
 *
 * @param <T> the generic type
 */
public class BrokerQueryPredicate<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPredicate<T> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 759591029481942568L;
	
	/** The broker equal predicate. */
	private BrokerEqualPredicate<T> brokerEqualPredicate;
	
	/**
	 * Instantiates a new broker query predicate which will evaluate the whole element.
	 */
	public BrokerQueryPredicate(){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>();
	}
	
	
	/**
	 * Instantiates a new broker query predicate which will only evaluate the attribute of a given position.
	 *
	 * @param position the position
	 */
	public BrokerQueryPredicate(int position){
		this.brokerEqualPredicate = new BrokerEqualPredicate<T>(position);
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.predicate.IPredicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(T input) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.predicate.IPredicate#evaluate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(T left, T right) {		 
		return brokerEqualPredicate.evaluate(left, right);		
	}


	@Override
	public BrokerQueryPredicate<T> clone() {
		BrokerQueryPredicate<T> clone = new  BrokerQueryPredicate<T>();
		clone.brokerEqualPredicate = this.brokerEqualPredicate.clone();
		return clone;
	}

}
