package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "SessionWindow", category = {
		LogicalOperatorCategory.BASE }, doc = "A session window is a combination of heartbeat and predicate window with a specific configuration. A session ends when a heartbeat is received. Than, all stored elements will\r\n"
				+ "	// be transferred.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/SessionWindow", hidden = true)
public class SessionWindowAO extends UnaryLogicalOp implements IStatefulAO {

	private static final long serialVersionUID = 4220026291775066980L;

	// The delay for the heartbeat
	private long threshold;

	// see heartbeat operator
	private boolean startAtCurrentTime;

	public SessionWindowAO() {
		super();
	}

	public SessionWindowAO(SessionWindowAO windowAO) {
		super(windowAO);
		threshold = windowAO.threshold;
		startAtCurrentTime = windowAO.startAtCurrentTime;
	}

	@Parameter(type = LongParameter.class, name = "threshold", optional = false, doc = "The time to wait for a new element before closing the window.")
	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

	public long getThreshold() {
		return threshold;
	}

	@Parameter(type = BooleanParameter.class, name = "startAtCurrentTime", optional = false, doc = "Configuration of heartbeats. True = system time is used; False: time starts at 0.")
	public void setStartAtCurrentTime(boolean startAtCurrentTime) {
		this.startAtCurrentTime = startAtCurrentTime;
	}

	public boolean startAtCurrentTime() {
		return startAtCurrentTime;
	}

	@Override
	public SessionWindowAO clone() {
		return new SessionWindowAO(this);
	}

}
