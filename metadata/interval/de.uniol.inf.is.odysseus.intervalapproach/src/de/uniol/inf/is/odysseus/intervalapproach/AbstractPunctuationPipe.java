package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

/**
 * Provides basic functionality for interval based operators to handle data
 * streams in presence of punctuations.
 * 
 * @author Jan Steinke, Jonas Jacobi
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

			Iterator<PointInTime> i = punctuationStorage.get(currentPort)
					.iterator();
			while (i.hasNext()) {
				PointInTime curPoint = i.next();
				if (start.afterOrEquals(curPoint)) {
					sendPunctuation(curPoint);
					cleanInternalStates(curPoint, object);
					i.remove();
				}
			}
		}
	}

	@Override
	//make sure the punctuation storage contains a list for every input port,
	//as the number of input ports may increase after a subscription
	public void subscribeTo(ISource<? extends R> source, int sinkPort,
			int sourcePort) {
		super.subscribeTo(source, sinkPort, sourcePort);
		for (int i = punctuationStorage.size(); i < getInputPortCount(); ++i) {
			punctuationStorage.add(new LinkedList<PointInTime>());
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
		//JJ: TODO das contains ist unter umstaenden relativ teuer.
		//wenn das oft aufgerufen werden muss, ist zu ueberlegen
		//statt einer liste einen baum zu verwenden (priority list).
		//die frage ist ausserdem, ob ein paar duplikate nicht weniger
		//kosten, als ein haeufiges contains(). bitte drueber nachdenken ;).
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
