package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;

/**
 * @author dtwumasi
 *
 */
public class FilterExpressionCovarianceUpdatePO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer> extends AbstractFilterExpressionPO<M> {


  public FilterExpressionCovarianceUpdatePO() {
    super();
  }

  public FilterExpressionCovarianceUpdatePO(FilterExpressionCovarianceUpdatePO<M> copy) {
    super(copy);
  }

	public void compute(IConnection connected, MVRelationalTuple<M> tuple) {
   
  }


  @Override
  public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
	object.getMetadata().setObjectTrackingLatencyStart("Filter Cov Update");
    // list of connections
    ArrayList<IConnection> tmpConList = object.getMetadata().getConnectionList();

    // traverse connection list and filter
    for (IConnection connected : tmpConList) {
		compute(connected, object);
    }

    object.getMetadata().setObjectTrackingLatencyEnd("Filter Cov Update");
    return object;
  }

  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new FilterExpressionCovarianceUpdatePO<M>(this);
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
	  this.sendPunctuation(timestamp);
  }

}
