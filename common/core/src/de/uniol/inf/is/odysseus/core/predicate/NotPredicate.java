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
package de.uniol.inf.is.odysseus.core.predicate;


/**
 * @author Jonas Jacobi, Christian Kuka
 */
public class NotPredicate<T> extends AbstractPredicate<T> {
    private static final long serialVersionUID = -3214605315259491423L;
    IPredicate<T> predicate;

    /**
     * 
     * Class constructor.
     *
     */
    protected NotPredicate() {
    }

    /**
     * 
     * Class constructor.
     *
     * @param predicate
     */
    public NotPredicate(IPredicate<T> predicate) {
        this.predicate = predicate;
    }

    /**
     * 
     * @return The predicate of this negation
     */
    public IPredicate<? super T> getChild() {
        return this.predicate;
    }

    /**
     * Sets the predicate
     * 
     * @param child
     *            The predicate of this negation
     */
    public void setChild(IPredicate<T> predicate) {
        this.predicate = predicate;
    }
    
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(T input) {
		Boolean result = this.predicate.evaluate(input);
		if (result == null) {
			return null;
		}
		return Boolean.valueOf(!result);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(T left, T right) {
		Boolean result = this.predicate.evaluate(left, right);
		if (result == null) {
			return null;
		}
		return Boolean.valueOf(!result);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> and(IPredicate<T> other) {
        return new AndPredicate<>(this, other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> or(IPredicate<T> other) {
        return new OrPredicate<>(this, other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> not() {
        return this.predicate;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public NotPredicate<T> clone() {
        return new NotPredicate<>(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "NOT (" + getChild() + ")";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(IPredicate<T> pred) {
        if (!(pred instanceof NotPredicate)) {
            return false;
        }
        if (((NotPredicate<T>) pred).getChild().equals(this.predicate)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isContainedIn(IPredicate<?> o) {
        if (!(o instanceof NotPredicate)) {
            return false;
        }
        if (((NotPredicate<?>) o).getChild().isContainedIn(this.predicate)) {
            return true;
        }
        return false;
    }
}
