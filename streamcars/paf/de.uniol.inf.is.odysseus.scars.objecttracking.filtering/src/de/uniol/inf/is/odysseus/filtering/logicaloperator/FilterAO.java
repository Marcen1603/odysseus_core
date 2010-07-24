package de.uniol.inf.is.odysseus.filtering.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.filtering.IFilterFunction;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterAO <M extends IProbability> extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String functionID;
	
	private IFilterFunction filterFunction;
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	// schemas
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	
	public FilterAO(String functionID) {
		super();
		this.functionID = functionID;
	}
	
	public FilterAO(FilterAO<M> copy) {
		super(copy);
	}
	
	
	/**
	 * @param filterFunctionID the filterfunction
	 *
	 * */
	public void setFilterfunction(IFilterFunction filterfunction) {
		this.filterFunction = filterfunction;
	
	}

	/**
	 * @return the filterfunction
	 */
	public IFilterFunction getFilterFunction() {
		return this.filterFunction;
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
		return new FilterAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}
}

