package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.CoordinatesCalculator;

/**
 * Feature Extraction is used to extract the most important information from
 * an input stream, e.g. calculating the orientation angle from given coordinates.
 * 
 * @author Michael Moebes, mmo
 * @author Christian Pieper, cpi
 * 
 */
public class FeatureExtractionPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	// Attributes
	private double minDistBetweenPoints ;
	static private CoordinatesCalculator lastValidCoordinate;
	// private boolean isSkeletonTracked = false;
	static double handLeftX;
	static double handLeftY;

	// Constructors
	public FeatureExtractionPO() {
		super();
	}

	public FeatureExtractionPO(FeatureExtractionPO<M> splitPO) {
		super();
		// initPredicates(splitPO.predicates);
	}

		
	// Methods
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 * 
	 * Sequence of execution:
	 * 1) Check whether skeleton is tracked
	 * 
	 * 2) Check whether the minimum distance is sufficient. 
	 *    If yes: calculate the orientation angle and give it to output stream.
	 * 
	 */
	@Override
	protected void process_next(Tuple<M> object, int port) {
		//1) check if skeleton is tracked.
		//in case it's tracked, the values are different in each iteration
		if(handLeftX != 0){
			if(handLeftX == (double) object.getAttribute(18)) {
				
				if(HmmTrainingPO.tracked == true) {
					System.out.println("--------- skeleton untracked --------");
				}
				HmmTrainingPO.tracked = false;
			} else {
				if(HmmTrainingPO.tracked == false){
					System.out.println("+++++++++++++++++++++++++ SKELETON TRACKED +++++++++++++++++++++++");
					if(HmmTrainingPO.trackingTime == 0){
						HmmTrainingPO.trackingTime = System.currentTimeMillis();
					}
				}
				HmmTrainingPO.tracked = true;
			}
		}
		
		//get X, Y, Z coordinates from incoming data stream
		handLeftX = object.getAttribute(18);
		handLeftY = object.getAttribute(19);
				
		//Debug Output
//		System.out.println("getMetadata():" + object.getMetadata());
//		System.out.println("getStart(): " + object.getMetadata().getStart());
//		System.out.println("getEnd(): " + object.getMetadata().getEnd());
		
		//set the first coordinate
		if (lastValidCoordinate == null) {
			lastValidCoordinate = new CoordinatesCalculator(handLeftX, handLeftY);
			
			//leave method
			return;
		}
		
		
		//check the distance between 2 coordinates
		//if minimum distance is sufficient, the new point will be accepted
		if (lastValidCoordinate.isNewObservation(handLeftX, handLeftY, minDistBetweenPoints)) {
			//calculate the angle between the coordinate and a horizontal line
			//results in orientation e.g. of the hand
			double angle = lastValidCoordinate.calculateAngle(handLeftX, handLeftY);
			
//			Debug output
			System.out.println("Feature Extraction\n  Orientation: " + angle + " degree\n");
			
			//set this point as last valid one
			lastValidCoordinate.setPoint(handLeftX, handLeftY);
			
//			System.err.println("getMetadata():" + object.getMetadata());
			
			//create a tupel including datatype and value
			Tuple<M> transferObject = new Tuple<>(2, false);
			//fist type
			transferObject.append("angle");
			transferObject.addAttributeValue(0, "angle");
			//second value
			transferObject.append(angle);
			transferObject.addAttributeValue(1, angle);
			
			@SuppressWarnings("unchecked")
			M clonedMD = ((M) object.getMetadata().clone());
			transferObject.setMetadata(clonedMD);
			transfer(transferObject,port);
		}

	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_open()
	 * 
	 * Set the default values
	 */
	@Override
	protected void process_open() throws OpenFailedException {
		minDistBetweenPoints = 0.15;
		handLeftX = 0;
		handLeftY = 0;
	}

	public static void setCurrentCoordsAsLastValidPoint() {
		lastValidCoordinate = new CoordinatesCalculator(handLeftX, handLeftY);
	}
}
