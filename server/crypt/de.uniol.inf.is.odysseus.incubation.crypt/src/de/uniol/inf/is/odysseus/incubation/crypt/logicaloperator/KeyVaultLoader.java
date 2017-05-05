package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

/**
 * This interface provides functions to set the specifications to load a key out
 * of a vault. <br>
 * The vault and the loading has to be implemented somewhere else.
 * 
 * @author MarkMilster
 *
 */
public interface KeyVaultLoader extends VaultLoader {

	/**
	 * Returns the id of the key, which will be loaded out of the vault.
	 * 
	 * @return the id of the key
	 */
	public int getKeyID();

	/**
	 * Sets the id of the key, which will be loaded out of the vault.
	 * 
	 * @param keyID
	 *            The id of the key
	 */
	public void setKeyID(int keyID);

}
