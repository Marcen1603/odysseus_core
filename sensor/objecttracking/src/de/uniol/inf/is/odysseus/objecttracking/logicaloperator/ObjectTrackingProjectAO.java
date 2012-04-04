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
package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.LinearProbabilityPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaMetadataTypes;

/**
 * @author Andre Bolles
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ObjectTrackingProjectAO extends ProjectAO {
	private static final long serialVersionUID = 5487345119018834806L;

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result *= prime;
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		return true;
//	}
	
	private SDFSchema outAttributes;
	
	public ObjectTrackingProjectAO(ObjectTrackingProjectAO projectMVAO) {
		super(projectMVAO);
		this.outAttributes = projectMVAO.outAttributes;
	}

	public ObjectTrackingProjectAO() {
		super();
	}

	public ObjectTrackingProjectAO(SDFSchema queryAttributes) {
		super();
		setOutputSchema(queryAttributes);
	}

	public @Override
	ProjectAO clone() {
		return new ObjectTrackingProjectAO(this);
	}
	
	/**
	 * This method sets the output attributes of this
	 * operator (the projection list). It does not
	 * concern the prediction functions. These will
	 * be set by the getOutputSchema method.
	 * 
	 * @param outAttributes the projectionList
	 */
	public void setOutAttributes(SDFSchema outAttributes){
		this.outAttributes = outAttributes;
	}
	
	/**
	 * This method recalcs the outputSchema according to the output attributes of this operator
	 * and the input schema of the preceding operator.
	 */
	@Override
	public SDFSchema getOutputSchemaIntern(){
		SDFSchemaExtended newOutputSchema = new SDFSchemaExtended(outAttributes.getAttributes());
		SDFSchemaExtended inputSchema = (SDFSchemaExtended)this.getSubscribedToSource(0).getSchema();
		
		Map<IPredicate, IPredictionFunction> newPredFcts = new HashMap<IPredicate, IPredictionFunction>();
		
		
		Map<IPredicate, IPredictionFunction> predFcts = (Map<IPredicate, IPredictionFunction>)inputSchema.getMetadata(SDFSchemaMetadataTypes.PREDICTION_FUNCTIONS);
		
		// maybe the prediction functions have not been set
		// this can happen, if we use a schema convert operator
		// in our query plan, that changes to SDFSchemaExtended
		// for compatibility with other operators
		if(predFcts != null){
			for(Entry<IPredicate, IPredictionFunction> entry : predFcts.entrySet()){
				IPredictionFunction newPredFct = ObjectTrackingProjectAO.getNewPredictionFunction(inputSchema, outAttributes, entry.getValue().getExpressions());			
				newPredFcts.put(entry.getKey().clone(), newPredFct);
			}
		
			newOutputSchema.setMetadata(SDFSchemaMetadataTypes.PREDICTION_FUNCTIONS, newPredFcts);
		}
		
		return newOutputSchema;
	}
	
	private static IPredictionFunction getNewPredictionFunction(SDFSchema inputSchema, SDFSchema outAttributes, SDFExpression[] oldExprs){
		/*
		 * it is not necessary to test, whether the attributes of the predicate
		 * are still available since the predicate will not be evaluated again.
		 * It will just be used as a key to know, which prediction function
		 * to use.
		 */
		IPredictionFunction newPredFct = new LinearProbabilityPredictionFunction();
		SDFExpression[] newExprs = new SDFExpression[outAttributes.size()];
		
		int[] restrictList = ProjectAO.calcRestrictList(inputSchema, outAttributes);
		for(int i = 0; i<restrictList.length; i++){
			SDFExpression oldExpr = oldExprs[restrictList[i]];
			boolean attributesStillAvailable = true;
			
			// an expression can be null, if
			// a constant prediction function will be used.
			if(oldExpr != null){
				for(SDFAttribute oldAttr : oldExpr.getAllAttributes()){
					if(outAttributes.indexOf(oldAttr) < 0){
						attributesStillAvailable = false;
						break;
					}
				}
			}
			newExprs[i] = attributesStillAvailable ? oldExpr : null;
			if(newExprs[i] != null)
				newExprs[i].initAttributePositions(outAttributes);
		}
		
		int[][] vars = new int[newExprs.length][];
		for(int u = 0; u<newExprs.length; u++){
			SDFExpression expression = newExprs[u];
			if(expression != null)
				vars[u] = expression.getAttributePositions();
		}
		
		
		newPredFct.setExpressions(newExprs);
		newPredFct.setVariables(vars);
		
		return newPredFct;
	}
	
	/**
	 * The restrict list is used to identify the old positions
	 * of the attributes.
	 * @return
	 */
	@Override
	public int[] determineRestrictList(){
		return ProjectAO.calcRestrictList(this.getInputSchema(), this.getOutputSchema());
	}

	/**
	 * The project matrix is used to calculate the new covariance
	 * matrix of incoming tuples.
	 * @return
	 */
	public static RealMatrix calcProjectMatrix(int[] restrictList, SDFSchemaExtended inputSchema){
		// if there are no measurement values, no covariance matrix
		// has to be modified, and therefore no project matrix
		// has to be calculated.
		if(inputSchema.getMeasurementAttributePositions().length == 0){
			return null;
		}
		
		double[][] projectMatrix = new double[restrictList.length][inputSchema.getMeasurementAttributePositions().length];
		
		// The first row of the matrix means,
		// that this is first measurement value
		// in the output schema.
		// Second row is the same for the second
		// attribute and so on.
		// The column in the row indicates the position
		// of the measurement attribute in the input schema.
		// So the measurement attribute move from pos col to
		// pos row. So we have to set the value in 
		// matrix[row][restrictList[row]] = 1.0
		for(int i = 0; i<projectMatrix.length; i++){
			projectMatrix[i][restrictList[i]] = 1.0;
		}
		
		return new RealMatrixImpl(projectMatrix);
		
	}
	
	public RealMatrix determineProjectMatrix(int[] restrictList){
		return calcProjectMatrix(restrictList, (SDFSchemaExtended)this.getInputSchema(0));
	}

}
