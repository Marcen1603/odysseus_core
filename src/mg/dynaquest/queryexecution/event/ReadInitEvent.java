package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;


/**
 * @author  Marco Grawunder
 */
public class ReadInitEvent extends InitEvent {

	private static final long serialVersionUID = 2237514094093434157L;
	/**
	 * @uml.property  name="port"
	 */
	int port = -1;

	public ReadInitEvent(TriggeredPlanOperator source, int port) {
		super(source);
		this.port = port;
	}

	/**
	 * @return  the port
	 * @uml.property  name="port"
	 */
	public int getPort() {
		return port;
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.ReadInit;
	}
	
	
}