package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.FileHandlerHMM;
import de.uniol.inf.is.odysseus.hmm.Gesture;
import de.uniol.inf.is.odysseus.hmm.HMM;

public class HmmTrainingPO<M extends ITimeInterval> extends	AbstractPipe<Tuple<M>, Tuple<M>> {
	// Attributes
	private String gestureName;
	static boolean tracked = true;
	public static long trackingTime = 0;
	boolean isTrainingStartet = false;

	boolean isStartMessageNeeded = true;
	Timer startTimer = new Timer();
	Timer timer = new Timer();
	long lastInputTime;
	ArrayList<Integer> observation = new ArrayList<Integer>();
	private String pathToConfigfiles = "G:/_christian/Dokumente/_Studium/sem07/odysseus_trunk/mining/hmm/src/de/uniol/inf/is/odysseus/hmm/gestures/";
	private final String pathToTrainingData = "G:/_christian/Dokumente/_Studium/sem07/odysseus_trunk/mining/hmm/src/de/uniol/inf/is/odysseus/hmm/gestures/trainingdata/";

	public HmmTrainingPO(String gestureName) {
		this.gestureName = gestureName;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<M> object, int port) {

		// Set time of input. If delay becomes to great, the training is successfully finished.
		lastInputTime = System.currentTimeMillis();
		observation.add(((ArrayList<Integer>) object.getAttribute(0)).get(0).intValue());
	}

	@Override
	protected void process_open() {
		// initialize default values
		tracked = true;
		trackingTime = 0;
		isTrainingStartet = false;

		isStartMessageNeeded = true;
		startTimer = new Timer();
		timer = new Timer();

//		System.err.println("GESTURE: " + gestureName);
//		System.out.println("Trackingtime: " + trackingTime);
//		System.out.println("isTracked: " + tracked);
		
		//initialize the training start timer
		//it counts from 3 to 0, if skeleton is tracked, and prints the countdown.
		initializeTrainingStartTimer();
		
		
		//Set timer that checks whether there still are inputs after having started training. 
		//If training is ongoing but no new inputs arrive training state will change to finished
		initializeTrainingFinishTimer();		
	}

	
	/**
	 * Initializes the training start timer.
	 * It'll print a countdown from 3 to 0 to the console, if skeleton is tracked.
	 */
	private void initializeTrainingStartTimer() {
		
		startTimer.schedule(new TimerTask() {
			@Override
			public void run() {
//				 System.out.println("trackingtime: " + trackingTime);
//				 System.out.println("tracked: " + tracked);
				if (tracked && (trackingTime > 0)) {
					if ((System.currentTimeMillis() - trackingTime) > 3000) {
						isTrainingStartet = true;
						startTimer.cancel();
					} else if ((System.currentTimeMillis() - trackingTime) > 2000) {
						System.out.println("+--- 1");
					} else if ((System.currentTimeMillis() - trackingTime) > 1000) {
						System.out.println("+--- 2");
					} else {
						System.out.println("+--- 3");
					}
				}
			}
		}, 0, 1000);
	}
	
	
	/**
	 * Initializes a timer that checks whether there still are inputs after having started training. 
	 * If training is ongoing but no new inputs arrive training state will change to finished.
	 */
	private void initializeTrainingFinishTimer() {
		final String path = pathToTrainingData + gestureName + "Training.csv";

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.err.println("isTrainingStartet: " + isTrainingStartet);
				if (isTrainingStartet) {

					if (isStartMessageNeeded) {
						System.err.println("\n+-------------");
						System.err.println("+--- START TRAINING");
						System.err.println("+-------------\n");
						
						//delete all unnecessary observation overhead
						observation.clear();
						FeatureExtractionPO.setCurrentCoordsAsLastValidPoint();
						isStartMessageNeeded = false;
					}

					// Continue training if tracked
					if (HmmTrainingPO.tracked) {
						// Don't finish training if there are no observations
						if (!observation.isEmpty()) { // <--- ausrufezeihen und
														// so
							//====== Training successfully finished ========================
							if (System.currentTimeMillis() - lastInputTime > 3000) {
								try {
									System.out.println(path);
									timer.cancel();
									FileHandlerHMM.appendNewTrainingData(observation, path);
									ArrayList<int[]> obsSequences = FileHandlerHMM.loadTrainingData(path);
									createHMMConfigFromUpdatedTrainingData(obsSequences);
									process_open();
									
								} catch (IOException e) {
									System.err.println("Failed to create new config file");
									e.printStackTrace();
								}

							}
						}
						
					// If untracked, clear observations to restart
					} else {
						observation.clear();
					}
				}
			}

		}, 1000, 3000);
	}
	

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	protected void process_close() {
		// startTimer.cancel();
		System.out.println("process_close()");
		tracked = false;
		trackingTime = 0;
		isTrainingStartet = false;
		isStartMessageNeeded = true;
		timer.cancel();
	}

	private void createHMMConfigFromUpdatedTrainingData(ArrayList<int[]> obsSequences) {
		// Calculate floored mean number of observations
		int numObs = 0;
		for (int i = 0; i < obsSequences.size(); i++) {
			for (int j = 0; j < obsSequences.get(i).length; j++) {
				numObs++;
			}
		}

		@SuppressWarnings("unused")
		int numStates = numObs / obsSequences.size();
		int observationLength = numObs / obsSequences.size();
		// Determine min and max observation length
		int numMinObs = 0;
		int numMaxObs = 0;
		for (int i = 0; i < obsSequences.size(); i++) {
			if (numMinObs == 0)
				numMinObs = obsSequences.get(i).length;
			if (obsSequences.get(i).length < numMinObs)
				numMinObs = obsSequences.get(i).length;
			if (obsSequences.get(i).length > numMaxObs)
				numMaxObs = obsSequences.get(i).length;
		}

		
		HMM hmm = new HMM();
		
		//creates an HMM config to save it persistent
		//at the moment the state amount of the created HMM is equal to the minimum observation length,
		//otherwise there are computation errors possible, in case of to short observation sequences.
		
//		Gesture gesture = new Gesture(numStates, HMM.observationLength,	numMinObs, numMaxObs, observationLength);
		Gesture gesture = new Gesture(numMinObs, HMM.observationLength,	numMinObs, numMaxObs, observationLength);
		gesture.setName(gestureName);
		System.out.println("createHMM");
		
		// hmm.printAMatrix(gesture);
		// convert to int[][]
		int[][] trainingData = new int[obsSequences.size()][];
		for (int i = 0; i < trainingData.length; i++) {
			trainingData[i] = obsSequences.get(i);
		}
		for (int i = 0; i < 20; i++) {
			hmm.training(trainingData, gesture);
		}

		// save new gesture to file
		FileHandlerHMM.saveHMMConfigToFile(gesture, pathToConfigfiles);
	}

}
