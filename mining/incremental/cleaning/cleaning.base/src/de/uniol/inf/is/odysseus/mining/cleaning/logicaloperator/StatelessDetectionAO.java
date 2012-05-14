package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless.IUnaryDetection;



public class StatelessDetectionAO<T> extends AbstractDetectionAO<T, IUnaryDetection<T>> {

	private static final long serialVersionUID = -2193273482190920976L;

	public StatelessDetectionAO(){
		super();
	}
	
	public StatelessDetectionAO(StatelessDetectionAO<T> detectionAO) {
		super(detectionAO);		
	}

	@Override
	public StatelessDetectionAO<T> clone() {
		return new StatelessDetectionAO<T>(this);
	}
	
	
		
}
