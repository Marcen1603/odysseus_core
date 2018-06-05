package de.uniol.inf.is.odysseus.timeseries.logicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.timeseries.imputation.ImputationRegistry;
import de.uniol.inf.is.odysseus.timeseries.imputation.LastValueCarriedForwardImputation;

/**
 * This is the logical operator for imputation data, when data is missing.
 * 
 * Same OutputSchema as InputSchema.
 * 
 * @author Christoph Schröer
 *
 */
@LogicalOperator(name = "IMPUTATION", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.MINING }, doc = "Impute missing data in a regular (time series) data stream.")
public class ImputationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7752589543824364696L;

	private String imputationStrategy = LastValueCarriedForwardImputation.NAME; // Default

	private final Map<String, String> optionsMap = new HashMap<>();
	private List<Option> optionsList;

	public ImputationAO(final ImputationAO op) {
		super(op);
		this.imputationStrategy = op.getImputationStrategy();
		this.optionsMap.putAll(op.optionsMap);
	}

	public ImputationAO() {
		super();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ImputationAO(this);
	}

	public String getImputationStrategy() {
		return this.imputationStrategy;
	}

	@Parameter(name = "imputation_strategy", type = StringParameter.class, doc = "Strategy to impute missing data.", optional = true, possibleValues = "getImputationStrategies")
	public void setImputationStrategy(String imputationStrategy) {
		this.imputationStrategy = imputationStrategy;
	}

	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options for imputation strategy.")
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

	public List<String> getImputationStrategies() {
		return ImputationRegistry.getInstance().getImputationStrategies();
	}

}
