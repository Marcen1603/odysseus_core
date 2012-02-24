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
package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;

public class LiesInPredicate extends AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1178472407512980479L;
	private static final LiesInPredicate instance = new LiesInPredicate();
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left, IMetaAttributeContainer<? extends ITimeInterval> right){
		if(left.getMetadata().getStart().beforeOrEquals(right.getMetadata().getStart()) &&
				left.getMetadata().getEnd().afterOrEquals(right.getMetadata().getEnd())){
			return true;
		}
		return false;
	}
	
	/**
	 * @deprecated This method is not supported by this predicate.
	 */
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public LiesInPredicate clone(){
		return this;
	}
	
	public static LiesInPredicate getInstance(){
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(IPredicate pred) {
		if(!(pred instanceof LiesInPredicate)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof LiesInPredicate)) {
			return false;
		}
		return true;
	}
}
