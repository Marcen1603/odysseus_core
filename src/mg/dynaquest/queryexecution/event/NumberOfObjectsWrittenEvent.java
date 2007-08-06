package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class NumberOfObjectsWrittenEvent extends InformationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1882450555695951842L;
	/**
	 * @uml.property  name="count"
	 */
	int count;

	public NumberOfObjectsWrittenEvent(TriggeredPlanOperator source, int count) {
		super(source, count + " Number of Objekts written");
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
		return POEventType.NumberOfObjectsWritten;
	}

	

}