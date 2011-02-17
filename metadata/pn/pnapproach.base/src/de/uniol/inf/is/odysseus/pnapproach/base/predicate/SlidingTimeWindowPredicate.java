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
package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

public class SlidingTimeWindowPredicate<T extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPredicate<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6486548910841710973L;
	long windowSize;
	
	public SlidingTimeWindowPredicate(long windowSize){
		this.windowSize = windowSize;
	}
	
	public SlidingTimeWindowPredicate(SlidingTimeWindowPredicate<T> old){
		this.windowSize = old.windowSize;
	}

	@Override
	@Deprecated
	public boolean evaluate(T object){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean evaluate(T left, T right){
		PointInTime end = new PointInTime(right.getMetadata().getTimestamp().sum(this.windowSize));
		if(end.beforeOrEquals(left.getMetadata().getTimestamp())){
			return true;
		}
		return false;
	}
	
	@Override
	public SlidingTimeWindowPredicate<T> clone(){
		return new SlidingTimeWindowPredicate<T>(this);
	}
}
