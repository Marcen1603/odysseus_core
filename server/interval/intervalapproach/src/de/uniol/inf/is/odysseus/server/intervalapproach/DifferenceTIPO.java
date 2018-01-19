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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DifferenceTIPO<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractPipe<T, T>
		implements IStatefulOperator {

	private static final int LEFT = 0;
	private static final int RIGHT = 1;
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
		IPredicate<? super T> predicate = ComplexPredicateHelper.createAndPredicate(OverlapsPredicate.getInstance(), EqualsPredicate.getInstance());
        if (ao.getPredicate() != null) {
            predicate = ComplexPredicateHelper.createOrPredicate(predicate, ComplexPredicateHelper.createNotPredicate(ao.getPredicate()));
        }

        leftArea.setQueryPredicate(predicate);
        rightArea.setQueryPredicate(predicate);
        setOutputSchema(ao.getOutputSchema());
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public PointInTime getLatestEndTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T object, int port) {

		if (port == LEFT) {
			processLeft(object, port);
		} else {
			processRight(object, port);
		}
	}

	// ---------------------------------------------------
	// internal processing methods
	// ---------------------------------------------------

	private void processLeft(T object, int port) {
		rightSA.purgeElements(object, null);
		Iterator<T> matchingElements = rightSA.query(object, Order.LeftRight);
		ArrayList<ITimeInterval> intervalsRight = this.extractTimeIntervals(matchingElements);
		ArrayList<TimeInterval> remainingIntervalsLeft = TimeInterval.cutOutIntervals(object.getMetadata(), intervalsRight);
		
		if (remainingIntervalsLeft.isEmpty()){
			leftSA.insert(object);
		} else {
			leftSA.insertAll(this.projectElementToTimeIntervals(object, remainingIntervalsLeft));
		}

	}

	private void processRight(T object, int port) {
		// 1. neues element in rechte sweep area einf�gen
		// 2. neues element mit jedem element e aus linker sweeparea vergleichen
		// 2.a wenn sich ein element e mit dem neuen element zeitlich und
		// inhaltlich �berschneidet, muss das e aus der linken sweeparea
		// entfernt und ggf.
		// durch ein oder zwei neue Elemente ersetzt werden, die die
		// �berschneidungsfreien Zeitr�ume abdecken
		// 2.b Was tun, wenn sich die elemente nicht zeitlich und/oder
		// inhaltlich �berschneiden?
		// 3. alle element der linken sweep area in die transfer area schreiben,
		// deren endzeitstempel kleiner ist als der startzeitstempel des neuen
		// elements (zuerst diesen Schritt ausf�hren um unn�tige Vergleiche zu sparen)
	}
	
	private ArrayList<ITimeInterval> extractTimeIntervals(Iterator<T> matchingElements){
		ArrayList<ITimeInterval> intervals = new ArrayList<ITimeInterval>();
		
		while(matchingElements.hasNext()){
			T elem = matchingElements.next();
			intervals.add(elem.getMetadata());
		}
		
		return intervals;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<T> projectElementToTimeIntervals(T element, ArrayList<TimeInterval> intervals){
		ArrayList<T> projected = new ArrayList<T>();
		
		for (TimeInterval interval : intervals) {
			T copy = (T) element.clone();
			copy.setMetadata((K) element.getMetadata().clone());
			copy.getMetadata().setStart(interval.getStart());
			copy.getMetadata().setEnd(interval.getEnd());
			projected.add(copy);
		}
		
		return projected;
	}
}
