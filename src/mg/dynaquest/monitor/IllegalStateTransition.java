package mg.dynaquest.monitor;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:11 $ Version: $Revision: 1.4 $ Log: $Log: IllegalStateTransition.java,v $ $Revision: 1.4 $ Log: Revision 1.4  2004/09/16 08:57:11  grawund $Revision: 1.4 $ Log: Quellcode durch Eclipse formatiert $Revision: 1.4 $ Log: Log: Revision 1.3 2004/09/16 08:53:53 grawund Log: *** empty log message *** Log: Log: Revision 1.2 2002/01/31 16:15:45 grawund Log: Versionsinformationskopfzeilen eingefuegt Log:
 */

public class IllegalStateTransition extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3173563883433869733L;
	// private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="lastState"
	 * @uml.associationEnd  
	 */
	private POState lastState = null;

	public IllegalStateTransition(String text) {
		super(text);
	}

	/**
	 * @param lastState  the lastState to set
	 * @uml.property  name="lastState"
	 */
	public void setLastState(POState state) {
		this.lastState = state;
	}

	/**
	 * @return  the lastState
	 * @uml.property  name="lastState"
	 */
	public POState getLastState() {
		return this.lastState;
	}
}