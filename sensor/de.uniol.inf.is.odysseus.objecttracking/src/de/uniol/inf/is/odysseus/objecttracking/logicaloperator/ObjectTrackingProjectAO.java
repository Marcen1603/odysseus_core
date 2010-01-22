package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.LinearProbabilityPredictionFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Andre Bolles
 */
public class ObjectTrackingProjectAO extends ProjectAO {
	private static final long serialVersionUID = 5487345119018834806L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(projectMatrix);
		result = prime * result + Arrays.hashCode(projectVector);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectTrackingProjectAO other = (ObjectTrackingProjectAO) obj;
		if (!Arrays.equals(projectMatrix, other.projectMatrix))
			return false;
		if (!Arrays.equals(projectVector, other.projectVector))
			return false;
		return true;
	}
	
	/**
	 * only used in multivariate case
	 */
	private double[][] projectMatrix;
	
	/**
	 * only used in multivariate case
	 */
	private double[] projectVector;
	
	private SDFAttributeList outAttributes;
	
	public ObjectTrackingProjectAO(ObjectTrackingProjectAO projectMVAO) {
		super(projectMVAO);
		this.projectMatrix = projectMVAO.projectMatrix;
		this.projectVector = projectMVAO.projectVector;
	}

	public ObjectTrackingProjectAO() {
		super();
	}

	public ObjectTrackingProjectAO(SDFAttributeList queryAttributes) {
		super();
		setOutputSchema(queryAttributes);
	}

	public @Override
	ProjectAO clone() {
		return new ProjectAO(this);
	}
	
	public double[][] getProjectMatrix(){
		return this.projectMatrix;
	}
	
	public void setProjectMatrix(double[][] matrix){
		this.projectMatrix = matrix;
	}
	
	public double[] getProjectVector(){
		return this.projectVector;
	}
	
	public void setProjectVector(double[] vector){
		this.projectVector = vector;
	}
	
	/**
	 * This method sets the output attributes of this
	 * operator (the projection list). It does not
	 * concern the prediction functions. These will
	 * be set by the getOutputSchema method.
	 * 
	 * @param outAttributes the projectionList
	 */
	public void setOutAttributes(SDFAttributeList outAttributes){
		this.outAttributes = outAttributes;
	}
	
	/**
	 * This method recalcs the outputSchema according to the output attributes of this operator
	 * and the input schema of the preceding operator.
	 */
	public SDFAttributeList getOutputSchema(){
		SDFAttributeListExtended newOutputSchema = new SDFAttributeListExtended(outAttributes);
		SDFAttributeListExtended inputSchema = (SDFAttributeListExtended)this.getSubscribedToSource(0).getSchema();
		
		Map<IPredicate, IPredictionFunction> newPredFcts = new HashMap<IPredicate, IPredictionFunction>();
		Map<IPredicate, IPredictionFunction> predFcts = (Map<IPredicate, IPredictionFunction>)inputSchema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
		
		
		for(Entry<IPredicate, IPredictionFunction> entry : predFcts.entrySet()){
			/*
			 * it is not necessary to test, whether the attributes of the predicate
			 * are still available since the predicate will not be evaluated again.
			 * It will just be used as a key to know, which prediction function
			 * to use.
			 */
			IPredictionFunction newPredFct = new LinearProbabilityPredictionFunction();
			SDFExpression[] oldExprs = entry.getValue().getExpressions();
			SDFExpression[] newExprs = new SDFExpression[outAttributes.getAttributeCount()];
			
			int[] restrictList = ProjectAO.calcRestrictList(inputSchema, outAttributes);
			for(int i = 0; i<restrictList.length; i++){
				SDFExpression oldExpr = oldExprs[restrictList[i]];
				boolean attributesStillAvailable = true;
				for(SDFAttribute oldAttr : oldExpr.getAllAttributes()){
					if(outAttributes.indexOf(oldAttr) < 0){
						attributesStillAvailable = false;
						break;
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
			
			newPredFcts.put(entry.getKey(), newPredFct);
		}
		
		newOutputSchema.setMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS, newPredFcts);
		
		return newOutputSchema;
	}
}
