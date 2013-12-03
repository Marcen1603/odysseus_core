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
package de.uniol.inf.is.odysseus.core.collection;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * First Element Sorted Pair
 * @author Marco Grawunder
 *
 * @param <E1>
 * @param <E2>
 */
public class FESortedClonablePair<E1 extends IClone,E2 extends IClone> extends ClonablePair<E1, E2> implements Comparable<FESortedClonablePair<E1,E2>>, IClone{
		
	public FESortedClonablePair(E1 e1, E2 e2) {
		super(e1, e2);
	}

	public FESortedClonablePair(FESortedClonablePair<E1, E2> feSortedPair, boolean deepClone) {
		super(feSortedPair,deepClone);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(FESortedClonablePair<E1, E2> o) {
		Comparable<E1> comparableE1 = (Comparable<E1>) this.getE1();
		return comparableE1.compareTo(o.getE1());
	}
	
	@Override
	public FESortedClonablePair<E1, E2> clone(){
		return new FESortedClonablePair<E1, E2>(this,false);
	}
	
}