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
 * Predicate that evaluates to TRUE.
 * 
 * @author Christian Kuka
 * 
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class TruePredicate<T> extends AbstractPredicate<T> {
    private static final long serialVersionUID = 7701679660439284127L;
    private static final TruePredicate instance = new TruePredicate();

    static public TruePredicate getInstance() {
        return instance;
    }

    private TruePredicate() {
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
	public Boolean evaluate(Object input) {
		return Boolean.TRUE;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
	public Boolean evaluate(Object left, Object right) {
		return Boolean.TRUE;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public AbstractPredicate<T> clone() {
        return this;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(IPredicate<T> predicate) {
        return predicate.getClass().equals(this.getClass());
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isContainedIn(IPredicate<?> predicate) {
        return predicate.getClass().equals(this.getClass());
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "true";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> and(IPredicate<T> predicate) {
        return predicate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> or(IPredicate<T> predicate) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public IPredicate<T> not() {
        return FalsePredicate.getInstance();
    }
}
