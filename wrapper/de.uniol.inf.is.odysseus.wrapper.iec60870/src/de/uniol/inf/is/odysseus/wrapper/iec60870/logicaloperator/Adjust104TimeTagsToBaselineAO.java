package de.uniol.inf.is.odysseus.wrapper.iec60870.logicaloperator;

import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * Logical operator to do the following with 104 messages (see OJ104 library).
 * <br />
 * <br />
 * It changes all {@link ITimeTag}s in a list of {@link IInformationObject}.
 * This list must be a attribute of the input tuples. For the first time tag in
 * the first tuple, it sets the time tag to a given time stamp. For all other
 * time tags in the same list of information objects or in subsequent tuples, it
 * sets the time tag so that the original difference between the time tags is
 * preserved. <br />
 * <br />
 * Example: <br />
 * First and only original time tag (in ms) in the first tuple is 0. First and
 * only original time tag (in ms) in the second tuple is 2. The given time stamp
 * (baseline) is 1000. Then, the new time tag in the first tuple is 1000 and the
 * new time tag in the second tuple is 1002. <br />
 * <br />
 * Further it can delay the transmission of the results according to the
 * difference in the first time tags of the current tuple and the previous one.
 * With this behavior, the original delay of the messages can be preserved.
 * Another option is to accelerate the messages by using an acceleration factor.
 * It influences the new time tags as well as the delay. <br />
 * <br />
 * Example with acceleration: <br />
 * First and only original time tag (in ms) in the first tuple is 0. First and
 * only original time tag (in ms) in the second tuple is 2. The given time stamp
 * (baseline) is 1000 and the acceleration factor is 2.0. Then, the new time tag
 * in the first tuple is 1000 and the new time tag in the second tuple is 1001.
 * 
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "Adjust104TimeTagsToBaseline", category = {
		LogicalOperatorCategory.APPLICATION }, doc = "Change all time tags in a list of information objects. For the first time tag in the first tuple, "
				+ "it sets the time tag to a given baseline. For all other time tags in the same list of information objects or in subsequent tuples,"
				+ " it sets the time tag so that the original difference between the time tags is preserved.")
public class Adjust104TimeTagsToBaselineAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -721510855629321746L;

	private SDFAttribute iosAttribute;

	private long baseline;

	private double acceleration = 1;

	private boolean delay = false;

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

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "InformationObjects", optional = false, isList = false, doc = "The attribute that contains the list of information objects.")
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

	public boolean hasDelay() {
		return delay;
	}

	@Parameter(type = BooleanParameter.class, name = "Delay", optional = true, isList = false, doc = "True: Output gets delayed according to difference in current time tag and time tag of previous tuple.")
	public void setDelay(boolean delay) {
		this.delay = delay;
	}

}