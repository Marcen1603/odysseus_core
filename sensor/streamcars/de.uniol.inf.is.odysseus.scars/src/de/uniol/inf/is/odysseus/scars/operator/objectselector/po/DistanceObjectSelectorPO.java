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
package de.uniol.inf.is.odysseus.scars.operator.objectselector.po;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleInfo;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIterator;

public class DistanceObjectSelectorPO<M extends IProbabilityPredictionFunctionKeyConnectionContainerTimeInterval<IPredicate<MVTuple<M>>>> extends
		AbstractPipe<MVTuple<M>, MVTuple<M>> {

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

	protected double getValueByName(MVTuple<M> tuple, TupleIndexPath tupleIndexPath, String attributName) {
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
	protected void process_next(MVTuple<M> object, int port) {
		TupleIndexPath trackedObjectListTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.trackedObjectListSchemaIndexPath, object);
		ArrayList<MVTuple<M>> newList = new ArrayList<MVTuple<M>>();

		for (TupleInfo tupleInfo : trackedObjectListTupleIndexPath) {
			double x = getValueByName(object, tupleInfo.tupleIndexPath, this.trackedObjectX);
			double y = getValueByName(object, tupleInfo.tupleIndexPath, this.trackedObjectY);

			boolean threat = x >= -this.distanceThresholdXLeft && x <= this.distanceThresholdXRight;
			threat &= y >= -this.distanceThresholdYLeft && y <= this.distanceThresholdYRight;
//			System.out.println("Threat: " + threat + " - " + tupleInfo.tupleObject.toString());
			if (threat) {
				newList.add((MVTuple<M>) tupleInfo.tupleObject);
			}
		}
		
		Object[] result = new Object[2];
		// get timestamp path from scanned data
		SchemaIndexPath path = schemaHelper.getSchemaIndexPath(schemaHelper.getStartTimestampFullAttributeName());
		result[0] = TupleIndexPath.fromSchemaIndexPath(path, object).getTupleObject();

		MVTuple<M> tuples = new MVTuple<M>(newList.size());
		int counter = 0;
		for (MVTuple<M> mvTuple : newList) {
			tuples.setAttribute(counter++, mvTuple);
		}

		// get scanned objects
		result[1] = new MVTuple<M>(tuples);

		MVTuple<M> base = new MVTuple<M>(1);
		base.setMetadata(object.getMetadata());
		base.setAttribute(0, new MVTuple<M>(result));

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
