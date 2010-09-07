package de.uniol.inf.is.odysseus.scars.objecttracking.initialization.logicaloperator;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
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

	private HashMap<Enum, Object> parameters;
	
	// path to new  objects
	private String newObjListPath;
	// path to old  objects
	private String oldObjListPath;
	
	/**
	 * @param AO
	 */
	public InitializationAO(InitializationAO<M> copy) {
		super(copy);
		this.setNewObjListPath(new String(copy.getNewObjListPath()));
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
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

	public void setParameters(HashMap<Enum, Object> parameters) {
		this.parameters = parameters;
	}

	public HashMap<Enum, Object> getParameters() {
		return parameters;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public String getOldObjListPath() {
		return oldObjListPath;
	}

}
