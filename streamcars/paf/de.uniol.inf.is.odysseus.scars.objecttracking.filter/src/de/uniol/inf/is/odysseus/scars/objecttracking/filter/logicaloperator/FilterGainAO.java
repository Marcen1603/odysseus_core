package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanGainFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterGainAO <M extends IProbability & IGain> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private String functionID;
	private String newListName;
	private String oldListName;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Enum, Object> parameters;
	
	private AbstractMetaDataCreationFunction<M> metaDataCreationFunction;
	
	public FilterGainAO()
	{
		super();
		
		// Standardwerte (Timo M)
		parameters = new HashMap<Enum, Object>();
		metaDataCreationFunction = new KalmanGainFunction<M>();
		functionID = "KALMAN";
	}
	
	public FilterGainAO(FilterGainAO<M> copy) {
		super(copy);
		this.setFunctionID(new String(copy.getFunctionID()));
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
		this.setMetaDataCreationFunction(copy.getMetaDataCreationFunction().clone());
		setNewListName(copy.getNewListName());
		setOldListName(copy.getOldListName());
	}


	@Override
	public AbstractLogicalOperator clone() {
		return new FilterGainAO<M>(this);
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

	public void setMetaDataCreationFunction(AbstractMetaDataCreationFunction<M> metaDataCreationFunction) {
		this.metaDataCreationFunction = metaDataCreationFunction;
	}

	public AbstractMetaDataCreationFunction<M> getMetaDataCreationFunction() {
		return metaDataCreationFunction;
	}

	public String getNewListName() {
		return newListName;
	}

	public void setNewListName(String newListName) {
		this.newListName = newListName;
	}

	public String getOldListName() {
		return oldListName;
	}

	public void setOldListName(String oldListName) {
		this.oldListName = oldListName;
	}
	
}

