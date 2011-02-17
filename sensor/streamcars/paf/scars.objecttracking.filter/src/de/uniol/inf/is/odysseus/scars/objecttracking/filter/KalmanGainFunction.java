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
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;

/**
 * @author dtwumasi
 * 
 */
public class KalmanGainFunction<M extends IProbability & IGain > extends AbstractMetaDataCreationFunction<M> {

	public KalmanGainFunction() {
		super();
	}

	public KalmanGainFunction(KalmanGainFunction<M> copy) {

		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));

	}

	public KalmanGainFunction(HashMap<Enum<Parameters>, Object> parameters) {
		this.setParameters(parameters);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void compute(IConnection connected, MVRelationalTuple<M> tuple, HashMap<Enum<Parameters>, Object> parameters) {

		//TupleHelper tHelper = new TupleHelper(tuple);
		MVRelationalTuple<M> oldTuple = (MVRelationalTuple<M>)connected.getRightPath().getTupleObject();
		MVRelationalTuple<M> newTuple = (MVRelationalTuple<M>)connected.getLeftPath().getTupleObject();

		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();

		RealMatrix oldCovariance = new RealMatrixImpl(covarianceOld);
		RealMatrix newCovariance = new RealMatrixImpl(covarianceNew);

		RealMatrix temp = new RealMatrixImpl();

		temp = oldCovariance.add(newCovariance);
		temp = temp.inverse();
		temp = oldCovariance.multiply(temp);

		// set gain
//		System.out.println("Gain: " + temp.getData());
		oldTuple.getMetadata().setGain(temp.getData());
		newTuple.getMetadata().setGain(temp.getData());
	}

	@Override
	public KalmanGainFunction<M> clone() {
		return new KalmanGainFunction<M>(this);
	}
}
