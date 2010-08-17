package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public abstract class AbstractFilterPO<M extends IProbability & IConnectionContainer> extends
    AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

  protected boolean havingData = false;

  // path to new and old objects
  private String oldObjListPath;
  private String newObjListPath;

  // optional parameters for the filter function. Not used right now
  private HashMap<Integer, Object> parameters;

  public AbstractFilterPO() {

  }

  @Override
  public abstract AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone();

  @Override
  public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
    return OutputMode.MODIFIED_INPUT;
  }

  protected void process_next(MVRelationalTuple<M> object, int port) {
    havingData = true;
    object = computeAll(object);
    // transfer to broker
    transfer(object);
  }

  /**
   * @param oldObjListPath
   *          the oldObjListPath to set
   */
  public void setOldObjListPath(String oldObjListPath) {
    this.oldObjListPath = oldObjListPath;
  }

  /**
   * @param newObjListPath
   *          the newObjListPath to set
   */
  public String getNewObjListPath() {
    return this.newObjListPath;
  }

  /**
   * @param newObjListPath
   *          the newObjListPath to set
   */
  public void setNewObjListPath(String newObjListPath) {
    this.newObjListPath = newObjListPath;
  }

  /**
   * @param oldObjListPath
   *          the oldObjListPath to set
   */
  public String getOldObjListPath() {
    return this.oldObjListPath;
  }

  public abstract MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object);

  /**
   * @param parameters
   *          the parameters to set
   */
  public void setParameters(HashMap<Integer, Object> parameters) {
    this.parameters = parameters;
  }

  /**
   * @return the parameters
   */
  public HashMap<Integer, Object> getParameters() {
    return parameters;
  }
}
