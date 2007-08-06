package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

abstract public class InformationEvent extends POEvent {

	private static final long serialVersionUID = -3283310523977158376L;
	/**
	 * @uml.property  name="message"
	 */
	private String message;

	public InformationEvent(TriggeredPlanOperator source, String message) {
		super(source);
		this.message = message;
	}
	
	public String toString() {
		return super.toString() + message;
	}

}