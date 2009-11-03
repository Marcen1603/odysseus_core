package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.List;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;


/**
 * An implementation of interface IPredictionFunction. This
 * interface can be used for a group of linear prediction
 * functions. It also updates the covariance matrix of an
 * element.
 * 
 * The functions must be linear, since the covariance
 * will be updated in a linear manner. If no covariance
 * will be used, this class can also be used with any other
 * prediction function.
 * 
 * @author Andre Bolles
 *
 */
public class LinearProbabilityPredictionFunction<M extends IProbability>
			extends AbstractPredictionFunction<MVRelationalTuple<M>, M>
			implements IProbabilityPredictionFunction<MVRelationalTuple<M>, M>{
		
	/**
	 * This matrix contains all variables, that are used for
	 * the different expressions. 
	 * 
	 * FIRST INDEX X marks the
	 * attribute for which the expression - whose variables
	 * are stored - is used.
	 * 
	 * 
	 * SECOND INDEX is the array of variables that are used in
	 * the expression expressions[X][Y]. Here the positions
	 * of the variables in the schema are stored.
	 */
	private int[][] variables;
	
	/**
	 * Array of expression functions. That means:
	 * 
	 * expressions[0] affects the first attribute.
	 * expressions[1] affects the second attribute and so on
	 * 
	 */
	SDFExpression[] expressions;
	
	/**
	 * Indicates in which time the expressions are applicable.
	 */
	ITimeInterval interval;
	
	/**
	 * This matrix represents the noise, that is introduced by
	 * the prediction function.
	 */
	double[][] noiseMatrix;
	
	/**
	 * The covariance matrix only references to measurement attributes.
	 * However, the first measurement attribute has not necessarily to be the
	 * first attribute in the schema and so on. So, we have to find the measurement
	 * attribute positions.
	 * 
	 * This array contains the positions of the measurement values.
	 * restrictList[0] contains the position of the first measurement attribute in the
	 * schema, restrictList[1] contains the position of the second measurement attribute
	 * in the schema and so on.
	 */
	int[] restrictList;
	
	public static long durationData;
	public static long durationMData;
	public static long counter;
	public static long durationExpr;
	
	public LinearProbabilityPredictionFunction(){
	}
	
	/**
	 * @deprecated Use default constructor and {@link LinearProbabilityPredictionFunction#init(SDFExpression[], ITimeInterval, double[][])} instead
	 */
	public LinearProbabilityPredictionFunction(SDFExpression[] expressions, ITimeInterval interval, double[][] noiseMatrix){
		if(expressions != null){
			this.expressions = new SDFExpression[expressions.length];
			for(int attrIndex = 0; attrIndex < expressions.length; attrIndex++){
				if(expressions[attrIndex] != null){
					this.expressions[attrIndex] = expressions[attrIndex].clone();
				}
			}
			
			this.variables = new int[expressions.length][];
		}
		this.interval = interval;
		this.noiseMatrix = noiseMatrix;
		
	}
	
	/**
	 * copy constructor
	 */
	public LinearProbabilityPredictionFunction(LinearProbabilityPredictionFunction original){
		// the private members will not be modified, so resetting the pointers should be ok
		this.variables = original.variables;
		this.expressions = original.expressions;
		this.interval = original.interval;
		this.noiseMatrix = original.noiseMatrix;
		
//		// using lazy evaluation
//		if(original.variables != null && original.variables.length > 0 && original.variables[0] != null){
//			this.variables = original.variables.clone();
//		}
//		else{
//			this.variables = null;
//		}
//		
//		if(original.expressions != null){
//			this.expressions = new SDFExpression[original.expressions.length];
//			for(int i = 0; i<original.expressions.length; i++){
//				// serveral expressions can be null, so only copy non-null
//				// expressions. Expressions that are already null
//				// will also be null in the new array (nothing has to be done then)
//				if(original.expressions[i] != null){
//					this.expressions[i] = original.expressions[i].clone();
//				}
//			}
//		}
//		else{
//			this.expressions = null;
//		}
//		
//		if(original.interval != null){
//			this.interval = original.interval.clone();
//		}
//		
//		// using lazy evaluation
//		if(original.noiseMatrix != null && original.noiseMatrix.length > 0 && original.noiseMatrix[0] != null){
//			this.noiseMatrix = original.noiseMatrix.clone();
//		}
	}
	
	public LinearProbabilityPredictionFunction clone(){
		return new LinearProbabilityPredictionFunction(this);
	}
	
	/**
	 * This method will be used instead of the constructor with the same parameters, due to 
	 * change in Odysseus. Now, metadata objects will be created centralized and than updated
	 * by a MetadataUpdater. The centralized mechanism simply uses a default constructor.
	 * 
	 * @param expressions the expressions to be used to predict the attribute values
	 * @param interval the interval, in which this prediction function is valid
	 * @param noiseMatrix the matrix used to add noise to covariance matrix of the tuple to be predicted
	 */
	public void init(SDFExpression[] expressions, int[][] variables, ITimeInterval interval, double[][] noiseMatrix){
		if(expressions != null){
			this.expressions = new SDFExpression[expressions.length];
			for(int attrIndex = 0; attrIndex < expressions.length; attrIndex++){
				if(expressions[attrIndex] != null){
					this.expressions[attrIndex] = expressions[attrIndex].clone();
				}
			}
			
			this.variables = variables;
		}
		this.interval = interval;
		this.noiseMatrix = noiseMatrix;
	}
	
	/**
	 * Predicts an element to the passed point in time. It does not update any metadata. Simply
	 * the metadata of the original element will inserted to the predicted new element.
	 * 
	 * @param schema The schema of the element. To identfy the correct attribute values
	 * @param object The element to be predicted
	 * @param t The point in time to which the element is to be predicted
	 */
	public MVRelationalTuple<M> predictData(SDFAttributeList schema, MVRelationalTuple<M> object, PointInTime t){	
		counter++;
		long start = System.currentTimeMillis();
		// if the PointInTime t is not in the interval, during which the expressions are applicable,
		// throw an exception
		if(!this.checkInterval(t)){
			throw new IllegalArgumentException("PredictionFunction is not applicable for PointInTime " + t);
		}
		
		// if the expressions are null
		// return the original object
		if(this.expressions == null){
			return object.clone();
		}
		
//		this.variables = this.getVariables(schema);
		
		// The following must not be done any more, since
		// this will be done in the method initVariables (former getVariables2)
		// called in PredictionPO operator
//		this.variables = this.getVariables2();
		
		synchronized (this.expressions) {
			
			long se = System.currentTimeMillis();
			
			Object[] values = new Object[object.getAttributes().length];
			for(int i = 0; i<object.getAttributeCount(); i++){
				values[i] = object.getAttribute(i);
			}

			
			
			Object[] tempValues = new Object[object.getAttributes().length];
			// predict every attribute to the end of the time interval
			// or to t
			
			
			for(int u = 0; u < this.expressions.length; u++ ){
				
				// if the expression is null
				// then a constant function for the attribute
				// has to be used
				// this is especially useful for
				// non-numeric values like String
				if(this.expressions[u] != null){
					
					
					Object[] varVals = new Object[this.variables[u].length];

	
					for (int j = 0; j < this.variables[u].length; ++j) {
						// insert the value of an attribute
						// Which attribute?: the attribute, that is the jth variable
						// in the idx-th expression for attribute u.
						
						// -1 indicates, that the variable is not an attribute
						// but the a variable for time ("t").
						if(this.variables[u][j] != -1){
							varVals[j] = values[this.variables[u][j]];
						}					
						// t has to be set to be delta t
						else {
							varVals[j] = t.getMainPoint() - this.interval.getStart().getMainPoint();
						}
					}
					
					
					this.expressions[u].bindVariables(varVals);
					tempValues[u] = this.expressions[u].getValue();
					
					
	//						outputVal.setAttribute(i, this.expressions[u][idx].getValue());
				}
				else{
					tempValues[u] = object.getAttribute(u);
				}
				
				
			}			
			M newMetadata = (M)object.getMetadata().clone();
			
			MVRelationalTuple<M> outputVal = new MVRelationalTuple<M>(tempValues);
			outputVal.setMetadata(newMetadata);
			
			long ee = System.currentTimeMillis();
			durationExpr += (ee - se);
			
			long end = System.currentTimeMillis();
			durationData += (end - start);
			
			return outputVal;
		}
	}
	
	public M predictMetadata(SDFAttributeList schem, MVRelationalTuple<M> object, PointInTime t){
		long start = System.currentTimeMillis();
		// if the PointInTime t is not in the interval, during which the expressions are applicable,
		// throw an exception
		if(!this.checkInterval(t)){
			throw new IllegalArgumentException("PredictionFunction is not applicable for PointInTime " + t);
		}
		
		// if the expressions are null
		// return the original metadata
		// the covariance matrix could be null
		// if there has no covariance been specified
		// in a stream
		if(this.expressions == null || object.getMetadata().getCovariance() == null){
			return (M)object.getMetadata().clone();
		}
		
//		this.variables = this.getVariables(schema);
		
		// The following must not be done any more, since
		// this will be done in the method initVariables (former getVariables2)
		// called in PredictionPO operator
//		this.variables = this.getVariables2();
		
		M metadata = object.getMetadata();
		double[][] covValues = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];

		for(int i = 0; i<covValues.length; i++){
			for(int u = 0; u<covValues[i].length; u++){
				covValues[i][u] = metadata.getCovariance()[i][u];
			}
		}
		
		// the covariance matrix references only the measurement attributes
		// so row[0] of the covariance matrix is for the first measurement
		// attribute. However, the first measurement attribute is not
		// necessarily the first attribute in the schema, so we have to
		// recalculate the indices
		
		// int the following array, the an entry restrictList[0] contains
		// the position of the first measurement attribute in the schema
		
		// Andre: I think the following should not be done here, but in
		// the constructor of the prediction function
//		int[] restrictList = new int[metadata.getCovariance().length];
//		
//		int counter = 0;
//		for(int i = 0; i<schema.getAttributeCount(); i++){
//			if(SDFDatatypes.isMeasurementValue(schema.getAttribute(i).getDatatype())){
//				restrictList[counter++] = i;
//			}
//		}
		
		// update the covariance matrix
		// H * \Sigma * H^T + Q 
		double[][] tempCov = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		
		// H * \Sigma ...
		for(int row = 0; row < tempCov.length; row++){
			for(int col = 0; col < tempCov[0].length; col++){
				// We now want to set the value in tempCov[row][col]
				// Row indicates, which expression has to be used. (expr for which attribute)
				// Col indicates, with which values the variables in the expression have to be filled
				// values from covariance matrix column col.
				
				// However, if there is not expression for measurement attribute no. restrictList[row],
				// the corresponding expression will also be null. A constant function will be used then.
				
				if(this.variables[restrictList[row]] != null){
					Object[] varVals = new Object[this.variables[restrictList[row]].length];
	
					for(int v = 0; v<this.variables[restrictList[row]].length; v++){
						// If the variable index is -1,
						// then the value for variable t must be set.
						// This has to be calculated from the point in
						// time.
						if(this.variables[restrictList[row]][v] != -1){
							varVals[v] = covValues[this.variables[restrictList[row]][v]][col];
						}
						else {
							varVals[v] = t.getMainPoint() - this.interval.getStart().getMainPoint();
						}
					}


				
					this.expressions[restrictList[row]].bindVariables(varVals);
					tempCov[row][col] = this.expressions[restrictList[row]].getValue();
				}
				else{
					tempCov[row][col] = covValues[row][col];
				}
			}
		}
		/*
		16 17 19    0 4t 2t
		20 21 22    2t 0 2
		23 34 12    1  2 0
		*/
		
		// ... * H^T
		double[][] tempCov2 = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int row = 0; row < tempCov.length; row++){
			for(int col = 0; col < tempCov[0].length; col++){
				// We now want to set the value in tempCov[row][col]
				// Row indicates, which expression has to be used. (expr for which attribute)
				// Col indicates, with which values the variables in the expression have to be filled
				// values from covariance matrix column col.
				
				// if variables[restrictList[col]] == null, then the corresponding
				// expression will also be null, so a constant prediction function will
				// be used.
				
				if(this.variables[restrictList[col]] != null){
					Object[] varVals = new Object[this.variables[restrictList[col]].length];
					for(int v = 0; v<this.variables[restrictList[col]].length; v++){
						// If the variable index is -1,
						// then the value for variable t must be set.
						// This has to be calculated from the point in
						// time.
						if(this.variables[restrictList[col]][v] != -1){
							varVals[v] = tempCov[row][this.variables[restrictList[col]][v]];
						}
						else {
							varVals[v] = t.getMainPoint() - this.interval.getStart().getMainPoint();
						}
					}
					
					this.expressions[restrictList[col]].bindVariables(varVals);
					tempCov2[row][col] = this.expressions[restrictList[col]].getValue();
				}
				else{
					tempCov2[row][col] = tempCov[row][col];
				}
			}
		}
		
		// ... + Q
		if(this.noiseMatrix != null){
			RealMatrix cov = new RealMatrixImpl(tempCov2);
			RealMatrix q = new RealMatrixImpl(this.noiseMatrix);
			tempCov2 = cov.add(q).getData();
		}
		
		long end = System.currentTimeMillis();
		durationMData += (end - start);
		
		M newMetadata = (M)metadata.clone();
		newMetadata.setCovariance(tempCov2);
		
		return newMetadata;
	}
	
	/**
	 * If a new schema is set in the prediction function
	 * the variables must also be reset.
	 * 
	 * This method always determines the variable positions
	 * at query runtime.
	 * 
	 * @param schema
	 */
	@Deprecated
	public int[][] getVariables(SDFAttributeList schema){
		int[][] tempVars = new int[this.variables.length][]; // this.variables.length = this.expressions.length
		for(int u =0; u<expressions.length; u++){
			
			SDFExpression expression = expressions[u];
			
			// the expression can be null
			// In this case a constant
			// prediction function will be used
			// This is especially useful for
			// non-numeric attributes like String
			if(expression != null){
				List<SDFAttribute> neededAttributes = expression.getAllAttributes();
				int[] newArray = new int[neededAttributes.size()];
				tempVars[u] = newArray;
				int j = 0;
				for (SDFAttribute curAttribute : neededAttributes) {
					newArray[j++] = schema.indexOf(curAttribute);
				}
			}
			else{
				tempVars[u] = null;
			}
		}
		
		return tempVars;
	}
	
	/**
	 * This method uses the fact, that the variable positions
	 * can be determined in advance of query processing.
	 * 
	 * TODO Andre: Diese Methode wird noch im RelationalProjectPredictionMVPO benutzt.
	 * Sollte aber eigentlich nicht mehr benutzt werden, da sich auch die Prädiktions-
	 * funktion, die nach einer Projektion noch verwendet werden kann, auf der logischen
	 * Ebene vorberechnet werden kann. (Ist aber jetzt zuviel Aufwand, um das einzubauen).
	 * 
	 * @return
	 */
	public void initVariables(){
		this.variables = new int[this.expressions.length][];
		for(int u = 0; u<expressions.length; u++){
			SDFExpression expression = expressions[u];
			if(expression != null)
				this.variables[u] = expression.getAttributePositions();
		}
	}
	
	/**
	 * @return the initialized variable positions
	 */
	public int[][] getVariables(){
		return this.variables;
	}
	
	public void setVariables(int[][] vars){
		this.variables = vars;
	}
	
	public void setSchema(){

	}
	
	/**
	 * Checks whether a point in time is in the interval in which
	 * this prediction function is applicable
	 * @param t The point in time to be checked
	 * @return true, if t is in the interval of applicability
	 */
	private boolean checkInterval(PointInTime t){
		if(!(this.interval.getStart().beforeOrEquals(t) && this.interval.getEnd().after(t))){
			return false;
		}
		return true;
	}
	
	@Override
	public void setNoiseMatrix(double[][] noiseMatrix) {
		this.noiseMatrix = noiseMatrix;
		
	}

	@Override
	public void setPredictionFunction(SDFExpression[] expressions) {
		this.expressions = expressions;
		
		// expressions can be null, what has
		// the meaning, that a constant prediction function will
		// be used
		if(expressions != null){
			this.variables = new int[expressions.length][];
		}
	}
	
	@Override
	public void setTimeInterval(ITimeInterval interval) {
		this.interval = interval;
	}

	@Override
	public SDFExpression[] getPredictionFunction() {
		// TODO Auto-generated method stub
		return this.expressions;
	}
	
	public void setRestrictList(int[] restrictList){
		this.restrictList = restrictList;
	}
}
