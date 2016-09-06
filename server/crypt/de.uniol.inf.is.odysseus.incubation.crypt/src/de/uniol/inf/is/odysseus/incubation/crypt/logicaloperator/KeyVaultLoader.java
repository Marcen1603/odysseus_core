/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

/**
 * @author MarkMilster
 *
 */
public interface KeyVaultLoader extends VaultLoader {
	
	public int getKeyID();
	public void setKeyID(int keyID);

}
