package de.uniol.inf.is.odysseus.dsp.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.dsp.aggregation.FIRFilter;
import de.uniol.inf.is.odysseus.dsp.aggregation.FIRFilterAggregationFunctionFactory;

@LogicalOperator(name = "FIR", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.PROCESSING }, doc = "Filters signals using a finite impulse response filter.")
public class FIRFilterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5633148709801986786L;
	private List<String> inputAttributes;
	private List<String> outputAttributes;
	private List<Double> coefficients;

	public FIRFilterAO() {
		super();
	}

	public FIRFilterAO(FIRFilterAO firFilterAO) {
		super(firFilterAO);
		this.inputAttributes = firFilterAO.inputAttributes;
		this.outputAttributes = firFilterAO.outputAttributes;
		this.coefficients = firFilterAO.coefficients;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FIRFilterAO(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(
				new FIRFilter(null, outputAttributes.stream().toArray(String[]::new), null).getOutputAttributes(),
				getInputSchema(0));
		return schema;
	}

	@Parameter(name = AggregationFunctionParseOptionsHelper.INPUT_ATTRIBUTES, optional = true, type = StringParameter.class, isList = true)
	public void setInputAttributes(final List<String> attributes) {
		this.inputAttributes = attributes;
	}

	@Parameter(name = AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES, type = StringParameter.class, isList = true)
	public void setOutputAttributes(final List<String> attributes) {
		this.outputAttributes = attributes;
	}

	@Parameter(name = FIRFilterAggregationFunctionFactory.COEFFICIENTS, type = DoubleParameter.class, isList = true)
	public void setCoefficients(final List<Double> coefficients) {
		this.coefficients = coefficients;
	}

	public List<String> getInputAttributes() {
		return inputAttributes;
	}

	public List<String> getOutputAttributes() {
		return outputAttributes;
	}

	public List<Double> getCoefficients() {
		return coefficients;
	}

}
