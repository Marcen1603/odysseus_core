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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.dsp.aggregation.FFT;

@LogicalOperator(name = "FFT", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.PROCESSING }, doc = "Divides a signal into its frequency components by using the Fast Fourier transform.")
public class FFTAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5633148709801986786L;
	private List<String> inputAttributes;
	private List<String> outputAttributes;
	private int windowSize;

	public FFTAO() {
		super();
	}

	public FFTAO(FFTAO fftAO) {
		super(fftAO);
		this.inputAttributes = fftAO.inputAttributes;
		this.outputAttributes = fftAO.outputAttributes;
		this.windowSize = fftAO.windowSize;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FFTAO(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(
				new FFT(null, outputAttributes.stream().toArray(String[]::new)).getOutputAttributes(),
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
