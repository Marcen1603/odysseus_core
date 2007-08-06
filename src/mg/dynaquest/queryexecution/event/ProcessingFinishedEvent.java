package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

public class ProcessingFinishedEvent extends InformationEvent {

	private static final long serialVersionUID = 5016350553599205936L;

	public ProcessingFinishedEvent(TriggeredPlanOperator source) {
		super(source, "Processing finished");
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.ProcessingFinished;
	}
	
	

}

