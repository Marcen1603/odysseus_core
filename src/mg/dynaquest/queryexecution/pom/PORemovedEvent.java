package mg.dynaquest.queryexecution.pom;

import mg.dynaquest.monitor.POMonitor;

/**
 * @author  Marco Grawunder
 */
public class PORemovedEvent extends POManagerEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -710746137837920445L;
	/**
	 * @uml.property  name="pom"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	POMonitor pom = null;

	public PORemovedEvent(Object source, POMonitor pom) {
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