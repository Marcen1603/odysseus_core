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
package de.uniol.inf.is.odysseus.scars.operator.association.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleInfo;
import de.uniol.inf.is.odysseus.scars.IProbabilityConnectionContainerTimeIntervalObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.metadata.IConnection;

/**
 * <p>
 * Physical operator for selecting <strong>unique connections</strong>. As a result there are three outputstreams:
 * <ul>
 *  <li>detected objects without assigned predicted objects on <strong>port 0</strong>,</li>
 *  <li>matched objects on <strong>port 1</strong> and</li>
 *  <li>predicted objects without assigned detected objects on <strong>port 2</strong>.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * To select those unique connections, the connection with the highest rating is selected. Therefore this operator
 * should be placed <strong>at the end</strong> of the association process.
 * </p>
 *
 * @author Nico Klein, Volker Janz
 */
public class HypothesisSelectionPO<M extends IProbabilityConnectionContainerTimeIntervalObjectTrackingLatency> extends AbstractPipe<MVTuple<M>, MVTuple<M>> {
	
	private String oldObjListPath;
	private String newObjListPath;
	private SchemaHelper schemaHelper;
	private SchemaIndexPath predictedObjectListPath;
	private SchemaIndexPath scannedObjectListPath;

	public HypothesisSelectionPO() {
		super();
	}

	public HypothesisSelectionPO(HypothesisSelectionPO<M> copy) {
		super(copy);

		this.oldObjListPath = copy.getOldObjListPath();
		this.newObjListPath = copy.getNewObjListPath();
	}

	// ----- SETTER AND GETTER -----

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public String getOldObjListPath() {
		return this.oldObjListPath;
	}

	public String getNewObjListPath() {
		return this.newObjListPath;
	}

	/**
	 * Diese Funktion sorgt dafuer, dass nur noch eindeutige Zuordnungen
	 * vorhanden sind.
	 */
	private ConnectionList matchObjects(MVTuple<M> mainTuple, ConnectionList connectionList) {

		Map<MVTuple<M>, List<IConnection>> connections = new HashMap<MVTuple<M>, List<IConnection>>();

		for (IConnection connection : connectionList) {
			@SuppressWarnings("unchecked")
			MVTuple<M> tuple = (MVTuple<M>) connection.getLeftPath().getTupleObject();
			List<IConnection> tuples = new ArrayList<IConnection>();
			if (!connections.containsKey(tuple)) {
				tuples.add(connection);
				connections.put(tuple, tuples);
			} else {
				connections.get(tuple).add(connection);
			}
		}

		return getSingleMatchingList(connections, mainTuple);
	}

	@SuppressWarnings("static-method")
    private ConnectionList getSingleMatchingList(Map<MVTuple<M>, List<IConnection>> connections, MVTuple<M> mainTuple) {
		Map<MVTuple<M>, IConnection> singleMatchingTuples = new HashMap<MVTuple<M>, IConnection>();

		for (MVTuple<M> matchingTuple : connections.keySet()) {
			IConnection connection = null;
			for (IConnection connectionComparator : connections.get(matchingTuple)) {
				if (connection == null) {
					connection = connectionComparator;
				} else if (connectionComparator.getRating() > connection.getRating()) {
					connection = connectionComparator;
				}
			}
			if (connection != null) {
				singleMatchingTuples.put(matchingTuple, connection);
			}
		}

		List<MVTuple<M>> removeTupleList = new ArrayList<MVTuple<M>>();
		for (MVTuple<M> tuple : singleMatchingTuples.keySet()) {
			for (MVTuple<M> tuple2 : singleMatchingTuples.keySet()) {
				if (tuple != tuple2) {
					if (singleMatchingTuples.get(tuple).getRightPath().getTupleObject() == singleMatchingTuples.get(tuple2).getRightPath().getTupleObject()) {
						if (singleMatchingTuples.get(tuple).getRating() > singleMatchingTuples.get(tuple2).getRating()) {
							removeTupleList.add(tuple2);
						} else {
							removeTupleList.add(tuple);
						}
					}
				}
			}
		}

		for (MVTuple<M> mvTuple : removeTupleList) {
			singleMatchingTuples.remove(mvTuple);
		}

		ConnectionList resultConnectionList = new ConnectionList();
		for (MVTuple<M> tuple : singleMatchingTuples.keySet()) {
			resultConnectionList.add(singleMatchingTuples.get(tuple));
		}

		return resultConnectionList;
	}

