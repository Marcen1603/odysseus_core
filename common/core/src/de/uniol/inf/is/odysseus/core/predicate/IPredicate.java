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

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * Base Interface for all types of predicates
 *
 * @author Jonas Jacobi, Christian Kuka
 */
public interface IPredicate<T> extends IClone, Serializable {

    /**
     * Evaluate the predicate with the input
     *
     * @param input
     *            The input
     * @return <code>true</code> if the input fulfills the predicate,
     *         <code>false</code> otherwise
     */
	Boolean evaluate(T input);

    /**
     * Evaluate the predicate with a left and a right input (e.g. in joins)
     *
     * @param left
     *            The left input
     * @param right
     *            The right input
     * @return <code>true</code> if the left and right inputs fulfills the
     *         predicate, <code>false</code> otherwise
     */
	Boolean evaluate(T left, T right);

	/**
	 * Some predicates may want to receive information from punctutions. The predicate
	 * can modify the punctuation or send the original punctuation back
	 *
	 * @param punctuation The punctuation
	 * @return the old or a new punctation (e.g. send by an operator)
	 */
	IPunctuation processPunctuation(IPunctuation punctuation);

    /**
     * Checks if this predicate is contained in another predicate.
     *
     * @param predicate
     *            The predicate
     * @return <code>true</code> if contained, <code>false</code> if not or not
     *         decidable
     */
    boolean isContainedIn(IPredicate<?> predicate);

    /**
     * Get the list of all attributes in this predicate.
     *
     * @return A {@link List} of all {@link SDFAttribute} in the predicate
     */
    public List<SDFAttribute> getAttributes();

    /**
     * Checks whether the predicate always evaluate to <code>true</code>
     *
     * @return Flag whether the predicate always evaluate to <code>true</code>
     */
    public boolean isAlwaysTrue();

    /**
     * Checks whether the predicate always evaluate to <code>false</code>
     *
     * @return Flag whether the predicate always evaluate to <code>false</code>
     */
    public boolean isAlwaysFalse();


    /**
     * Returns the conjunction of this predicate and the given predicate.
     *
     *
     * @param predicate
     *            The predicate
     * @return The conjunction of the two predicates
     */
    IPredicate<T> and(IPredicate<T> predicate);

    /**
     * Returns the disjunction of this predicate and the given predicate.
     *
     * @param predicate
     *            The predicate
     * @return The disjunction of the two predicates
     */
    IPredicate<T> or(IPredicate<T> predicate);

    /**
     * Returns the negation of this predicate.
     *
     * @return The negation of this predicate
     */
    IPredicate<T> not();

    /**
     * Compares two predicates for equality.
     *
     * @param predicate
     *            The predicate to compare with
     * @return true, if both predicates are equal
     */
    public boolean equals(IPredicate<T> predicate);

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> clone();

	public List<? extends IPredicate<? super T>> conjunctiveSplit();

}
