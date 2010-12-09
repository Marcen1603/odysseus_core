package de.uniol.inf.is.odysseus.scars.operator.objectselector.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;

public class DistanceObjectSelectorPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer & ITimeInterval> extends
		AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private Double distanceThresholdYRight;
	private Double distanceThresholdYLeft;
	private Double distanceThresholdXRight;
	private Double distanceThresholdXLeft;
	private String trackedObjectY;
	private String trackedObjectX;
	private String trackedObjectList;
	private SchemaHelper schemaHelper;
	private SchemaIndexPath trackedObjectListSchemaIndexPath;

	public DistanceObjectSelectorPO() {
	}

	public DistanceObjectSelectorPO(DistanceObjectSelectorPO<M> distanceObjectSelectorPO) {
		super(distanceObjectSelectorPO);
		this.distanceThresholdYRight = distanceObjectSelectorPO.distanceThresholdYRight;
		this.distanceThresholdYLeft = distanceObjectSelectorPO.distanceThresholdYLeft;
		this.distanceThresholdXRight = distanceObjectSelectorPO.distanceThresholdXRight;
		this.distanceThresholdXLeft = distanceObjectSelectorPO.distanceThresholdXLeft;
		this.trackedObjectY = distanceObjectSelectorPO.trackedObjectY;
		this.trackedObjectX = distanceObjectSelectorPO.trackedObjectX;
		this.trackedObjectList = distanceObjectSelectorPO.trackedObjectList;
	}

	protected double getValueByName(MVRelationalTuple<M> tuple, TupleIndexPath tupleIndexPath, String attributName) {
		for (TupleInfo info : new TupleIterator(tuple, tupleIndexPath)) {
			if (info.attribute.getAttributeName().equals(attributName)) {
				return new Double(info.tupleObject.toString());
			}
		}

		return 0;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleIndexPath trackedObjectListTupleIndexPath = this.trackedObjectListSchemaIndexPath.toTupleIndexPath(object);
		ArrayList<MVRelationalTuple<M>> newList = new ArrayList<MVRelationalTuple<M>>();

		for (TupleInfo tupleInfo : trackedObjectListTupleIndexPath) {
			double x = getValueByName(object, tupleInfo.tupleIndexPath, this.trackedObjectX);
			double y = getValueByName(object, tupleInfo.tupleIndexPath, this.trackedObjectY);

			boolean threat = x >= -this.distanceThresholdXLeft && x <= this.distanceThresholdXRight;
			threat &= y >= -this.distanceThresholdYLeft && y <= this.distanceThresholdYRight;
//			System.out.println("Threat: " + threat + " - " + tupleInfo.tupleObject.toString());
			if (threat) {
				newList.add((MVRelationalTuple<M>) tupleInfo.tupleObject);
			}
		}
		
		Object[] result = new Object[2];
		// get timestamp path from scanned data
		SchemaIndexPath path = schemaHelper.getSchemaIndexPath(schemaHelper.getStartTimestampFullAttributeName());
		result[0] = path.toTupleIndexPath(object).getTupleObject();

		MVRelationalTuple<M> tuples = new MVRelationalTuple<M>(newList.size());
		int counter = 0;
		for (MVRelationalTuple<M> mvRelationalTuple : newList) {
			tuples.setAttribute(counter++, mvRelationalTuple);
		}

		// get scanned objects
		result[1] = new MVRelationalTuple<M>(tuples);

		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.setMetadata(object.getMetadata());
		base.setAttribute(0, new MVRelationalTuple<M>(result));

		transfer(base);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		schemaHelper = new SchemaHelper(getSubscribedToSource(0).getSchema());
		trackedObjectListSchemaIndexPath = schemaHelper.getSchemaIndexPath(this.trackedObjectList);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public DistanceObjectSelectorPO<M> clone() {
		return new DistanceObjectSelectorPO<M>(this);
	}

	public void setTrackedObjectList(String trackedObjectList) {
		this.trackedObjectList = trackedObjectList;
	}

	public void setTrackedObjectX(String trackedObjectX) {
		this.trackedObjectX = trackedObjectX;
	}

	public void setTrackedObjectY(String trackedObjectY) {
		this.trackedObjectY = trackedObjectY;
	}

	public void setDistanceThresholdXLeft(Double distanceThresholdXLeft) {
		this.distanceThresholdXLeft = distanceThresholdXLeft;
	}

	public void setDistanceThresholdXRight(Double distanceThresholdXRight) {
		this.distanceThresholdXRight = distanceThresholdXRight;
	}

	public void setDistanceThresholdYLeft(Double distanceThresholdYLeft) {
		this.distanceThresholdYLeft = distanceThresholdYLeft;
	}

	public void setDistanceThresholdYRight(Double distanceThresholdYRight) {
		this.distanceThresholdYRight = distanceThresholdYRight;
	}

	public Double getDistanceThresholdYRight() {
		return distanceThresholdYRight;
	}

	public Double getDistanceThresholdYLeft() {
		return distanceThresholdYLeft;
	}

	public Double getDistanceThresholdXRight() {
		return distanceThresholdXRight;
	}

	public Double getDistanceThresholdXLeft() {
		return distanceThresholdXLeft;
	}

	public String getTrackedObjectY() {
		return trackedObjectY;
	}

	public String getTrackedObjectX() {
		return trackedObjectX;
	}

	public String getTrackedObjectList() {
		return trackedObjectList;
	}
}
