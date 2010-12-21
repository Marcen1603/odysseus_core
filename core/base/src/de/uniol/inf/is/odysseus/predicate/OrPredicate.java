package de.uniol.inf.is.odysseus.predicate;

import java.util.ArrayList;

/**
 * @author Jonas Jacobi
 */
public class OrPredicate<T> extends ComplexPredicate<T> {
	private static final long serialVersionUID = -5476180354530944122L;

	public OrPredicate() {
		super();
	}

	public OrPredicate(IPredicate<? super T> leftPredicate,
			IPredicate<? super T> rightPredicate) {
		super(leftPredicate, rightPredicate);
	}

	public OrPredicate(OrPredicate<T> pred) {
		super(pred);
	}

	@Override
	public boolean evaluate(T input) {
		return getLeft().evaluate(input) || getRight().evaluate(input);
	}

	@Override
	public boolean evaluate(T left, T right) {
		return getLeft().evaluate(left, right)
				|| getRight().evaluate(left, right);
	}

	@Override
	public OrPredicate<T> clone() {
		return new OrPredicate<T>(this);
	}

	@Override
	public String toString() {
		return "(" + getLeft().toString() + ") OR (" + getRight().toString()
				+ ")";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object other){
		if(!(other instanceof OrPredicate)){
			return false;
		}
		else{
			return this.getLeft().equals(((OrPredicate)other).getLeft()) && this.getRight().equals(((OrPredicate)other).getRight());
		}
	}
	
	@Override
	public int hashCode(){
		return 19 * this.getLeft().hashCode() + 19 * this.getRight().hashCode();
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		if(!(pred instanceof OrPredicate)) {
			return false;
		}
		OrPredicate<T> op = (OrPredicate<T>) pred;
		// The Order of the Predicates shouldn't matter
		return (this.getLeft().equals(op.getLeft()) && this.getRight().equals(op.getRight()))
		|| (this.getLeft().equals(op.getRight()) && this.getRight().equals(op.getLeft()));
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof IPredicate)) {
			return false;
		}
		// Falls o kein OrPredicate ist, wird false zurück gegeben, es sei denn, beide Prädikate dieses Oders sind in o enthalten
		if(!(o instanceof OrPredicate)) {
			if(this.getLeft().isContainedIn(((IPredicate<?>)o)) && this.getRight().isContainedIn(((IPredicate<?>)o))) {
				return true;
			}
			return false;
		} else {
		// Falls es sich beim anderen Prädikat ebenfalls um ein OrPredicate handelt, müssen beide Prädikate verglichen werden (inklusiver aller "Unterprädikate")

			OrPredicate<T> op = (OrPredicate<T>) o;


			
			ArrayList<IPredicate<?>> a = extractAllPredicates(this);
			ArrayList<IPredicate<?>> b = extractAllPredicates(op);

			// Für JEDES Prädikat aus diesem OrPredicate muss ein enthaltenes Prädikat in dem anderen OrPredicate gefunden werden
			// (Das andere Oder-Prädikat darf noch weitere Prädikate haben)
			for(IPredicate<?> preda : a) {
				boolean foundmatch = false;
				for(IPredicate<?> predb : b) {
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
	}
	
	private ArrayList<IPredicate<?>> extractAllPredicates(OrPredicate<?> op) {
		ArrayList<IPredicate<?>> a = new ArrayList<IPredicate<?>>();
		if(op.getLeft() instanceof OrPredicate) {
			a.addAll(op.extractAllPredicates((OrPredicate<?>)op.getLeft()));
		} else {
			a.add(op.getLeft());
		}
		if(op.getRight() instanceof OrPredicate) {
			a.addAll(extractAllPredicates((OrPredicate<?>)op.getRight()));
		} else {
			a.add(op.getRight());
		}
		return a;
	}
	
	// Liefert true, falls ein Prädikat o in dieser Oder-Verknüpfung enthalten ist
	public boolean contains(Object o) {
		if(!(o instanceof IPredicate)) {
			return false;
		}
		for(IPredicate<?> a : extractAllPredicates(this)) {
			if(((IPredicate<?>)o).isContainedIn(a)) {
				return true;
			}
		}
		return false;
	}
}
