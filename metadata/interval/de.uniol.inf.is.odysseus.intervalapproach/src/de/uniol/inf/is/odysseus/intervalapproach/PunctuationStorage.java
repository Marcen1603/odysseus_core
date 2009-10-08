package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class PunctuationStorage<W extends IMetaAttributeContainer<?>, R> {
	
	private List<List<PointInTime>> storage = new ArrayList<List<PointInTime>>();


	public List<List<PointInTime>> getStorage() {
		return storage;
	}

	private int currentPort = 0;
	
	private IPunctuationPipe<R,W> pipe;
	
	public PunctuationStorage(IPunctuationPipe<R,W> pipe) {
		this.pipe = pipe;
	}
	
	public int getCurrentPort() {
		return currentPort;
	}

	public void setCurrentPort(int currentPort) {
		this.currentPort = currentPort;
	}	
	
	/**
	 * Tests if the given input object matches with a number of stored
	 * punctuations and is used to handle punctuations when they get out of date
	 * (cleaning the punctuationStorage, send punctuations, ...).
	 * 
	 * @param object
	 *            input data with a timestamp
	 */
	protected void updatePunctuationData(W object) {
		if (storage.size() > 0) {
			ITimeInterval time = (ITimeInterval) object.getMetadata();
			PointInTime start = time.getStart();

			Iterator<PointInTime> i = storage.get(currentPort)
					.iterator();
			while (i.hasNext()) {
				PointInTime curPoint = i.next();
				if (start.afterOrEquals(curPoint)) {
					pipe.sendPunctuation(curPoint);
					pipe.cleanInternalStates(curPoint, object);
					i.remove();
				}
			}
		}
	}

	public void subscribePort(int inputPortCount) {
		for (int i = storage.size(); i < inputPortCount; ++i) {
			storage.add(new LinkedList<PointInTime>());
		}	
	}

	public void storePunctuation(PointInTime timestamp) {
		//JJ: TODO das contains ist unter umstaenden relativ teuer.
		//wenn das oft aufgerufen werden muss, ist zu ueberlegen
		//statt einer liste einen baum zu verwenden (priority list).
		//die frage ist ausserdem, ob ein paar duplikate nicht weniger
		//kosten, als ein haeufiges contains(). bitte drueber nachdenken ;).
		//JS: Okay, da hast du wohl recht mit dem contains. Duplikate waeren
		//allerdings nicht so gut, da man die Punctuations ja auch irgendwann
		//rausschreibt. Spaetestens da muss man ja mit ihnen umgehen.		
		if (!storage.get(currentPort).contains(timestamp)) {
			storage.get(currentPort).add(timestamp);
		}
		
	}	
	
}
