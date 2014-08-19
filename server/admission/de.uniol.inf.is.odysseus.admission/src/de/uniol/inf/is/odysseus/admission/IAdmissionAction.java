package de.uniol.inf.is.odysseus.admission;


public interface IAdmissionAction {

	public void execute(IAdmissionEvent baseEvent);
	public void revert(IAdmissionEvent baseEvent);
	
}
