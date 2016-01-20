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
import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;


public class EqualsPredicate<T> extends AbstractPredicate<T> {

	private static final long serialVersionUID = 405493232110297596L;
	@SuppressWarnings({ "rawtypes" })
	final private static EqualsPredicate predicate = new EqualsPredicate();

	@Override
	public Boolean evaluate(T input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Boolean evaluate(T left, T right) {
		return left.equals(right);
	}

	@SuppressWarnings("unchecked")
	public static <T> EqualsPredicate<T> getInstance() {
		return predicate;
	}

	@Override
	@SuppressWarnings("unchecked")
	public EqualsPredicate<T> clone() {
		return predicate;
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		if(!(pred instanceof EqualsPredicate)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isContainedIn(IPredicate<?> o) {
		if(!(o instanceof EqualsPredicate)) {
			return false;
		}
		return true;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> and(IPredicate<T> predicate) {
        return new AndPredicate<>(this, predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> or(IPredicate<T> predicate) {
        return new OrPredicate<>(this, predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> not() {
        return new NotPredicate<>(this);
    }
}
