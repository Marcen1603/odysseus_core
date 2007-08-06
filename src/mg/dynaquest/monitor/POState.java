package mg.dynaquest.monitor;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:11 $ Version: $Revision: 1.5 $ Log: $Log: POState.java,v $ $Revision: 1.5 $ Log: Revision 1.5  2004/09/16 08:57:11  grawund $Revision: 1.5 $ Log: Quellcode durch Eclipse formatiert $Revision: 1.5 $ Log: Log: Revision 1.4 2004/08/25 08:54:31 grawund Log: POState ILLEGAL in ERROR umbenannt Log: Log: Revision 1.3 2004/08/25 08:44:54 grawund Log: Anpassungen an neuen Zustand finished Log: Log: Revision 1.2 2002/01/31 16:15:43 grawund Log: Versionsinformationskopfzeilen eingefuegt Log:
 */

public class POState {

	/**
	 * @uml.property  name="name"
	 */
	private final String name;

	//private static int nextOrdinal = 0;

	//private final int ordinal = nextOrdinal++;

	private POState(String name) {
		this.name = name;
	}

	// Fehlerzustand
	public static final POState ERROR = new POState("error");

	// mögliche Zustände
	public static final POState INACTIVE = new POState("inactive");

	public static final POState OPEN = new POState("open");

	public static final POState CLOSE = new POState("close");

	public static final POState INITIALIZED = new POState("initialized");

	public static final POState NEXT = new POState("next");

	public static final POState READ_BLOCKED = new POState("read_blocked");

	public static final POState WRITE_BLOCKED = new POState("write_blocked");

	public static final POState FINISHED = new POState("finished");

	public String toString() {
		return this.name;
	}

}