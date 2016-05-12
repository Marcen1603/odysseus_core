package de.uniol.inf.is.odysseus.core.usermanagement;

import java.io.Serializable;

public interface ITenant extends IAbstractEntity, Comparable<ITenant>, Serializable{
	
	String getName();
	void setName(String name);

}
