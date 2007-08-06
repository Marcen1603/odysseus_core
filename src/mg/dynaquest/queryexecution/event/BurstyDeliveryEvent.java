package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

public class BurstyDeliveryEvent extends InformationEvent {

	private static final long serialVersionUID = 2093302180524175602L;

	public BurstyDeliveryEvent(TriggeredPlanOperator source) {
		super(source, "Bursty Delivery");
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.BurstyDelivery;
	}
	
	
}