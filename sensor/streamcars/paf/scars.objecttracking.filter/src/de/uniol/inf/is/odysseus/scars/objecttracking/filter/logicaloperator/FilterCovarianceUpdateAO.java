package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;


import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterCovarianceUpdateAO <M extends IProbability & IGain & IConnectionContainer> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private String functionID;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Enum, Object> parameters;
	
	private AbstractMetaDataUpdateFunction<M> metaDataUpdateFunction;
	
	public FilterCovarianceUpdateAO()
	{
		super();
		parameters = new HashMap<Enum, Object>();
		metaDataUpdateFunction = new KalmanCorrectStateCovarianceFunction<M>();
	}
	
	public FilterCovarianceUpdateAO(FilterCovarianceUpdateAO<M> copy) {
		super(copy);
		this.setFunctionID(new String(copy.getFunctionID()));
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
		this.setMetaDataUpdateFunction(copy.getMetaDataUpdateFunction().clone());
		
	}

	
	@Override
	public AbstractLogicalOperator clone() {
		return new FilterCovarianceUpdateAO<M>(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	
	// Getter & Setter

	public String getFunctionID() {
		return functionID;
	}
	
	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}

	public HashMap<Enum, Object> getParameters() {
		return parameters;
	}
	
	public void setParameters(HashMap<Enum, Object> parameters) {
		this.parameters = parameters;
	}

	public void setMetaDataUpdateFunction(AbstractMetaDataUpdateFunction<M> metaDataUpdateFunction) {
		this.metaDataUpdateFunction = metaDataUpdateFunction;
	}

	public AbstractMetaDataUpdateFunction<M> getMetaDataUpdateFunction() {
		return metaDataUpdateFunction;
	}
}

