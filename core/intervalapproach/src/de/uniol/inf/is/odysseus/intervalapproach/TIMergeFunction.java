package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction;

/**
 * 
 * @author Jonas Jacobi, Andre Bolles
 *
 * @param <T> Typ der gemerged werden muss.
 */
public class TIMergeFunction implements
		IMetadataMergeFunction<ITimeInterval> {

	private static final TIMergeFunction instance = new TIMergeFunction();
	
	private TIMergeFunction() {
	}
	
	public TIMergeFunction(TIMergeFunction tiMergeFunction) {
		
	}

	@Override
	public ITimeInterval mergeMetadata(
			ITimeInterval left,
			ITimeInterval right) {
		return TimeInterval.intersection(left, right);
	}

	public static TIMergeFunction getInstance(){
		return instance;
	}
	
	@Override
	public void init(){
	}
	
	@Override
	public TIMergeFunction clone() {
		return new TIMergeFunction(this);
	}
	
}
