package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * This operator assures that every n time elements there will be a heartbeat on
 * the garantees, that no element (heartbeat or streamobject) is send, that is
 * older than the last send hearbeat (i.e. the generated heartbeats are in order
 * and indicate time progress). Heartbeats can be send periodically
 * (sendAlwaysHeartbeats = true) or only if no other stream elements indicate
 * time progess (e.g. in out of order scenarios) independent if a new element
 * has been received or not.
 *
 * @author Marco Grawunder
 *
 */
@LogicalOperator(minInputPorts = 0, maxInputPorts = 1, name = "Heartbeat", category = {
		LogicalOperatorCategory.PROCESSING }, doc = "This operator assures that every n time elements there will be a heartbeat on "
				+ " the garantees, that no element (heartbeat or streamobject) is send, that is"
				+ " older than the last send hearbeat (i.e. the generated heartbeats are in order"
				+ " and indicate time progress). Heartbeats can be send periodically"
				+ " (sendAlwaysHeartbeats = true) or only if no other stream elements indicate"
				+ " time progess (e.g. in out of order scenarios) independent if a new "
				+ " element has been received or not. ")
public class HeartbeatAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5715561730592890314L;

	private long realTimeDelay;
	private long applicationTimeDelay;
	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	private boolean sendAlwaysHeartbeat = false;
	private boolean startAtCurrentTime = false;
	private boolean startTimerAfterFirstElement = false;
	private boolean restartTimerForEveryInput = false;
	
	// Stops the generator after sending a heartbeat. The generator will be started again if a new element is received by this operator.
	private boolean sendOnlyOneHeartbeat = false;

	public HeartbeatAO() {
	}

	public HeartbeatAO(HeartbeatAO other) {
		super(other);
		this.realTimeDelay = other.realTimeDelay;
		this.applicationTimeDelay = other.applicationTimeDelay;
		this.timeUnit = other.timeUnit;
		this.sendAlwaysHeartbeat = other.sendAlwaysHeartbeat;
		this.startAtCurrentTime = other.startAtCurrentTime;
		this.startTimerAfterFirstElement = other.startTimerAfterFirstElement;
		this.sendAlwaysHeartbeat = other.sendAlwaysHeartbeat;
	}

	public long getRealTimeDelay() {
		return realTimeDelay;
	}

	@Parameter(type = LongParameter.class, name = "RealTimeDelay", optional = false)
	public void setRealTimeDelay(long realTimeDelay) {
		this.realTimeDelay = realTimeDelay;
	}

	public long getApplicationTimeDelay() {
		return applicationTimeDelay;
	}

	@Parameter(type = LongParameter.class, name = "ApplicationTimeDelay", optional = false)
	public void setApplicationTimeDelay(long applicationTimeDelay) {
		this.applicationTimeDelay = applicationTimeDelay;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	@Parameter(type = StringParameter.class, name = "TimeUnit", optional = true)
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = TimeUnit.valueOf(timeUnit);
	}

	public boolean isSendAlwaysHeartbeat() {
		return sendAlwaysHeartbeat;
	}

	@Parameter(type = BooleanParameter.class, name = "SendAlwaysHeartbeat", optional = true)
	public void setSendAlwaysHeartbeat(boolean sendAlwaysHeartbeat) {
		this.sendAlwaysHeartbeat = sendAlwaysHeartbeat;
	}

	@Parameter(type = BooleanParameter.class, name = "AllowOutOfOrderCreation", aliasname="AllowOutOfOrder", optional = true, doc="Deprecated. Value will be ignored!", deprecated=true)
	public void setAllowOutOfOrder(boolean allowOutOfOrder) {
	}

	@Parameter(type = BooleanParameter.class, name = "startAtCurrentTime", optional = true)
	public void setStartAtCurrentTime(boolean startAtCurrentTime) {
		this.startAtCurrentTime = startAtCurrentTime;
	}

	public boolean isStartAtCurrentTime() {
		return startAtCurrentTime;
	}

	/**
	 * @param startTimeAfterFirstElement
	 *            the startTimeAfterFirstElement to set
	 */
	@Parameter(type = BooleanParameter.class, name = "startTimerAfterFirstElement", optional = true)
	public void setStartTimerAfterFirstElement(boolean startTimerAfterFirstElement) {
		this.startTimerAfterFirstElement = startTimerAfterFirstElement;
	}
	
	/**
	 * @return the startTimerAfterFirstElement
	 */
	public boolean isStartTimerAfterFirstElement() {
		return startTimerAfterFirstElement;
	}
	
	public boolean isRestartTimerForEveryInput() {
		return restartTimerForEveryInput;
	}
	
	@Parameter(type = BooleanParameter.class, name = "restartTimerForEveryInput", optional = true)
	public void setRestartTimerForEveryInput(boolean restartTimerForEveryInput) {
		this.restartTimerForEveryInput = restartTimerForEveryInput;
	}

	public boolean isSendOnlyOneHeartbeat() {
		return sendOnlyOneHeartbeat;
	}

	@Parameter(type = BooleanParameter.class, name = "sendOnlyOneHeartbeat", optional = true, doc = "Stops the generator after sending a heartbeat. The generator will be started again if a new element is received by this operator.")
	public void setSendOnlyOneHeartbeat(boolean sendOnlyOneHeartbeat) {
		this.sendOnlyOneHeartbeat = sendOnlyOneHeartbeat;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new HeartbeatAO(this);
	}

	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.NONE;
	}



}
