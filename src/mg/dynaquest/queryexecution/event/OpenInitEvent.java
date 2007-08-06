package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;


public class OpenInitEvent extends InitEvent {

	private static final long serialVersionUID = 6636826594115038173L;

	public OpenInitEvent(TriggeredPlanOperator source) {
		super(source);
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.OpenInit;
	}

}