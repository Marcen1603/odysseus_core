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
/**
 * First Element Sorted Pair
 * @author Marco Grawunder
 *
 * @param <E1>
 * @param <E2>
 */
public class FESortedPair<E1 extends Comparable<E1>,E2> extends Pair<E1, E2> implements Comparable<FESortedPair<E1,E2>>{
		
	public FESortedPair(E1 e1, E2 e2) {
		super(e1, e2);
	}

	@Override
	public int compareTo(FESortedPair<E1, E2> o) {
		return this.getE1().compareTo(o.getE1());
	}
	
	
}

