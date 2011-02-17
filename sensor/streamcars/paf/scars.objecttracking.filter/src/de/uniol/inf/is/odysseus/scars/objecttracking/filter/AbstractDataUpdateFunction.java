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
/**
 * Interface for the implementation of a filter function,
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.scars.util.TypeCaster;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author dtwumasi
 * 
 */
public abstract class AbstractDataUpdateFunction<M extends IProbability & IConnectionContainer> {

	private HashMap<Enum<Parameters>, Object> parameters;

	public AbstractDataUpdateFunction() {
		parameters = new HashMap<Enum<Parameters>, Object>();
	}

	public AbstractDataUpdateFunction(HashMap<Enum<Parameters>, Object> parameters) {
		this.setParameters(parameters);
	}

	public AbstractDataUpdateFunction(AbstractDataUpdateFunction<M> copy) {
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));
	}

	@Override
	public abstract AbstractDataUpdateFunction<M> clone();

	/**
	 * this method executes the function
	 * 
	 * @return Object the result of the computation
	 */
	public abstract void compute(MVRelationalTuple<M> tuple, TupleIndexPath scannedObjectTupleIndex, TupleIndexPath predictedObjectTupleIndex, HashMap<Enum<Parameters>, Object> parameters);

	public double[] getMeasurementValues(MVRelationalTuple<M> tuple, TupleIndexPath tupleIndexPath) {

		ArrayList<Double> values = new ArrayList<Double>();

		for (TupleInfo info : new TupleIterator(tuple, tupleIndexPath)) {
			if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
				values.add(new Double(info.tupleObject.toString()));
			}
		}

		double[] result = new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			result[i] = values.get(i);
		}

		return result;
	}

	public double[] getMeasurementValues(MVRelationalTuple<M> tuple, SchemaIndexPath path) {
		ArrayList<Double> values = new ArrayList<Double>();
		for (TupleInfo info : new TupleIterator(tuple, path)) {
			if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
				values.add(new Double(info.tupleObject.toString()));
			}
		}

		double[] result = new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			result[i] = values.get(i);
		}

		return result;
	}

	public void setMeasurementValues(MVRelationalTuple<M> tuple, TupleIndexPath scannedObjectTupleIndex, double[] result) {
		int i = 0;
		for (TupleInfo info : new TupleIterator(tuple, scannedObjectTupleIndex)) {
			if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
				Object val = TypeCaster.cast(result[i], info.tupleIndexPath.getTupleObject());
				info.tupleIndexPath.setTupleObject(val);
				i += 1;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<MVRelationalTuple<M>> getObjectList(MVRelationalTuple<M> mvRelationalTuple) {
		ArrayList<MVRelationalTuple<M>> objects = new ArrayList<MVRelationalTuple<M>>();
		for (Object attribute : mvRelationalTuple.getAttributes()) {
			if (attribute instanceof MVRelationalTuple<?>) {
				objects.add((MVRelationalTuple<M>) attribute);
			}
		}
		return objects;
	}

	/**
	 * @param parameters
	 *            the parameters needed for computation
	 */
	public void setParameters(HashMap<Enum<Parameters>, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the parameters
	 */
	public HashMap<Enum<Parameters>, Object> getParameters() {

		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void addParameter(Enum<Parameters> key, Object value) {
		this.parameters.put(key, value);
	}

}
