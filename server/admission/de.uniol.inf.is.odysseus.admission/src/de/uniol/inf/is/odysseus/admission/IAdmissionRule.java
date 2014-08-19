package de.uniol.inf.is.odysseus.admission;

import java.util.List;

public interface IAdmissionRule<T extends IAdmissionEvent> {

	public Class<T> getEventType();
	public AdmissionRuleGroup getRouleGroup();
	public int getPriority();
	
	public boolean isExecutable( T event, IAdmissionStatus status);
	public List<IAdmissionAction> execute( T event, IAdmissionStatus status);
	
}
