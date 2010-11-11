package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.util.SchemaInfo;
import de.uniol.inf.is.odysseus.scars.util.SchemaIterator;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class CovarianceExpressionMatrix {

	private SDFAttributeList schema;
	private PredictionExpression[][] expressionMatrix;
	private TupleHelper tupleHelper;

	public CovarianceExpressionMatrix(SDFAttributeList schema) {
		this.schema = schema;
		initialize(this.schema);
	}

	public void setRootTuple(MVRelationalTuple<?> rootTuple) {
		this.tupleHelper = new TupleHelper(rootTuple);
	}

	public void initialize(SDFAttributeList schema) {
		this.schema = schema;

		List<SDFAttribute> measurementAttributes = new ArrayList<SDFAttribute>();
		for (SchemaInfo info : new SchemaIterator(this.schema)) {
			// if current attribute is measurement attribute
			if (info.attribute.getDatatype() != null
					&& SDFDatatypes.isMeasurementValue(info.attribute
							.getDatatype())) {
				// save attribute for later use
				measurementAttributes.add(info.attribute);
			}
		}

		this.expressionMatrix = new PredictionExpression[measurementAttributes
				.size()][measurementAttributes.size()];

		HashMap<String, PredictionExpression> expressionCache = new HashMap<String, PredictionExpression>();
		// work through all measurement attributes saved before
		for (int j = 0; j < measurementAttributes.size(); j++) {
			List<?> covarianceList = measurementAttributes.get(j)
					.getCovariance();
			// initialize current matrix row with covariance list
			for (int i = 0; i < covarianceList.size(); ++i) {
				String expressionString = (String) covarianceList.get(i);
				if (expressionCache.containsKey(expressionString)) {
					this.expressionMatrix[j][i] = expressionCache
							.get(expressionString);
				} else {
					this.expressionMatrix[j][i] = new PredictionExpression("",
							expressionString);
					expressionCache.put(expressionString,
							this.expressionMatrix[j][i]);
				}
			}
		}
	}

	public double[][] calculateMatrix(int objectIterator) {
		double[][] covarianceMatrix = new double[this.expressionMatrix.length][this.expressionMatrix[0].length];

		HashMap<String, Double> expressionValueCache = new HashMap<String, Double>();
		HashMap<String, Object> attributValueCache = new HashMap<String, Object>();

		for (int i = 0; i < this.expressionMatrix.length; i++) {
			for (int j = 0; j < this.expressionMatrix[i].length; j++) {
				String expressionString = this.expressionMatrix[i][j]
						.getExpression();
				if (expressionValueCache.containsKey(expressionString)) {
					covarianceMatrix[i][j] = expressionValueCache
							.get(expressionString);
				} else {
					this.expressionMatrix[i][j].replaceVaryingAttributeIndex(
							this.schema, objectIterator);
					covarianceMatrix[i][j] = calculateValue(
							this.expressionMatrix[i][j], attributValueCache);
					expressionValueCache.put(expressionString,
							covarianceMatrix[i][j]);
				}
			}
		}
		
		RealMatrix testMatrix = new RealMatrixImpl(covarianceMatrix);
		try {
			testMatrix.inverse();
		} catch (Exception exception) {
			if (this.expressionMatrix.length > 0) {
				covarianceMatrix = new double[this.expressionMatrix.length][this.expressionMatrix[0].length];
				for (int i = 0; i < this.expressionMatrix.length; i++) {
					covarianceMatrix[i][i] = 0.1;
				}
			}
		}

		return covarianceMatrix;
	}

	private double calculateValue(PredictionExpression expression,
			HashMap<String, Object> attributValueCache) {
		try {
			for (String attribute : expression.getAttributeNames(this.schema)) {
				if (attributValueCache.containsKey(attribute)) {
					expression.bindVariable(attribute,
							attributValueCache.get(attribute));
				} else {
					int[] objectPath = expression.getAttributePath(attribute);
					expression.bindVariable(attribute,
							tupleHelper.getObject(objectPath));
				}
			}

			expression.evaluate();
			return expression.getTargetDoubleValue();
		} catch (Exception exception) {
			return 0;
		}
	}
}
