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
package de.uniol.inf.is.odysseus.scars.objecttracking.association.algorithms;

import java.util.HashMap;

/**
 * <p>
 * IAssociationAlgorithm represents a algorithm for the evaluation of connections. Each implementation can have a set of <strong>individual</strong> parameters
 * and within {@link #evaluate(double[][], double[], double[][], double[], double)} a new rating is be calculated.
 * </p>
 * 
 * @author Volker Janz
 *
 */
public interface IAssociationAlgorithm {

	/**
	 * Since each algorithm can have special parameters, they are defined in the query and afterwards are stored within a HashMap.
	 * initAlgorithmParameter takes the parameters out of the HashMap and puts them into class variables.
	 * @param parameterMap The HashMap that contains the parameters defined in the query.
	 */
	public void initAlgorithmParameter(HashMap<String, String> parameterMap);

	/**
	 * Evaluate returns a new rating for a specific connection.
	 * @param scannedObjCovariance Covariance matrix of the scanned object.
	 * @param scannedObjMesurementValues Mesurement values of the scanned object.
	 * @param predictedObjCovariance Covariance matrix of the predicted object.
	 * @param predictedObjMesurementValues Mesurement values of the predicted object.
	 * @param currentRating The current rating of this connection.
	 * @return New rating of this connection.
	 */
	public double evaluate(double[][] scannedObjCovariance, double[] scannedObjMesurementValues, double[][] predictedObjCovariance, double[] predictedObjMesurementValues, double currentRating);

}
