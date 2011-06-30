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

/**
 * @author Jonas Jacobi
 */
public class NotPredicate<T> extends AbstractPredicate<T> {
	private static final long serialVersionUID = -3214605315259491423L;
	IPredicate<? super T> predicate;

	public NotPredicate() {
	}
	
	protected NotPredicate(NotPredicate<T> predicate) {
		this.predicate = predicate.predicate;
	}

	protected NotPredicate(IPredicate<? super T> predicate) {
		this.predicate = predicate;
	}
	
	public IPredicate<? super T> getChild() {
		return this.predicate;
	}

	@Override
	public boolean evaluate(T input) {
		return !this.predicate.evaluate(input);
	}

	@Override
	public boolean evaluate(T left, T right) {
		return !this.predicate.evaluate(left, right);
	}
	
	@Override
	public NotPredicate<T> clone() {
		return new NotPredicate<T>(this);
	}
	
	@Override
	public String toString() {
		return "NOT (" + getChild() + ")";
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		if(!(pred instanceof NotPredicate)) {
			return false;
		}
		if(((NotPredicate<T>)pred).getChild().equals(this.predicate)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof NotPredicate)) {
			return false;
		} else {
			if(((NotPredicate<?>)o).getChild().isContainedIn(this.predicate)) {
				return true;
			}
		}
		return false;
	}
}
