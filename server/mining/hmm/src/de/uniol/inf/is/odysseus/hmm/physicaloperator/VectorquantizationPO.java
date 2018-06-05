package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.hmm.CoordinatesCalculator;

/**
 * Process the incoming feature vector, from the Feature Extraction operator 
 * to determine the cluster id.
 * Distinguish autonomous the incoming data, e.g. orientation, velocity, coordinates, to determine
 * the correct method to work with. 
 * 
 * @author Michael Moebes, mmo
 * @author Christian Pieper, cpi
 *
 */
public class VectorquantizationPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	// Attributes
	private int numCluster;

	// Constructors
	public VectorquantizationPO() {
		super();
	}

	public VectorquantizationPO(int numCluster) {
		this.numCluster = numCluster;
		System.out.println("debug vqPO: " + this.numCluster);
	}

	public VectorquantizationPO(VectorquantizationPO<M> splitPO) {
		super();
		// initPredicates(splitPO.predicates);
	}

	
	// Methods
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		// NEW_ELEMENT: operator creates a new element (e.g. join)
		return OutputMode.NEW_ELEMENT;
	}

	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 * 
	 * Choose autonomous the correct method to determine the cluster id, by comparing the data type in attribute 1.
	 */
	@Override
	protected void process_next(Tuple<M> object, int port) {
		
		@SuppressWarnings("unchecked")
		String inputDatatype = ((ArrayList<String>) object.getAttribute(0)).get(0);
		@SuppressWarnings("unchecked")
		double inputValue = ((ArrayList<Double>) object.getAttribute(1)).get(0);
		
		System.out.println("getMetadata():" + object.getMetadata());
		System.out.println("getStart(): " + object.getMetadata().getStart());
		System.out.println("getEnd(): " + object.getMetadata().getEnd());

	
		//check the input type
		//in case it's "angle", then determine the cluster id by process the first if-clause
		if(inputDatatype.equals("angle")) {
			CoordinatesCalculator calc = new CoordinatesCalculator();
			
			int targetCluster = calc.determineTargetCluster(inputValue, numCluster);
			System.out.println("Vectorquantization - " + numCluster + " cluster\n  Clustergroup: " + targetCluster + "\n");
			
			Tuple<M> transferObject = new Tuple<>(1, false);
			transferObject.append(targetCluster);
			transferObject.addAttributeValue(0, targetCluster);
			@SuppressWarnings("unchecked")
			M clonedMD = ((M) object.getMetadata().clone());
			transferObject.setMetadata(clonedMD);
			transfer(transferObject,port);
			
		} else {
			//there might be other input types, e.g. velocity or coordinates 
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
}
