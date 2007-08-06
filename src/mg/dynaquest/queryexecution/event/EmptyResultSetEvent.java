package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

public class EmptyResultSetEvent extends InformationEvent {
	private static final long serialVersionUID = 1943780261473947355L;

	public EmptyResultSetEvent(TriggeredPlanOperator source) {
		super(source, "Empty ResultSet");
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.EmptyResultSet;
	}
	
	
}