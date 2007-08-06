package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class NumberOfObjectsReadEvent extends InformationEvent {

	private static final long serialVersionUID = -4172759758514349067L;
	/**
	 * @uml.property  name="count"
	 */
	int count;

	public NumberOfObjectsReadEvent(TriggeredPlanOperator source, int count) {
		super(source, count + " Number of Objects read");
		this.count = count;
	}

	/**
	 * @return  the count
	 * @uml.property  name="count"
	 */
	public int getCount() {
		return count;
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.NumberOfObjectRead;
	}

	
}