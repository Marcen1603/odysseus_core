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
package de.uniol.inf.is.odysseus.scars.operator.prediction.ao;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.scars.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.operator.prediction.ao.PredictionAO;
import de.uniol.inf.is.odysseus.scars.util.helper.SchemaHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PredictionAO<M extends IProbability> extends BinaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	private PredictionFunctionContainer<M> predictionFunctions;
	private int[] objListPath;
	
	public PredictionAO() {
		super();
		predictionFunctions = new PredictionFunctionContainer<M>();
	}
	
	public PredictionAO(PredictionAO<M> copy) {
		super(copy);
		predictionFunctions = new PredictionFunctionContainer<M>(copy.getPredictionFunctions());
		this.objListPath = new int[copy.objListPath.length];
		System.arraycopy(copy.objListPath, 0, this.objListPath, 0, copy.objListPath.length);
	}
	

	public void setPredictionFunctions(PredictionFunctionContainer<M> predictionFunctions) {
		this.predictionFunctions = predictionFunctions;
		
		SDFAttributeList left = getInputSchema(0); // zeit
		SDFAttributeList right = getInputSchema(1); // tupel
		
		for( IPredictionFunction<M> f : this.predictionFunctions.getMap().values() ) {
			f.init(right, left);
		}
		IPredictionFunction<M> defaultPred = predictionFunctions.getDefaultPredictionFunction();
		if(defaultPred != null) {
			defaultPred.init(right, left);
		}
	}
	
	public PredictionFunctionContainer<M> getPredictionFunctions() {
		return predictionFunctions;
	}

	
	public int[] getObjListPath() {
		return objListPath;
	}
	
	public void initNeededAttributeIndices(SDFAttributeList right,String objListPathName ) {
		SchemaHelper helper = new SchemaHelper(right);
		this.objListPath = helper.getSchemaIndexPath(objListPathName).toArray(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void subscribeToSource(ILogicalOperator source, int sinkInPort, int sourceOutPort, SDFAttributeList inputSchema) {
		super.subscribeToSource(source, sinkInPort, sourceOutPort, inputSchema);
		if(sinkInPort == RIGHT) {
			SDFAttributeListExtended schema = (SDFAttributeListExtended)getInputSchema(RIGHT);
			Object metadata = schema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
			if(metadata instanceof PredictionFunctionContainer<?>) {
				setPredictionFunctions((PredictionFunctionContainer<M>)metadata);
			}
		}
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(RIGHT);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PredictionAO<M>(this);
	}
}
