package de.uniol.inf.is.odysseus.admission;


public interface IAdmissionAction {

	public void execute(IAdmissionEvent baseEvent, IAdmissionStatus status);
	public void revert(IAdmissionEvent baseEvent, IAdmissionStatus status);
	
}
