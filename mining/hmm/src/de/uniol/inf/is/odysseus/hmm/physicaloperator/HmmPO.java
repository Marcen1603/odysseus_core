package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.hmm.HMMPoint;
import de.uniol.inf.is.odysseus.hmm.HmmWindow;
import de.uniol.inf.is.odysseus.hmm.ObservationAlphas;

public class HmmPO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	private List<IPredicate<? super Tuple<M>>> predicates;
	private HMMPoint lastValidPoint;
	private HmmWindow hmmWindow;

	// Konstruktoren
	public HmmPO() {
		super();
	}

	public HmmPO(
			HmmPO<M> splitPO) {
		super();
		// initPredicates(splitPO.predicates);
	}

	// Methoden
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		// NEW_ELEMENT: Wir bekommen eine diskrete Richtungsangabe und geben einen String zurueck.
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {
		// Process new input element on input port, a new created element can be
		// send to the next operator with the transfer-method
		System.out.println("HMM: process_next");

		double handLeftX = object.getAttribute(0);
		
		System.out.println("getMetadata():" + object.getMetadata());
		System.out.println("getStart(): " + object.getMetadata().getStart());
		System.out.println("getEnd(): " + object.getMetadata().getEnd());
		
		ObservationAlphas newSet = new ObservationAlphas(hmmWindow.numStates);
		int timestamp = Integer.parseInt(object.getMetadata().getStart().toString());
		newSet.timestamp = timestamp;
		hmmWindow.addAlphas(newSet);
		
		int timeWindow = 10000;
		hmmWindow.checkTimestamps(timeWindow, timestamp);
		

	}
	
	protected void process_open() throws OpenFailedException {
		super.process_open();
		System.out.println("MUUUUUUUUUUUUUUUUUUUUUUUUUH macht die Katze");
		hmmWindow = new HmmWindow(2);
	}

	@Override
	public HmmPO<M> clone() {
		return new HmmPO<M>(this);
	}

}
