package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.SimplePlanOperator;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;


/**
 * @author  Marco Grawunder
 */
public class WriteInitEvent extends InitEvent {

	private static final long serialVersionUID = -2812270486300847910L;
    /**
	 * @uml.property  name="consumer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SimplePlanOperator consumer;

	public WriteInitEvent(TriggeredPlanOperator source, SimplePlanOperator consumer) {
		super(source);
		this.consumer = consumer;
	}

    /**
	 * @return  the consumer
	 * @uml.property  name="consumer"
	 */
    public synchronized SimplePlanOperator getConsumer() {
        return consumer;
    }

	@Override
	public POEventType getPOEventType() {
		return POEventType.WriteInit;
	}

    
	
}