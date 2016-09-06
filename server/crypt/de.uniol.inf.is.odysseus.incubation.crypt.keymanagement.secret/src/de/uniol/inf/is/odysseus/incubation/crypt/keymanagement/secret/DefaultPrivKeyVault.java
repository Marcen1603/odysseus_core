/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.PrivateKey;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.FileKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IKeyVault;

/**
 * @author MarkMilster
 *
 */
public class DefaultPrivKeyVault implements IPrivKeyVault {

	//TODO config file
	public final static String DEFAULT_PRIVATEKEYS_PATH = "/PrivKeys/";

	private IKeyVault privKeyVault;

	public DefaultPrivKeyVault() {
		this.privKeyVault = new FileKeyVault(DEFAULT_PRIVATEKEYS_PATH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.
	 * IPrivKeyVault#getPrivateKey(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public KeyWrapper<PrivateKey> getPrivateKey(int id) {
		return (KeyWrapper<PrivateKey>) this.privKeyVault.getKey(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.
	 * IPrivKeyVault#setPrivateKey(java.security.PrivateKey)
	 */
	@Override
	public boolean setPrivateKey(KeyWrapper<PrivateKey> privateKey) {
		return this.privKeyVault.insertKey(privateKey);
	}

}
