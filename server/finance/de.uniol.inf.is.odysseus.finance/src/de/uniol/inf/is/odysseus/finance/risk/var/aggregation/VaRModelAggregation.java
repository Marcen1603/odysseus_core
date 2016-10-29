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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.finance.risk.var.estimator.IVaRModelEstimator;
import de.uniol.inf.is.odysseus.finance.risk.var.estimator.VaRModelEstimatorFactory;
import de.uniol.inf.is.odysseus.finance.risk.var.model.IVaRForecaster;
import de.uniol.inf.is.odysseus.timeseries.IHasEstimationData;

/**
 * Aggregation Function to estimate model for forecasting variance
 * 
 * @author Christoph Schröer
 *
 */
public class VaRModelAggregation<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T>implements IAggregationFunctionFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6481641905388320088L;

	protected static Logger logger = LoggerFactory.getLogger(VaRModelAggregation.class);

	private IVaRModelEstimator varModelEstimator;

	private Collection<SDFAttribute> outputAttributes;

	public VaRModelAggregation() {
		super();
		this.outputAttributes = new ArrayList<>();
	}

	public VaRModelAggregation(final int[] inputAttributesIndices, final String[] outputAttribute,
			Collection<SDFAttribute> outputAttributes, IVaRModelEstimator varModelEstimator) {
		super(inputAttributesIndices, outputAttribute);
		this.varModelEstimator = varModelEstimator;
		this.outputAttributes = outputAttributes;
	}

	public VaRModelAggregation(VaRModelAggregation<M, T> varModelEstimator) {
		super(varModelEstimator);
		this.varModelEstimator = varModelEstimator.varModelEstimator;
		this.outputAttributes = varModelEstimator.outputAttributes;
	}

	@Override
	public void addNew(T newElement) {
		logger.debug("addNew");
		if (this.varModelEstimator instanceof IHasEstimationData<?>) {
			if (newElement != null) {
				((IHasEstimationData) this.varModelEstimator).addEstimationData(newElement);
			}
		}
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		logger.debug("removeOutdated");
		if (this.varModelEstimator instanceof IHasEstimationData<?>) {
			for (T outdatedElement : outdatedElements) {

				((IHasEstimationData) this.varModelEstimator).removeEstimationData(outdatedElement);

			}
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {

		logger.debug("evaluate");

		// TODO: add object to learning data...
		// TODO: handle also, when no learning is required.

		// newLearningObject
		// this.autoregressionLearner.addLearningData(trigger);

		this.varModelEstimator.estimateModel();
		
		Object[] returnObjects = new Object[this.outputAttributes.size()];

		IVaRForecaster model = this.varModelEstimator.getModel();
		returnObjects[0] = model;

		// Object[] outputAttributes = trigger.getAttributes();
		// for (int i = 1; i < returnObjects.length; ++i) {
		//
		// returnObjects[i] = outputAttributes[i - 1];
		//
		// }

		return returnObjects;

	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return this.outputAttributes;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {

		// Model

		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {

		// int[] residualInputAttributesIndices =
		// AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
		// attributeResolver, "residual_attribute");
		//
		// if (residualInputAttributesIndices == null) {
		int[] residualInputAttributesIndices = AggregationFunctionParseOptionsHelper
				.getInputAttributeIndices(parameters, attributeResolver, 0, false);
				// }

		// output attributes
		// the first output attribute is the model
		// the rest attributes are the input attributes
		final SDFSchema inputSchema = attributeResolver.getSchema().get(0);

		final String[] outputAttributesNames = new String[1]; // only one model as output

		// one output attribute
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputName == null) {
			// Default
			outputAttributesNames[0] = "var_model";
		}

		SDFAttribute outputVarianceAttribute = new SDFAttribute(null, outputAttributesNames[0], SDFDatatype.OBJECT,
				null, null, null);
		Collection<SDFAttribute> outputAttributesLocal = new ArrayList<>();
		outputAttributesLocal.add(outputVarianceAttribute);

		//
		// int ouputIndex = 1;
		// for (SDFAttribute inputAttribute : inputSchema.getAttributes()) {
		//
		// outputAttributesNames[ouputIndex] =
		// AggregationFunctionParseOptionsHelper.getFunctionName(parameters) +
		// "_"
		// + inputAttribute.getAttributeName();
		// SDFAttribute outputAttribute = new SDFAttribute(null,
		// outputAttributesNames[ouputIndex],
		// inputAttribute.getDatatype(), null, null, null);
		// outputAttributesLocal.add(outputAttribute);
		//
		// ouputIndex++;
		// }

		// Factory to create the estimator
		VaRModelEstimatorFactory factory = new VaRModelEstimatorFactory(parameters, attributeResolver);

		String estimator_name = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				"estimator_name");

		// if(AggregationFunctionParseOptionsHelper.){
		//
		// }
		//
		Map<String, String> estimator_options = null;
		if (parameters.containsKey("estimator_options")) {
			estimator_options = AggregationFunctionParseOptionsHelper.getFunctionParameterAsMap(parameters,
					"estimator_options");
		}

		IVaRModelEstimator estimator = factory.createEstimator(estimator_name, estimator_options);

		VaRModelAggregation<M, T> varModelAggregation = new VaRModelAggregation<>(residualInputAttributesIndices,
				outputAttributesNames, outputAttributesLocal, estimator);

		return varModelAggregation;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new VaRModelAggregation<>(this);
	}

}
