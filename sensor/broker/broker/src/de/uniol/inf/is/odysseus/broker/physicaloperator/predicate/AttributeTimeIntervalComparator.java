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

import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class AttributeTimeIntervalComparator<T extends IClone> implements Comparator<Tuple<ITimeInterval>>{
	private int attributePos = 1;
	
	@Override
	public int compare(Tuple<ITimeInterval> left, Tuple<ITimeInterval> right) {	
		int compare = left.getMetadata().compareTo(right.getMetadata());
		if(compare==0){
			int idLeft = ((Integer)left.getAttribute(attributePos)).intValue();
			int idRight = ((Integer)right.getAttribute(attributePos)).intValue();
			if(idLeft<idRight){
				return -1;
			}
            if(idLeft>idRight){
            	return 1;
            }
            return 0;
		}
        return compare;
		
	}


}
