package de.uniol.inf.is.odysseus.mining.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.mining.MiningAlgorithmRegistry;

@LogicalOperator(name = "FREQUENTPATTERN", minInputPorts = 1, maxInputPorts = 1, doc = "This operator create frequent item sets from a given stream. The result stream creates a tuple with 3 attributes: id: the number (a simple counter) of the pattern, set: the frequent pattern, which is a list of tuples (a nested attribute ~ NF^2), support: the support of the pattern", category = { LogicalOperatorCategory.MINING })
public class FrequentPatternMiningAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7771591123865284928L;
	private int transactions = Integer.MAX_VALUE;
	private String learner = "";
	private List<Option> options = new ArrayList<>();
	private double support = 0.5;

	public FrequentPatternMiningAO() {

	}

	public FrequentPatternMiningAO(FrequentPatternMiningAO frequentItemsetAO) {
		this.learner = frequentItemsetAO.learner;
		this.support = frequentItemsetAO.support;
		this.transactions = frequentItemsetAO.transactions;
		this.options = new ArrayList<>(frequentItemsetAO.options);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FrequentPatternMiningAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		SDFAttribute attributeId = new SDFAttribute(null, "id", SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeId);
		// subschema is inputschema!
		SDFDatatype list = new SDFDatatype("PATTERN", SDFDatatype.KindOfDatatype.LIST, this.getInputSchema(0));
		SDFAttribute attributeSet = new SDFAttribute(null, "set", list, null, null, null);
		attributes.add(attributeSet);
		SDFAttribute support = new SDFAttribute(null, "support", SDFDatatype.INTEGER, null, null, null);
		attributes.add(support);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		return outSchema;

	}

	public String getLearner() {
		return learner;
	}

	@Parameter(name = "algorithm", type = StringParameter.class, possibleValues = "getAlgorithmValues")
	public void setAlgorithm(String algorithm) {
		this.options.add(new Option("model", algorithm));
	}

	@Parameter(name = "learner", type = StringParameter.class, possibleValues = "getLearnerValues")
	public void setLearner(String learner) {
		this.learner = learner;
	}

	public List<String> getLearnerValues() {
		return MiningAlgorithmRegistry.getInstance().getFrequentPatternMinerNames();
	}

	public List<String> getAlgorithmValues() {
		return MiningAlgorithmRegistry.getInstance().getFrequentPatternMinerAlgorithms();
	}

	@Parameter(name = "options", type = OptionParameter.class, optional = true, isList = true)
	public void setOptions(List<Option> options) {		
		this.options = options;		
	}

	public List<Option> getOptions() {
		return this.options;
	}

	@Parameter(name = "support", optional = true, type = DoubleParameter.class)
	public void setSupport(double support) {
		this.support = support;
	}

	public double getSupport() {
		return this.support;
	}

	public int getMinSupport() {
		if (support <= 1.0) {
			return (int) (this.transactions * support);
		} else {
			return (int) support;
		}
	}

	@Parameter(name = "transactions", optional = true, type = IntegerParameter.class)
	public void setMaxTransactions(int transactions) {
		this.transactions = transactions;
	}

	public int getMaxTransactions() {
		return transactions;
	}

	public Map<String, String> getOptionsMap() {
		Map<String, String> optionsMap = new HashMap<>();
		for(Option option : this.options){
			optionsMap.put(option.getName(), option.getValue());
		}
		return optionsMap;
	}
}
