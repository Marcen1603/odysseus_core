/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author dtwumasi
 * 
 */
public class FilterEstimateUpdatePO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {

  private AbstractDataUpdateFunction<M> dataUpdateFunction;

  private SchemaHelper schemaHelper;
  private SchemaIndexPath oldObjectListPath;
  private SchemaIndexPath newObjectListPath;
  private SchemaIndexPath newObjPath;
  private SchemaIndexPath oldObjPath;
  
  public FilterEstimateUpdatePO() {
    super();
  }

  public FilterEstimateUpdatePO(FilterEstimateUpdatePO<M> copy) {
    super(copy);
    this.dataUpdateFunction = copy.getDataUpdateFunction().clone();
   
  }

  @Override
  protected void process_open() throws OpenFailedException {
    super.process_open();
    SDFAttributeList inputSchema = this.getSubscribedToSource(0).getTarget().getOutputSchema(); 
    this.schemaHelper = new SchemaHelper(inputSchema);

    this.newObjectListPath = this.schemaHelper.getSchemaIndexPath(this.getNewObjListPath());
    this.newObjPath = this.schemaHelper.getSchemaIndexPath(this.getNewObjListPath() + SchemaHelper.ATTRIBUTE_SEPARATOR
        + this.newObjectListPath.getAttribute().getSubattribute(0).getAttributeName());

    this.oldObjectListPath = this.schemaHelper.getSchemaIndexPath(this.getOldObjListPath());
    this.oldObjPath = this.schemaHelper.getSchemaIndexPath(this.getOldObjListPath() + SchemaHelper.ATTRIBUTE_SEPARATOR
        + this.oldObjectListPath.getAttribute().getSubattribute(0).getAttributeName());
  }
  
  @Override
  public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

	// list of connections
    ArrayList<Connection> objConList = object.getMetadata().getConnectionList();

    // traverse connection list and filter
    for (Connection connected : objConList) {
       compute(connected, (MVRelationalTuple<M>)object, newObjPath, oldObjPath);
    }
    
   newObjectListPath.toTupleIndexPath(object).setTupleObject(null);
   

    return object;
  }
  
  public void compute(Connection connected, MVRelationalTuple<M> tuple, SchemaIndexPath newObjPath, SchemaIndexPath oldObjPath) {
    this.dataUpdateFunction.compute(connected, tuple, newObjPath, oldObjPath);
  }
  
  
  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new FilterEstimateUpdatePO<M>(this);
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
    this.sendPunctuation(timestamp);
  }

  
  // Getter & Setter
  
  public AbstractDataUpdateFunction<M> getDataUpdateFunction() {
    return dataUpdateFunction;
  }
  
  public void setDataUpdateFunction(AbstractDataUpdateFunction<M> dataUpdateFunction) {
    this.dataUpdateFunction = dataUpdateFunction;
  }
}
