package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;

/**
 * @author dtwumasi
 * 
 */
public class FilterCovarianceUpdatePO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {

  private AbstractMetaDataUpdateFunction updateMetaDataFunction;
  private SchemaHelper helper;


  public FilterCovarianceUpdatePO() {
    super();
  }

  public FilterCovarianceUpdatePO(FilterCovarianceUpdatePO<M> copy) {
    super(copy);
    this.setUpdateMetaDataFunction(copy.getUpdateMetaDataFunction().clone());
  }

	public void compute(Connection connected, MVRelationalTuple<M> tuple, SchemaIndexPath oldPath, SchemaIndexPath newPath) {
    updateMetaDataFunction.compute(connected, (MVRelationalTuple<StreamCarsMetaData>)tuple, oldPath, newPath);
  }

  
  @Override
  public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

    // list of connections
    ArrayList<Connection> tmpConList = object.getMetadata().getConnectionList();

    if( helper == null ) 
		helper = new SchemaHelper(getOutputSchema());
	
	// traverse connection list and filter
	SchemaIndexPath oldPath = helper.getSchemaIndexPath(getOldObjListPath());
	SchemaIndexPath newPath = helper.getSchemaIndexPath(getNewObjListPath());		

    // traverse connection list and filter
    for (Connection connected : tmpConList) {
		compute(connected, object, oldPath, newPath);
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
