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
package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;

/**
 * Singleton because no object state is needed.
 * 
 * This will also be needed parallel to TotallyAfterPredicate, because it is
 * impossible to switch between Order.LeftRight and Order.RightLeft in complex
 * predicates.
 * 
 * @author Jonas Jacobi
 */
public class TotallyBeforePredicate extends AbstractPredicate<IStreamObject<? extends ITimeInterval>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3010416001774544889L;
	private static final TotallyBeforePredicate predicate = new TotallyBeforePredicate();

	@Override
	public boolean evaluate(IStreamObject<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean evaluate(IStreamObject<? extends ITimeInterval> left,
			IStreamObject<? extends ITimeInterval> right) {
		return TimeInterval.totallyBefore(left.getMetadata(), right
				.getMetadata());
	}

	@Override
	public TotallyBeforePredicate clone() {
		return this;
	}

	public static TotallyBeforePredicate getInstance() {
		return predicate;
	}

	private TotallyBeforePredicate() {

	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(IPredicate pred) {
		return (pred instanceof TotallyBeforePredicate);
	}
	
	@Override
	public boolean isContainedIn(IPredicate<?> o) {
		if(!(o instanceof TotallyBeforePredicate)) {
			return false;
		}
		return true;
	}

}
