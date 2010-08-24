package de.uniol.inf.is.odysseus.scars.objecttracking.initialization.logicaloperator;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * @author dtwumasi
 *
 */
public class InitializationAO<M extends IProbability> extends UnaryLogicalOp {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3037302552522493103L;

	private HashMap<Integer, Object> parameters;
	
	private String functionID;
	
	private AbstractDataUpdateFunction dataUpdateFunction;
	
	// path to new  objects
	private String newObjListPath;
	
	
	/**
	 * @param AO
	 */
	public InitializationAO(InitializationAO<M> copy) {
		super(copy);
		this.setNewObjListPath(new String(copy.getNewObjListPath()));
		this.setFunctionID(new String(copy.getFunctionID()));
		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));	
	}

	/**
	 * 
	 */
	public InitializationAO() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new InitializationAO<M>(this);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.ILogicalOperator#getOutputSchema()
	 */
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getOutputSchema();
		}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public String getNewObjListPath() {
		return newObjListPath;
	}


	/**
	 * @param functionID the functionID to set
	 */
	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}

	/**
	 * @return the functionID
	 */
	public String getFunctionID() {
		return functionID;
	}

	public void setDataUpdateFunction(AbstractDataUpdateFunction dataUpdateFunction) {
		this.dataUpdateFunction = dataUpdateFunction;
	}

	public AbstractDataUpdateFunction getDataUpdateFunction() {
		return dataUpdateFunction;
	}

	public void setParameters(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
	}

	public HashMap<Integer, Object> getParameters() {
		return parameters;
	}

}
