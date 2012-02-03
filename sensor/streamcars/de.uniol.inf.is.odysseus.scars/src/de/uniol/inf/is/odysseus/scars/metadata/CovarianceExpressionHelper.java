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
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class CovarianceExpressionHelper {
	
	public CovarianceExpressionHelper() {
	}
	
	public double[][] getCovarianceForExpressionVariables(IStreamCarsExpression exp, IProbability cov) {
		List<IStreamCarsExpressionVariable> variables = exp.getVariables();
		ArrayList<Integer> attributePositions = new ArrayList<Integer>();
		
		for (int i = 0; i < variables.size(); i++) {
			IStreamCarsExpressionVariable var = variables.get(i);
			if(!var.hasMetadataInfo() && var.isSchemaVariable()) {
				int tmpIndex = cov.getCovarianceIndex(variables.get(i).getNameWithoutMetadata());
				if(tmpIndex != -1 && !attributePositions.contains(tmpIndex)) {
					attributePositions.add(tmpIndex);
				}
			}
		}
	
		
		double[][] initialMatrix = cov.getCovariance();
		double[][] resultMatrix = new double[attributePositions.size()][attributePositions.size()];
		for (int i = 0; i < attributePositions.size(); i++) {
			for (int j = 0; j <attributePositions.size(); j++) {
				resultMatrix[i][j] = initialMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}
	
	public double[][] getCovarianceForRestictedAttributes(List<String> restrictedAttrs, IProbability cov) {
		ArrayList<Integer> attributePositions = new ArrayList<Integer>();
		
		for (String attr : restrictedAttrs) {
			int tmpIndex = cov.getCovarianceIndex(attr);
			if(tmpIndex != -1 && !attributePositions.contains(tmpIndex)) {
				attributePositions.add(tmpIndex);
			}
		}
		double[][] initialMatrix = cov.getCovariance();
		double[][] resultMatrix = new double[attributePositions.size()][attributePositions.size()];
		for (int i = 0; i < attributePositions.size(); i++) {
			for (int j = 0; j <attributePositions.size(); j++) {
				resultMatrix[i][j] = initialMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}
	
	public double[][] getCovarianceForRestictedAttributes(String[] restrictedAttrs, IProbability cov) {
		ArrayList<Integer> attributePositions = new ArrayList<Integer>();
		
		for (String attr : restrictedAttrs) {
			int tmpIndex = cov.getCovarianceIndex(attr);
			if(tmpIndex != -1 && !attributePositions.contains(tmpIndex)) {
				attributePositions.add(tmpIndex);
			}
		}
		double[][] initialMatrix = cov.getCovariance();
		double[][] resultMatrix = new double[attributePositions.size()][attributePositions.size()];
		for (int i = 0; i < attributePositions.size(); i++) {
			for (int j = 0; j <attributePositions.size(); j++) {
				resultMatrix[i][j] = initialMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}
}
