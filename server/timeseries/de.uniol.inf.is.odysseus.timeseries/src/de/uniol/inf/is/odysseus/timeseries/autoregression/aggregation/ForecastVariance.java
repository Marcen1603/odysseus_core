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
import java.util.LinkedList;
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
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * Aggregation Function to forecast variance by a model.
 * 
 * The model (type {@link IAutoregressionForecaster} has to be sent on the
 * datastream.
 * 
 * @author Christoph Schröer
 *
 */
public class ForecastVariance<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T>implements IAggregationFunctionFactory {

	/**
	 * Default additional output attribute names
	 */
	public final static String FORECASTED_VARIANCE_ATTRIBUTE_NAME = "forecasted_variance";

	public final static String FORECASTED_TIME_HORIZON_ATTRIBUTE_NAME = "forecasted_time_horizon";

	public final static String FORECASTED_TIME_HORIZON_PARAMETER_NAME = "time_horizon";

	public final static String RESIDUAL_PARAMETER_NAME = "residual_attribute";

	public final static String MODEL_PARAMETER_NAME = "model_attribute";

	public final static String DO_STOCHASTIC_PROCESS_PARAMETER_NAME = "do_stochastic_process";

	/**
	 * 
	 */
	private static final long serialVersionUID = 6481641905388320088L;

	protected static Logger logger = LoggerFactory.getLogger(ForecastVariance.class);

	private Collection<SDFAttribute> outputAttributes;

	private Integer forecastHorizon;

	/**
	 * list with the original sample data.
	 */
	private LinkedList<T> sampleResiduals;

	/**
	 * list with historical (lag) residuals
	 */
	private LinkedList<Double> lagResiduals;

	/**
	 * list with historical (lag) variances
	 */
	private LinkedList<Double> lagVariances;

	/**
	 * To detect, whether a model is changed.
	 */
	private IAutoregressionForecaster<Double> oldModel;

	/**
	 * This parameter indicates, that the stochastic process should always
	 * processed.
	 */
	private boolean doStochasticProcess;

	public ForecastVariance() {
		super();
		this.outputAttributes = new ArrayList<>();
		this.doStochasticProcess = false;
	}

	public ForecastVariance(final int[] inputAttributesIndices, final String[] outputAttribute,
			Collection<SDFAttribute> outputAttributes, int forecastHorizon) {
		super(inputAttributesIndices, outputAttribute);
		this.outputAttributes = outputAttributes;
		this.sampleResiduals = new LinkedList<>();
		this.lagResiduals = new LinkedList<>();
		this.lagVariances = new LinkedList<>();
		this.forecastHorizon = forecastHorizon;
		this.doStochasticProcess = false;
	}

	public ForecastVariance(final int[] inputAttributesIndices, final String[] outputAttribute,
			Collection<SDFAttribute> outputAttributes, int forecastHorizon, boolean doStochasticProcess) {
		super(inputAttributesIndices, outputAttribute);
		this.outputAttributes = outputAttributes;
		this.sampleResiduals = new LinkedList<>();
		this.lagResiduals = new LinkedList<>();
		this.lagVariances = new LinkedList<>();
		this.forecastHorizon = forecastHorizon;
		this.doStochasticProcess = doStochasticProcess;
	}

	public ForecastVariance(ForecastVariance<M, T> modelVariance) {
		super(modelVariance);
		this.outputAttributes = modelVariance.outputAttributes;
		this.sampleResiduals = modelVariance.sampleResiduals;
		this.lagResiduals = modelVariance.lagResiduals;
		this.lagVariances = modelVariance.lagVariances;
		this.oldModel = modelVariance.oldModel;
		this.forecastHorizon = modelVariance.forecastHorizon;
		this.doStochasticProcess = modelVariance.doStochasticProcess;
	}

	@Override
	public void addNew(T newElement) {
		this.sampleResiduals.add(newElement);
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		this.sampleResiduals.removeAll(outdatedElements);
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {

		Object[] elementsValues = getAttributes(trigger);

		double residual = (double) elementsValues[0]; // resiudal
		@SuppressWarnings("unchecked")
		IAutoregressionForecaster<Double> forecaster = (IAutoregressionForecaster<Double>) elementsValues[1]; // model

		// new model?
		boolean hasModelChanged = false;
		if (!(forecaster.equals(this.oldModel))) {
			hasModelChanged = true;
		}
		this.oldModel = forecaster;

		// ----------------------------
		// init
		Integer countOfResiudals = forecaster.getQ();
		Integer countOfVariances = forecaster.getP();
		this.updateLagResiduals(countOfResiudals, residual);

		// ----------------------------
		// output
		Object[] returnObjects = new Object[this.outputAttributes.size()];
		double forecastedVariance = 0.0;

		if (countOfResiudals > this.lagResiduals.size() || countOfVariances > this.lagVariances.size()) {
			// not enough data yet to estimate variance
			// transfer default variance
			this.updateLagVariances(countOfVariances, forecastedVariance);
		} else {

			// if model not changed, lag data can be used,
			// else new recalculation
			if (hasModelChanged || this.doStochasticProcess) {

				// recalculation of whole stochastic process
				LinkedList<Double> sampleResidualsDouble = new LinkedList<Double>();

				for (T tuple : this.sampleResiduals) {

					Object[] elementTupleValues = getAttributes(tuple);

					double residualTuple = (double) elementTupleValues[0]; // residual

					sampleResidualsDouble.add(residualTuple);
				}

				// new stochastic process with new model
				try {
					forecastedVariance = forecaster.forecast(sampleResidualsDouble, this.forecastHorizon);
				} catch (IllegalArgumentException e) {
					logger.debug(e.getMessage());
					forecastedVariance = Double.NaN;
				}

			} else {
				forecastedVariance = forecaster.forecast(this.lagResiduals, this.lagVariances, this.forecastHorizon);
			}

			if (this.forecastHorizon == 1) {
				// only at the first time horizon the lags have to be
				// updated.
				this.updateLagVariances(countOfVariances, forecastedVariance);
			}

		}

		returnObjects[0] = forecastedVariance;
		returnObjects[1] = this.forecastHorizon;

		Object[] outputAttributes = trigger.getAttributes();
		for (int i = 2; i < returnObjects.length; ++i) {

			returnObjects[i] = outputAttributes[i - 2];

		}

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

		// get index of input attributes
		// in this case, the residual attribute is the important one.
		int[] residualInputAttributesIndexArray = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, ForecastVariance.RESIDUAL_PARAMETER_NAME);
		Integer residualInputAttributesIndex = null;
		if (residualInputAttributesIndexArray != null) {
			residualInputAttributesIndex = residualInputAttributesIndexArray[0];
		}

		int[] modelInputAttributesIndexArray = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, ForecastVariance.MODEL_PARAMETER_NAME);
		Integer modelInputAttributesIndex = null;
		if (modelInputAttributesIndexArray != null) {
			modelInputAttributesIndex = modelInputAttributesIndexArray[0];
		}

		int[] inputAttributesIndices = new int[] { residualInputAttributesIndex, modelInputAttributesIndex };

		if (residualInputAttributesIndexArray == null && modelInputAttributesIndexArray == null) {
			inputAttributesIndices = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
					attributeResolver, 0, false);
		}

		// output attributes
		// the first output attribute is the model
		// the rest attributes are the input attributes
		final SDFSchema inputSchema = attributeResolver.getSchema().get(0);

		final String[] outputAttributesNames = new String[inputSchema.getAttributes().size() + 2];

		// two additional output attributes are the forecasted variance and the
		// time_horizon
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputName == null) {
			outputAttributesNames[0] = ForecastVariance.FORECASTED_VARIANCE_ATTRIBUTE_NAME;
			outputAttributesNames[1] = ForecastVariance.FORECASTED_TIME_HORIZON_ATTRIBUTE_NAME;
		}

		SDFAttribute outputVarianceAttribute = new SDFAttribute(null, outputAttributesNames[0], SDFDatatype.OBJECT,
				null, null, null);

		SDFAttribute outputTimeHorizonAttribute = new SDFAttribute(null, outputAttributesNames[1], SDFDatatype.INTEGER,
				null, null, null);

		Collection<SDFAttribute> outputAttributesLocal = new ArrayList<>();
		outputAttributesLocal.add(outputVarianceAttribute);
		outputAttributesLocal.add(outputTimeHorizonAttribute);

		int ouputIndex = 2;
		for (SDFAttribute inputAttribute : inputSchema.getAttributes()) {

			outputAttributesNames[ouputIndex] = inputAttribute.getAttributeName();
			SDFAttribute outputAttribute = new SDFAttribute(null, outputAttributesNames[ouputIndex],
					inputAttribute.getDatatype(), null, null, null);
			outputAttributesLocal.add(outputAttribute);

			ouputIndex++;
		}

		int time_horizon = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters,
				ForecastVariance.FORECASTED_TIME_HORIZON_PARAMETER_NAME, 1);

		boolean doStochasticProcess = AggregationFunctionParseOptionsHelper.getFunctionParameterAsBoolean(parameters,
				ForecastVariance.DO_STOCHASTIC_PROCESS_PARAMETER_NAME, false);

		// create the aggregation function
		ForecastVariance<M, T> forecastVarianceFunction = new ForecastVariance<>(inputAttributesIndices,
				outputAttributesNames, outputAttributesLocal, time_horizon, doStochasticProcess);

		return forecastVarianceFunction;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new ForecastVariance<>(this);
	}

	private void updateLagResiduals(Integer q, Double newResidual) {

		if (q > this.lagResiduals.size()) {
			this.lagResiduals.addLast(newResidual);
		} else if (q <= this.lagResiduals.size()) {
			this.lagResiduals.addLast(newResidual);
			this.lagResiduals.removeFirst();
		}
	}

	private void updateLagVariances(Integer p, Double newVariance) {

		if (p > this.lagVariances.size()) {
			this.lagVariances.addLast(newVariance);
		} else if (p <= this.lagVariances.size()) {
			this.lagVariances.addLast(newVariance);
			this.lagVariances.removeFirst();
		}
	}
}
