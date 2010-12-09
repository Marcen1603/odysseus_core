package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;

/**
 * Diese Klasse sorgt dafuer, dass nur noch eindeutige Zuordnungen
 * weitergeleitet werden
 *
 * @author N da G
 *
 * @param <M>
 */
public class HypothesisSelectionPO<M extends IProbability & ITimeInterval & IConnectionContainer & IObjectTrackingLatency> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

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
	private ConnectionList matchObjects(MVRelationalTuple<M> mainTuple, ConnectionList connectionList) {

//		TupleHelper tupleHelper = new TupleHelper(mainTuple);
		Map<MVRelationalTuple<M>, List<IConnection>> connections = new HashMap<MVRelationalTuple<M>, List<IConnection>>();

		for (IConnection connection : connectionList) {
			@SuppressWarnings("unchecked")
			MVRelationalTuple<M> tuple = (MVRelationalTuple<M>) connection.getLeftPath().getTupleObject();
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

	private ConnectionList getSingleMatchingList(Map<MVRelationalTuple<M>, List<IConnection>> connections, MVRelationalTuple<M> mainTuple) {
		Map<MVRelationalTuple<M>, IConnection> singleMatchingTuples = new HashMap<MVRelationalTuple<M>, IConnection>();

		for (MVRelationalTuple<M> matchingTuple : connections.keySet()) {
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

//		TupleHelper tupleHelper = new TupleHelper(mainTuple);
		List<MVRelationalTuple<M>> removeTupleList = new ArrayList<MVRelationalTuple<M>>();
		for (MVRelationalTuple<M> tuple : singleMatchingTuples.keySet()) {
			for (MVRelationalTuple<M> tuple2 : singleMatchingTuples.keySet()) {
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

		for (MVRelationalTuple<M> mvRelationalTuple : removeTupleList) {
			singleMatchingTuples.remove(mvRelationalTuple);
		}

		ConnectionList resultConnectionList = new ConnectionList();
		for (MVRelationalTuple<M> tuple : singleMatchingTuples.keySet()) {
			resultConnectionList.add(singleMatchingTuples.get(tuple));
		}

		return resultConnectionList;
	}

	private List<Object> getDifferenceSet(MVRelationalTuple<M> mainTuple, TupleIndexPath baseObjects, ConnectionList matchedObjects) {
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
	protected void process_next(MVRelationalTuple<M> object, int port) {
		object.getMetadata().setObjectTrackingLatencyStart();
		object.getMetadata().setObjectTrackingLatencyStart("Association Selection");
		// PORT: 1, get matched objects
		ConnectionList matchedObjects = matchObjects(object, object.getMetadata().getConnectionList());
		if( matchedObjects.size() > 0 ) {
			object.getMetadata().setConnectionList(matchedObjects);
//			hasDublicates(object);
			MVRelationalTuple<M> tmpObject = object.clone();
			tmpObject.getMetadata().setObjectTrackingLatencyEnd("Association Selection");
			tmpObject.getMetadata().setObjectTrackingLatencyEnd();
			transfer(tmpObject, 1);
		} else {
			this.sendPunctuation(new PointInTime(object.getMetadata().getStart()), 1);
		}

		// PORT: 0, get new not matching objects
		List<Object> scannedNotMatchedObjects = getDifferenceSet(object, this.scannedObjectListPath.toTupleIndexPath(object), matchedObjects);
		MVRelationalTuple<M> scannedTuple = new MVRelationalTuple<M>(scannedNotMatchedObjects.size());
		for (int i = 0; i < scannedNotMatchedObjects.size(); i++) {
			scannedTuple.setAttribute(i, scannedNotMatchedObjects.get(i));
		}
		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.setMetadata((M)object.getMetadata().clone());
		Object[] objArray = new Object[2];
		objArray[0] = schemaHelper.getSchemaIndexPath(schemaHelper.getStartTimestampFullAttributeName()).toTupleIndexPath(object).getTupleObject();
		objArray[1] = scannedTuple;
		base.setAttribute(0, new MVRelationalTuple<M>(objArray));


//		hasDublicates(base);
		base.getMetadata().setObjectTrackingLatencyEnd("Association Selection");
		base.getMetadata().setObjectTrackingLatencyEnd();
		transfer(base.clone(), 0);

//		MVRelationalTuple<M> scannedNotMatchedTuple = new MVRelationalTuple<M>(object); // Timo: Wieso wird das Ursprungsobjekt geklont?
//		TupleIndexPath scannedObjectTuplePath = this.scannedObjectListPath.toTupleIndexPath(scannedNotMatchedTuple);
//		scannedObjectTuplePath.setTupleObject(scannedTuple);
//		transfer(scannedNotMatchedTuple, 0); // immer senden, auch wenn leere liste

		// PORT: 2, get predicted not matching objects
		List<Object> predictedNotMatchedObjects = getDifferenceSet(object, this.predictedObjectListPath.toTupleIndexPath(object), matchedObjects);
		if (predictedNotMatchedObjects.size() > 0) {
			MVRelationalTuple<M> predictedTuple = new MVRelationalTuple<M>(predictedNotMatchedObjects.size());
			for (int i = 0; i < predictedNotMatchedObjects.size(); i++) {
				predictedTuple.setAttribute(i, predictedNotMatchedObjects.get(i));
			}
			MVRelationalTuple<M> predictedNotMatchedTuple = object.clone();
			TupleIndexPath predictedObjectList = this.predictedObjectListPath.toTupleIndexPath(predictedNotMatchedTuple);
			predictedObjectList.setTupleObject(predictedTuple);
//			hasDublicates(predictedNotMatchedTuple);
			predictedNotMatchedTuple.getMetadata().setObjectTrackingLatencyEnd("Association Selection");
			predictedNotMatchedTuple.getMetadata().setObjectTrackingLatencyEnd();
			transfer(predictedNotMatchedTuple.clone(), 2);
		} else {
			this.sendPunctuation(new PointInTime(object.getMetadata().getStart()), 2);
		}
	}
//
//	private void hasDublicates( MVRelationalTuple<?> list ) {
//
//		MVRelationalTuple<?> cars = ((MVRelationalTuple<?>)list.getAttribute(0)).getAttribute(1);
//
//		for( int i = 0; i < cars.getAttributeCount(); i++ ) {
//			for( int o = 0; o < cars.getAttributeCount(); o++ ) {
//				if( i != o ) {
//					int id1 = (Integer)((MVRelationalTuple<?>)cars.getAttribute(i)).getAttribute(1);
//					int id2 = (Integer)((MVRelationalTuple<?>)cars.getAttribute(o)).getAttribute(1);
//					if( id1 == id2 ) {
//						System.out.println("Double!");
//					}
//				}
//			}
//		}
//
//	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisSelectionPO<M>(this);
	}
}
