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

public class LiesInPNPredicate extends AbstractPredicate<IMetaAttributeContainer<? extends IPosNeg>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2502764977802196462L;
	PointInTime t_start;
	PointInTime t_end;
	
	public LiesInPNPredicate(PointInTime t_start, PointInTime t_end){
		this.t_start = t_start;
		this.t_end = t_end;
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> left, IMetaAttributeContainer<? extends IPosNeg> right){
		if(left.getMetadata().getTimestamp().afterOrEquals(this.t_start) &&
				left.getMetadata().getTimestamp().beforeOrEquals(this.t_end)){
			return true;
		}
		return false;
	}
	
	/**
	 * @deprecated This method is not supported by this predicate.
	 */
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> input) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public LiesInPNPredicate clone(){
		return new LiesInPNPredicate(this.t_start, this.t_end);
	}
}
