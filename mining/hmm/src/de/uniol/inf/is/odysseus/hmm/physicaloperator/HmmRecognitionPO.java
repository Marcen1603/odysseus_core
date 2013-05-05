package de.uniol.inf.is.odysseus.hmm.physicaloperator;


import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.FileHandlerHMM;
import de.uniol.inf.is.odysseus.hmm.Gesture;
import de.uniol.inf.is.odysseus.hmm.HMM;
import de.uniol.inf.is.odysseus.hmm.HmmAlphaGroup;
import de.uniol.inf.is.odysseus.hmm.HmmAlphas;
import de.uniol.inf.is.odysseus.hmm.HmmWindow;

public class HmmRecognitionPO<M extends ITimeInterval> extends	AbstractPipe<Tuple<M>, Tuple<M>> {

//	private List<IPredicate<? super Tuple<M>>> predicates;
//	private HMMPoint lastValidPoint;
	private HmmWindow hmmWindow;
	private ArrayList<Gesture> gesturelist = new ArrayList<Gesture>();
//	private ArrayList<HmmWindowGroup> windowGroups= new ArrayList<HmmWindowGroup>();
//	private int timewindow = 10000;
//	private int numStates;
	private HMM hmm = new HMM();
	
	private String pathToConfigfiles = "G:/_christian/Dokumente/_Studium/sem07/odysseus_trunk/mining/hmm/src/de/uniol/inf/is/odysseus/hmm/gestures/";
//	private int currentTimestamp = 0; 
	
	// Konstruktoren
	public HmmRecognitionPO() {
		super();
//		System.out.println("-----------------HmmPO");
	}


	public HmmRecognitionPO(HmmRecognitionPO<M> splitPO) {
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
//		System.out.println("HMM: process_next");
		@SuppressWarnings("unchecked")
		int observation = ((ArrayList<Integer>) object.getAttribute(0)).get(0);

		//1) Window-Einträge (Gruppen) checken. Gruppen mit alten Timestamps rauslöschen.
		hmmWindow.sweapOldItems();
		
		//2) Neue Gruppe anlegen, die Alphas aufnimmt
		HmmAlphaGroup newAlphaGroup = new HmmAlphaGroup();
//		newAlphaGroup.setTimestamp(currentTimestamp);
		
		//Beobachtung kommt an
		//3) Durch Beobachtung für jede Geste eine neue Alphas anlegen
		for (int i = 0; i < gesturelist.size(); i++) {
			HmmAlphas newAlphas = new HmmAlphas(gesturelist.get(i).getNumStates());
			//tuple<m> object übergeben an newAlphaRow und mit Forward Algo, Init-Alphawerte berechnen
			hmm.forwardInit(gesturelist.get(i), newAlphas, observation);
			newAlphaGroup.addRow(newAlphas);
		}
		
		//4) Gruppe ins Window eintragen
		hmmWindow.addGroup(newAlphaGroup);
		
		//5) Calculate new alphas
		//Iterate through AlphaGroups
		int[] highestProbs = new int[hmmWindow.getAlphaGroups().size()];
		for (int i = 0; i < hmmWindow.getAlphaGroups().size()-1; i++) {
			
			//Iterate through Gestures/Alphas
			double[] probs = new double[gesturelist.size()];
			for (int j = 0; j < hmmWindow.getAlphaGroups().get(i).getAlphas().size(); j++) {
				//Fill array with probabilites
				probs[j] = hmm.forwardStream(gesturelist.get(j), hmmWindow.getAlphaGroups().get(i).getAlphas().get(j), observation);
//				System.out.println("Probs: " +  probs[j]);
			}
			
			//Determine gesture with greatest probability
			int gestureIndex = -1;
			for (int j = 0; j < probs.length; j++) {
				if(hmmWindow.getAlphaGroups().size()-i >= gesturelist.get(j).getNumMinObs()) {
					if(gestureIndex == -1) gestureIndex = j;
					else if (probs[j] > probs[gestureIndex]) {
						gestureIndex = j;
					}
				}
			}
			//Remember index of highest probability for this AlphaGroup
			highestProbs[i] = gestureIndex;
			//print 
			if(gestureIndex != -1) {
				System.err.println("AlphaGroup: " + i + " " + gesturelist.get(gestureIndex).getName() + ": " + probs[gestureIndex]);
			} else {
//				System.out.println("AlphaGroup: " + i + " nüx");
			}
//			System.out.println("--------------------------");
		}
		System.out.println("--------------------------\n");
		
		
//		double handLeftX = object.getAttribute(0);
		
//		System.out.println("getMetadata():" + object.getMetadata());
//		System.out.println("getStart(): " + object.getMetadata().getStart());
//		System.out.println("getEnd(): " + object.getMetadata().getEnd());
		
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
		gesturelist = FileHandlerHMM.loadHMMConfigFromFile(pathToConfigfiles);
		//Determine maxObsLength
		int numMaxObs = 0;
		for (int i = 0; i < gesturelist.size(); i++) {
			if(gesturelist.get(i).getNumMaxObs() > numMaxObs) numMaxObs = gesturelist.get(i).getNumMaxObs();
		}
//		System.out.println(gesturelist.get(1).getName() + ": ");
		hmm.printAMatrix(gesturelist.get(1));
		//Create HMMWindow
		hmmWindow = new HmmWindow(numMaxObs);
//		System.out.println("MUUUUUUUUUUUUUUUUUUUUUUUUUH macht die Katze");
//		double wkeit = 0;
//		hmm = new HMM();
//		//**
//		wkeit = hmm.productionProbability(hmm.forward(gesturelist.get(0), new int[] {4, 0, 12, 8, 5}));
//		System.out.println("wkeit: " + wkeit);
//		//**
//		//groesse des Windows festlegen
//		//TODO Parameter per Operator übergeben
//		hmmWindow = new HmmWindow(timewindow);
//		
//		//Gestenobjekte erzeugen - zu jeder Geste existiert eine CSV-Datei mit Anz Zustände, Übergangstabelle A und B
//		//TODO alle Dateien einlesen und Gestenobjekt initialisieren
	}

	@Override
	public HmmRecognitionPO<M> clone() {
		return new HmmRecognitionPO<M>(this);
	}

}
