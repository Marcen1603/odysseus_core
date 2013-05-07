package de.uniol.inf.is.odysseus.core.usermanagement;

import java.io.Serializable;
import java.security.Principal;

public interface ITenant extends IAbstractEntity, Principal, Comparable<ITenant>, Serializable{
	
	@Override
	String getName();
	void setName(String name);

}
