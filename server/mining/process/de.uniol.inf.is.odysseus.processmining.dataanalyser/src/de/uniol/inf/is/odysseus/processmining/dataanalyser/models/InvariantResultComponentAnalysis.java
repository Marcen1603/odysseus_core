package de.uniol.inf.is.odysseus.processmining.dataanalyser.models;

public class InvariantResultComponentAnalysis implements IInvariantResult {
	boolean sciEqual;
	
	
	public boolean isSciEqual() {
		return sciEqual;
	}


	public void setSciEqual(boolean sciEqual) {
		this.sciEqual = sciEqual;
	}

	@Override
	public InvariantResultStrategyEnum getStrategy() {
		return !sciEqual ? InvariantResultStrategyEnum.UPDATE :InvariantResultStrategyEnum.DELAY;
	}


	@Override
	public void print() {
		System.out.println("PRINT NOT IMPLEMENTED");
	}
	
	

}