	@SuppressWarnings("static-method")
    private List<Object> getDifferenceSet(MVTuple<M> mainTuple, TupleIndexPath baseObjects, ConnectionList matchedObjects) {
		List<Object> tupleList = new ArrayList<Object>();
//		TupleHelper tupleHelper = new TupleHelper(mainTuple);
		for (TupleIndexPath obj : matchedObjects.getAllElements()) {
			tupleList.add(obj.getTupleObject());
		}
		List<Object> result = new ArrayList<Object>();

		for (TupleInfo tupleInfo : baseObjects) {
			if (!tupleList.contains(tupleInfo.tupleObject)) {
				result.add(tupleInfo.tupleObject);
			}
		}

		return result;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.scannedObjectListPath = this.schemaHelper.getSchemaIndexPath(this.newObjListPath);
		this.predictedObjectListPath = this.schemaHelper.getSchemaIndexPath(this.oldObjListPath);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// theoretisch kriegen wir hier keine Punctuations mehr. Die Generation sendet nur Daten!
		throw new IllegalArgumentException("HypothesisSelectionPO recieved a punctuation!");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVTuple<M> object, int port) {
		object.getMetadata().setObjectTrackingLatencyStart();
		object.getMetadata().setObjectTrackingLatencyStart("Association Selection");
		// PORT: 1, get matched objects
		ConnectionList matchedObjects = matchObjects(object, object.getMetadata().getConnectionList());
		if( matchedObjects.size() > 0 ) {
			object.getMetadata().setConnectionList(matchedObjects);
//			hasDublicates(object);
			MVTuple<M> tmpObject = object.clone();
			tmpObject.getMetadata().setObjectTrackingLatencyEnd("Association Selection");
			tmpObject.getMetadata().setObjectTrackingLatencyEnd();
			transfer(tmpObject, 1);
		} else {
			this.sendPunctuation(new PointInTime(object.getMetadata().getStart().getMainPoint()), 1);
		}

		// PORT: 0, get new not matching objects
		MVTuple<M> base = new MVTuple<M>(1);
		base.setMetadata((M)object.getMetadata().clone());
		Object[] objArray = new Object[2];
		objArray[0] = TupleIndexPath.fromSchemaIndexPath(schemaHelper.getSchemaIndexPath(schemaHelper.getStartTimestampFullAttributeName()), object).getTupleObject();
		objArray[1] = getDifferenceSet(object, TupleIndexPath.fromSchemaIndexPath(this.scannedObjectListPath, object), matchedObjects);
		base.setAttribute(0, new MVTuple<M>(objArray));

		base.getMetadata().setObjectTrackingLatencyEnd("Association Selection");
		base.getMetadata().setObjectTrackingLatencyEnd();
		transfer(base.clone(), 0);

		// PORT: 2, get predicted not matching objects
		List<Object> predictedNotMatchedObjects = getDifferenceSet(object, TupleIndexPath.fromSchemaIndexPath(this.predictedObjectListPath, object), matchedObjects);
		if (predictedNotMatchedObjects.size() > 0) {
//			MVTuple<M> predictedTuple = new MVTuple<M>(predictedNotMatchedObjects.size());
//			for (int i = 0; i < predictedNotMatchedObjects.size(); i++) {
//				predictedTuple.setAttribute(i, predictedNotMatchedObjects.get(i));
//			}
			MVTuple<M> predictedNotMatchedTuple = object.clone();
			TupleIndexPath predictedObjectList = TupleIndexPath.fromSchemaIndexPath(this.predictedObjectListPath, predictedNotMatchedTuple);
			predictedObjectList.setTupleObject(predictedNotMatchedObjects);
			predictedNotMatchedTuple.getMetadata().setObjectTrackingLatencyEnd("Association Selection");
			predictedNotMatchedTuple.getMetadata().setObjectTrackingLatencyEnd();
			transfer(predictedNotMatchedTuple.clone(), 2);
		} else {
			this.sendPunctuation(new PointInTime(object.getMetadata().getStart().getMainPoint()), 2);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public AbstractPipe<MVTuple<M>, MVTuple<M>> clone() {
		return new HypothesisSelectionPO<M>(this);
	}
}
