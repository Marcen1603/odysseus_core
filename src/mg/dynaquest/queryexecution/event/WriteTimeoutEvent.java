package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class WriteTimeoutEvent extends TimeoutEvent {
	private static final long serialVersionUID = 65267652082541393L;
    /**
	 * @uml.property  name="consumer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private PlanOperator consumer;


	public WriteTimeoutEvent(TriggeredPlanOperator source, PlanOperator consumer) {
		super(source, "Write Timeout");
		this.consumer = consumer;
	}

	public PlanOperator getPort() {
        return consumer;
    }

	@Override
	public POEventType getPOEventType() {
		return POEventType.WriteTimeout;
	}
	
	

}