package mg.dynaquest.queryexecution.pom;

import mg.dynaquest.monitor.POMonitor;

/**
 * @author  Marco Grawunder
 */
public class POInsertedEvent extends POManagerEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3816241203041739840L;
	/**
	 * @uml.property  name="pom"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	POMonitor pom = null;

	public POInsertedEvent(Object source, POMonitor pom) {
		super(source);
		this.pom = pom;
	}

	/**
	 * @return  the pom
	 * @uml.property  name="pom"
	 */
	public POMonitor getPom() {
		return pom;
	}
}