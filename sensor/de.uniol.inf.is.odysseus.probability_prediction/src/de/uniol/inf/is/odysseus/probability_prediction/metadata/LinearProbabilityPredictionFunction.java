package de.uniol.inf.is.odysseus.probability_prediction.metadata;

import java.util.List;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.base.ITimeInterval;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.prediction.metadata.AbstractPredictionFunction;
import de.uniol.inf.is.odysseus.probability.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * An implementation of interface IPredictionFunction. This
 * interface can be used for a group of linear prediction
 * functions. It also updates the covariance matrix of an
 * element.
 * 
 * @author Andre Bolles
 *
 */
public class LinearProbabilityPredictionFunction<M extends IProbability> extends AbstractPredictionFunction<RelationalTuple<M>, M>{
		
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
	final private int[][] variables;
	
	/**
	 * Matrix of expression functions. The first array
	 * of the matrix always indicates which attribute
	 * is affected by the function. That means:
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
	 * 
	 * @param expressions the expressions to be used to predict the attribute values
	 */
	public LinearProbabilityPredictionFunction(SDFExpression[] expressions, ITimeInterval interval, double[][] noiseMatrix){
		this.expressions = new SDFExpression[expressions.length];
		for(int attrIndex = 0; attrIndex < expressions.length; attrIndex++){
			this.expressions[attrIndex] = expressions[attrIndex].clone();
		}
		
		this.variables = new int[expressions.length][];
		this.interval = interval;
		this.noiseMatrix = noiseMatrix;
		
	}
	
	/**
	 * copy constructor
	 */
	public LinearProbabilityPredictionFunction(LinearProbabilityPredictionFunction original){
		// using lazy evaluation
		if(original.variables != null && original.variables.length > 0 && original.variables[0] != null){
			this.variables = original.variables.clone();
		}
		else{
			this.variables = null;
		}
		
		if(original.expressions != null){
			this.expressions = new SDFExpression[original.expressions.length];
			for(int i = 0; i<original.expressions.length; i++){
				this.expressions[i] = original.expressions[i].clone();
			}
		}
		else{
			this.expressions = null;
		}
		
		if(original.interval != null){
			this.interval = original.interval.clone();
		}
		
		// using lazy evaluation
		if(original.noiseMatrix != null && original.noiseMatrix.length > 0 && original.noiseMatrix[0] != null){
			this.noiseMatrix = original.noiseMatrix.clone();
		}
	}
	
	public LinearProbabilityPredictionFunction clone(){
		return new LinearProbabilityPredictionFunction(this);
	}
	
