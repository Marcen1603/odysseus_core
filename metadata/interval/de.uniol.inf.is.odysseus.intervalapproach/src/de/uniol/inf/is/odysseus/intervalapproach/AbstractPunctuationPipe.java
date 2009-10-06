package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

/**
 * Provides basic functionality for interval based operators to handle data
 * streams in presence of punctuations.
 * 
 * @author jan
 * 
 * @param <W>
 * @param <R>
 */
public abstract class AbstractPunctuationPipe<W extends IMetaAttributeContainer<?>, R>
		extends AbstractPipe<R, W> {

	final protected List<List<PointInTime>> punctuationStorage = new ArrayList<List<PointInTime>>();
	protected int currentPort = 0;

	/**
	 * Tests if the given input object matches with a number of stored
	 * punctuations and is used to handle punctuations when they get out of date
	 * (cleaning the punctuationStorage, send punctuations, ...).
	 * 
	 * @param object
	 *            input data with a timestamp
	 */
	protected void updatePunctuationData(W object) {
		if (punctuationStorage.size() > 0) {
			ITimeInterval time = (ITimeInterval) object.getMetadata();
			PointInTime start = time.getStart();
			List<PointInTime> toDelete = new ArrayList<PointInTime>();

			for (PointInTime each : punctuationStorage.get(currentPort)) {
				if (start.afterOrEquals(each)) {
					sendPunctuation(each);
					cleanInternalStates(each, object);
					toDelete.add(each);
				}
			}

			for (PointInTime each : toDelete) {
				punctuationStorage.remove(each);
			}
		}
	}

	/**
	 * 
	 * @param punctuation
	 * @param current
	 */
	abstract protected void cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current);

	@Override
	public void processPunctuation(PointInTime timestamp) {

		while (punctuationStorage.size() <= currentPort) {
			punctuationStorage.add(new ArrayList<PointInTime>());
		}

		if (!punctuationStorage.get(currentPort).contains(timestamp)) {
			punctuationStorage.get(currentPort).add(timestamp);
		}
	}

	@Override
	public void transfer(W object) {
		transfer(object, 0);
		updatePunctuationData(object);
	};

}
