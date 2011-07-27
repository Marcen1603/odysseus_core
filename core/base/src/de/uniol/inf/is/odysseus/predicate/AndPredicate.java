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
package de.uniol.inf.is.odysseus.predicate;

import java.util.ArrayList;

/**
 * @author Jonas Jacobi
 */
class AndPredicate<T> extends ComplexPredicate<T> {

	private static final long serialVersionUID = -3438130138466305862L;

	protected AndPredicate() {
		super();
	}

	protected AndPredicate(IPredicate<? super T> leftPredicate,
			IPredicate<? super T> rightPredicate) {
		super(leftPredicate, rightPredicate);
	}

	protected AndPredicate(AndPredicate<T> pred) {
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		//return this.getLeft().equals(ap.getLeft()) && this.getRight().equals(ap.getRight());
		if((this.getLeft().equals(ap.getLeft()) && this.getRight().equals(ap.getRight()))
				|| (this.getLeft().equals(ap.getRight()) && this.getRight().equals(ap.getLeft()))) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isContainedIn(Object o) {
		// Falls o ein Oder-Pr�dikat, �berhaupt kein Pr�dikat oder eines der beiden Argumente des And-Pr�dikats ein Oder-Pr�dikat ist, wird false zur�ck gegeben
//		if(o instanceof OrPredicate || !(o instanceof IPredicate) || this.getLeft() instanceof OrPredicate || this.getRight() instanceof OrPredicate) {
//			return false;
//		}
		if(!(o instanceof IPredicate)) {
			return false;
		}
		
		// Z.B. ist a in b enthalten, falls a= M && N und b = M oder b=N ist (Zus�tzliche Versch�rfung bestehender Pr�dikate)
		if(!(o instanceof AndPredicate) && (this.getLeft().isContainedIn(o)
			|| this.getRight().isContainedIn(o))) {
			return true;
		}
		// Falls es sich beim anderen Pr�dikat ebenfalls um ein AndPredicate handelt, m�ssen beide Pr�dikate verglichen werden (inklusiver aller "Unterpr�dikate")
		if(o instanceof AndPredicate) {
			AndPredicate<T> ap = (AndPredicate<T>) o;


			
			ArrayList<IPredicate<?>> a = extractAllPredicates(this);
			ArrayList<IPredicate<?>> b = extractAllPredicates(ap);

			// F�r JEDES Pr�dikat aus dem anderen AndPredicate muss ein enthaltenes Pr�dikat in diesem AndPredicate gefunden werden
			// (Nur weitere Versch�rfungen sind zul�ssig, deshalb darf keine Bedingung des anderen Pr�dikats st�rker sein)
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
