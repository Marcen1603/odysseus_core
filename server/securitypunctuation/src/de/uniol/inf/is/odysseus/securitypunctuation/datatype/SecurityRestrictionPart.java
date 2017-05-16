package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityRestrictionPart implements ISecurityRestrictionPart{
	private List<String> roles;
	// private static final Logger LOG =
	// LoggerFactory.getLogger(SecurityRestrictionPart.class);

	



	public SecurityRestrictionPart(String roles) {
		this.roles = new ArrayList<String>(Arrays.asList(roles.split(",")));
		//Collections.sort(this.roles);

	}

	public SecurityRestrictionPart(SecurityRestrictionPart srp) {
		this.roles = srp.getRoles();
	}

	public List<String> getRoles() {
		return this.roles;
	}
	
	// Merges two SRPs
	@Override
	public void union(ISecurityRestrictionPart srp) {
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
	

	}

	@Override
	public String toString() {
		String str = "Roles: [";
		for (String role : roles) {
			str += role;
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
		if (!inputOne.isEmpty() && inputTwo.isEmpty() || inputOne.isEmpty() && inputTwo.isEmpty()) {
			return false;

		} else if (inputTwo.containsAll(inputOne) && inputOne.containsAll(inputTwo)) {
			return true;
		} else
			return false;

	}
	

}
