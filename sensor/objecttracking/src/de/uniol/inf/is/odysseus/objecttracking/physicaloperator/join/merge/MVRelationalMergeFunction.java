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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.relational.AbstractRelationalMergeFunction;

public class MVRelationalMergeFunction<M extends IProbability> extends AbstractRelationalMergeFunction<MVRelationalTuple<M>, M> implements IDataMergeFunction<MVRelationalTuple<M>> {

	
	public MVRelationalMergeFunction(int resultSchemaSize){
		super(resultSchemaSize);
	}
	
	public MVRelationalMergeFunction(MVRelationalMergeFunction<M> original){
		super(original.schemaSize);
	}
	
	@Override
	public MVRelationalTuple<M> merge(MVRelationalTuple<M> left, MVRelationalTuple<M> right){
		Object[] newAttributes = super.mergeAttributes(left != null ? left.getAttributes(): null, 
				right != null ? right.getAttributes() : null);
		MVRelationalTuple<M> r = new MVRelationalTuple<M>(newAttributes);
		return r;
	}
	
	@Override
	public void init(){
	}
	
	@Override
	public MVRelationalMergeFunction<M> clone(){
		return new MVRelationalMergeFunction<M>(this);
	}
}
