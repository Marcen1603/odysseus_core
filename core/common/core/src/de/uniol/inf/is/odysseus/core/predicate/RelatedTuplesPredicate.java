/********************************************************************************** 
  * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * This class represents a predicate to evaluate whether the given pair of tuples
 * are related or not.
 * */

public class RelatedTuplesPredicate<T extends Tuple<? extends IMetaAttribute>> extends AbstractPredicate<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6464367843640034137L;
	
	/**
	 * The positions of the keys to be evaluated for checking related tuples
	 * */
	private List<Integer> keysPositions;
	
	public RelatedTuplesPredicate(List<Integer> keysPositions){
		this.setKeysPositions(keysPositions);
	}
	
	public RelatedTuplesPredicate(RelatedTuplesPredicate<T> another){
		this.setKeysPositions(another.getKeysPositions());
	}

	@Override
	public Boolean evaluate(T input) {
		//any tuple is related to itself
		return Boolean.TRUE;
	}

	@Override
	public Boolean evaluate(T left, T right) {
		for(int i=0; i<this.keysPositions.size(); i+=2){
			int leftKey = this.keysPositions.get(i);
			int rightKey = this.keysPositions.get(i+1);
			if(leftKey < 0 || rightKey < 0 || leftKey >= left.size() || rightKey >= right.size())
				throw new IndexOutOfBoundsException("keys indices are not compatible with the tuples");
			if(left.getAttribute(leftKey).getClass() != right.getAttribute(rightKey).getClass())
				throw new IncompatibleClassChangeError("keys and tuples types are not compatible");
			if(!left.getAttribute(leftKey).equals(right.getAttribute(rightKey) ) )
				return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public AbstractPredicate<T> clone() {
		return new RelatedTuplesPredicate<>(this);
	}

	/**
	 * @return the keysPositions
	 */
	public List<Integer> getKeysPositions() {
		return keysPositions;
	}

	/**
	 * @param keysPositions the keysPositions to set
	 */
	public void setKeysPositions(List<Integer> keysPositions) {
		this.keysPositions = keysPositions;
	}

}
