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
package de.uniol.inf.is.odysseus.priority_interval;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class NonPriorityPredicate<T extends IMetaAttributeContainer<? extends IPriority>> implements IPredicate<T> {

	private static final long serialVersionUID = 3293073689856493084L;

	@Override
	public boolean evaluate(T input) {
		return input.getMetadata().getPriority() == 0;
	}

	@Override
	public boolean evaluate(T left, T right) {
		return left.getMetadata().getPriority() == 0;
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public NonPriorityPredicate<T> clone() {
		return new NonPriorityPredicate<T>();
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		return pred instanceof NonPriorityPredicate;
	}
	
	@Override
	public boolean isContainedIn(IPredicate<?> o) {
		return false;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		return false;
	}
	
	@Override
	public List<SDFAttribute> getAttributes() {
		return null;
	}

}
