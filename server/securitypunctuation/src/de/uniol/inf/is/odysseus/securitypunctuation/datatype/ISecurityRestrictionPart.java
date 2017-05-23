package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.io.Serializable;
import java.util.List;

public interface ISecurityRestrictionPart extends Serializable{
	
	/** Merges the the roles of this SRP with the roles of another SRP
	 * @param srp Input to be merged with this SRP
	 */
	public void union(ISecurityRestrictionPart srp);

	public List<String> getRoles();

}
