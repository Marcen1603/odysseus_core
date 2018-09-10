package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecurityRestrictionPart implements ISecurityRestrictionPart{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6650668868603118657L;
	private List<String> roles;


	public SecurityRestrictionPart(List<String> roles) {
		this.roles = roles;
		Collections.sort(this.roles);

	}



	public SecurityRestrictionPart(String roles) {
		this.roles = new ArrayList<String>(Arrays.asList(roles.split(",")));
		Collections.sort(this.roles);

	}

	public SecurityRestrictionPart(SecurityRestrictionPart srp) {
		this.roles = srp.getRoles();
		Collections.sort(this.roles);
	}

	public List<String> getRoles() {
		return this.roles;
	}
	
	// Merges two SRPs
	@Override
	public void unionRoles(ISecurityRestrictionPart srp) {
		if (this.roles.get(0).equals("*") || srp.getRoles().get(0).equals("")) {
			return;
		} else if (srp.getRoles().get(0).equals("*")) {
			this.roles.clear();
			this.roles.add("*");
			return;
		} else
			for (String roleInput : srp.getRoles()) {
				if (!this.roles.contains(roleInput)) {
					this.roles.add(roleInput);
				}
			}
		Collections.sort(this.roles);

	}

	@Override
	public String toString() {
		String str = "Roles: [";
		for (String role : roles) {
			
			str += role+",";
		}
		str += "]";
		return str;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityRestrictionPart other = (SecurityRestrictionPart) obj;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!compare(this.roles, other.getRoles()))
			return false;
		return true;
	}

	private boolean compare(List<String> inputOne, List<String> inputTwo) {
		if(inputOne.size()!=inputTwo.size()){
			return false;
		}else if(!inputOne.equals(inputTwo)){
			return false;
		}return true;
		
		
	}	

}
