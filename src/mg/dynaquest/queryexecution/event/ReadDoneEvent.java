package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:10 $ Version: $Revision: 1.5 $ Log: $Log: ReadDoneEvent.java,v $ $Revision: 1.5 $ Log: Revision 1.5  2004/09/16 08:57:10  grawund $Revision: 1.5 $ Log: Quellcode durch Eclipse formatiert $Revision: 1.5 $ Log: Log: Revision 1.4 2004/09/16 08:53:52 grawund Log: *** empty log message *** Log: Log: Revision 1.3 2004/08/25 09:32:50 grawund Log: Port an dem das Event auftrat wurde im Konstruktor nicht initialisiert Log: Log: Revision 1.2 2002/01/31 16:15:24 grawund Log: Versionsinformationskopfzeilen eingefuegt Log:
 */

public class ReadDoneEvent extends DoneEvent {

	private static final long serialVersionUID = -3640547741045516386L;

	/**
	 * @uml.property  name="port"
	 */
	int port = -1;

	public ReadDoneEvent(TriggeredPlanOperator source, int port) {
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
		return POEventType.ReadDone;
	}
}