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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas Jacobi, Christian Kuka
 */
public class OrPredicate<T> extends ComplexPredicate<T> {
    private static final long serialVersionUID = -5476180354530944122L;

    /**
     * 
     * Class constructor.
     *
     */
    public OrPredicate() {
        super();
    }

    /**
     * 
     * Class constructor.
     *
     * @param leftPredicate
     * @param rightPredicate
     */
    public OrPredicate(IPredicate<? super T> leftPredicate, IPredicate<? super T> rightPredicate) {
        super(leftPredicate, rightPredicate);
    }

    /**
     * 
     * Class constructor.
     *
     * @param predicate
     */
    protected OrPredicate(OrPredicate<T> predicate) {
        super(predicate);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
	public Boolean evaluate(T input) {
		Boolean left = getLeft().evaluate(input);
		Boolean right = getRight().evaluate(input);
		if ((left == null) || (right == null)) {
			if (((left == null) && (right == null)) || ((left == null) && (right != null) && (right == false))
					|| ((right == null) && (left != null) && (left == false))) {
				return null;
			}
			return Boolean.TRUE;
		}
		return Boolean.valueOf(left || right);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
	public Boolean evaluate(T leftInput, T rightInput) {
		Boolean left = getLeft().evaluate(leftInput, rightInput);
		Boolean right = getRight().evaluate(leftInput, rightInput);
		if ((left == null) || (right == null)) {
			if (((left == null) && (right == null)) || ((left == null) && (right != null) && (right == false))
					|| ((right == null) && (left != null) && (left == false))) {
				return null;
			}
			return Boolean.TRUE;
		}
		return Boolean.valueOf(left || right);
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

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public OrPredicate<T> clone() {
        return new OrPredicate<>(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + getLeft().toString() + ") OR (" + getRight().toString() + ")";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof OrPredicate)) {
            return false;
        }
        return this.getLeft().equals(((OrPredicate<?>) other).getLeft()) && this.getRight().equals(((OrPredicate<?>) other).getRight());
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 19 * this.getLeft().hashCode() + 19 * this.getRight().hashCode();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(IPredicate<T> pred) {
        if (!(pred instanceof OrPredicate)) {
            return false;
        }
        OrPredicate<T> op = (OrPredicate<T>) pred;
        // The Order of the Predicates shouldn't matter
        return (this.getLeft().equals(op.getLeft()) && this.getRight().equals(op.getRight())) || (this.getLeft().equals(op.getRight()) && this.getRight().equals(op.getLeft()));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isContainedIn(IPredicate<?> predicate) {
        // Falls o kein OrPredicate ist, wird false zur�ck gegeben, es sei denn,
        // beide Pr�dikate dieses Oders sind in o enthalten
        if (!(predicate instanceof OrPredicate)) {
            if (this.getLeft().isContainedIn(predicate) && this.getRight().isContainedIn(predicate)) {
                return true;
            }
            return false;
        }
        OrPredicate<?> op = (OrPredicate<?>) predicate;

        List<IPredicate<?>> a = extractAllPredicates(this);
        List<IPredicate<?>> b = extractAllPredicates(op);

        // F�r JEDES Pr�dikat aus diesem OrPredicate muss ein enthaltenes
        // Pr�dikat in dem anderen OrPredicate gefunden werden
        // (Das andere Oder-Pr�dikat darf noch weitere Pr�dikate haben)
        for (IPredicate<?> preda : a) {
            boolean foundmatch = false;
            for (IPredicate<?> predb : b) {
                if (preda.isContainedIn(predb)) {
                    foundmatch = true;
                }
            }
            if (!foundmatch) {
                return false;
            }
        }
        return true;
    }

    private List<IPredicate<?>> extractAllPredicates(OrPredicate<?> predicate) {
        List<IPredicate<?>> a = new ArrayList<>();
        if (predicate.getLeft() instanceof OrPredicate) {
            a.addAll(predicate.extractAllPredicates((OrPredicate<?>) predicate.getLeft()));
        }
        else {
            a.add(predicate.getLeft());
        }
        if (predicate.getRight() instanceof OrPredicate) {
            a.addAll(extractAllPredicates((OrPredicate<?>) predicate.getRight()));
        }
        else {
            a.add(predicate.getRight());
        }
        return a;
    }

    /**
     * Checks whether the predicate is contained in this disjunction.
     * 
     * @param predicate
     *            The predicate
     * @return <code>true</code> if this predicate is contained in this
     *         disjunction
     */
    public boolean contains(Object predicate) {
        if (!(predicate instanceof IPredicate)) {
            return false;
        }
        for (IPredicate<?> a : extractAllPredicates(this)) {
            if (((IPredicate<?>) predicate).isContainedIn(a)) {
                return true;
            }
        }
        return false;
    }

}
