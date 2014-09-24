package windscadaanwendung.views;

import java.util.Observer;

import windscadaanwendung.ca.FarmList;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

public class ObserverHandler {
	
	public static void addObserverToWKA(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			for (WKA w: wf.getWkas()) {
				w.addObserver(obs);
			}
		}
	}

}
