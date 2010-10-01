package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

/**
 * Stores temporally punctuations for later processing, when they get out of
 * date.
 * 
 * @author jan steinke
 * 
 * @param <W>
 * @param <R>
 */
public class PunctuationStorage<W extends IMetaAttributeContainer<?>, R> {

	private List<List<PointInTime>> storage = new ArrayList<List<PointInTime>>();

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
	public void updatePunctuationData(W object) {

		if (!storage.isEmpty()) {
			ITimeInterval time = (ITimeInterval) object.getMetadata();
			PointInTime start = time.getStart();

			Iterator<PointInTime> it = storage.get(currentPort).iterator();

			PointInTime lastPoint = null;

			while (it.hasNext()) {
				PointInTime curPoint = it.next();
				if (start.before(curPoint)) {
					break;
				} else {
					lastPoint = curPoint;
					it.remove();
				}
			}

			if (lastPoint != null) {
				pipe.sendPunctuation(lastPoint);
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

	public int size() {
		int size = 0;
		for (List<PointInTime> each : storage) {
			size += each.size();
		}
		return size;
	}

}
