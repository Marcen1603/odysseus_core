/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;


import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
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

	private String functionID;
	
	private AbstractMetaDataUpdateFunction filterFunction;
	
	// path to new  objects
	private String newObjListPath;
	
	// schemas
	private SDFAttributeList leftSchema;

	
	
	/**
	 * @param po
	 */
	public InitializationAO(InitializationAO<M> copy) {
		super(copy);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public InitializationAO() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.ILogicalOperator#getOutputSchema()
	 */
	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLeftSchema(SDFAttributeList leftSchema) {
		this.leftSchema = leftSchema;
	}

	public SDFAttributeList getLeftSchema() {
		return leftSchema;
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

}
