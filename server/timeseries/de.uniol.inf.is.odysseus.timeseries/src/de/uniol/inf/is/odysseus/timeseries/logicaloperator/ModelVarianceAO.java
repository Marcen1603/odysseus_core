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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.timeseries.autoregression.estimator.EstimationMode;

/**
 * @author Christoph Schröer
 *
 */
@Deprecated
@LogicalOperator(deprecation = true, name = "MODEL_VARIANCE", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.MINING }, doc = "Operator creates autoregressive model for variance forecasting.")
public class ModelVarianceAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7752589543824364696L;
	
	public static final String MODEL_ATTRIBUTE_NAME = "model";

	/**
	 * Name of Forecaster-Model (like GARCH11)
	 */
	private String modelName;

	/**
	 * Name of the estimator
	 */
	private String estimationMethod;

	/**
	 * Attribute in input data stream for residual.
	 */
	private SDFAttribute residualAttribute;

	// for non learning mode:

	private EstimationMode learningMode;

	private final Map<String, String> optionsMap = new HashMap<>();
	private List<Option> optionsList;

	private final Map<String, String> estimationOptionsMap = new HashMap<>();
	private List<Option> estimationOptionsList;

	private final Map<String, String> modelOptionsMap = new HashMap<>();
	private List<Option> modelOptionsList;

	public ModelVarianceAO(final ModelVarianceAO op) {
		super(op);
		this.modelName = op.getModelName();
		this.residualAttribute = op.getResidualAttribute();
		this.optionsMap.putAll(op.optionsMap);
		this.estimationOptionsList = op.getEstimationOptions();
		this.estimationOptionsMap.putAll(op.estimationOptionsMap);
		this.modelOptionsList = op.getModelOptions();
		this.modelOptionsMap.putAll(op.modelOptionsMap);
		this.learningMode = op.getLearningMode();
	}

	public ModelVarianceAO() {
		super();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ModelVarianceAO(this);
	}

	public String getModelName() {
		return modelName;
	}

	@Parameter(name = "model_name", type = StringParameter.class, doc = "Name of the autoregressive Model", optional = true)
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public SDFAttribute getResidualAttribute() {
		return residualAttribute;
	}

	@Parameter(name = "residual_attribute", type = ResolvedSDFAttributeParameter.class, doc = "Name of Attribute of input data streams, which should be used as residuals.")
	public void setResidualAttribute(final SDFAttribute residualAttributeName) {
		this.residualAttribute = residualAttributeName;
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

	// Model Options
	@Parameter(type = OptionParameter.class, name = "model_options", optional = true, isList = true, doc = "Required options for model (like parameter).")
	public void setModelOptions(List<Option> modelOptionsList) {
		for (Option option : modelOptionsList) {
			this.modelOptionsMap.put(option.getName().toLowerCase(), option.getValue());
		}
		this.modelOptionsList = modelOptionsList;
	}

	public List<Option> getModelOptions() {
		return this.modelOptionsList;
	}

	protected void addModelOption(String key, String value) {
		this.modelOptionsMap.put(key, value);
	}

	protected String getModelOption(String key) {
		return this.modelOptionsMap.get(key);
	}

	public void setModelOptionMap(Map<String, String> options) {
		this.modelOptionsMap.clear();
		this.modelOptionsMap.putAll(options);
	}

	public Map<String, String> getModelOptionsMap() {
		return this.modelOptionsMap;
	}

	// Estimation options
	@Parameter(type = OptionParameter.class, name = "estimation_options", optional = true, isList = true, doc = "Required options for estimation method (like parameter).")
	public void setEstimationOptions(List<Option> estimationOptionsList) {
		for (Option option : estimationOptionsList) {
			this.estimationOptionsMap.put(option.getName().toLowerCase(), option.getValue());
		}
		this.estimationOptionsList = estimationOptionsList;
	}

	public List<Option> getEstimationOptions() {
		return this.estimationOptionsList;
	}

	protected void addEstimationOption(String key, String value) {
		this.estimationOptionsMap.put(key, value);
	}

	protected String getEstimationOption(String key) {
		return this.estimationOptionsMap.get(key);
	}

	public void setEstimationOptionMap(Map<String, String> options) {
		this.estimationOptionsMap.clear();
		this.estimationOptionsMap.putAll(options);
	}

	public Map<String, String> getEstimationOptionsMap() {
		return this.estimationOptionsMap;
	}

	// For learning mode

	public EstimationMode getLearningMode() {
		return learningMode;
	}

	@Parameter(type = EnumParameter.class, name = "learning_mode", optional = true, doc = "Optionale parameter, if no learning is needed. Then, the parameters alphas, betas and omega must be set.")
	public void setLearningMode(EstimationMode learningMode) {
		this.learningMode = learningMode;
	}

	public String getEstimationMethod() {
		return estimationMethod;
	}

	@Parameter(name = "estimation_name", type = StringParameter.class, doc = "Name of the Estimator.", optional = true)
	public void setEstimationMethod(String estimationMethod) {
		this.estimationMethod = estimationMethod;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		final SDFAttribute model = new SDFAttribute(null, ModelVarianceAO.MODEL_ATTRIBUTE_NAME, new SDFDatatype("IAutoregressionForecaster"), null,
				null, null);
		attributes.add(model);
		final SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		return outSchema;

	}

}
