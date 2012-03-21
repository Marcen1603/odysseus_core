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
package de.uniol.inf.is.odysseus.core.collection;

import de.uniol.inf.is.odysseus.core.IClone;

public class CloneableIdPair<E1 extends IClone, E2 extends IClone> implements IPair<E1, E2> {

	protected E1 e1;
	protected E2 e2;
	
	public CloneableIdPair(E1 e1, E2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@SuppressWarnings("unchecked")
	public CloneableIdPair(ClonablePair<E1, E2> pair, boolean deepClone) {
		if (deepClone){
			this.e1 = (E1)pair.e1.clone();
			this.e2 = (E2)pair.e2.clone();
		}else{
			this.e1 = pair.e1;
			this.e2 = pair.e2;
		}
	}

	@Override
	public E1 getE1() {
		return e1;
	}

	@Override
	public void setE1(E1 e1) {
		this.e1 = e1;
	}
	
	@Override
	public E2 getE2() {
		return e2;
	}
	
	@Override
	public void setE2(E2 e2) {
		this.e2 = e2;
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("(");		
		ret.append(e1).append(",");
		ret.append(e2).append(")");
		return ret.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
		result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CloneableIdPair other = (CloneableIdPair) obj;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!(e1 == other.e1)) // Identity!
			return false;
		if (e2 == null) {
			if (other.e2 != null)
				return false;
		} else if (!(e2 == other.e2)) // Identity!
			return false;
		return true;
	}
	
	
}
