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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.scars.util.helper.CovarianceMapper;

public class CovarianceHelper {

	private SDFSchema schema;
	private CovarianceMapper mapper;
	private IStreamCarsExpression expression;
	
	private String[] restrictedVariables;
	
	public CovarianceHelper(CovarianceHelper copy) {
		this.schema = copy.schema.clone();
		this.mapper = new CovarianceMapper(copy.mapper);
		this.expression = copy.expression;
		this.restrictedVariables = copy.restrictedVariables;
	}
	
	public CovarianceHelper(SDFSchema schema) {
		this.schema = schema;
		this.mapper = new CovarianceMapper(this.schema);
		this.expression = null;
		restrictedVariables = null;
	}

	public CovarianceHelper(IStreamCarsExpression expression, SDFSchema schema) {
		this.schema = schema;
		this.mapper = new CovarianceMapper(this.schema);
		this.expression = expression;
		restrictedVariables = null;
	}
	
	public CovarianceHelper(String[] restrictedVariables, SDFSchema schema) {
		this.schema = schema;
		this.mapper = new CovarianceMapper(this.schema);
		this.expression = null;
		this.restrictedVariables = restrictedVariables;
	}

	public double[][] getCovarianceForAttributes(double[][] initialCovarianceMatrix) {
		List<IStreamCarsExpressionVariable> variables = this.expression.getVariables();
		ArrayList<Integer> attributePositions = new ArrayList<Integer>();
		for (int i = 0; i < variables.size(); i++) {
			if(variables.get(i).isSchemaVariable(schema) && !variables.get(i).hasMetadataInfo()) {
				int tmpIndex = mapper.getCovarianceIndex(variables.get(i).getNameWithoutMetadata());
				if(!attributePositions.contains(tmpIndex)) {
					attributePositions.add(tmpIndex);
				}
			}
		}
		double[][] resultMatrix = new double[attributePositions.size()][attributePositions.size()];
		for (int i = 0; i < attributePositions.size(); i++) {
			for (int j = 0; j <attributePositions.size(); j++) {
				resultMatrix[i][j] = initialCovarianceMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}

	public double[][] getCovarianceForAttributes(double[][] initialCovarianceMatrix, SDFSchema schema) {
		List<IStreamCarsExpressionVariable> variables = this.expression.getVariables();
		ArrayList<Integer> attributePositions = new ArrayList<Integer>();
		for (int i = 0; i < variables.size(); i++) {
			if(variables.get(i).isSchemaVariable(schema) && !variables.get(i).hasMetadataInfo()) {
				int tmpIndex = mapper.getCovarianceIndex(variables.get(i).getNameWithoutMetadata());
				if(!attributePositions.contains(tmpIndex)) {
					attributePositions.add(tmpIndex);
				}
			}
		}
		
		double[][] resultMatrix = new double[attributePositions.size()][attributePositions.size()];
		for (int i = 0; i < attributePositions.size(); i++) {
			for (int j = 0; j <attributePositions.size(); j++) {
				resultMatrix[i][j] = initialCovarianceMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}
	
	public double[][] getCovarianceForRestrictedVariables(double[][] initialCovarianceMatrix) {
		
		ArrayList<Integer> attributePositions = new ArrayList<Integer>();
		for(String resVar : restrictedVariables) {
			int tmpIndex = mapper.getCovarianceIndex(resVar);
			attributePositions.add(tmpIndex);
		}
		
		double[][] resultMatrix = new double[attributePositions.size()][attributePositions.size()];
		for (int i = 0; i < attributePositions.size(); i++) {
			for (int j = 0; j < attributePositions.size(); j++) {
				resultMatrix[i][j] = initialCovarianceMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}
	
	public double[][] getCovarianceForRestrictedVariables(double[][] initialCovarianceMatrix, String[] restrictedVariables) {
		
		ArrayList<Integer> attributePositions = new ArrayList<Integer>();
		for(String resVar : restrictedVariables) {
			int tmpIndex = mapper.getCovarianceIndex(resVar);
			attributePositions.add(tmpIndex);
		}
		
		double[][] resultMatrix = new double[attributePositions.size()][attributePositions.size()];
		for (int i = 0; i < attributePositions.size(); i++) {
			for (int j = 0; j <attributePositions.size(); j++) {
				resultMatrix[i][j] = initialCovarianceMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}
}
