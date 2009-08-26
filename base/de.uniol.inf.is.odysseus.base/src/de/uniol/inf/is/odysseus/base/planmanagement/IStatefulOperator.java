package de.uniol.inf.is.odysseus.base.planmanagement;

public interface IStatefulOperator {
	public void activateRequest(IOperatorControl operatorControl);
	
	public void deactivateRequest(IOperatorControl operatorControl);
	
	public boolean deactivateRequestedBy(IOperatorControl operatorControl);
	
	public boolean isActive();
}
