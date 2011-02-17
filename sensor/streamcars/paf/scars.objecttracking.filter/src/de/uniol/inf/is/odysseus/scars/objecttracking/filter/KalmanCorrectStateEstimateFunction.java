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
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;

public class KalmanCorrectStateEstimateFunction<M extends IProbability & IConnectionContainer & IGain>
		extends AbstractDataUpdateFunction<M> {

	public KalmanCorrectStateEstimateFunction() {

	}

	public KalmanCorrectStateEstimateFunction(
			KalmanCorrectStateEstimateFunction<M> copy) {
		super(copy);
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));

	}

	public KalmanCorrectStateEstimateFunction(HashMap<Enum<Parameters>, Object> parameters) {

		this.setParameters(parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(MVRelationalTuple<M> tuple, TupleIndexPath scannedObjectTupleIndex,
			TupleIndexPath predictedObjectTupleIndex,
			HashMap<Enum<Parameters>, Object> parameters) {

		MVRelationalTuple<M> oldTuple = (MVRelationalTuple<M>) predictedObjectTupleIndex
				.getTupleObject();

		double[][] gain = null;
		
//		System.out.println(this.toString() + " Gain: " + oldTuple.getMetadata().getGain());

		// check if there is a gain in the parameters
		if (parameters != null && parameters.containsKey(Parameters.Gain)) {
			gain = (double[][]) parameters.get(Parameters.Gain);
		} else {
			gain = oldTuple.getMetadata().getGain();
		}
		
		double[] measurementOld = getMeasurementValues(tuple,
				predictedObjectTupleIndex);
		double[] measurementNew = getMeasurementValues(tuple,
				scannedObjectTupleIndex);

		double[] result;
		if (gain == null) {
			System.err.println("Gain is null!");
			return;
		}
		RealMatrix measurementOldMatrix = new RealMatrixImpl(measurementOld);
		RealMatrix measurementNewMatrix = new RealMatrixImpl(measurementNew);
		RealMatrix gainMatrix = new RealMatrixImpl(gain);

		RealMatrix temp = new RealMatrixImpl();

		temp = measurementNewMatrix.subtract(measurementOldMatrix);
		temp = gainMatrix.multiply(temp);
		temp = measurementOldMatrix.add(temp);

		result = temp.getColumn(0);

		setMeasurementValues(tuple, predictedObjectTupleIndex, result);

	}

	@Override
	public AbstractDataUpdateFunction<M> clone() {
		return new KalmanCorrectStateEstimateFunction<M>(this);
	}
}
