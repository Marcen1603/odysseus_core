package de.uniol.inf.is.odysseus.dsp.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "CROSSCORRELATION", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.PROCESSING }, doc = "todo")
public class CrossCorrelationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5633148709801986786L;
	private List<String> inputAttributes;
	private List<String> outputAttributes;
	private int windowSize;

	public CrossCorrelationAO() {
		super();
	}

	public CrossCorrelationAO(CrossCorrelationAO crossCorrelationAO) {
		super(crossCorrelationAO);
		this.inputAttributes = crossCorrelationAO.inputAttributes;
		this.outputAttributes = crossCorrelationAO.outputAttributes;
		this.windowSize = crossCorrelationAO.windowSize;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CrossCorrelationAO(this);
	}

	@Parameter(name = AggregationFunctionParseOptionsHelper.INPUT_ATTRIBUTES, optional = true, type = StringParameter.class, isList = true)
	public void setInputAttributes(final List<String> attributes) {
		this.inputAttributes = attributes;
	}

	@Parameter(name = AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES, optional = true, type = StringParameter.class, isList = true)
	public void setOutputAttributes(final List<String> attributes) {
		this.outputAttributes = attributes;
	}

	@Parameter(name = "WINDOW_SIZE", type = IntegerParameter.class)
	public void setWindowSize(final int windowSize) {
		this.windowSize = windowSize;
	}

	public List<String> getInputAttributes() {
		return inputAttributes;
	}

	public List<String> getOutputAttributes() {
		return outputAttributes;
	}

	public int getWindowSize() {
		return windowSize;
	}

}
