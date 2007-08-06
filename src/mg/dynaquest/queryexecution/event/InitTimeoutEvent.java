package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

public class InitTimeoutEvent extends TimeoutEvent {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 5180008695859723825L;

	public InitTimeoutEvent(TriggeredPlanOperator source) {
		super(source, "Init Timeout");
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.InitTimeout;
	}
	
	

}