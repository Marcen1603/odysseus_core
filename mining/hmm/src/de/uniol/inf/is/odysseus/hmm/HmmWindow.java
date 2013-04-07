package de.uniol.inf.is.odysseus.hmm;

import java.util.ArrayList;
import java.util.Iterator;


public class HmmWindow {
	
	public int numStates;
	ArrayList<ObservationAlphas> alphas;
	
	public HmmWindow(int numStates) {
		alphas = new ArrayList<ObservationAlphas>();
		this.numStates = numStates;
	}
	
	public void addAlphas(ObservationAlphas alphas) {
		this.alphas.add(alphas);
		initializeAlphas();
	}

	private void initializeAlphas() {
		
	}

	public void checkTimestamps(int timeWindow, int timestamp) {
		Iterator<ObservationAlphas> it = alphas.iterator();
		while(it.hasNext()) {
			ObservationAlphas obs = it.next();
			if((timestamp - obs.timestamp) > timeWindow) {
				it.remove();
			} else {
				break;
			}
		}
	}
}
