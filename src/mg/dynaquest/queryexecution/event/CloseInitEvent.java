package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;


public class CloseInitEvent extends InitEvent {

	private static final long serialVersionUID = -9093599897460015303L;

	public CloseInitEvent(TriggeredPlanOperator source) {
		super(source);
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.CloseInit;
	}

}