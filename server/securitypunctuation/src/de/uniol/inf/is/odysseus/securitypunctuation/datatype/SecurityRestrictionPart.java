package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecurityRestrictionPart {
List<String> roles;
SecurityRestrictionPart(){
	
}
//Merges two SRPs
public void mergeSRP(SecurityRestrictionPart srp){
	for (String role : srp.getRoles()) {
		if (!this.roles.contains(role))
			this.roles.add(role);
		}
}


public SecurityRestrictionPart(String roles){
	this.roles= Arrays.asList(roles.split(","));
	Collections.sort(this.roles);
}
public SecurityRestrictionPart(SecurityRestrictionPart srp) {
	this.roles=srp.getRoles();
}
public List<String> getRoles(){
	return this.roles;
}

}
