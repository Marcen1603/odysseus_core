package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionComparator;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;

/**
 * Diese Klasse sorgt dafuer, dass nur noch eindeutige Zuordnungen weitergeleitet werden
 * 
 * @author N da G
 *
 * @param <M>
 */
public class HypothesisSelectionPO<M extends IProbability & ITimeInterval & IConnectionContainer>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String oldObjListPath;
	private String newObjListPath;
	private SchemaHelper schemaHelper;
	private SchemaIndexPath predictedObjectListPath;
	private SchemaIndexPath scannedObjectListPath;

	private boolean havingData = false;

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
	private ConnectionList matchObjects(MVRelationalTuple<M> mainTuple,
			ConnectionList connectionList) {

		TupleHelper tupleHelper = new TupleHelper(mainTuple);
		Map<MVRelationalTuple<M>, List<ConnectionComparator>> connections = new HashMap<MVRelationalTuple<M>, List<ConnectionComparator>>();

		for (ConnectionComparator connection : connectionList) {
			@SuppressWarnings("unchecked")
			MVRelationalTuple<M> tuple = (MVRelationalTuple<M>) tupleHelper
					.getObject(connection.getLeftPath());
			List<ConnectionComparator> tuples = new ArrayList<ConnectionComparator>();
			if (!connections.containsKey(tuple)) {
				tuples.add(connection);
				connections.put(tuple, tuples);
			} else {
				connections.get(tuple).add(connection);
			}
		}

		return getSingleMatchingList(connections, mainTuple);
	}

	private ConnectionList getSingleMatchingList(
			Map<MVRelationalTuple<M>, List<ConnectionComparator>> connections, MVRelationalTuple<M> mainTuple) {
		Map<MVRelationalTuple<M>, ConnectionComparator> singleMatchingTuples = new HashMap<MVRelationalTuple<M>, ConnectionComparator>();
		
		for (MVRelationalTuple<M> matchingTuple : connections.keySet()) {
			ConnectionComparator connection = null;
			for (ConnectionComparator connectionComparator : connections
					.get(matchingTuple)) {
				if (connection == null) {
					connection = connectionComparator;
				} else if (connectionComparator.getRating() > connection
						.getRating()) {
					connection = connectionComparator;
				}
			}
			if (connection != null) {
				singleMatchingTuples.put(matchingTuple, connection);
			}
		}
		
		TupleHelper tupleHelper = new TupleHelper(mainTuple);
		List<MVRelationalTuple<M>> removeTupleList = new ArrayList<MVRelationalTuple<M>>();
		for (MVRelationalTuple<M> tuple : singleMatchingTuples.keySet()) {
			for (MVRelationalTuple<M> tuple2 : singleMatchingTuples.keySet()) {
				if(tuple != tuple2) {
					if(tupleHelper.getObject(singleMatchingTuples.get(tuple).getRightPath()) == tupleHelper.getObject(singleMatchingTuples.get(tuple2).getRightPath())) {
						if(singleMatchingTuples.get(tuple).getRating() > singleMatchingTuples.get(tuple2).getRating()) {
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


	private List<Object> getDifferenceSet(TupleIndexPath baseObjects,
			List<?> matchedObjects) {
		List<Object> result = new ArrayList<Object>();

		for (TupleInfo scannedTupleInfo : baseObjects) {
			if (!matchedObjects.contains(scannedTupleInfo.tupleObject)) {
				result.add(scannedTupleInfo.tupleObject);
			}
		}

		return result;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.scannedObjectListPath = this.schemaHelper
				.getSchemaIndexPath(this.newObjListPath);
		this.predictedObjectListPath = this.schemaHelper
				.getSchemaIndexPath(this.oldObjListPath);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if (!havingData) {
			this.sendPunctuation(timestamp);
		} else {
			this.sendPunctuation(timestamp, 0);
			this.sendPunctuation(timestamp, 1);
		}
		this.havingData = false;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		this.havingData = true;

		// PORT: 1, get matched objects
		ConnectionList matchedObjects = matchObjects(object, object
				.getMetadata().getConnectionList());
		object.getMetadata().setConnectionList(matchedObjects);
		transfer(object, 1);

		// PORT: 0, get new not matching objects
		List<Object> scannedNotMatchedObjects = getDifferenceSet(
				this.scannedObjectListPath.toTupleIndexPath(object),
				matchedObjects);
		MVRelationalTuple<M> scannedNotMatchedTuple = new MVRelationalTuple<M>(
				object);
		TupleIndexPath scannedObjectList = this.scannedObjectListPath
				.toTupleIndexPath(scannedNotMatchedTuple);
		scannedObjectList.setTupleObject(scannedNotMatchedObjects);
		transfer(scannedNotMatchedTuple, 0);

		// PORT: 2, get predicted not matching objects
		List<Object> predictedNotMatchedObjects = getDifferenceSet(
				this.predictedObjectListPath.toTupleIndexPath(object),
				matchedObjects);
		if (predictedNotMatchedObjects.size() > 0) {
			MVRelationalTuple<M> predictedNotMatchedTuple = new MVRelationalTuple<M>(
					object);
			TupleIndexPath predictedObjectList = this.predictedObjectListPath
					.toTupleIndexPath(predictedNotMatchedTuple);
			predictedObjectList.setTupleObject(predictedNotMatchedObjects);
			transfer(predictedNotMatchedTuple, 2);
		} else {
			this.sendPunctuation(new PointInTime(object.getMetadata()
					.getStart()), 2);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisSelectionPO<M>(this);
	}
}
