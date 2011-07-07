package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless.IUnaryDetection;



public class StatelessDetectionSplitAO<T> extends AbstractDetectionSplitAO<T, IUnaryDetection<T>> {

	private static final long serialVersionUID = -2193273482190920976L;

	public StatelessDetectionSplitAO(){
		super();
	}
	
	public StatelessDetectionSplitAO(StatelessDetectionSplitAO<T> detectionAO) {
		super(detectionAO);		
	}

	@Override
	public StatelessDetectionSplitAO<T> clone() {
		return new StatelessDetectionSplitAO<T>(this);
	}
	
	
		
}
