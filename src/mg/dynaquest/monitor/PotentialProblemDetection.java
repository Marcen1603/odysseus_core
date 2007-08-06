package mg.dynaquest.monitor;

import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.pom.POInsertedEvent;
import mg.dynaquest.queryexecution.pom.POManager;
import mg.dynaquest.queryexecution.pom.POManagerEvent;
import mg.dynaquest.queryexecution.pom.POManagerEventListener;
import mg.dynaquest.queryexecution.pom.PORemovedEvent;

/**
 * @author  Marco Grawunder
 */
public class PotentialProblemDetection implements POManagerEventListener,
		POMonitorEventListener {

	/**
	 * @uml.property  name="poManager"
	 * @uml.associationEnd  
	 */
	POManager poManager = null;

	// Es hat sich was bei den Planoperatoren in der POBox getan
	public void poManagerEventOccured(POManagerEvent poEvent) {
		if (poEvent instanceof POInsertedEvent) {
			// Neuer PO
			POMonitor pom = ((POInsertedEvent) poEvent).getPom();
			pom.addPOMonitorEventListener(this);
		}
		if (poEvent instanceof PORemovedEvent) {
			// Neuer PO
			POMonitor pom = ((PORemovedEvent) poEvent).getPom();
			pom.removePOMonitorEventListener(this);
		}

	}

	public void poMonitorEventOccured(POMonitorEvent ev) {
		//System.out.println("PotentialProblemDetection "+ev.getEvent()+"
		// "+ev.getSource()+" "+((POMonitor)ev.getSource()).getPlanOperator());
	}

	/**
	 * @param poManager  the poManager to set
	 * @uml.property  name="poManager"
	 */
	public void setPoManager(POManager poManager) {
		this.poManager = poManager;
		poManager.addPOManagerEventListener(this);
	}

	/**
	 * @return  the poManager
	 * @uml.property  name="poManager"
	 */
	public POManager getPoManager() {
		return poManager;
	}

	public static void main(String[] args) throws TimeoutException {
	}

}