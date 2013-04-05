package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;

public class VectorquantizationOrientationPO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	private List<IPredicate<? super Tuple<M>>> predicates;
	private HMMPoint lastValidPoint;

	// Konstruktoren
	public VectorquantizationOrientationPO() {
		super();
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
		System.out.println("HMM: process_next");
//		double handLeftX = object.getAttribute(18);
//		double handLeftY = object.getAttribute(19);
		double handLeftX = object.getAttribute(0);
		double handLeftY = object.getAttribute(1);
		if (lastValidPoint == null) {
			lastValidPoint = new HMMPoint(handLeftX, handLeftY);
			return;
		}
		if (lastValidPoint.isNewObservation(handLeftX, handLeftY)) {
			double cluster = lastValidPoint.calculateCluster(handLeftX, handLeftY);
			System.out.println("NEUES OBST: " + cluster);
			lastValidPoint.setPoint(handLeftX, handLeftY);
			Tuple<M> transferObject = new Tuple<>(1, false);
			transferObject.append(cluster);
			transferObject.addAttributeValue(0, cluster);
			M clonedMD = (M) object.getMetadata().clone();
			transferObject.setMetadata(clonedMD);
			transfer(transferObject,port);
		}

		// System.out.println(object);
		System.out.println(object.getAttribute(18));
		System.out.println(object.getAttribute(19));
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
	public VectorquantizationOrientationPO<M> clone() {
		return new VectorquantizationOrientationPO<M>(this);
	}

}
