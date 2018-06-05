package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

/**
 * A Key Vault, which is implemented in a database. The keys are stored in a
 * database.
 * 
 * @author MarkMilster
 *
 */
public interface IDbKeyVault extends IKeyVault {

	/**
	 * Returns the next, available id, which could be given from the
	 * autoIncerement feature from the database.
	 * 
	 * @return The next id, which is'nt used at the moment.
	 */
	int getNextKeyId();

}
