package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterEstimateUpdateAO <M extends IProbability & IConnectionContainer & IGain> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private String functionID;
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	private String expressionString;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Enum<Parameters>, Object> parameters;

	private AbstractDataUpdateFunction<M>  dataUpdateFunction;
	
	
	public FilterEstimateUpdateAO()
	{
		super();
		functionID = "KALMAN";
		parameters = new HashMap<Enum<Parameters>, Object>();
		dataUpdateFunction = new KalmanCorrectStateEstimateFunction<M>();
	}
	
	public FilterEstimateUpdateAO(FilterEstimateUpdateAO<M> copy) {
		super(copy);
		this.setOldObjListPath(new String(copy.getOldObjListPath()));
		this.setNewObjListPath(new String(copy.getNewObjListPath()));
		this.setFunctionID(new String(copy.getFunctionID()));
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));	
		this.setDataUpdateFunction(copy.getDataUpdateFunction().clone());
		this.setExpressionString(new String(copy.getExpressionString()));
	}

	
	@Override
	public AbstractLogicalOperator clone() {
		return new FilterEstimateUpdateAO<M>(this);
	}
	
	  @Override
	  public SDFAttributeList getOutputSchema() {
		 /* SchemaHelper helper = new SchemaHelper(this.getInputSchema().clone());
		  SDFAttributeListExtended newSchema = new SDFAttributeListExtended((SDFAttributeListExtended)this.getInputSchema());
		  helper = new SchemaHelper(newSchema);
		  SchemaIndexPath path = helper.getSchemaIndexPath(newObjListPath);
		  SDFAttribute oldListAttr = helper.getAttribute(newObjListPath);
		  SDFAttribute oldListAttrParent = path.getSchemaIndex(path.getLength()-2).getAttribute();
		  oldListAttrParent.removeSubattribute(oldListAttr);
		  
		  return newSchema; */
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

	public HashMap<Enum<Parameters>, Object> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<Enum<Parameters>, Object> parameters) {
		this.parameters = parameters;
	}

	public void setDataUpdateFunction(AbstractDataUpdateFunction<M> dataUpdateFunction) {
		this.dataUpdateFunction = dataUpdateFunction;
	}

	public AbstractDataUpdateFunction<M> getDataUpdateFunction() {
		return dataUpdateFunction;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	public String getExpressionString() {
		return expressionString;
	}
}

