package de.uniol.inf.is.odysseus.dsp.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(name = "SAMPLESIGNAL", minInputPorts = 1, maxInputPorts = 1, doc = "todo", url = "todo", category = {
		LogicalOperatorCategory.PROCESSING })
public class SampleSignalAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -531044957735085573L;
	private int sampleInterval;
	private InterpolationPolicy interpolationPolicy;
	private boolean fillWithZeros;

	public SampleSignalAO() {
		super();
	}

	public SampleSignalAO(SampleSignalAO sampleSignalAO) {
		super(sampleSignalAO);
		this.interpolationPolicy = sampleSignalAO.interpolationPolicy;
		this.sampleInterval = sampleSignalAO.sampleInterval;
		this.fillWithZeros = sampleSignalAO.fillWithZeros;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SampleSignalAO(this);
	}

	@Parameter(type = IntegerParameter.class, name = "SAMPLE_INTERVAL", aliasname="INTERVAL", doc = "todo")
	public void setSampleInterval(int sampleInterval) {
		this.sampleInterval = sampleInterval;
	}

	public int getSampleInterval() {
		return sampleInterval;
	}

	@Parameter(type = EnumParameter.class, name = "INTERPOLATION_POLICY", aliasname="INTERPOLATION", doc = "todo")
	public void setInterpolationPolicy(InterpolationPolicy interpolationPolicy) {
		this.interpolationPolicy = interpolationPolicy;
	}

	public InterpolationPolicy getInterpolationPolicy() {
		return interpolationPolicy;
	}
	
	@Parameter(type = BooleanParameter.class, optional=true, name = "FILL_WITH_ZEROS", doc = "todo")
	public void setFillWithZeros(boolean fillWithZeros) {
		this.fillWithZeros = fillWithZeros;
	}

	public boolean getFillWithZeros() {
		return this.fillWithZeros;
	}
}
