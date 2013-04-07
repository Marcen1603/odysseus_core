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
import de.uniol.inf.is.odysseus.hmm.Gesture;
import de.uniol.inf.is.odysseus.hmm.HMMPoint;
import de.uniol.inf.is.odysseus.hmm.HmmWindow;
import de.uniol.inf.is.odysseus.hmm.HmmObservationAlphaRow;
import de.uniol.inf.is.odysseus.hmm.HmmWindowGroup;

public class HmmPO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	private List<IPredicate<? super Tuple<M>>> predicates;
	private HMMPoint lastValidPoint;
	private HmmWindow hmmWindow;
	private ArrayList<Gesture> gesturelist = new ArrayList<Gesture>();
	private ArrayList<HmmWindowGroup> windowGroups= new ArrayList<HmmWindowGroup>();

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

		
		//1) Neue Gruppe anlegen, die AlphaReihen aufnimmt
		HmmWindowGroup newWindowGroup = new HmmWindowGroup();
		newWindowGroup.setTimestamp(Integer.parseInt(object.getMetadata().getStart().toString()));
		
		//Beobachtung kommt an
		//2) Durch Beobachtung f�r jede Geste eine neue Reihe anlegen
		for (int i = 0; i < gesturelist.size(); i++) {
			HmmObservationAlphaRow newAlphaRow = new HmmObservationAlphaRow(hmmWindow.getNumStates());
			//tuple<m> object �bergeben an newAlphaRow und mit Forward Algo, Init-Alphawerte berechnen
			
			newWindowGroup.addRow(newAlphaRow);
		}
		
		//3) Gruppe ins Window eintragen
		hmmWindow.addGroup(newWindowGroup);
		
		//4) Window-Eintr�ge (Gruppen) checken. Gruppen mit alten Timestamps rausl�schen.
		hmmWindow.sweapOldItems();
		
		
		
		
		double handLeftX = object.getAttribute(0);
		
		System.out.println("getMetadata():" + object.getMetadata());
		System.out.println("getStart(): " + object.getMetadata().getStart());
		System.out.println("getEnd(): " + object.getMetadata().getEnd());
		
//		HmmObservationAlphaRow newSet = new HmmObservationAlphaRow(hmmWindow.numStates);
//		int timestamp = Integer.parseInt(object.getMetadata().getStart().toString());
//		newSet.timestamp = timestamp;
//		hmmWindow.addAlphas(newSet);
//		
//		int timeWindow = 10000;
//		hmmWindow.checkTimestamps(timeWindow, timestamp);
		

	}
	
	protected void process_open() throws OpenFailedException {
		super.process_open();
		System.out.println("MUUUUUUUUUUUUUUUUUUUUUUUUUH macht die Katze");
		//groesse des Windows festlegen
		//TODO Parameter per Operator �bergeben
		hmmWindow = new HmmWindow(2);
		
		//Gestenobjekte erzeugen - zu jeder Geste existiert eine CSV-Datei mit Anz Zust�nde, �bergangstabelle A und B
		//TODO alle Dateien einlesen und Gestenobjekt initialisieren
		gesturelist.add(new Gesture("test1", null, null));
		gesturelist.add(new Gesture("test2", null, null));
	}

	@Override
	public HmmPO<M> clone() {
		return new HmmPO<M>(this);
	}

}
