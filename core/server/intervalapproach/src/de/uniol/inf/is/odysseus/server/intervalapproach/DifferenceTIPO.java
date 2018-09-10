package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.OverlapsPredicate;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class DifferenceTIPO<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractPipe<T, T>
		implements IStatefulOperator {

	private static final int LEFT = 0;
	private final ITimeIntervalSweepArea<T> leftSA;
	private final ITimeIntervalSweepArea<T> rightSA;

	//
	final ITransferArea<T, T> transferArea;

	@SuppressWarnings("unchecked")
	public DifferenceTIPO(DifferenceAO ao, ITimeIntervalSweepArea<T> leftArea, ITimeIntervalSweepArea<T> rightArea,
			ITransferArea<T, T> transferArea) {
		super();
		this.leftSA = leftArea;
		this.rightSA = rightArea;
		this.transferArea = transferArea;
		IPredicate<? super T> predicate = ComplexPredicateHelper.createAndPredicate(OverlapsPredicate.getInstance(),
				EqualsPredicate.getInstance());
		if (ao.getPredicate() != null) {
			predicate = ComplexPredicateHelper.createOrPredicate(predicate,
					ComplexPredicateHelper.createNotPredicate(ao.getPredicate()));
		}

		leftArea.setQueryPredicate(predicate);
		rightArea.setQueryPredicate(predicate);
		setOutputSchema(ao.getOutputSchema());
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transferArea.sendPunctuation(punctuation);
	}

	@Override
	public PointInTime getLatestEndTimestamp() {
		return transferArea.getWatermark();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() {
		transferArea.init(this, 2);
		leftSA.clear();
		rightSA.clear();
	}

	@Override
	protected void process_next(T object, int port) {

		if (port == LEFT) {
			processLeft(object);
		} else {
			processRight(object);
		}
	}
	

	// ---------------------------------------------------
	// internal processing methods
	// ---------------------------------------------------

	private synchronized void processLeft(T object) {
		rightSA.purgeElements(object, null);
		Iterator<T> matchingElements = rightSA.query(object, Order.LeftRight);
		ArrayList<ITimeInterval> intervalsRight = this.extractTimeIntervals(matchingElements);
		ArrayList<TimeInterval> remainingIntervalsLeft = TimeInterval.cutOutIntervals(object.getMetadata(),
				intervalsRight);

		if (!remainingIntervalsLeft.isEmpty()) {
			ArrayList<T> replacements = new ArrayList<>();
			this.projectElementToTimeIntervals(object, remainingIntervalsLeft, replacements);
			leftSA.insertAll(replacements);
		}
	}

	private synchronized void processRight(T object) {
		Iterator<T> output = leftSA.extractElementsBefore(object.getMetadata().getStart());

		while (output.hasNext()) {
			T toTransfer = output.next();
			transferArea.transfer(toTransfer);
		}
		
		Iterator<T> leftElements = leftSA.queryCopy(object, Order.RightLeft, true);

		ArrayList<T> replacements = new ArrayList<>();

		while (leftElements.hasNext()) {
			T currentLeftElem = leftElements.next();
			ArrayList<TimeInterval> intervals = TimeInterval.cutOutInterval(currentLeftElem.getMetadata(),
					object.getMetadata());

			if (!intervals.isEmpty()) {
				projectElementToTimeIntervals(currentLeftElem, intervals, replacements);
			}
		}

		if (!replacements.isEmpty()) {
			leftSA.insertAll(replacements);
		}
		rightSA.insert(object);

		transferArea.newElement(object, 0);
		transferArea.newElement(object, 1);
	}

	private ArrayList<ITimeInterval> extractTimeIntervals(Iterator<T> matchingElements) {
		ArrayList<ITimeInterval> intervals = new ArrayList<>();

		while (matchingElements.hasNext()) {
			T elem = matchingElements.next();
			intervals.add(elem.getMetadata());
		}

		return intervals;
	}

	@SuppressWarnings("unchecked")
	private void projectElementToTimeIntervals(T element, ArrayList<TimeInterval> intervals, ArrayList<T> result) {
		for (TimeInterval interval : intervals) {
			T copy = (T) element.clone();
			copy.getMetadata().setStart(interval.getStart());
			copy.getMetadata().setEnd(interval.getEnd());
			result.add(copy);
		}
	}
}
