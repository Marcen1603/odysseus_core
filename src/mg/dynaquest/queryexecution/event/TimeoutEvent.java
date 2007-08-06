package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;


abstract public class TimeoutEvent extends InformationEvent {

	private static final long serialVersionUID = 4607482233477547708L;

	public TimeoutEvent(TriggeredPlanOperator source, String message) {
		super(source, "Timeout");
	}

}