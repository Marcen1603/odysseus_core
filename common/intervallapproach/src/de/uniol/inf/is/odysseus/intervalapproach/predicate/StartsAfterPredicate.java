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
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * 
 * @author Christian Kuka
 *
 */
public class StartsAfterPredicate extends AbstractTimeIntervalPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6773517774767435885L;
	private static final StartsAfterPredicate instance = new StartsAfterPredicate();
	
	@Override
	public StartsAfterPredicate clone(){
		return this;
	}
	
	@Override
	public Boolean evaluate(IStreamObject<? extends ITimeInterval> elem) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Boolean evaluate(IStreamObject<? extends ITimeInterval> left, IStreamObject<? extends ITimeInterval> right) {
		return left.getMetadata().getStart().afterOrEquals(right.getMetadata().getStart());
	}
	
	public static StartsAfterPredicate getInstance(){
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(IPredicate pred) {
		return (pred instanceof StartsAfterPredicate);
	}
	
	@Override
	public boolean isContainedIn(IPredicate<?> o) {
		if(!(o instanceof StartsAfterPredicate)) {
			return false;
		}
		return true;
	}
}
