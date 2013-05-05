package de.uniol.inf.is.odysseus.hmm;

import java.util.ArrayList;


/**
 * @author Michael Möbes
 * @author Christian Pieper
 *
 * Contains all AlphaGroups for a given window size
 *
 */
public class HmmWindow {
	
//	private int numStates;
	private int windowSize;
	
	private ArrayList<HmmAlphaGroup> alphaGroups;
	
	public HmmWindow(int windowSize) {
		this.alphaGroups = new ArrayList<HmmAlphaGroup>();
		this.windowSize = windowSize;
	}

	
	public void addGroup(HmmAlphaGroup pGroup){
		alphaGroups.add(pGroup);
	}
	
//	public void sweapOldItems(int currentTimestamp){
//		Iterator<HmmAlphaGroup> it = groups.iterator();
//		while(it.hasNext()) {
//			HmmAlphaGroup hwg = it.next();
//			if((currentTimestamp - hwg.getTimestamp()) > windowSize) {
//				it.remove();
//			} else {
//				break;
//			}
//		}
//	}
	
	public void sweapOldItems(){
		if(alphaGroups.size() == windowSize) {
			alphaGroups.remove(0);
		}
	}
	
	public ArrayList<HmmAlphaGroup> getAlphaGroups() {
		return alphaGroups;
	}


	public void setAlphaGroups(ArrayList<HmmAlphaGroup> alphaGroups) {
		this.alphaGroups = alphaGroups;
	}
}
