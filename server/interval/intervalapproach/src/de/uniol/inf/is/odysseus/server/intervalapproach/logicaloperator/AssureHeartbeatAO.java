package de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
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
@LogicalOperator(minInputPorts = 0, maxInputPorts = 1, name = "AssureHeartbeat", category = { LogicalOperatorCategory.PROCESSING }, doc = "This operator assures that every n time elements there will be a heartbeat on "
		+ " the garantees, that no element (heartbeat or streamobject) is send, that is"
		+ " older than the last send hearbeat (i.e. the generated heartbeats are in order"
		+ " and indicate time progress). Heartbeats can be send periodically"
		+ " (sendAlwaysHeartbeats = true) or only if no other stream elements indicate"
		+ " time progess (e.g. in out of order scenarios) independent if a new "
		+ " element has been received or not. ")
public class AssureHeartbeatAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5715561730592890314L;

	private long realTimeDelay;
	private long applicationTimeDelay;
	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	private boolean sendAlwaysHeartbeat = false;
	private boolean allowOutOfOrder = false;
	private boolean startAtCurrentTime = false;
	private boolean startTimerAfterFirstElement = false;

	public AssureHeartbeatAO() {
	}

	public AssureHeartbeatAO(AssureHeartbeatAO other) {
		super(other);
		this.realTimeDelay = other.realTimeDelay;
		this.applicationTimeDelay = other.applicationTimeDelay;
		this.timeUnit = other.timeUnit;
		this.sendAlwaysHeartbeat = other.sendAlwaysHeartbeat;
		this.startAtCurrentTime = other.startAtCurrentTime;
		this.allowOutOfOrder = other.allowOutOfOrder;
		this.startTimerAfterFirstElement = other.startTimerAfterFirstElement;
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

	@Parameter(type = BooleanParameter.class, name = "AllowOutOfOrder", optional = true)
	public void setAllowOutOfOrder(boolean allowOutOfOrder) {
		this.allowOutOfOrder = allowOutOfOrder;
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
	public void setStartTimerAfterFirstElement(
			boolean startTimerAfterFirstElement) {
		this.startTimerAfterFirstElement = startTimerAfterFirstElement;
	}

	/**
	 * @return the startTimerAfterFirstElement
	 */
	public boolean isStartTimerAfterFirstElement() {
		return startTimerAfterFirstElement;
	}

	public boolean isAllowOutOfOrder() {
		return allowOutOfOrder;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (getInputSchema() != null) {
			return super.getOutputSchemaIntern(pos);
		} else {
			return new SDFSchema("_created", Tuple.class,
					new LinkedList<SDFAttribute>());
		}

	}

	@Override
	public AbstractLogicalOperator clone() {
		return new AssureHeartbeatAO(this);
	}

}
