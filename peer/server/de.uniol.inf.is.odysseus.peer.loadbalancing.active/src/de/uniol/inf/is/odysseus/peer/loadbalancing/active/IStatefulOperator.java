package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.io.Serializable;

public interface IStatefulOperator {
	public Serializable getStatus();
	public void setStatus(Serializable status);
	
}
