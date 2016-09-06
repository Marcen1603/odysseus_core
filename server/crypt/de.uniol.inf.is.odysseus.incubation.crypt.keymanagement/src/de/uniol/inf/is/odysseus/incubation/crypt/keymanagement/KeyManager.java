/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement;

import java.security.PublicKey;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util.MoreFileUtils;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.DbEncSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.DbKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IDbEncSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IDbKeyVault;

/**
 * Quasi gleichzeitig encsym und public vault
 * 
 * @author MarkMilster
 *
 */
public class KeyManager implements IKeyManager {
	
	public static final String PROPERTIES_PATH = "Config/pubKeyVault.properties";
	
	private static KeyManager instance;
	
	private IDbEncSymKeyVault encSymKeyVault;
	private IDbKeyVault pubKeyVault;
	
	public static KeyManager getInstance() {
		if (instance == null) {
			instance = new KeyManager();
		}
		return KeyManager.instance;
	}
	
	private KeyManager() {
		this.encSymKeyVault = new DbEncSymKeyVault();
		this.pubKeyVault = new DbKeyVault(MoreFileUtils.getAbsolutePath(Activator.getContext().getBundle(), PROPERTIES_PATH));
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager#getPublicKey(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public KeyWrapper<PublicKey> getPublicKey(int id) {
		return (KeyWrapper<PublicKey>) this.pubKeyVault.getKey(id);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager#setPublicKey(java.lang.String)
	 */
	@Override
	public boolean setPublicKey(KeyWrapper<PublicKey> key) {
		return this.pubKeyVault.insertKey(key);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager#getEncSymKey(java.lang.String)
	 */
	@Override
	public EncKeyWrapper getEncSymKey(int receiverID, int streamID) {
		return this.encSymKeyVault.getEncSymKey(receiverID, streamID);
	}

	@Override
	public boolean setEncSymKey(EncKeyWrapper encKeyWrapper) {
		return this.encSymKeyVault.insertEncSymKey(encKeyWrapper);
	}
	
	public int getNextAsymKeyId() {
		return this.pubKeyVault.getNextKeyId();
	}

}
