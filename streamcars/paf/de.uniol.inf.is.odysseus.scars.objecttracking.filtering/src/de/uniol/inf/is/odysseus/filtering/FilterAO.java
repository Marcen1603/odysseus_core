package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

public class FilterAO<M extends IProbability>  extends BinaryLogicalOp{

	private IGainFunction gainFunction;
	
	private ICorrectStateEstimateFunction correctStateEstimateFunction;
	
	private ICorrectStateCovarianceFunction correctStateCovarianceFunction;
	
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	
	//private double[][] outputModell;
	
	public FilterAO() {
	super();
	}
	
	public FilterAO(FilterAO<M> copy) {
	super(copy);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int hashCode() {
		return 0;
	
	}

	public boolean equals (Object obj) {
		return recalcOutputSchemata;}

	
	


	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.oldObjListPath = oldObjListPath;
		this.newObjListPath = newObjListPath;
	}
	
	public int[] getNewObjListPath() {
		this.leftSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[0].getSchema();
		return OrAttributeResolver.getAttributePath(leftSchema, this.newObjListPath);
	}

	public int[] getOldObjListPath() {
		this.rightSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[1].getSchema();
		return OrAttributeResolver.getAttributePath(rightSchema, this.oldObjListPath);
	}

	/**
	 * @param gainfunctionID the gainfunction
	 *
	 * */
	public void setGainfunction(int gainFunctionID, HashMap<String, Object> correctStateEstimateFunctionParameters) {
		
		
		if (gainFunctionID == 1) { 
	//	this.gainFunction = new KalmanGainFunction(correctStateEstimateFunctionParameters);
		
		}
	
	}

	/**
	 * @return the gainfunction
	 */
	public IGainFunction getGainFunction() {
		return this.gainFunction;
	}

	


	
	/**
	 * @param 
	 */
	public void setCorrectStateCovarianceFunction(int correctStateCovarianceFunctionID, HashMap<String, Object> correctStateCovarianceFunctionParameters) {
		
		if (correctStateCovarianceFunctionID == 1) { 
//		this.correctStateCovarianceFunction = new KalmanCorrectStateCovarianceFunction(correctStateCovarianceFunctionParameters);
		
		}
	
	}

	/**
	 * @return the 
	 */
	public ICorrectStateCovarianceFunction getCorrectStateCovarianceFunction() {
		return this.correctStateCovarianceFunction;
	}

	
	
	/**
	 * @param 
	 */
	public void setCorrectStateEstimateFunction(int correctStateEstimateFunctionID, HashMap<String, Object> correctStateEstimateFunctionParameters) {
		
		if (correctStateEstimateFunctionID == 1) { 
	//	this.correctStateEstimateFunction = new KalmanCorrectStateEstimateFunction(correctStateEstimateFunctionParameters);
		
		}
	
	}

	/**
	 * @return the 
	 */
	public ICorrectStateEstimateFunction getCorrectStateEstimateFunction() {
		return this.correctStateEstimateFunction;
	}






}