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
package de.uniol.inf.is.odysseus.finance.risk.var.aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import de.uniol.inf.is.odysseus.finance.risk.var.estimator.IVaRModelEstimator;
import de.uniol.inf.is.odysseus.finance.risk.var.estimator.VaRModelEstimatorFactory;
import de.uniol.inf.is.odysseus.finance.risk.var.model.IVaRForecaster;
import de.uniol.inf.is.odysseus.timeseries.IHasEstimationData;

/**
 * Aggregation Function to estimate model for forecasting Value at Risk.
 * 
 * @author Christoph Schröer
 *
 */
public class ModelValueAtRisk<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T>implements IAggregationFunctionFactory {

	/**
	 * Parameter-names
	 */
	public final static String ESTIMATOR_NAME_PARAM_NAME = "estimator_name";
	public final static String ESTIMATOR_OPTIONS_PARAM_NAME = "estimator_options";
	public final static String RETURN_ATTR_PARAM_NAME = "return_attribute";
	public final static String VOLATILITY_ATTR_PARAM_NAME = "volatility_attribute";

	/**
	 * Default additional output attribute names
	 */
	public final static String VAR_MODEL_ATTRIBUTE_NAME = "var_model";

	/**
	 * 
	 */
	private static final long serialVersionUID = 6481641905388320088L;

	protected static Logger logger = LoggerFactory.getLogger(ModelValueAtRisk.class);

	private IVaRModelEstimator varModelEstimator;

	private Collection<SDFAttribute> outputAttributes;

	public ModelValueAtRisk() {
		super();
		this.outputAttributes = new ArrayList<>();
	}

	public ModelValueAtRisk(final int[] inputAttributesIndices, final String[] outputAttribute,
			Collection<SDFAttribute> outputAttributes, IVaRModelEstimator varModelEstimator) {
		super(inputAttributesIndices, outputAttribute);
		this.varModelEstimator = varModelEstimator;
		this.outputAttributes = outputAttributes;
	}

	public ModelValueAtRisk(ModelValueAtRisk<M, T> varModelEstimator) {
		super(varModelEstimator);
		this.varModelEstimator = varModelEstimator.varModelEstimator;
		this.outputAttributes = varModelEstimator.outputAttributes;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addNew(T newElement) {
		if (this.varModelEstimator instanceof IHasEstimationData<?>) {
			if (newElement != null) {
				((IHasEstimationData) this.varModelEstimator).addEstimationData(newElement);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		if (this.varModelEstimator instanceof IHasEstimationData<?>) {
			for (T outdatedElement : outdatedElements) {

				((IHasEstimationData) this.varModelEstimator).removeEstimationData(outdatedElement);

			}
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {

		this.varModelEstimator.estimateModel();

		Object[] returnObjects = new Object[this.outputAttributes.size()];

		IVaRForecaster model = this.varModelEstimator.getModel();
		returnObjects[0] = model;

		return returnObjects;

	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return this.outputAttributes;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return parameters.containsKey(ModelValueAtRisk.ESTIMATOR_NAME_PARAM_NAME);
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {

		int[] residualInputAttributesIndices = AggregationFunctionParseOptionsHelper
				.getInputAttributeIndices(parameters, attributeResolver, 0, false);

		// output attributes
		final String[] outputAttributesNames = new String[1]; // only one model
																// as output
		// one output attribute
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputName == null) {
			// Default
			outputAttributesNames[0] = ModelValueAtRisk.VAR_MODEL_ATTRIBUTE_NAME;
		}

		Collection<SDFAttribute> outputAttributesLocal = new ArrayList<>();
		SDFAttribute outputVaRAttribute = new SDFAttribute(null, outputAttributesNames[0], SDFDatatype.OBJECT, null,
				null, null);
		outputAttributesLocal.add(outputVaRAttribute);

		// Factory to create the estimator
		VaRModelEstimatorFactory factory = new VaRModelEstimatorFactory(parameters, attributeResolver);

		String estimator_name = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				ModelValueAtRisk.ESTIMATOR_NAME_PARAM_NAME);

		Map<String, String> estimator_options = new HashMap<>();
		if (parameters.containsKey(ModelValueAtRisk.ESTIMATOR_OPTIONS_PARAM_NAME)) {

			estimator_options = AggregationFunctionParseOptionsHelper.getFunctionParameterAsMap(parameters,
					ModelValueAtRisk.ESTIMATOR_OPTIONS_PARAM_NAME);
		}

		IVaRModelEstimator estimator = factory.createEstimator(estimator_name, estimator_options);

		ModelValueAtRisk<M, T> varModelAggregation = new ModelValueAtRisk<>(residualInputAttributesIndices,
				outputAttributesNames, outputAttributesLocal, estimator);

		return varModelAggregation;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new ModelValueAtRisk<>(this);
	}

}
