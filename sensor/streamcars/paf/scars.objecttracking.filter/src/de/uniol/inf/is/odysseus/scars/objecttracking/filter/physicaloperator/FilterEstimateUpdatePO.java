/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author dtwumasi
 * 
 */
public class FilterEstimateUpdatePO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer> extends AbstractFilterPO<M> {

	private AbstractDataUpdateFunction<M> dataUpdateFunction;

//	private SchemaHelper schemaHelper;
//	private SchemaIndexPath oldObjectListPath;
//	private SchemaIndexPath newObjectListPath;
//	private SchemaIndexPath newObjPath;
//	private SchemaIndexPath oldObjPath;
//	private TupleHelper tupleHelper;
	SDFAttributeList inputSchema;

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
		inputSchema = this.getSubscribedToSource(0).getTarget().getOutputSchema();
//		this.schemaHelper = new SchemaHelper(inputSchema);

//		this.newObjectListPath = this.schemaHelper.getSchemaIndexPath(this.getNewObjListPath());
//		this.newObjPath = this.schemaHelper.getSchemaIndexPath(this.getNewObjListPath() + SchemaHelper.ATTRIBUTE_SEPARATOR
//				+ this.newObjectListPath.getAttribute().getSubattribute(0).getAttributeName());

//		this.oldObjectListPath = this.schemaHelper.getSchemaIndexPath(this.getOldObjListPath());
//		this.oldObjPath = this.schemaHelper.getSchemaIndexPath(this.getOldObjListPath() + SchemaHelper.ATTRIBUTE_SEPARATOR
//				+ this.oldObjectListPath.getAttribute().getSubattribute(0).getAttributeName());
	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

		// list of connections
		ArrayList<IConnection> objConList = object.getMetadata().getConnectionList();

		// traverse connection list and filter
		for (IConnection connected : objConList) {
			compute(connected.getLeftPath(), connected.getRightPath());
		}

		/*
		 * MVRelationalTuple<M> oldList = (MVRelationalTuple<M>)
		 * oldObjectListPath .toTupleIndexPath(object).getTupleObject();
		 * 
		 * String timeStampName = schemaHelper
		 * .getStartTimestampFullAttributeName(); // SDFAttribute timestamp =
		 * schemaHelper.getAttribute(timeStampName); Object timeStamp =
		 * schemaHelper.getSchemaIndexPath(timeStampName)
		 * .toTupleIndexPath(object).getTupleObject();
		 * 
		 * MVRelationalTuple<M> newObject = new MVRelationalTuple<M>(1);
		 * 
		 * newObject.setMetadata(object.getMetadata()); MVRelationalTuple<M>
		 * scan = new MVRelationalTuple<M>(2);
		 * 
		 * scan.setAttribute(0, timeStamp); scan.setAttribute(1, oldList);
		 * 
		 * newObject.setAttribute(0, scan);
		 * 
		 * tupleHelper = new TupleHelper(object);
		 * 
		 * return newObject;
		 */
		return object;
	}

	private void compute(TupleIndexPath scannedObjectTupleIndex, TupleIndexPath predictedObjectTupleIndex) {
		this.dataUpdateFunction.compute(scannedObjectTupleIndex, predictedObjectTupleIndex, this.getParameters());
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
