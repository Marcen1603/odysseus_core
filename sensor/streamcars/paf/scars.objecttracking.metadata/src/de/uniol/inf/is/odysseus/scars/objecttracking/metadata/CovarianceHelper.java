package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.scars.util.CovarianceMapper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CovarianceHelper {

	private SDFAttributeList schema;
	private CovarianceMapper mapper;
	private IStreamCarsExpression expression;

	public CovarianceHelper(IStreamCarsExpression expression, SDFAttributeList schema) {
		this.schema = schema;
		this.mapper = new CovarianceMapper(this.schema);
		this.expression = expression;
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

	public double[][] getCovarianceForAttributes(double[][] initialCovarianceMatrix, SDFAttributeList schema) {
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
		for (int i = 0; i < variables.size(); i++) {
			for (int j = 0; j <variables.size(); j++) {
				resultMatrix[i][j] = initialCovarianceMatrix[attributePositions.get(i)][attributePositions.get(j)];
			}
		}
		return resultMatrix;
	}
}
