/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 * 
 */
public class FilterGainPO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {

  private AbstractMetaDataCreationFunction metaDataCreationFunction;

  
  public FilterGainPO() {
    super();
  }

  public FilterGainPO(FilterGainPO<M> copy) {
    this.setMetaDataCreationFunction(copy.getMetaDataCreationFunction().clone());
  }
  
  public void compute(Connection connected) {
    metaDataCreationFunction.compute(connected);
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
  public AbstractPipe clone() {
    return new FilterGainPO<M>(this);
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
    this.sendPunctuation(timestamp);
  }

  
  // Getter & Setter
  
  public AbstractMetaDataCreationFunction getMetaDataCreationFunction() {
    return metaDataCreationFunction;
  }
  
  public void setMetaDataCreationFunction(AbstractMetaDataCreationFunction metaDataCreationFunction) {
    this.metaDataCreationFunction = metaDataCreationFunction;
  }
}
