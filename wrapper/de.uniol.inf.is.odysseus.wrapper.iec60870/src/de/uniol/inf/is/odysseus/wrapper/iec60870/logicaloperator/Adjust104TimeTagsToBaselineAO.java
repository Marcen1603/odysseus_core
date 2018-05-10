package de.uniol.inf.is.odysseus.wrapper.iec60870.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

// TODO javaDoc
// TODO operatorDoc
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "Adjust104TimeTagsToBaseline", category = {
		LogicalOperatorCategory.APPLICATION }, doc = "")
public class Adjust104TimeTagsToBaselineAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -721510855629321746L;

	private SDFAttribute iosAttribute;

	private long baseline;

	private double acceleration = 1;

	public Adjust104TimeTagsToBaselineAO() {
		super();
	}

	public Adjust104TimeTagsToBaselineAO(Adjust104TimeTagsToBaselineAO other) {
		super(other);
		iosAttribute = other.iosAttribute;
		baseline = other.baseline;
		acceleration = other.acceleration;
	}

	@Override
	public Adjust104TimeTagsToBaselineAO clone() {
		return new Adjust104TimeTagsToBaselineAO(this);
	}

	public SDFAttribute getIosAttribute() {
		return iosAttribute;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "IOsAttribute", optional = false, isList = false, doc = "The attribute that contains the list of information objects.")
	public void setIosAttribute(SDFAttribute iosAttribute) {
		this.iosAttribute = iosAttribute;
	}

	public long getBaseline() {
		return baseline;
	}

	@Parameter(type = LongParameter.class, name = "Baseline", optional = false, isList = false, doc = "The timestamp the time tags shall be adjusted to. Must be greater than or equal to 0.")
	public void setBaseline(long baseline) {
		this.baseline = baseline;
	}

	public double getAcceleration() {
		return acceleration;
	}

	@Parameter(type = DoubleParameter.class, name = "Acceleration", optional = true, isList = false, doc = "The acceleration: <1 increases the time shift between messages; >1 decreases the time shift between messages. Must be greater than 0. Default = 1")
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

}