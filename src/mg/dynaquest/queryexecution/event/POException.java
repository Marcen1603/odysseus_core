package mg.dynaquest.queryexecution.event;

/**
 * Klasse dient zum Kapseln von unerwarteten Ereignissen im Laufe des
 * Ausfuerhungsprozesses. Unerwartete Ereignisse sind die Ereignisse, die ein
 * Einschreiten notwendig machen, d.h. local nicht behandelt werden können
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:10 $ Version:
 * $Revision: 1.5 $ Log: $Log: POException.java,v $
 * $Revision: 1.5 $ Log: Revision 1.5  2004/09/16 08:57:10  grawund
 * $Revision: 1.5 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.5 $ Log: Log: Revision 1.4 2004/09/16
 * 08:53:52 grawund Log: *** empty log message *** Log: Log: Revision 1.3
 * 2002/02/06 09:02:58 grawund Log: [no comments] Log: Log: Revision 1.2
 * 2002/01/31 16:14:07 grawund Log: Versionsinformationskopfzeilen eingefuegt
 * Log:
 */

public class POException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4684621340827678046L;
	//private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="ex"
	 */
	Exception ex = null;

	public POException() {
		super();
	}

	public POException(Exception e) {
		super(e.getMessage());
		this.ex = e;
	}

	public POException(String message) {
		super(message);
	}

	public Exception getException() {
		return ex;
	}
}