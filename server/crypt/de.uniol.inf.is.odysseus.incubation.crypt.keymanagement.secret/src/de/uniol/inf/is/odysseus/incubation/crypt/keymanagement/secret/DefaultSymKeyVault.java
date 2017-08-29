package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.Key;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.AbstractDbKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.DbKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IDbKeyVault;

/**
 * Default Vault for storing symmetric keys
 * 
 * @author MarkMilster
 *
 */
public class DefaultSymKeyVault implements ISymKeyVault {

	private IDbKeyVault symKeyVault;

	/**
	 * Default Constructor
	 */
	public DefaultSymKeyVault() {
		this.symKeyVault = new DbKeyVault(AbstractDbKeyVault.Configuration.symKeys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.incubation.crypt.symKeyManagement.ISymKeyVault#
	 * getSymKey(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public KeyWrapper<Key> getSymKey(int id) {
		return (KeyWrapper<Key>) this.symKeyVault.getKey(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.incubation.crypt.symKeyManagement.ISymKeyVault#
	 * setSymKey(java.security.Key)
	 */
	@Override
	public boolean setSymKey(KeyWrapper<Key> key) {
		return this.symKeyVault.insertKey(key);
	}

	/**
	 * Returns the key, which will be used for the next symmetric key
	 * 
	 * @return The next (current not used) key
	 */
	public int getNextSymKey() {
		return this.symKeyVault.getNextKeyId();
	}

}
