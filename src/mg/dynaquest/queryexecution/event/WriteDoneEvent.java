package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.SimplePlanOperator;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;



/**
 * @author  Marco Grawunder
 */
public class WriteDoneEvent extends DoneEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7598385625776221492L;

    /**
	 * @uml.property  name="consumer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SimplePlanOperator consumer;

    /**
	 * @return  the consumer
	 * @uml.property  name="consumer"
	 */
    public synchronized SimplePlanOperator getConsumer() {
        return consumer;
    }

	public WriteDoneEvent(TriggeredPlanOperator source, SimplePlanOperator consumer) {
		super(source);
		this.consumer = consumer;
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.WriteDone;
	}
	
	
}