package de.uniol.inf.is.odysseus.predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas Jacobi
 */
public class AndPredicate<T> extends ComplexPredicate<T> {

	private static final long serialVersionUID = -3438130138466305862L;

	public AndPredicate() {
		super();
	}

	public AndPredicate(IPredicate<? super T> leftPredicate,
			IPredicate<? super T> rightPredicate) {
		super(leftPredicate, rightPredicate);
	}

	public AndPredicate(AndPredicate<T> pred) {
		super(pred);
	}

	@Override
	public boolean evaluate(T input) {
		return getLeft().evaluate(input) && getRight().evaluate(input);
	}

	@Override
	public boolean evaluate(T left, T right) {
		return getLeft().evaluate(left, right)
				&& getRight().evaluate(left, right);
	}

	@Override
	public AndPredicate<T> clone() {
		return new AndPredicate<T>(this);
	}

	@Override
	public String toString() {
		return "(" + getLeft().toString() + ") AND (" + getRight().toString()
				+ ")";
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if (!(other instanceof AndPredicate)) {
			return false;
		} else {
			return this.getLeft().equals(((AndPredicate) other).getLeft())
					&& this.getRight()
							.equals(((AndPredicate) other).getRight());
		}
	}

	@Override
	public int hashCode() {
		return 17 * this.getLeft().hashCode() * 19 * this.getRight().hashCode();
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		if(!(pred instanceof AndPredicate)) {
			return false;
		}
		AndPredicate<T> ap = (AndPredicate<T>) pred;
		return this.getLeft().equals(ap.getLeft()) && this.getRight().equals(ap.getRight());
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		// Falls o ein Oder-Prädikat, überhaupt kein Prädikat oder eines der beiden Argumente des And-Prädikats ein Oder-Prädikat ist, wird false zurück gegeben
		if(o instanceof OrPredicate || !(o instanceof IPredicate) || this.getLeft() instanceof OrPredicate || this.getRight() instanceof OrPredicate) {
			return false;
		}
		
		// Z.B. ist a in b enthalten, falls a= M && N und b = M oder b=N ist (Zusätzliche Verschärfung bestehender Prädikate)
		if(!(o instanceof AndPredicate) && (this.getLeft().isContainedIn(o)
			|| this.getRight().isContainedIn(o))) {
			return true;
		}
		// Falls es sich beim anderen Prädikat ebenfalls um ein AndPredicate handelt, müssen beide Prädikate verglichen werden (inklusiver aller "Unterprädikate")
		if(o instanceof AndPredicate) {
			AndPredicate<T> ap = (AndPredicate<T>) o;


			
			ArrayList<IPredicate<?>> a = extractAllPredicates(this);
			ArrayList<IPredicate<?>> b = extractAllPredicates(ap);

			// Für JEDES Prädikat aus dem anderen AndPredicate muss ein enthaltenes Prädikat in diesem AndPredicate gefunden werden
			// (Nur weitere Verschärfungen sind zulässig, deshalb darf keine Bedingung des anderen Prädikats stärker sein)
			for(IPredicate<?> predb : b) {
//				if(predb instanceof OrPredicate) {
//					return false;
//				}
				boolean foundmatch = false;
				for(IPredicate<?> preda : a) {
//					if(preda instanceof OrPredicate) {
//						return false;
//					}
					if(preda.isContainedIn(predb)) {
						foundmatch = true;
					}
				}
				if(!foundmatch) {
					return false;
				}
			}
			return true;
		}
	
		return false;
	}
	
	private ArrayList<IPredicate<?>> extractAllPredicates(AndPredicate<?> ap) {
		ArrayList<IPredicate<?>> a = new ArrayList<IPredicate<?>>();
		if(ap.getLeft() instanceof AndPredicate) {
			a.addAll(ap.extractAllPredicates((AndPredicate<?>)ap.getLeft()));
		} else {
			a.add(ap.getLeft());
		}
		if(ap.getRight() instanceof AndPredicate) {
			a.addAll(extractAllPredicates((AndPredicate<?>)ap.getRight()));
		} else {
			a.add(ap.getRight());
		}
		return a;
	}

}
