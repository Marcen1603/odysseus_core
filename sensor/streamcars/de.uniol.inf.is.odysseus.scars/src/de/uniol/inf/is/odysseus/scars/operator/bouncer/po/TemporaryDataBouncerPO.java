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
package de.uniol.inf.is.odysseus.scars.operator.bouncer.po;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.IProbabilityTimeIntervalObjectTrackingLatency;

public class TemporaryDataBouncerPO<M extends IProbabilityTimeIntervalObjectTrackingLatency> extends AbstractPipe<MVTuple<M>, MVTuple<M>> {

	private String objListPath;
	private double threshold;

	private static final String LESS = "LESS";
	private static final String LESS_EQUAL = "LESSEQUAL";
	private static final String GREATER = "GREATER";
	private static final String GREATER_EQUAL = "GREATEREQUAL";
	private static final String EQUAL = "EQUAL";

	private String operator = LESS;

	public TemporaryDataBouncerPO() {
		super();
	}

	public TemporaryDataBouncerPO(TemporaryDataBouncerPO<M> clone) {
		super(clone);
		this.objListPath = clone.objListPath;
		this.threshold = clone.threshold;
		this.operator = clone.operator;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if( !this.operator.toUpperCase().equals(LESS))
			sendPunctuation(timestamp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVTuple<M> obj, int port) {
		obj.getMetadata().setObjectTrackingLatencyStart();
		MVTuple<M> object = obj.clone();

		SchemaHelper sh = new SchemaHelper(getSubscribedToSource(0).getSchema());
		// Get the list of cars
		List<Object> carListTuple = (List<Object>) TupleIndexPath.fromSchemaIndexPath(sh.getSchemaIndexPath(this.objListPath), object).getTupleObject();
		// Init an arraylist for the elements that should be transfered
		ArrayList<MVTuple<M>> transferCarListArrayList = new ArrayList<MVTuple<M>>();

		PointInTime timestamp = object.getMetadata().getStart();

		double val = 0;
		for(Object carObject : carListTuple) {
			MVTuple<M> car = (MVTuple<M>) carObject;
			val = 0;
			double[][] cov = car.getMetadata().getCovariance();
			for(int i = 0; i < cov.length; i++) {
				val += cov[i][i];
			}
			if (this.operator.toUpperCase().equals(LESS)) {
				//System.out.println("Bouncer Less: " + val);
				if(val < this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(LESS_EQUAL)) {
				if(val <= this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(GREATER)) {
				if(val > this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(GREATER_EQUAL)) {
				//System.out.println("Bouncer GREATER_EQUAL: " + val);
				if(val >= this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(EQUAL)) {
				if(val == this.threshold) {
					transferCarListArrayList.add(car);
				}
			}
		}

//		MVTuple<M> tuples = new MVTuple<M>(transferCarListArrayList.size());
//		int counter = 0;
//		for (MVTuple<M> mvTuple : transferCarListArrayList) {
//			tuples.setAttribute(counter++, mvTuple);
//		}

		SchemaIndexPath schemaPath = sh.getSchemaIndexPath(this.objListPath);
		TupleIndexPath tuplePath = TupleIndexPath.fromSchemaIndexPath(schemaPath, object);
		tuplePath.setTupleObject(transferCarListArrayList);

		// Falls NICHTS weitergeleitet wird -> punctuation senden
		if(transferCarListArrayList.size() == 0) {
			obj.getMetadata().setObjectTrackingLatencyEnd();
			sendPunctuation(timestamp);
		} else {
			obj.getMetadata().setObjectTrackingLatencyEnd();
			transfer(object);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public AbstractPipe<MVTuple<M>, MVTuple<M>> clone() {
		return new TemporaryDataBouncerPO<M>(this);
	}

	public String getObjListPath() {
		return objListPath;
	}

	public void setObjListPath(String objListPath) {
		this.objListPath = objListPath;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
