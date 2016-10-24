package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.Key;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.Activator;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util.MoreFileUtils;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.DbKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IDbKeyVault;

/**
 * Default Vault for storing symmetric keys
 * 
 * @author MarkMilster
 *
 */
public class DefaultSymKeyVault implements ISymKeyVault {
	
	public static final String PROPERTIES_PATH = "Config/symKeyVault.properties";

	private IDbKeyVault symKeyVault;

	/**
	 * Default Constructor
	 */
	public DefaultSymKeyVault() {
		String path = MoreFileUtils.getAbsolutePath(Activator.getContext().getBundle(), PROPERTIES_PATH);
		this.symKeyVault = new DbKeyVault(path);
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
