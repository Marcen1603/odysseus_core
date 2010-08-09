package de.uniol.inf.is.odysseus.filtering.logicaloperator;

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
	
	// path to default tuple
	private String defaultObjListPath;
	
	// schemas
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	
	public FilterAO(String functionID) {
		super();
		this.functionID = functionID;
	}
	
	public FilterAO(FilterAO<M> copy) {
		super(copy);
		this.oldObjListPath = copy.getOldObjListPath();
		this.newObjListPath = copy.getNewObjListPath();
		this.defaultObjListPath = copy.getDefaultObjListPath();
		this.leftSchema = copy.getLeftSchema().clone();
		this.rightSchema = copy.getRightSchema().clone();
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
	
	
	public String getOldObjListPath() {
		return oldObjListPath;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}
	
	public String getNewObjListPath() {
		return newObjListPath;
	}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}
	
	public int[] getNewObjListPathInt() {
		this.leftSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[0].getSchema();
		return OrAttributeResolver.getAttributePath(leftSchema, this.newObjListPath);
	}

	public int[] getOldObjListPathInt() {
		this.rightSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[1].getSchema();
		return OrAttributeResolver.getAttributePath(rightSchema, this.oldObjListPath);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new FilterAO<M>(this);
	}
	
	public SDFAttributeList getLeftSchema() {
		return leftSchema;
	}

	public void setLeftSchema(SDFAttributeList leftSchema) {
		this.leftSchema = leftSchema;
	}

	public SDFAttributeList getRightSchema() {
		return rightSchema;
	}

	public void setRightSchema(SDFAttributeList rightSchema) {
		this.rightSchema = rightSchema;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	public void setDefaultObjListPath(String defaultObjListPath) {
		this.defaultObjListPath = defaultObjListPath;
	}

	public String getDefaultObjListPath() {
		return defaultObjListPath;
	}
}

