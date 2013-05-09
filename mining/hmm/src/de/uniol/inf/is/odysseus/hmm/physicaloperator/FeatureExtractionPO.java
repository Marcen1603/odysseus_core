package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.CoordinatesCalculator;

public class FeatureExtractionPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>>{

	//Attributes
	private double minDistBetweenPoints = 0.15;
	static private CoordinatesCalculator lastValidCoordinate;
//	private boolean isSkeletonTracked = false;
	static double handLeftX=0;
	static double handLeftY=0;
	
	
	//Constructors
	public FeatureExtractionPO() {
		super();
	}

	public FeatureExtractionPO(FeatureExtractionPO<M> splitPO) {
		super();
		// initPredicates(splitPO.predicates);
	}
	
	//Methods
	@Override
	public OutputMode getOutputMode() {
		//NEW_ELEMENT: operator creates a new element
		return OutputMode.NEW_ELEMENT;
	}

	
	@Override
	protected void process_next(Tuple<M> object, int port) {
//		System.out.println("FeatureExtraction: process_next");
		
		
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
//						System.err.println("zeit setzen");
						HmmTrainingPO.trackingTime = System.currentTimeMillis();
					}
				}
				HmmTrainingPO.tracked = true;
			}
		}
		
		
		//get X, Y, Z coordinates from incoming data stream
		handLeftX = object.getAttribute(18);
		handLeftY = object.getAttribute(19);
//		@SuppressWarnings("unused")
//		double handLeftZ = object.getAttribute(20);
		
//		System.out.println("Feature Extraction\n " +
//				"  handLeftX: " + handLeftX +
//				"  handLeftY: " + handLeftY + "\n");
				
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
			
			//Debug output
			System.out.println("Feature Extraction\n  Orientation: " + angle + " degree\n");
			
			//set this point as last valid one
			lastValidCoordinate.setPoint(handLeftX, handLeftY);
			
			
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
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new FeatureExtractionPO<M>(this);
	}

	public static void setCurrentCoordsAsLastValidPoint() {
		lastValidCoordinate = new CoordinatesCalculator(handLeftX, handLeftY);
	}
}
