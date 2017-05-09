package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.FileKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IKeyVault;

/**
 * Default Vault for Private Keys
 * 
 * @author MarkMilster
 *
 */
public class DefaultPrivKeyVault implements IPrivKeyVault {

	public final static String DEFAULT_PRIVATEKEYS_PATH = "/PrivKeys/";

	private IKeyVault privKeyVault;

	/**
	 * Default constructor
	 */
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
	public List<KeyWrapper<PrivateKey>> getPrivateKey(List<Integer> id) {
		List<KeyWrapper<PrivateKey>> keys = new ArrayList<>();
		for (int i = 0; i < id.size(); i++) {
			keys.add((KeyWrapper<PrivateKey>) this.privKeyVault.getKey(id.get(i)));
		}
		return keys;
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
