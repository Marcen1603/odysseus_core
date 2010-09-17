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

  private AbstractMetaDataUpdateFunction<M> metaDataUpdateFunction;

  public FilterCovarianceUpdatePO() {
    super();
  }

  public FilterCovarianceUpdatePO(FilterCovarianceUpdatePO<M> copy) {
    super(copy);
    this.setMetaDataUpdateFunction(copy.getUpdateMetaDataFunction().clone());
  }

	public void compute(Connection connected, MVRelationalTuple<M> tuple) {
    metaDataUpdateFunction.compute(connected, (MVRelationalTuple<M>)tuple, this.getParameters());
  }

  
  @Override
  public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

    // list of connections
    ArrayList<Connection> tmpConList = object.getMetadata().getConnectionList();

    // traverse connection list and filter
    for (Connection connected : tmpConList) {
		compute(connected, object);
    }

    return object;
  }

  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new FilterCovarianceUpdatePO<M>(this);
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
	  this.sendPunctuation(timestamp);
  }
  
  
  // Getter & Setter

  public AbstractMetaDataUpdateFunction<M> getUpdateMetaDataFunction() {
    return metaDataUpdateFunction;
  }

  public void setMetaDataUpdateFunction(AbstractMetaDataUpdateFunction<M> updateMetaDataFunction) {
    this.metaDataUpdateFunction = updateMetaDataFunction;
  }
}
