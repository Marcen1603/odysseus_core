package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

public class SlowDeliveryEvent extends InformationEvent {

	private static final long serialVersionUID = -2575036410341669343L;

	public SlowDeliveryEvent(TriggeredPlanOperator source) {
		super(source, "Slow Delivery");
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.SlowDelivery;
	}
}