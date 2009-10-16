package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

/**
 * Stores temporally punctuations for later processing, when they get out of date.
 * @author jan steinke
 *
 * @param <W>
 * @param <R>
 */
public class PunctuationStorage<W extends IMetaAttributeContainer<?>, R> {

	private List<List<PointInTime>> storage = new ArrayList<List<PointInTime>>();

	public List<List<PointInTime>> getStorage() {
		return storage;
	}

	private int currentPort = 0;

	private IPunctuationPipe<R, W> pipe;

	public PunctuationStorage(IPunctuationPipe<R, W> pipe) {
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

			Iterator<PointInTime> i = storage.get(currentPort).iterator();

			// Hiermit werden Duplikate von Punctuations beseitigt
			PointInTime lastPoint = null;

			while (i.hasNext()) {

				PointInTime curPoint = i.next();
				if (lastPoint == null || !curPoint.equals(lastPoint)) {
					if (start.afterOrEquals(curPoint)) {
						if (pipe.cleanInternalStates(curPoint, object)) {
							pipe.sendPunctuation(curPoint);
							i.remove();
						}
					}
				} else {
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
		storage.get(currentPort).add(timestamp);
	}

}
