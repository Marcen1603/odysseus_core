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

/**
 * @author Jonas Jacobi, Christian Kuka
 */
public class AndPredicate<T> extends ComplexPredicate<T> {

    private static final long serialVersionUID = -3438130138466305862L;

    /**
     * 
     * Class constructor.
     *
     */
    public AndPredicate() {
        super();
    }

    /**
     * 
     * Class constructor.
     *
     * @param leftPredicate
     * @param rightPredicate
     */
    public AndPredicate(IPredicate<? super T> leftPredicate, IPredicate<? super T> rightPredicate) {
        super(leftPredicate, rightPredicate);
    }

    /**
     * 
     * Class constructor.
     *
     * @param predicate
     */
    protected AndPredicate(AndPredicate<T> predicate) {
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
			if (((left == null) && (right == null)) || ((left == null) && (right != null) && (right == true))
					|| ((right == null) && (left != null) && (left == true))) {
				return null;
			}
			return Boolean.FALSE;
		}
		return Boolean.valueOf(left && right);
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
			if (((left == null) && (right == null)) || ((left == null) && (right != null) && (right == true))
					|| ((right == null) && (left != null) && (left == true))) {
				return null;
			}
			return Boolean.FALSE;
		}
		return Boolean.valueOf(left && right);
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
    public AndPredicate<T> clone() {
        return new AndPredicate<>(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + getLeft().toString() + ") AND (" + getRight().toString() + ")";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean equals(Object other) {
        if (!(other instanceof AndPredicate)) {
            return false;
        }
        return this.getLeft().equals(((AndPredicate) other).getLeft()) && this.getRight().equals(((AndPredicate) other).getRight());
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 17 * this.getLeft().hashCode() * 19 * this.getRight().hashCode();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(IPredicate<T> predicate) {
        if (!(predicate instanceof AndPredicate)) {
            return false;
        }
        AndPredicate<T> ap = (AndPredicate<T>) predicate;
        // return this.getLeft().equals(ap.getLeft()) &&
        // this.getRight().equals(ap.getRight());
        if ((this.getLeft().equals(ap.getLeft()) && this.getRight().equals(ap.getRight())) || (this.getLeft().equals(ap.getRight()) && this.getRight().equals(ap.getLeft()))) {
            return true;
        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isContainedIn(IPredicate<?> predicate) {
        // Z.B. ist a in b enthalten, falls a= M && N und b = M oder b=N ist
        // (Zus�tzliche Versch�rfung bestehender Pr�dikate)
        if (!(predicate instanceof AndPredicate) && (this.getLeft().isContainedIn(predicate) || this.getRight().isContainedIn(predicate))) {
            return true;
        }
        // Falls es sich beim anderen Pr�dikat ebenfalls um ein AndPredicate
        // handelt, m�ssen beide Pr�dikate verglichen werden (inklusiver aller
        // "Unterpr�dikate")
        if (predicate instanceof AndPredicate) {
            AndPredicate<T> ap = (AndPredicate<T>) predicate;

            ArrayList<IPredicate<?>> a = extractAllPredicates(this);
            ArrayList<IPredicate<?>> b = extractAllPredicates(ap);

            // F�r JEDES Pr�dikat aus dem anderen AndPredicate muss ein
            // enthaltenes Pr�dikat in diesem AndPredicate gefunden werden
            // (Nur weitere Versch�rfungen sind zul�ssig, deshalb darf keine
            // Bedingung des anderen Pr�dikats st�rker sein)
            for (IPredicate<?> predb : b) {
                // if(predb instanceof OrPredicate) {
                // return false;
                // }
                boolean foundmatch = false;
                for (IPredicate<?> preda : a) {
                    // if(preda instanceof OrPredicate) {
                    // return false;
                    // }
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

        return false;
    }

    private ArrayList<IPredicate<?>> extractAllPredicates(AndPredicate<?> ap) {
        ArrayList<IPredicate<?>> a = new ArrayList<>();
        if (ap.getLeft() instanceof AndPredicate) {
            a.addAll(ap.extractAllPredicates((AndPredicate<?>) ap.getLeft()));
        }
        else {
            a.add(ap.getLeft());
        }
        if (ap.getRight() instanceof AndPredicate) {
            a.addAll(extractAllPredicates((AndPredicate<?>) ap.getRight()));
        }
        else {
            a.add(ap.getRight());
        }
        return a;
    }
}
