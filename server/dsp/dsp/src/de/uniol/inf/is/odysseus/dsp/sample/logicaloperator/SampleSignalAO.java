package de.uniol.inf.is.odysseus.dsp.sample.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(name = "SAMPLESIGNAL", minInputPorts = 1, maxInputPorts = 1, doc = "todo", url = "todo", category = {
		LogicalOperatorCategory.PROCESSING })
public class SampleSignalAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -531044957735085573L;
	private int sampleInterval;
	private InterpolationPolicy interpolationPolicy;

	public SampleSignalAO() {
		super();
	}

	public SampleSignalAO(SampleSignalAO sampleSignalAO) {
		super(sampleSignalAO);
		this.interpolationPolicy = sampleSignalAO.interpolationPolicy;
		this.sampleInterval = sampleSignalAO.sampleInterval;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SampleSignalAO(this);
	}

	@Parameter(type = IntegerParameter.class, name = "sampleinterval", doc = "todo")
	public void setSampleInterval(int sampleInterval) {
		this.sampleInterval = sampleInterval;
	}

	public int getSampleInterval() {
		return sampleInterval;
	}

	@Parameter(type = EnumParameter.class, name = "interpolationpolicy", doc = "todo")
	public void setInterpolationPolicy(InterpolationPolicy interpolationPolicy) {
		this.interpolationPolicy = interpolationPolicy;
	}

	public InterpolationPolicy getInterpolationPolicy() {
		return interpolationPolicy;
	}

}
