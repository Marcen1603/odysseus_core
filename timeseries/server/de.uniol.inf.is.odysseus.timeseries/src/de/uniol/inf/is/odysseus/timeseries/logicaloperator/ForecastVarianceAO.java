package de.uniol.inf.is.odysseus.timeseries.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christoph Schröer
 *
 */
@Deprecated
@LogicalOperator(deprecation = true, name = "FORECAST_VARIANCE", minInputPorts = 1, maxInputPorts = 2, category = {
		LogicalOperatorCategory.MINING }, doc = "Operator forecasts variances for the next time period(s). Operatore requires model and data stream (with current resiudals and varaince) as input.")
public class ForecastVarianceAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7752589543824364696L;

	public final static String FORECASTED_VARIANCE_ATTRIBUTE_NAME = "forecasted_variance";

	public final static String FORECASTED_TIME_HORIZON_ATTRIBUTE_NAME = "forecasted_time_horizon";

	public final static String FORECASTED_TIME_INTERVAL_START_ATTRIBUTE_NAME = "forecasted_time_interval_start";

	public final static String FORECASTED_TIME_INTERVAL_END_ATTRIBUTE_NAME = "forecasted_time_interval_end";

	// QN: was ist besser, options oder parameter?
	private Integer forecastHorizon = 1;

	/**
	 * Attribute in input data stream for residual.
	 */
	private SDFAttribute residualAttribute;

	/**
	 * Attribute in input data stream for variance.
	 * 
	 * @deprecated
	 */
	private SDFAttribute varianceAttribute;

	/**
	 * Attribute in input data stream for model.
	 */
	private SDFAttribute modelAttribute;

	private final Map<String, String> optionsMap = new HashMap<>();
	private List<Option> optionsList;

	public ForecastVarianceAO(final ForecastVarianceAO op) {
		super(op);
		this.residualAttribute = op.getResidualAttribute();
		this.varianceAttribute = op.getVarianceAttribute();
		this.modelAttribute = op.getModelAttribute();
		this.forecastHorizon = op.getForecastHorizon();
		this.optionsMap.putAll(op.optionsMap);
	}

	public ForecastVarianceAO() {
		super();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ForecastVarianceAO(this);
	}

	public SDFAttribute getResidualAttribute() {
		return residualAttribute;
	}

	@Parameter(name = "residual_attribute", type = ResolvedSDFAttributeParameter.class, doc = "Name of Attribute of input data streams, which should be used as residuals.")
	public void setResidualAttribute(final SDFAttribute residualAttributeName) {
		this.residualAttribute = residualAttributeName;
	}

	public SDFAttribute getVarianceAttribute() {
		return varianceAttribute;
	}

	@Parameter(name = "variance_attribute", type = ResolvedSDFAttributeParameter.class, doc = "Name of Attribute of input data streams, which should be used as variances.", optional = true)
	public void setVarianceAttribute(final SDFAttribute varianceAttributeName) {
		this.varianceAttribute = varianceAttributeName;
	}

	public Integer getForecastHorizon() {
		return forecastHorizon;
	}

	@Parameter(name = "forecast_horizon", type = IntegerParameter.class, doc = "Number of forecasting horizons. Default is 1.", optional = true)
	public void setForecastHorizon(Integer forecastHorizon) {
		this.forecastHorizon = forecastHorizon;
	}

	public SDFAttribute getModelAttribute() {
		return modelAttribute;
	}

	@Parameter(name = "model_attribute", type = ResolvedSDFAttributeParameter.class, doc = "Name of Attribute of input data streams, which should be used as (GARCH-)model.", optional = true)
	public void setModelAttribute(SDFAttribute modelAttribute) {
		this.modelAttribute = modelAttribute;
	}

	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	public void setOptions(List<Option> value) {
		for (Option option : value) {
			optionsMap.put(option.getName().toLowerCase(), option.getValue());
		}
		optionsList = value;
	}

	public List<Option> getOptions() {
		return optionsList;
	}

	protected void addOption(String key, String value) {
		optionsMap.put(key, value);
	}

	protected String getOption(String key) {
		return optionsMap.get(key);
	}

	public void setOptionMap(Map<String, String> options) {
		this.optionsMap.clear();
		this.optionsMap.putAll(options);
	}

	public Map<String, String> getOptionsMap() {
		return optionsMap;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {

		// SDFAttribute residualAttribute = this.getResidualAttribute();
		// if (residualAttribute == null) {
		// residualAttribute = FindAttributeHelper.findAttributeByName(this,
		// "residual");
		// }
		//
		// final int port = FindAttributeHelper.findPortWithAttribute(this,
		// residualAttribute);
		//
		// SDFAttribute modelAttribute = getModelAttribute();
		// if (modelAttribute == null) {
		// modelAttribute = FindAttributeHelper.findAttributeByName(this,
		// "model");
		// }

		final List<SDFAttribute> attributes = new ArrayList<>();
		// for (final SDFAttribute oldAttribute : this.getInputSchema(port)
		// .getAttributes()) {
		// if (!oldAttribute.equals(modelAttribute)) {
		// attributes
		// .add(new SDFAttribute(null, oldAttribute
		// .getAttributeName(),
		// oldAttribute.getDatatype(), oldAttribute
		// .getUnit(), oldAttribute
		// .getDtConstraints()));
		// }
		// }

		final SDFAttribute attributeId = new SDFAttribute(null, ForecastVarianceAO.FORECASTED_VARIANCE_ATTRIBUTE_NAME,
				SDFDatatype.DOUBLE, null, null, null);
		attributes.add(attributeId);

		final SDFAttribute attributeTimeHorizon = new SDFAttribute(null,
				ForecastVarianceAO.FORECASTED_TIME_HORIZON_ATTRIBUTE_NAME, SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeTimeHorizon);

		final SDFAttribute attributeIntervalStart = new SDFAttribute(null,
				ForecastVarianceAO.FORECASTED_TIME_INTERVAL_START_ATTRIBUTE_NAME, SDFDatatype.STRING, null, null, null);
		attributes.add(attributeIntervalStart);

		final SDFAttribute attributeIntervalEnd = new SDFAttribute(null,
				ForecastVarianceAO.FORECASTED_TIME_INTERVAL_END_ATTRIBUTE_NAME, SDFDatatype.STRING, null, null, null);
		attributes.add(attributeIntervalEnd);

		final SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		return outSchema;
	}

}
