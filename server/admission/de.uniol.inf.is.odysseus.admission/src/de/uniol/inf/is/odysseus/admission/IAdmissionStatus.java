package de.uniol.inf.is.odysseus.admission;


public interface IAdmissionStatus {

	public <T extends IAdmissionStatusComponent> T getStatusComponent( Class<T> statusType );
	
}
