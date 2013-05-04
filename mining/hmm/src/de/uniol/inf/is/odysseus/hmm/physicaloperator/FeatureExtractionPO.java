package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.CoordinatesCalculator;

public class FeatureExtractionPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>>{

	//Attributes
	private double minDistBetweenPoints = 0.15;
	private CoordinatesCalculator lastValidCoordinate;
	private boolean isSkeletonTracked = false;
	
	
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
		
		//get X, Y, Z coordinates from incoming data stream
		double handLeftX = object.getAttribute(0);
		double handLeftY = object.getAttribute(1);
		@SuppressWarnings("unused")
		double handLeftZ = object.getAttribute(2);
		
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
		
		if(handLeftX != lastValidCoordinate.getPoint_x() &&
				handLeftY != lastValidCoordinate.getPoint_y()){
			this.isSkeletonTracked = true;
		} else {
			this.isSkeletonTracked = false;
		}
		
		//check the distance between 2 coordinates
		//if minimum distance is sufficient, the new point will be accepted
		if (lastValidCoordinate.isNewObservation(handLeftX, handLeftY, minDistBetweenPoints)) {
			//calculate the angle between the coordinate and a horizontal line
			//results in orientation e.g. of the hand
			double angle = lastValidCoordinate.calculateAngle(handLeftX, handLeftY);
			
			//Debug output
			System.out.println("Feature Extraction\n  Orientation: " + angle + " degree\n" +
					"  isSkeletonTracked: " + this.isSkeletonTracked+"\n");
			
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

}
