package de.uniol.inf.is.odysseus.filtering.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.filtering.IGainFunction;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class GainFunctionAO <M extends IProbability> extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String functionID;
	
	private IGainFunction gainFunction;
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	// schemas
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	
	public GainFunctionAO(String functionID) {
		super();
		this.functionID = functionID;
	}
	
	public GainFunctionAO(GainFunctionAO<M> copy) {
		super(copy);
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
	
	
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new GainFunctionAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}
}

