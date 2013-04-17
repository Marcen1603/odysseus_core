package de.uniol.inf.is.odysseus.hmm.physicaloperator;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.Gesture;
import de.uniol.inf.is.odysseus.hmm.HMM;
import de.uniol.inf.is.odysseus.hmm.HmmObservationAlphaRow;
import de.uniol.inf.is.odysseus.hmm.HmmWindow;
import de.uniol.inf.is.odysseus.hmm.HmmWindowGroup;

public class HmmPO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

//	private List<IPredicate<? super Tuple<M>>> predicates;
//	private HMMPoint lastValidPoint;
	private HmmWindow hmmWindow;
	private ArrayList<Gesture> gesturelist = new ArrayList<Gesture>();
//	private ArrayList<HmmWindowGroup> windowGroups= new ArrayList<HmmWindowGroup>();
	private int timewindow = 10000;
//	private int numStates;
	@SuppressWarnings("rawtypes")
	private HMM hmm;

	// Konstruktoren
	public HmmPO() {
		super();
		loadHmmsFromCSV();
	}

	private void loadHmmsFromCSV() {
		double[] pi = null;
		double[][] a = null;
		double[][] b = null;
		int numStates = 0;
		int numObs = 0;
		String gestureName = "";
		
		String path = "gestures";
		File dir = new File(path);
		File[] fileList = dir.listFiles();
		for (File f : fileList) {
			try {
				gestureName = f.getName().substring(0, f.getName().length()-4);
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while((line = br.readLine()) != null) {
					if(line.charAt(0) == '#') {
						//Read metadata
						if(line.contains("Metadata")) {
							line = br.readLine();
							String[] lineSplit = line.split(",");
							numStates = Integer.parseInt(lineSplit[0].trim());
							numObs = Integer.parseInt(lineSplit[1].trim());
							//init arrays
							pi = new double[numStates];
							a = new double[numStates][numStates];
							b = new double[numStates][numObs];

						//Read Pi
						} else if(line.contains("Matrix Pi")) {
							line = br.readLine();
							String[] lineSplit = line.split(",");
							
							for (int i = 0; i < lineSplit.length; i++) {
								pi[i] = Double.parseDouble(lineSplit[i]);
							}
							
						//Read A
						} else if(line.contains("Matrix A")) {
							int i = 0;
							while((line = br.readLine()) != null && line.charAt(0) != '#') {
								String[] lineSplit = line.split(",");
								for(int j = 0; j < lineSplit.length; j++) {
									a[i][j] = Double.parseDouble(lineSplit[j]);
								}
								i++;
							}
							
						//Read B
						} else if(line.contains("Matrix B")) {
							int i = 0;
							while((line = br.readLine()) != null && line.charAt(0) != '#') {
								String[] lineSplit = line.split(",");
								for(int j = 0; j < lineSplit.length; j++) {
									b[i][j] = Double.parseDouble(lineSplit[j]);
								}
								i++;
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("BAD FILE");
			}
			
			gesturelist.add(new Gesture(gestureName, pi, a, b));
		}
		
	}

	public HmmPO(HmmPO<M> splitPO) {
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
		long currentTimestamp = Long.parseLong(object.getMetadata().getStart().toString());
		newWindowGroup.setTimestamp(currentTimestamp);
		
		//Beobachtung kommt an
		//2) Durch Beobachtung für jede Geste eine neue Reihe anlegen
		for (int i = 0; i < gesturelist.size(); i++) {
			HmmObservationAlphaRow newAlphaRow = new HmmObservationAlphaRow(gesturelist.get(i).getNumStates());
			//tuple<m> object übergeben an newAlphaRow und mit Forward Algo, Init-Alphawerte berechnen
			System.out.println(gesturelist.get(i));
			System.out.println(newAlphaRow);
			System.out.println(((Double) object.getAttribute(0)).intValue());
			int tmp = ((Double) object.getAttribute(0)).intValue();
			
			hmm.forwardInit(gesturelist.get(i), newAlphaRow, tmp);
			newWindowGroup.addRow(newAlphaRow);
		}
		
		//3) Gruppe ins Window eintragen
		hmmWindow.addGroup(newWindowGroup);
		
		//4) Window-Einträge (Gruppen) checken. Gruppen mit alten Timestamps rauslöschen.
		hmmWindow.sweapOldItems(currentTimestamp);
		
		
		
		
//		double handLeftX = object.getAttribute(0);
		
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
	
	@SuppressWarnings("rawtypes")
	protected void process_open() throws OpenFailedException {
		super.process_open();
		System.out.println("MUUUUUUUUUUUUUUUUUUUUUUUUUH macht die Katze");
		
		hmm = new HMM();
		//**
		hmm.forward(gesturelist.get(0), new int[] {4, 0, 12, 8});
		//**
		//groesse des Windows festlegen
		//TODO Parameter per Operator übergeben
		hmmWindow = new HmmWindow(timewindow);
		
		//Gestenobjekte erzeugen - zu jeder Geste existiert eine CSV-Datei mit Anz Zustände, Übergangstabelle A und B
		//TODO alle Dateien einlesen und Gestenobjekt initialisieren
	}

	@Override
	public HmmPO<M> clone() {
		return new HmmPO<M>(this);
	}

}
