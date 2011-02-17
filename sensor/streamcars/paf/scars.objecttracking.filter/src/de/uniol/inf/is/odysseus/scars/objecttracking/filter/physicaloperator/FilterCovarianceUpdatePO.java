/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
public class FilterCovarianceUpdatePO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer> extends AbstractFilterPO<M> {

  private AbstractMetaDataUpdateFunction<M> metaDataUpdateFunction;

  public FilterCovarianceUpdatePO() {
    super();
  }

  public FilterCovarianceUpdatePO(FilterCovarianceUpdatePO<M> copy) {
    super(copy);
    this.setMetaDataUpdateFunction(copy.getUpdateMetaDataFunction().clone());
  }

	public void compute(IConnection connected, MVRelationalTuple<M> tuple) {
    metaDataUpdateFunction.compute(connected, (MVRelationalTuple<M>)tuple, this.getParameters());
  }


  @Override
  public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
	object.getMetadata().setObjectTrackingLatencyStart("Filter Cov Update");
    // list of connections
    ArrayList<IConnection> tmpConList = object.getMetadata().getConnectionList();

    // traverse connection list and filter
    for (IConnection connected : tmpConList) {
		connected.getLeftPath().updateValues(object);
		connected.getRightPath().updateValues(object);
		compute(connected, object);
    }

    object.getMetadata().setObjectTrackingLatencyEnd("Filter Cov Update");
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
