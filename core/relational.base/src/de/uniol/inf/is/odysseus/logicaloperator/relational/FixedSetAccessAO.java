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
package de.uniol.inf.is.odysseus.logicaloperator.relational;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;

public class FixedSetAccessAO<T extends IMetaAttributeContainer<? extends IClone>> extends AccessAO {

	private static final long serialVersionUID = -4026927772571867684L;
	private final T[] tuples;
	
	public FixedSetAccessAO(String name, String type, T... tuples) {
		super(name, type, null);
		this.tuples = tuples;
	}
	
	public T[] getTuples() {
		return tuples;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + Arrays.hashCode(tuples);
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		@SuppressWarnings("unchecked")
//		FixedSetAccessAO other = (FixedSetAccessAO) obj;
//		if (!Arrays.equals(tuples, other.tuples))
//			return false;
//		return true;
//	}
}
