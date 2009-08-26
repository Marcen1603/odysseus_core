package de.uniol.inf.is.odysseus.base.planmanagement;

public interface IOperatorControl extends IOperatorOwner {
	public void start();
	
	public void stop();
	
	public boolean isStarted();
}
