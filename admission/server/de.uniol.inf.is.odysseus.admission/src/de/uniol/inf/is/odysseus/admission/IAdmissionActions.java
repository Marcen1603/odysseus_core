package de.uniol.inf.is.odysseus.admission;

public interface IAdmissionActions {

	public <T extends IAdmissionActionComponent> T getAdmissionActionComponent( Class<T> componentClass );
	
	public void addEvent( IAdmissionEvent event );
}
