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

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaMetadataTypes;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionFunctionContainer;

public class PredictionAssignAO<M extends IProbability> extends UnaryLogicalOp {
	
	private static final long serialVersionUID = 1L;
	
	private PredictionFunctionContainer<M> predictionFunctions;
	private int[] pathToList;

	
	public PredictionAssignAO() {
		super();
		predictionFunctions = new PredictionFunctionContainer<M>();
	}
	
	public PredictionAssignAO(PredictionAssignAO<M> predictionAO) {
		super(predictionAO);
		predictionFunctions = new PredictionFunctionContainer<M>(predictionAO.getPredictionFunctions());
		this.pathToList = new int[predictionAO.pathToList.length];
		System.arraycopy(predictionAO.pathToList, 0, this.pathToList, 0, predictionAO.pathToList.length);
	}
	
	public void initListPath(SDFSchema inputSchema, String absoluteListNamePath) {
		SchemaHelper helper = new SchemaHelper(inputSchema);
		pathToList = helper.getSchemaIndexPath(absoluteListNamePath).toArray(true);
	}
	
	public int[] getPathToList() {
		return pathToList;
	}
	
	public void setDefaultPredictionFunction(PredictionExpression[] defaultPredictionFunction) {
		predictionFunctions.setDefaultPredictionFunction(defaultPredictionFunction);
	}
	
	public void setPredictionFunction(PredictionExpression[] expressions, IPredicate<MVTuple<M>> predicate) {
		predictionFunctions.setPredictionFunction(expressions, predicate);
	}
	
	public PredictionFunctionContainer<M> getPredictionFunctions() {
		return predictionFunctions;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PredictionAssignAO<M>(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchemaExtended outputSchema = new SDFSchemaExtended(this.getInputSchema().getAttributes());
		outputSchema.setMetadata(SDFSchemaMetadataTypes.PREDICTION_FUNCTIONS, predictionFunctions);
		return outputSchema;
	}
}
