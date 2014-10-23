package windscadaanwendung.views;

import java.util.Observer;

import windscadaanwendung.ca.FarmList;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

/**
 * This class handles the Observers for the historical Data of windFarms and WKAs. Call the static methods of this class if you want to add or remove a Observer
 * @author MarkMilster
 *
 */
public class ObserverHandler {

	/**
	 * Adds a Observer to every WKA loaded in the FarmList
	 * @param obs	The Observer to add
	 */
	public static void addObserverToWKA(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			for (WKA w: wf.getWkas()) {
				w.addObserver(obs);
			}
		}
	}

	/**
	 * Removes a Observer from every WKA in the FarmList
	 * 
	 * @param obs The Observer to remove
	 */
	public static void removeObserverFromWKA(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			for (WKA w: wf.getWkas()) {
				w.deleteObserver(obs);
			}
		}
	}
	
	/**
	 * Adds a Observer to every windFarm in the FarmList
	 * @param obs The Observer to add
	 */
	public static void addObserverToWindFarm(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			wf.addObserver(obs);
		}
	}
	
	/**
	 * Removes a Observer from every windFarm in the FarmList
	 * @param obs
	 */
	public static void removeObserverFromWindFarm(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			wf.deleteObserver(obs);
		}
	}

}
