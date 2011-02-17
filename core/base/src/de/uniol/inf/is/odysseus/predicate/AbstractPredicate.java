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
package de.uniol.inf.is.odysseus.predicate;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

/**
 * This is an abstract superclass for all predicates, that provides an empty init method.
 * @author Andre Bolles
 *
 */
public abstract class AbstractPredicate<T> implements IPredicate<T>, Serializable {


	private static final long serialVersionUID = -2182745249884399237L;

	@Override
	public void init(){
	}


	public AbstractPredicate() {
	}

	public AbstractPredicate(AbstractPredicate<T> pred) {
	}
	
	@Override
	abstract public AbstractPredicate<T> clone();
	
	@Override
	public void updateAfterClone(Map<ILogicalOperator,ILogicalOperator> updated) {};
	
	// TODO: IMplement in Child Classes... 
	@Override
	public boolean equals(IPredicate<T> pred) {
		return false;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		return false;
	}
}
