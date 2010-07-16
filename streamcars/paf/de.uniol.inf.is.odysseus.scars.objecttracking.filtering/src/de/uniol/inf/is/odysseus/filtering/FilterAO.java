package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

public class FilterAO<M extends IProbability>  extends BinaryLogicalOp{

	
	private IGainFunction<M> gainfunction;
	
	private ICorrectStateCovarianceFunction<M> correctStateCovarianceFunction;
	
	private ICorrectStateEstimateFunction<M> correctStateEstimate;
	
	
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

	
	
	/**
	 * @param gainfunction the gainfunction to set
	 */
	public void setGainfunction(IGainFunction<M> gainfunction) {
		this.gainfunction = gainfunction;
	}

	/**
	 * @return the gainfunction
	 */
	public IGainFunction<M> getGainfunction() {
		return gainfunction;
	}

	/**
	 * @param correctStateCovarianceFunction the correctStateCovarianceFunction to set
	 */
	public void setCorrectStateCovarianceFunction(
			ICorrectStateCovarianceFunction<M> correctStateCovarianceFunction) {
		this.correctStateCovarianceFunction = correctStateCovarianceFunction;
	}

	/**
	 * @return the correctStateCovarianceFunction
	 */
	public ICorrectStateCovarianceFunction<M> getCorrectStateCovarianceFunction() {
		return correctStateCovarianceFunction;
	}

	/**
	 * @param correctStateEstimate the correctStateEstimate to set
	 */
	public void setCorrectStateEstimate(ICorrectStateEstimateFunction<M> correctStateEstimate) {
		this.correctStateEstimate = correctStateEstimate;
	}

	/**
	 * @return the correctStateEstimate
	 */
	public ICorrectStateEstimateFunction<M> getCorrectStateEstimate() {
		return correctStateEstimate;
	}

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

	





}