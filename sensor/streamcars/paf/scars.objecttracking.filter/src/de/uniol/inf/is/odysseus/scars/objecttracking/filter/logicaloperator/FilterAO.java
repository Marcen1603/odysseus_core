package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;
import java.util.Vector;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterAO<M extends IProbability> extends UnaryLogicalOp {

  private static final long serialVersionUID = 1L;
  private String functionID;

  private Vector<Object> functions;
  


// path to new and old objects
  private String oldObjListPath;
  private String newObjListPath;

  
  // Optional parameters for the Filter function. Not used right now.
  private HashMap<Enum, Object> parameters;

  
  public FilterAO() {
    super();
  }

  public FilterAO(FilterAO<M> copy) {
    super(copy);
    this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
    this.oldObjListPath = copy.getOldObjListPath();
    this.newObjListPath = copy.getNewObjListPath();
    this.functions = (Vector<Object>) copy.getFunctions().clone();
  }

  
  @Override
  public AbstractLogicalOperator clone() {
    return new FilterAO<M>(this);
  }

  @Override
  public SDFAttributeList getOutputSchema() {
    return this.getInputSchema();
  }
  
  
  // Getter & Setter
  
  public Vector<Object> getFunctions() {
		return functions;
	}

  public void setFunctions(Vector<Object> functions) {
		this.functions = functions;
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
}
