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

/**
 * Predicate that evaluates to FALSE.
 * 
 * @author Tobias Witt, Christian Kuka
 * 
 * @param <T>
 */
public class FalsePredicate<T> extends AbstractPredicate<T> {
    private static final long serialVersionUID = -134272927237350983L;
    private static final FalsePredicate instance = new FalsePredicate();

    static public FalsePredicate getInstance() {
        return instance;
    }

    private FalsePredicate() {
    }

    @Override
    public boolean evaluate(T input) {
        return false;
    }

    @Override
    public boolean evaluate(T left, T right) {
        return false;
    }

    @Override
    public AbstractPredicate<T> clone() {
        return this;
    }

    @Override
    public boolean equals(IPredicate<T> predicate) {
        return predicate.getClass().equals(this.getClass());
    }

    @Override
    public boolean isContainedIn(IPredicate<?> predicate) {
        return predicate.getClass().equals(this.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> and(IPredicate<T> predicate) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> or(IPredicate<T> predicate) {
        return predicate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> not() {
        return TruePredicate.getInstance();
    }
}