	/**
	 * Predicts an element to the passed point in time. It does not update any metadata. Simply
	 * the metadata of the original element will inserted to the predicted new element.
	 * 
	 * @param schema The schema of the element. To identfy the correct attribute values
	 * @param object The element to be predicted
	 * @param t The point in time to which the element is to be predicted
	 */
	public RelationalTuple<M> predictData(SDFAttributeList schema, RelationalTuple<M> object, PointInTime t){	
		// if the PointInTime t is not in the interval, during which the expressions are applicable,
		// throw an exception
		if(!this.checkInterval(t)){
			throw new IllegalArgumentException("PredictionFunction is not applicable for PointInTime " + t);
		}
		
		this.setSchema(schema);
		
		synchronized (this.expressions) {
			
			Object[] values = new Object[object.getAttributes().length];
			for(int i = 0; i<object.getAttributeCount(); i++){
				values[i] = object.getAttribute(i);
			}

			Object[] tempValues = new Object[object.getAttributes().length];
			// predict every attribute to the end of the time interval
			// or to t
			for(int u = 0; u < this.expressions.length; u++ ){
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
			
			
			
			// update the values used for the next idx
			values = tempValues;
			
			M newMetadata = (M)object.getMetadata().clone();
			
			RelationalTuple<M> outputVal = new RelationalTuple<M>(values);
			outputVal.setMetadata(newMetadata);
			
			return outputVal;
		}
	}
	
	public M predictMetadata(SDFAttributeList schema, RelationalTuple<M> object, PointInTime t){
		// if the PointInTime t is not in the interval, during which the expressions are applicable,
		// throw an exception
		if(!this.checkInterval(t)){
			throw new IllegalArgumentException("PredictionFunction is not applicable for PointInTime " + t);
		}
		
		this.setSchema(schema);
		
		M metadata = object.getMetadata();
		
		double[][] covValues = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int i = 0; i<covValues.length; i++){
			for(int u = 0; u<covValues[i].length; u++){
				covValues[i][u] = metadata.getCovariance()[i][u];
			}
		}
		
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
				Object[] varVals = new Object[this.variables[row].length];
				for(int v = 0; v<this.variables[row].length; v++){
					// If the variable index is -1,
					// then the value for variable t must be set.
					// This has to be calculated from the point in
					// time.
					if(this.variables[row][v] != -1){
						varVals[v] = covValues[this.variables[row][v]][col];
					}
					else {
						varVals[v] = t.getMainPoint() - this.interval.getStart().getMainPoint();
					}
				}
				
				this.expressions[row].bindVariables(varVals);
				tempCov[row][col] = this.expressions[row].getValue();
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
				Object[] varVals = new Object[this.variables[col].length];
				for(int v = 0; v<this.variables[col].length; v++){
					// If the variable index is -1,
					// then the value for variable t must be set.
					// This has to be calculated from the point in
					// time.
					if(this.variables[col][v] != -1){
						varVals[v] = tempCov[row][this.variables[col][v]];
					}
					else {
						varVals[v] = t.getMainPoint() - this.interval.getStart().getMainPoint();
					}
				}
				
				this.expressions[col].bindVariables(varVals);
				tempCov2[row][col] = this.expressions[col].getValue();
			}
		}
		
		// ... + Q
		if(this.noiseMatrix != null){
			RealMatrix cov = new RealMatrixImpl(tempCov2);
			RealMatrix q = new RealMatrixImpl(this.noiseMatrix);
			tempCov2 = cov.add(q).getData();
		}
		
		M newMetadata = (M)metadata.clone();
		newMetadata.setCovariance(tempCov2);

		return newMetadata;
	}
	
	/**
	 * If a new schema is set in the prediction function
	 * the variables must also be reset.
	 * 
	 * @param schema
	 */
	public void setSchema(SDFAttributeList schema){
		for(int u =0; u<expressions.length; u++){
			SDFExpression expression = expressions[u];
			List<SDFAttribute> neededAttributes = expression.getAllAttributes();
			int[] newArray = new int[neededAttributes.size()];
			this.variables[u] = newArray;
			int j = 0;
			for (SDFAttribute curAttribute : neededAttributes) {
				newArray[j++] = schema.indexOf(curAttribute);
			}
		}
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
	
//	public static void main(String[] args){
//		try{
//			SDFAttributeList schema = new SDFAttributeList();
//			
//			CQLAttribute a = new CQLAttribute(null, "a");
//			CQLAttribute b = new CQLAttribute(null, "b");
//			CQLAttribute c = new CQLAttribute(null, "c");
//			
//			schema.add(a);
//			schema.add(b);
//			schema.add(c);
//			
//			SDFSource source = new SDFSource("source", SourceType.RelationalStreaming);
//			AccessAO access = new AccessAO(source);
//			access.setOutputSchema(schema);
//			
//			AttributeResolver resolver = new AttributeResolver();
//			resolver.addSource("source", access);
////			resolver.addAttribute(a);
////			resolver.addAttribute(b);
////			resolver.addAttribute(c);
//			
//			SDFExpression expr_a = new SDFExpression(null, "2*b*t + c", resolver);
//			SDFExpression expr_b = new SDFExpression(null, "4*a*t + 2*c", resolver);
//			SDFExpression expr_c = new SDFExpression(null, "2*a*t + 2*b", resolver);
//			
//			SDFExpression[] exprList = new SDFExpression[3];
//			exprList[0] = expr_a;
//			exprList[1] = expr_b;
//			exprList[2] = expr_c;
//			
//			ITimeInterval interval = new TimeInterval(new PointInTime(0,0), new PointInTime(10, 0));//, new TimeInterval(new PointInTime(3,0), new PointInTime(10, 0))};
//			
//			double[][] cov = new double[3][3];
//			
//			cov[0][0] = 2.0;
//			cov[0][1] = 1.5;
//			cov[0][2] = 1.2;
//			
//			cov[1][0] = 1.5;
//			cov[1][1] = 2.0;
//			cov[1][2] = 1.5;
//			
//			cov[2][0] = 1.2;
//			cov[2][1] = 1.5;
//			cov[2][2] = 2;
//			
//			Object[] vals = new Object[3];
//			vals[0] = 60;
//			vals[1] = 70;
//			vals[2] = 80;
//			
//			double[][] q = new double[3][3];
//			for(int i = 0; i<q.length; i++){
//				for(int u = 0; u<q[i].length; u++){
//					q[i][u] = i * 3 + u + 1;
//				}
//			}
//			
//			long sum = 0;
//			for(int i = 0; i<1; i++){
//			
//				RelationalTuple<Probability> tuple = new RelationalTuple<Probability>(vals);
//				Probability prob = new Probability(cov);
//				tuple.setMetadata(prob);
//				
//				LinearProbabilityPredictionFunction<Probability> predFkt = new LinearProbabilityPredictionFunction<Probability>(exprList, interval, q);
//			
//				long start = System.nanoTime();
//				RelationalTuple<Probability> predTuple = predFkt.predictData(schema, tuple, new PointInTime(5,0));
//				long end = System.nanoTime();
//				
//				System.out.println(predTuple.toString());
//				System.out.println("Duration: " + (end - start));
//				sum += (end-start);
//				
//				predTuple = null;
//				predFkt = null;
//				
//				System.gc();
//			}
//			
//			System.out.println("Avg Duration: " + sum/1000);
////			
////			JEP myJep = new JEP();
////			myJep.setImplicitMul(true);
////			try {
////				myJep.addStandardConstants();
////				myJep.addStandardFunctions();
////				myJep.setAllowUndeclared(true);
////				myJep.addVariable("a", null);
////				myJep.addVariable("b", null);
////				myJep.addVariable("c", null);
////				myJep.addVariable("t", null);
////				SimpleNode n = (SimpleNode)myJep.parse("- 3 a t + (4 t b + 5 c)");
////				ParserDumpVisitor v = new ParserDumpVisitor();
////				v.visit(n, null);
////			} catch (ParseException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
}
