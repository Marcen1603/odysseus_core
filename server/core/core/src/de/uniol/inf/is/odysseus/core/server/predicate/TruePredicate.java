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
package de.uniol.inf.is.odysseus.core.server.predicate;

import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

public class TruePredicate<T> extends AbstractPredicate<T> {
	private static final long serialVersionUID = 7701679660439284127L;

	@Override
	public boolean evaluate(T input) {
		return true;
	}

	@Override
	public boolean evaluate(T left, T right) {
		return true;
	}

	@Override
	public AbstractPredicate<T> clone() {
		return new TruePredicate<T>();
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		return (pred instanceof TruePredicate);
	}
	
	@Override
	public boolean isContainedIn(IPredicate<?> o) {
		if(!(o instanceof TruePredicate)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		return "true";
	}
	
}
