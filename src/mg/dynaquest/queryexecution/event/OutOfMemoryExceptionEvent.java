package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

public class OutOfMemoryExceptionEvent extends ExceptionEvent {

	private static final long serialVersionUID = -421589221337210410L;

	public OutOfMemoryExceptionEvent(TriggeredPlanOperator source) {
		super(source, "Out of Memory");
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.OutOfMemoryException;
	}
	
	

}