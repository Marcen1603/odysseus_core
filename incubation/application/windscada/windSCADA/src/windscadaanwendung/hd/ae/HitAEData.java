package windscadaanwendung.hd.ae;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class HitAEData extends Observable {
	
	private static List<AEEntry> entryList = new ArrayList<AEEntry>();
	private static List<AEObserver> observers = new ArrayList<AEObserver>();
	
	public static void addAEObserver(AEObserver obs) {
		observers.add(obs);
	}
	
	public static void removeAEListener(Object obs) {
		System.out.println(observers.remove(obs));
	}

	/**
	 * @return the entryList
	 */
	public static List<AEEntry> getEntryList() {
		return entryList;
	}

	/**
	 * @param entryList the entryList to set
	 */
	public static void setEntryList(List<AEEntry> entryList) {
		HitAEData.entryList = entryList;
	}
	
	public static void addAEEntry(AEEntry aeEntry) {
		entryList.add(aeEntry);
		notifyAEObservers(aeEntry);
	}
	
	public static void clearEntryList() {
		entryList.clear();
		notifyAEObservers(null);
	}
	
	private static void notifyAEObservers(AEEntry aeEntry) {
		for (AEObserver obs: observers) {
			obs.onChangedData(aeEntry);
		}
	}

}
