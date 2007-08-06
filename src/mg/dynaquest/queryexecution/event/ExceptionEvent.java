package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;



/**
 * @author  Marco Grawunder
 */
public class ExceptionEvent extends POEvent {

	private static final long serialVersionUID = -3585842591469878214L;

	/**
	 * @uml.property  name="message"
	 */
	private String message;
	/**
	 * @uml.property  name="exception"
	 */
	private Exception exception;

	public ExceptionEvent(TriggeredPlanOperator source, String message) {
		super(source);
		this.message = message;
	}

	public ExceptionEvent(TriggeredPlanOperator source, Exception exception) {
		super(source);
		this.message = exception.getMessage();
		this.exception = exception;
	}

	public String toString() {
		return super.toString() + message;
	}

	/**
	 * @return  the message
	 * @uml.property  name="message"
	 */
	public String getMessage() {
		if (message == null){
			if (exception != null){
				return exception.getMessage();
			}
		}
		return message;
	}

	public Exception getExeception() {
		return exception;
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.Exeception;
	}
}