package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;

public abstract class AbstractFilterPO<M extends IProbability & IConnectionContainer> extends
    AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

  // path to new and old objects
  private String oldObjListPath;
  private String newObjListPath;

  // optional parameters for the filter function. Not used right now
  private HashMap<Enum, Object> parameters;

  public AbstractFilterPO() {
	 super(); 
  }
  
  public AbstractFilterPO(AbstractFilterPO<M> copy) {
	  super(copy);
	  this.setNewObjListPath(new String(copy.getNewObjListPath()));
	  this.setOldObjListPath(new String(copy.getOldObjListPath()));
	  this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
  }
  
  
  
  protected void process_next(MVRelationalTuple<M> object, int port) {
    object = computeAll(object);
    // transfer to broker
    transfer(object);
  }
  
  public abstract MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object);
  
  
  @Override
  public abstract AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone();

  @Override
  public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
    return OutputMode.MODIFIED_INPUT;
  }
  
  // Getter & Setter

  public String getOldObjListPath() {
    return this.oldObjListPath;
  }
  
  public void setOldObjListPath(String oldObjListPath) {
    this.oldObjListPath = oldObjListPath;
  }

  public String getNewObjListPath() {
    return this.newObjListPath;
  }

  public void setNewObjListPath(String newObjListPath) {
    this.newObjListPath = newObjListPath;
  }
  
  public HashMap<Enum, Object> getParameters() {
	return parameters;
  }

  public void setParameters(HashMap<Enum, Object> parameters) {
    this.parameters = parameters;
  }
}
