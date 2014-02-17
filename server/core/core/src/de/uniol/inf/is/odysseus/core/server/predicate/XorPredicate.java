/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.predicate;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class XorPredicate<T> extends ComplexPredicate<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -5503207814541831948L;

    protected XorPredicate() {
        super();
    }

    protected XorPredicate(IPredicate<? super T> leftPredicate, IPredicate<? super T> rightPredicate) {
        super(leftPredicate, rightPredicate);
    }

    protected XorPredicate(XorPredicate<T> pred) {
        super(pred);
    }

    @Override
    public boolean evaluate(T input) {
        Boolean a = getLeft().evaluate(input);
        Boolean b = getRight().evaluate(input);
        return (a && !b) || (!a && b);
    }

    @Override
    public boolean evaluate(T left, T right) {
        Boolean a = getLeft().evaluate(left, right);
        Boolean b = getRight().evaluate(left, right);
        return (a && !b) || (!a && b);
    }

    @Override
    public XorPredicate<T> clone() {
        return new XorPredicate<T>(this);
    }

    @Override
    public String toString() {
        return "(" + getLeft().toString() + ") XOR (" + getRight().toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof OrPredicate)) {
            return false;
        }
        return this.getLeft().equals(((XorPredicate<?>) other).getLeft()) && this.getRight().equals(((XorPredicate<?>) other).getRight());
    }

    @Override
    public int hashCode() {
        return 19 * this.getLeft().hashCode() + 23 * this.getRight().hashCode();
    }

    @Override
    public boolean equals(IPredicate<T> pred) {
        if (!(pred instanceof XorPredicate)) {
            return false;
        }
        XorPredicate<T> op = (XorPredicate<T>) pred;
        return (this.getLeft().equals(op.getLeft()) && this.getRight().equals(op.getRight())) || (this.getLeft().equals(op.getRight()) && this.getRight().equals(op.getLeft()));
    }

    @Override
    public boolean isContainedIn(IPredicate<?> o) {
        if (!(o instanceof XorPredicate)) {
            if (this.getLeft().isContainedIn(o) && this.getRight().isContainedIn(o)) {
                return true;
            }
            return false;
        }
        XorPredicate<?> op = (XorPredicate<?>) o;

        ArrayList<IPredicate<?>> a = extractAllPredicates(this);
        ArrayList<IPredicate<?>> b = extractAllPredicates(op);

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

    private ArrayList<IPredicate<?>> extractAllPredicates(XorPredicate<?> op) {
        ArrayList<IPredicate<?>> a = new ArrayList<IPredicate<?>>();
        if (op.getLeft() instanceof XorPredicate) {
            a.addAll(op.extractAllPredicates((XorPredicate<?>) op.getLeft()));
        }
        else {
            a.add(op.getLeft());
        }
        if (op.getRight() instanceof XorPredicate) {
            a.addAll(extractAllPredicates((XorPredicate<?>) op.getRight()));
        }
        else {
            a.add(op.getRight());
        }
        return a;
    }

    public boolean contains(Object o) {
        if (!(o instanceof IPredicate)) {
            return false;
        }
        for (IPredicate<?> a : extractAllPredicates(this)) {
            if (((IPredicate<?>) o).isContainedIn(a)) {
                return true;
            }
        }
        return false;
    }
}
