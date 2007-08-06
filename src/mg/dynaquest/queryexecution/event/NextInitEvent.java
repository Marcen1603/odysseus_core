package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;


public class NextInitEvent extends InitEvent {


	private static final long serialVersionUID = -8702360867291463935L;

	public NextInitEvent(TriggeredPlanOperator source) {
		super(source);
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.NextInit;
	}
	
	
}