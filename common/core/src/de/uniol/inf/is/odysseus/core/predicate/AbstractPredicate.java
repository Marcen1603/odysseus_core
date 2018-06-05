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

import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * This is an abstract superclass for all predicates, that provides an empty
 * init method.
 *
 * @author Andre Bolles, Christian Kuka
 *
 */
public abstract class AbstractPredicate<T> implements IPredicate<T> {

    private static final long serialVersionUID = -2182745249884399237L;

    /**
     *
     * Class constructor.
     *
     */
    public AbstractPredicate() {
    }

    /**
     *
     * Class constructor.
     *
     * @param pred
     */
    public AbstractPredicate(AbstractPredicate<T> pred) {
        // Empty block
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    abstract public AbstractPredicate<T> clone();

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<SDFAttribute> getAttributes() {
        return Collections.EMPTY_LIST;
    }

    /**
     *
     * {@inheritDoc}
     */
    // TODO: IMplement in Child Classes...
    @Override
    public boolean equals(IPredicate<T> pred) {
        return false;
    }

    @Override
    public IPunctuation processPunctuation(IPunctuation punctuation) {
    	return punctuation;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isContainedIn(IPredicate<?> o) {
        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isAlwaysTrue() {
        return false;
    }

    @Override
    public boolean isAlwaysFalse() {
    	return false;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException
     *             if not implemented in inherited class.
     */
    @Override
    public IPredicate<T> and(IPredicate<T> predicate) {
        throw new UnsupportedOperationException("Conjunction not supported for " + this.getClass());
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException
     *             if not implemented in inherited class.
     */
    @Override
    public IPredicate<T> or(IPredicate<T> predicate) {
        throw new UnsupportedOperationException("Disjunction not supported for " + this.getClass());
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException
     *             if not implemented in inherited class.
     */
    @Override
    public IPredicate<T> not() {
        throw new UnsupportedOperationException("Negation not supported for " + this.getClass());
    }

    @Override
    public List<? extends IPredicate<? super T>> conjunctiveSplit() {
        throw new UnsupportedOperationException("Spit not supported for " + this.getClass());
    }
}
