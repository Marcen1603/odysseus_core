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

	public static void removeObserverFromWKA(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			for (WKA w: wf.getWkas()) {
				w.deleteObserver(obs);
			}
		}
	}
	
	public static void addObserverToWindFarm(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			wf.addObserver(obs);
		}
	}
	
	public static void removeObserverFromWindFarm(Observer obs) {
		for(WindFarm wf: FarmList.getFarmList()) {
			wf.deleteObserver(obs);
		}
	}

}
