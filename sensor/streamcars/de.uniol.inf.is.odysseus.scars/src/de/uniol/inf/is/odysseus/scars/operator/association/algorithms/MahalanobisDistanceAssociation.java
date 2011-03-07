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
package de.uniol.inf.is.odysseus.scars.operator.association.algorithms;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class MahalanobisDistanceAssociation implements IAssociationAlgorithm {

	private static final String THRESHOLD_ID = "threshold";
	private static final String OPERATOR_ID = "operator";

	private static final String LESS = "LESS";
	private static final String LESS_EQUAL = "LESSEQUAL";
	private static final String GREATER = "GREATER";
	private static final String GREATER_EQUAL = "GREATEREQUAL";
	private static final String EQUAL = "EQUAL";

	private double threshold = 5;
	private String operator = LESS_EQUAL;

	@Override
	public void initAlgorithmParameter(HashMap<String, String> parameterMap) {
		if (parameterMap.containsKey(THRESHOLD_ID)) {
			this.threshold = Double.valueOf(parameterMap.get(THRESHOLD_ID));
		}
		if (parameterMap.containsKey(OPERATOR_ID)) {
			this.operator = parameterMap.get(OPERATOR_ID);
		}
	}

	@Override
	public double evaluate(double[][] scannedObjCovariance,
			double[] scannedObjMesurementValues,
			double[][] predictedObjCovariance,
			double[] predictedObjMesurementValues, double currentRating) {

		RealMatrix leftV = new RealMatrixImpl(scannedObjMesurementValues);
		RealMatrix rightV = new RealMatrixImpl(predictedObjMesurementValues);

		RealMatrix rightCovMatrix = new RealMatrixImpl(scannedObjCovariance);
		RealMatrix leftCovMatrix = new RealMatrixImpl(predictedObjCovariance);

		RealMatrix covInvMatrix = rightCovMatrix.add(leftCovMatrix);
		try {
			covInvMatrix = covInvMatrix.inverse();
		} catch (Exception e) {
			return 0;
		}

		RealMatrix distanceMatrix = leftV.subtract(rightV).transpose()
				.multiply(covInvMatrix).multiply(leftV.subtract(rightV));
		double distance = Math.abs(distanceMatrix.getEntry(0, 0));

		if (this.operator.toUpperCase().equals(LESS)) {
			if (distance < this.threshold) {
				return 100;
			} else {
				return 0;
			}
		} else if (this.operator.toUpperCase().equals(LESS_EQUAL)) {
			if (distance <= this.threshold) {
				return 100;
			} else {
				return 0;
			}
		} else if (this.operator.toUpperCase().equals(EQUAL)) {
			if (distance == this.threshold) {
				return 100;
			} else {
				return 0;
			}
		} else if (this.operator.toUpperCase().equals(GREATER_EQUAL)) {
			if (distance >= this.threshold) {
				return 100;
			} else {
				return 0;
			}
		} else if (this.operator.toUpperCase().equals(GREATER)) {
			if (distance > this.threshold) {
				return 100;
			} else {
				return 0;
			}
		}
		return -1;
	}

}
