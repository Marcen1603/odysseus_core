package de.uniol.inf.is.odysseus.admission;


public interface IAdmissionRule<T extends IAdmissionEvent> {

	public Class<T> getEventType();
	public AdmissionRuleGroup getRouleGroup();
	public int getPriority();
	
	public boolean isExecutable( T event, IAdmissionStatus status);
	public void execute( T event, IAdmissionStatus status, IAdmissionActions actions);
	
}
