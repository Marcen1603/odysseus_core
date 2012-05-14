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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.association.merge;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.IProbabilityPredictionFunctionTimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class AssociationDataMergeFunction<M extends IProbabilityPredictionFunctionTimeInterval<Tuple<M>, M>>
		implements IDataMergeFunction<MVTuple<M>>{

	/**
	 * This is the resultschema of elements
	 * that have been merged
	 */
	SDFSchema schema;
	
	/**
	 * This is the schema of the left input stream
	 */
	SDFSchema leftSchema;
	
	/**
	 * This is the schema of the right input stream
	 */
	SDFSchema rightSchema;
	
	public AssociationDataMergeFunction(SDFSchema leftSchema, SDFSchema rightSchema, SDFSchema resultSchema){
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.schema = resultSchema;
	}
	
	private AssociationDataMergeFunction(AssociationDataMergeFunction<M> original){
		this.leftSchema = original.leftSchema.clone();
		this.rightSchema = original.rightSchema.clone();
		this.schema = original.schema.clone();
	}
	
	@Override
	public AssociationDataMergeFunction<M> clone(){
		return new AssociationDataMergeFunction<M>(this);
	}
	
	@Override
	public void init() {
		// nothing to do
		
	}

	@SuppressWarnings("deprecation")
    @Override
	/**
	 * Metadata will not be merged. Returned object contains no metadata
	 */
	public MVTuple<M> merge(MVTuple<M> left,
			MVTuple<M> right) {
		Object[] newAttributes = new Object[schema.size()];
		if (left != null && right != null) {
			Object[] leftAttributes = left.getAttributes();
			
			if(left.getMetadata().getStart().before(right.getMetadata().getStart())){
				System.arraycopy(left.getMetadata().predictData(this.leftSchema, left, right.getMetadata().getStart()).getAttributes(), 0, newAttributes, 0,
						leftAttributes.length);
				
				System.arraycopy(right.getAttributes(), 0, newAttributes, schema.size()
						- right.getAttributes().length, right.getAttributes().length);
			}
			else if(right.getMetadata().getStart().before(left.getMetadata().getStart())){
				System.arraycopy(left.getAttributes(), 0, newAttributes, 0,
						left.getAttributes().length);
				
				System.arraycopy(right.getMetadata().predictData(this.rightSchema, right, left.getMetadata().getStart()).getAttributes(), 0, newAttributes, schema.size()
						- right.getAttributes().length, right.getAttributes().length);
			}
			else{
				System.arraycopy(left.getAttributes(), 0, newAttributes, 0,
						left.getAttributes().length);
				System.arraycopy(right.getAttributes(), 0, newAttributes, schema.size()
						- right.getAttributes().length, right.getAttributes().length);
			}
		}

		MVTuple<M> r = new MVTuple<M>(newAttributes);
		r.findMeasurementValuePositions(this.schema);
		
		return r;
	}

}
