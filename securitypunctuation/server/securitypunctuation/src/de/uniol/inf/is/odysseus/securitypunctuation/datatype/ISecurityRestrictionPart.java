package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.io.Serializable;
import java.util.List;

public interface ISecurityRestrictionPart extends Serializable{
	
	//returns the roles contained in the SRP
	public List<String> getRoles();
	
	//unions the Roles of this SRP with the roles of another given SRP
	void unionRoles(ISecurityRestrictionPart srp);

}
