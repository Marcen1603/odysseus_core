package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.List;

public interface ISecurityRestrictionPart {
	
	/** Merges the the roles of this SRP with the roles of another SRP
	 * @param srp Input to be merged with this SRP
	 */
	public void union(ISecurityRestrictionPart srp);

	public List<String> getRoles();

}
