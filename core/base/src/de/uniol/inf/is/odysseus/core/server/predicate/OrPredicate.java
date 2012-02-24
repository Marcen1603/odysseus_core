/** Copyright [2011] [The Odysseus Team]
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

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Jonas Jacobi
 */ 

class OrPredicate<T> extends ComplexPredicate<T> {
	private static final long serialVersionUID = -5476180354530944122L;

	protected OrPredicate() {
		super();
	}

	protected OrPredicate(IPredicate<? super T> leftPredicate,
			IPredicate<? super T> rightPredicate) {
		super(leftPredicate, rightPredicate);
	}

	protected OrPredicate(OrPredicate<T> pred) {
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
	public boolean equals(Object other){
		if(!(other instanceof OrPredicate)){
			return false;
		}
		else{
			return this.getLeft().equals(((OrPredicate<?>)other).getLeft()) && this.getRight().equals(((OrPredicate<?>)other).getRight());
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
		// Falls o kein OrPredicate ist, wird false zur�ck gegeben, es sei denn, beide Pr�dikate dieses Oders sind in o enthalten
		if(!(o instanceof OrPredicate)) {
			if(this.getLeft().isContainedIn(((IPredicate<?>)o)) && this.getRight().isContainedIn(((IPredicate<?>)o))) {
				return true;
			}
			return false;
		} else {
		// Falls es sich beim anderen Pr�dikat ebenfalls um ein OrPredicate handelt, m�ssen beide Pr�dikate verglichen werden (inklusiver aller "Unterpr�dikate")

			OrPredicate<?> op = (OrPredicate<?>) o;


			
			ArrayList<IPredicate<?>> a = extractAllPredicates(this);
			ArrayList<IPredicate<?>> b = extractAllPredicates(op);

			// F�r JEDES Pr�dikat aus diesem OrPredicate muss ein enthaltenes Pr�dikat in dem anderen OrPredicate gefunden werden
			// (Das andere Oder-Pr�dikat darf noch weitere Pr�dikate haben)
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
	
	// Liefert true, falls ein Pr�dikat o in dieser Oder-Verkn�pfung enthalten ist
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
