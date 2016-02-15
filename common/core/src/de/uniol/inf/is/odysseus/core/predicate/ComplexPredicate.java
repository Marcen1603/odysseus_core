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
public abstract class ComplexPredicate<T> extends AbstractPredicate<T> {
    private static final long serialVersionUID = 5112319812675937729L;
    /** The left predciate. */
    private IPredicate<? super T> left;
    /** The right predicate. */
    private IPredicate<? super T> right;

    /**
     * 
     * Class constructor.
     *
     */
    public ComplexPredicate() {

    }

    /**
     * 
     * Class constructor.
     *
     * @param left
     *            The left predicate
     * @param right
     *            The right predicate
     */
    public ComplexPredicate(IPredicate<? super T> left, IPredicate<? super T> right) {
        this.left = left;
        this.right = right;
    }

    /**
     * 
     * Class constructor.
     *
     * @param predicate
     */
    public ComplexPredicate(ComplexPredicate<T> predicate) {
        this.left = predicate.left.clone();
        this.right = predicate.right.clone();
    }

    /**
     * Gets the left predicate.
     * 
     * @return The left predicate
     */
    public IPredicate<? super T> getLeft() {
        return this.left;
    }

    /**
     * Sets the left predicate
     * 
     * @param left
     *            The left predicate to set
     */
    public void setLeft(IPredicate<? super T> left) {
        this.left = left;
    }

    /**
     * Gets the right predicate
     * 
     * @return The right predicate
     */
    public IPredicate<? super T> getRight() {
        return this.right;
    }

    /**
     * Sets the right predicate
     * 
     * @param right
     *            The right predicate to set
     */
    public void setRight(IPredicate<? super T> right) {
        this.right = right;
    }

    // @Override
    // public ComplexPredicate<T> clone() {
    // ComplexPredicate<T> newPred;
    // newPred = (ComplexPredicate<T>) super.clone();
    // newPred.left = this.left.clone();
    // newPred.right = this.right.clone();
    // return newPred;
    // }

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
    public boolean equals(Object other) {
        if (!(other instanceof ComplexPredicate)) {
            return false;
        }
        return this.left.equals(((ComplexPredicate<?>) other).left) && this.right.equals(((ComplexPredicate<?>) other).right);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 37 * this.left.hashCode() + 41 * this.right.hashCode();
    }

}
