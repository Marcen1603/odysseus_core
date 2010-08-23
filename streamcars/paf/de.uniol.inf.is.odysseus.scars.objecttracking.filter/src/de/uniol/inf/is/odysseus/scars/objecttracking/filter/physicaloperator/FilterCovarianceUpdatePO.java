package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

/**
 * @author dtwumasi
 * 
 */
public class FilterCovarianceUpdatePO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {

  private AbstractMetaDataUpdateFunction updateMetaDataFunction;


  public FilterCovarianceUpdatePO() {
    super();
  }

  public FilterCovarianceUpdatePO(FilterCovarianceUpdatePO<M> copy) {
    super(copy);
    this.setUpdateMetaDataFunction(copy.getUpdateMetaDataFunction().clone());
  }

  public void compute(Connection connected) {
    updateMetaDataFunction.compute(connected);
  }

  
  @Override
  public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

    // list of connections
    Connection[] objConList = new Connection[object.getMetadata().getConnectionList().toArray().length];
    ArrayList<Connection> tmpConList = object.getMetadata().getConnectionList();

    for (int i = 0; i < objConList.length; i++) {
      objConList[i] = tmpConList.get(i);
    }

    // traverse connection list and filter
    for (Connection connected : objConList) {
      compute(connected);
    }

    return object;
  }

  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new FilterCovarianceUpdatePO<M>(this);
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
    if (!havingData) {
      this.sendPunctuation(timestamp);
      havingData = false;
    }
  }
  
  
  // Getter & Setter

  public AbstractMetaDataUpdateFunction getUpdateMetaDataFunction() {
    return updateMetaDataFunction;
  }

  public void setUpdateMetaDataFunction(AbstractMetaDataUpdateFunction updateMetaDataFunction) {
    this.updateMetaDataFunction = updateMetaDataFunction;
  }
}
