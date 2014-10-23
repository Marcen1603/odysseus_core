package windscadaanwendung.hd.ae;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds a List of AEEntrys and represents the historical data of
 * Warnings and Errors. Also it implements a specific type of observable for the
 * GUI which wants to listen to the event of insert or delete a AEEntry.
 * 
 * @author MarkMilster
 * 
 */
public class HitAEData {

	private static List<AEEntry> entryList = new ArrayList<AEEntry>();
	private static List<AEObserver> observers = new ArrayList<AEObserver>();

	/**
	 * adds a AEObserver to this static Observable
	 * 
	 * @param obs
	 */
	public static void addAEObserver(AEObserver obs) {
		observers.add(obs);
	}

	/**
	 * Removes a AEObserver from this Observable if it is registered here. You
	 * have to call this before a AEObserver will be disposed!
	 * 
	 * @param obs
	 *            AEObservable to remove
	 */
	public static void removeAEListener(Object obs) {
		observers.remove(obs);
	}

	/**
	 * Returns the whole List of AEEntrys
	 * 
	 * @return the entryList
	 */
	public static List<AEEntry> getEntryList() {
		return entryList;
	}

	/**
	 * Sets a whole new List of AEEntrys
	 * 
	 * @param entryList
	 *            the entryList to set
	 */
	public static void setEntryList(List<AEEntry> entryList) {
		HitAEData.entryList = entryList;
	}

	/**
	 * Adds one new AEEntry and informs the AEObservers
	 * 
	 * @param aeEntry
	 *            to add
	 */
	public static void addAEEntry(AEEntry aeEntry) {
		entryList.add(aeEntry);
		notifyAEObservers(aeEntry);
	}

	/**
	 * Removes all the AEEntrys and informs the observers with a sended null
	 */
	public static void clearEntryList() {
		entryList.clear();
		notifyAEObservers(null);
	}

	/**
	 * Method to inform the Observers about a change in a specific AEEntry
	 * 
	 * @param aeEntry
	 *            The AEEntry which is changed. Null if there are no more
	 *            AEEntrys, because the List was cleared
	 */
	private static void notifyAEObservers(AEEntry aeEntry) {
		for (AEObserver obs : observers) {
			obs.onChangedData(aeEntry);
		}
	}

}
