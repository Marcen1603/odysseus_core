package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.CoordinatesCalculator;

public class VectorquantizationOrientationPO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	//Attributes
//	private List<IPredicate<? super Tuple<M>>> predicates;
//	private CoordinatesCalculator lastValidPoint;
	private int numCluster;
	
	// Konstruktoren
	public VectorquantizationOrientationPO() {
		super();
	}
	
	public VectorquantizationOrientationPO(int numCluster) {
		this.numCluster = numCluster;
		System.out.println("debug vqPO: " + this.numCluster);
	}

	public VectorquantizationOrientationPO(
			VectorquantizationOrientationPO<M> splitPO) {
		super();
		// initPredicates(splitPO.predicates);
	}

	// Methoden
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		// NEW_ELEMENT: operator creates a new element (e.g. join)
		// Denke ich zumindest mal...weil wir ja eigene Werte erzeugen
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {
		// Process new input element on input port, a new created element can be
		// send to the next operator with the transfer-method
//		System.out.println("Vectorquantization: process_next");
//		System.out.println("Attribut 1: " + object.getAttribute(0));
//		System.out.println("Attribut 2: " + ((ArrayList<Double>)object.getAttribute(1)).get(0));
		
		
		@SuppressWarnings("unchecked")
		double orientationAngle = ((ArrayList<Double>) object.getAttribute(1)).get(0);
		
		

//		System.out.println("getMetadata():" + object.getMetadata());
//		System.out.println("getStart(): " + object.getMetadata().getStart());
//		System.out.println("getEnd(): " + object.getMetadata().getEnd());

		CoordinatesCalculator calc = new CoordinatesCalculator();
		
		int targetCluster = calc.determineTargetCluster(orientationAngle, numCluster);
		System.out.println("Vectorquantization - " + numCluster + " cluster\n  Clustergroup: " + targetCluster + "\n");
		
//		if (lastValidPoint.isNewObservation(handLeftX, handLeftY)) {
//			double cluster = lastValidPoint.calculateCluster(handLeftX, handLeftY);
//			System.out.println("NEUES OBST: " + targetCluster);
//			lastValidPoint.setPoint(handLeftX, handLeftY);
			Tuple<M> transferObject = new Tuple<>(1, false);
			transferObject.append(targetCluster);
			transferObject.addAttributeValue(0, targetCluster);
			@SuppressWarnings("unchecked")
			M clonedMD = ((M) object.getMetadata().clone());
			transferObject.setMetadata(clonedMD);
			transfer(transferObject,port);
//		}

		// System.out.println(object);

		// for (int i=0;i<predicates.size();i++){
		// if (predicates.get(i).evaluate(object)) {
		// transfer(object,i);
		// return;
		// }
		// }
		// transfer(object,predicates.size());

		// String[] aa = object.toString().split("\\|");
		// for (int i = 0; i < aa.length; i++) {
		// System.out.println(aa[i]);
		// }

	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
