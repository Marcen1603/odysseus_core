package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterEstimateUpdateAO <M extends IProbability> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private String functionID;
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Integer, Object> parameters;

	private AbstractDataUpdateFunction  dataUpdateFunction;
	
	
	public FilterEstimateUpdateAO()
	{
		super();
	}
	
	public FilterEstimateUpdateAO(FilterEstimateUpdateAO<M> copy) {
		super(copy);
		this.setOldObjListPath(new String(copy.getOldObjListPath()));
		this.setNewObjListPath(new String(copy.getNewObjListPath()));
		this.setFunctionID(new String(copy.getFunctionID()));
		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));	
		this.setDataUpdateFunction(copy.getDataUpdateFunction().clone());
	}

	
	@Override
	public AbstractLogicalOperator clone() {
		return new FilterEstimateUpdateAO<M>(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}
	
	
	// Getter & Setter

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

	public String getFunctionID() {
		return functionID;
	}
	
	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}

	public HashMap<Integer, Object> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
	}

	public void setDataUpdateFunction(AbstractDataUpdateFunction dataUpdateFunction) {
		this.dataUpdateFunction = dataUpdateFunction;
	}

	public AbstractDataUpdateFunction getDataUpdateFunction() {
		return dataUpdateFunction;
	}
}

