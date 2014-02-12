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

public class Pair<E1, E2> implements IPair<E1, E2>{

	final E1 e1;
	final E2 e2;
	
	public Pair(E1 e1, E2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((e1 == null) ? 0 : e1.hashCode());
		result = PRIME * result + ((e2 == null) ? 0 : e2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair<?,?> other = (Pair<?,?>) obj;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!e1.equals(other.e1))
			return false;
		if (e2 == null) {
			if (other.e2 != null)
				return false;
		} else if (!e2.equals(other.e2))
			return false;
		return true;
	}


	@Override
	public E1 getE1() {
		return e1;
	}


	@Override
	public E2 getE2() {
		return e2;
	}
	
	@Override
	public String toString() {
		return "["+e1+";"+e2+"]";
	}
	
}
