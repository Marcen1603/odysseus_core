package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class ReadTimeoutEvent extends TimeoutEvent {
	private static final long serialVersionUID = 2169673296739645332L;
    /**
	 * @uml.property  name="port"
	 */
    int port = -1;
 

	public ReadTimeoutEvent(TriggeredPlanOperator source, int port) {
		super(source, "Read Timeout");
		this.port = port;
	}

	/**
	 * @return  the port
	 * @uml.property  name="port"
	 */
	public synchronized int getPort() {
        return port;
    }

	@Override
	public POEventType getPOEventType() {
		return POEventType.ReadTimeout;
	}
	
	

}