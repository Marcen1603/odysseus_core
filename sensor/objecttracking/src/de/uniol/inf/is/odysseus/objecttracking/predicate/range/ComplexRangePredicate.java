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
package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class ComplexRangePredicate<T> extends
		AbstractRangePredicate<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6498295738653383555L;
	protected IRangePredicate<T> left;
	public IRangePredicate<T> getLeft() {
		return left;
	}

	public void setLeft(IRangePredicate<T> left) {
		this.left = left;
	}

	public IRangePredicate<T> getRight() {
		return right;
	}

	public void setRight(IRangePredicate<T> right) {
		this.right = right;
	}

	protected IRangePredicate<T> right;
	
	public ComplexRangePredicate(IRangePredicate<T> left, IRangePredicate<T> right){
		super();
		this.left = left;
		this.right = right;
	}
	
	@Override
	public ComplexRangePredicate<T> clone(){
		ComplexRangePredicate<T> newPred = (ComplexRangePredicate<T>)super.clone();
		newPred.left = this.left.clone();
		newPred.right = this.right.clone();
		return newPred;
	}
	
	@Override
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		this.left.init(leftSchema, rightSchema);
		this.right.init(leftSchema, rightSchema);
	}
	
	@Override
	public long getAdditionalEvaluationDuration() {
		// TODO Auto-generated method stub
		return this.left.getAdditionalEvaluationDuration() + this.right.getAdditionalEvaluationDuration();
	}
}
