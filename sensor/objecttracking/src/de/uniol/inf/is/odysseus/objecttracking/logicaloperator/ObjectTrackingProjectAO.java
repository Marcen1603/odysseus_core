package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.LinearProbabilityPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Andre Bolles
 */
@SuppressWarnings("unchecked")
public class ObjectTrackingProjectAO extends ProjectAO {
	private static final long serialVersionUID = 5487345119018834806L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result *= prime;
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
		return true;
	}
	
	private SDFAttributeList outAttributes;
	
	public ObjectTrackingProjectAO(ObjectTrackingProjectAO projectMVAO) {
		super(projectMVAO);
		this.outAttributes = projectMVAO.outAttributes;
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
		
		// maybe the prediction functions have not been set
		// this can happen, if we use a schema convert operator
		// in our query plan, that changes to SDFAttributeListExtended
		// for compatibility with other operators
		if(predFcts != null){
			for(Entry<IPredicate, IPredictionFunction> entry : predFcts.entrySet()){
				IPredictionFunction newPredFct = this.getNewPredictionFunction(inputSchema, outAttributes, entry.getValue().getExpressions());			
				newPredFcts.put(entry.getKey().clone(), newPredFct);
			}
		
			newOutputSchema.setMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS, newPredFcts);
		}
		
		return newOutputSchema;
	}
	
	private IPredictionFunction getNewPredictionFunction(SDFAttributeList inputSchema, SDFAttributeList outAttributes, SDFExpression[] oldExprs){
		/*
		 * it is not necessary to test, whether the attributes of the predicate
		 * are still available since the predicate will not be evaluated again.
		 * It will just be used as a key to know, which prediction function
		 * to use.
		 */
		IPredictionFunction newPredFct = new LinearProbabilityPredictionFunction();
		SDFExpression[] newExprs = new SDFExpression[outAttributes.getAttributeCount()];
		
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
	public int[] determineRestrictList(){
		return ProjectAO.calcRestrictList(this.getInputSchema(), this.getOutputSchema());
	}

	/**
	 * The project matrix is used to calculate the new covariance
	 * matrix of incoming tuples.
	 * @return
	 */
	public static RealMatrix calcProjectMatrix(int[] restrictList, SDFAttributeListExtended inputSchema){
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
		return calcProjectMatrix(restrictList, (SDFAttributeListExtended)this.getInputSchema(0));
	}

}
