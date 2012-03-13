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

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Jonas Jacobi
 */
public abstract class ComplexPredicate<T> extends AbstractPredicate<T> {
	private static final long serialVersionUID = 5112319812675937729L;

	private IPredicate<? super T> left;

	private IPredicate<? super T> right;

	public ComplexPredicate() {
		
	}
	
	public ComplexPredicate(IPredicate<? super T> left,
			IPredicate<? super T> right) {
		this.left = left;
		this.right = right;
	}

	public ComplexPredicate(ComplexPredicate<T> pred) {
		this.left = pred.left.clone();
		this.right = pred.right.clone();
	}

	public IPredicate<? super T> getLeft() {
		return left;
	}

	protected void setLeft(IPredicate<? super T> left) {
		this.left = left;
	}

	public IPredicate<? super T> getRight() {
		return right;
	}

	protected void setRight(IPredicate<? super T> right) {
		this.right = right;
	}

//	@Override
//	public ComplexPredicate<T> clone() {
//		ComplexPredicate<T> newPred;
//		newPred = (ComplexPredicate<T>) super.clone();
//		newPred.left = this.left.clone();
//		newPred.right = this.right.clone();
//		return newPred;
//	}
	
	@Override
	public void init(){
		this.left.init();
		this.right.init();
	}
		
	@Override
	public boolean equals(Object other){
		if(!(other instanceof ComplexPredicate)){
			return false;
		}
        return this.left.equals(((ComplexPredicate<?>)other).left) &&
        	this.right.equals(((ComplexPredicate<?>)other).right);
	}
	
	@Override
	public int hashCode(){
		return 37 * this.left.hashCode() + 41 * this.right.hashCode();
	}

}
