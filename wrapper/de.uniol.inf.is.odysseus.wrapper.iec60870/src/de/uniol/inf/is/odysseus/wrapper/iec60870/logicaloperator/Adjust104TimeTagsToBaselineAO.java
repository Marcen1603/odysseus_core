package de.uniol.inf.is.odysseus.wrapper.iec60870.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

// TODO javaDoc
// TODO operatorDoc
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "Adjust104TimeTagsToBaseline", category = {
		LogicalOperatorCategory.APPLICATION }, doc = "")
public class Adjust104TimeTagsToBaselineAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -721510855629321746L;

	private int iosAttributePos;

	private long baseline;

	public Adjust104TimeTagsToBaselineAO() {
		super();
	}

	public Adjust104TimeTagsToBaselineAO(Adjust104TimeTagsToBaselineAO other) {
		super(other);
		iosAttributePos = other.iosAttributePos;
		baseline = other.baseline;
	}

	@Override
	public Adjust104TimeTagsToBaselineAO clone() {
		return new Adjust104TimeTagsToBaselineAO(this);
	}

	public int getIosAttributePos() {
		return iosAttributePos;
	}

	@Parameter(type = IntegerParameter.class, name = "IOsAttributePosition", optional = false, isList = false, doc = "The position of the attribute that contains the list of information objects.")
	public void setIosAttributePos(int iosAttributePos) {
		this.iosAttributePos = iosAttributePos;
	}

	public long getBaseline() {
		return baseline;
	}

	@Parameter(type = LongParameter.class, name = "Baseline", optional = false, isList = false, doc = "The timestamp the time tags shall be adjusted to.")
	public void setBaseline(long baseline) {
		this.baseline = baseline;
	}

}