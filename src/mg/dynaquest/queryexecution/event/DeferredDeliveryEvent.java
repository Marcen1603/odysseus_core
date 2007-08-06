package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

public class DeferredDeliveryEvent extends InformationEvent {

	private static final long serialVersionUID = 8032542524676299212L;

	public DeferredDeliveryEvent(TriggeredPlanOperator source) {
		super(source, "Deferred Delivery");
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.DeferredDelivery;
	}
	
}