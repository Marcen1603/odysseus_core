/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.timeseries.autoregression.aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.timeseries.autoregression.estimator.AutoregressionEstimatorFactory;
import de.uniol.inf.is.odysseus.timeseries.autoregression.estimator.IAutoregressionEstimator;
import de.uniol.inf.is.odysseus.timeseries.autoregression.estimator.EstimationMode;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * Aggregation Function to estimate model for forecasting variance
 * 
 * @author Christoph Schröer
 *
 */
public class ModelVariance<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T>implements IAggregationFunctionFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6481641905388320088L;
	
	protected static Logger logger = LoggerFactory
			.getLogger(ModelVariance.class);

	/**
	 * the estimator to estimate the autoregrassion forecaster
	 */
	private IAutoregressionEstimator<Double> autoregressionEstimator;

	private Collection<SDFAttribute> outputAttributes;

	public ModelVariance() {
		super();
		this.outputAttributes = new ArrayList<>();
	}

	public ModelVariance(final int[] inputAttributesIndices, final String[] outputAttribute,
			Collection<SDFAttribute> outputAttributes, IAutoregressionEstimator<Double> autoregressionLearner) {
		super(inputAttributesIndices, outputAttribute);
		this.autoregressionEstimator = autoregressionLearner;
		this.outputAttributes = outputAttributes;
	}

	public ModelVariance(ModelVariance<M, T> modelVariance) {
		super(modelVariance);
		this.autoregressionEstimator = modelVariance.autoregressionEstimator;
		this.outputAttributes = modelVariance.outputAttributes;
	}

	@Override
	public void addNew(T newElement) {
		Object[] elementsValue = getAttributes(newElement);
		if (elementsValue != null) {
			Object residual = elementsValue[0];

			if (residual instanceof Double) {
				this.autoregressionEstimator.addEstimationData((Double) residual);
			} else {
				throw new IllegalArgumentException("The value of the residual attribute should be double, current: "
						+ residual.getClass().getName());
			}
		}
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		for (T outdatedElement : outdatedElements) {
			Object[] elementsValue = getAttributes(outdatedElement);

			if (elementsValue != null) {
				Object oldResidual = elementsValue[0];

				if (oldResidual instanceof Double) {
					this.autoregressionEstimator.removeEstimationData((Double) oldResidual);
				} else {
					throw new IllegalArgumentException("The value of the residual attribute should be double, current: "
							+ oldResidual.getClass().getName());
				}
			}

		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		
		Object[] returnObjects = new Object[this.outputAttributes.size()];

		IAutoregressionForecaster<Double> model = this.autoregressionEstimator.getModel();
		returnObjects[0] = model;

		Object[] outputAttributes = trigger.getAttributes();
		for (int i = 1; i < returnObjects.length; ++i) {

			returnObjects[i] = outputAttributes[i - 1];

		}

		return returnObjects;

	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return this.outputAttributes;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {

		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {

		int[] residualInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, "residual_attribute");
		
		
		
		if (residualInputAttributesIndices == null) {
			residualInputAttributesIndices = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
					attributeResolver, 0, false);
		}

		// output attributes
		// the first output attribute is the model
		// the rest attributes are the input attributes
		final SDFSchema inputSchema = attributeResolver.getSchema().get(0);

		final String[] outputAttributesNames = new String[inputSchema.getAttributes().size() + 1];

		// additional output attribute
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputName == null) {
			outputAttributesNames[0] = "model_variance";
		}

		SDFAttribute outputVarianceAttribute = new SDFAttribute(null, outputAttributesNames[0], SDFDatatype.OBJECT,
				null, null, null);
		Collection<SDFAttribute> outputAttributesLocal = new ArrayList<>();
		outputAttributesLocal.add(outputVarianceAttribute);

		int ouputIndex = 1;
		for (SDFAttribute inputAttribute : inputSchema.getAttributes()) {
			
			// output attributes of input attributes with same name
			outputAttributesNames[ouputIndex] = inputAttribute.getAttributeName();
			SDFAttribute outputAttribute = new SDFAttribute(null, outputAttributesNames[ouputIndex],
					inputAttribute.getDatatype(), null, null, null);
			outputAttributesLocal.add(outputAttribute);

			ouputIndex++;
		}

		// Factory to create the estimator
		AutoregressionEstimatorFactory factory = new AutoregressionEstimatorFactory(
				EstimationMode.valueOf((String) parameters.get("estimation_mode")));

		String model_name = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				"model_name");
		Map<String, String> model_options = AggregationFunctionParseOptionsHelper.getFunctionParameterAsMap(parameters,
				"model_options");

		IAutoregressionEstimator<Double> estimator = factory.createEstimator(model_name, model_options);

		ModelVariance<M, T> modelVariance = new ModelVariance<>(residualInputAttributesIndices, outputAttributesNames,
				outputAttributesLocal, estimator);

		return modelVariance;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new ModelVariance<>(this);
	}

}
