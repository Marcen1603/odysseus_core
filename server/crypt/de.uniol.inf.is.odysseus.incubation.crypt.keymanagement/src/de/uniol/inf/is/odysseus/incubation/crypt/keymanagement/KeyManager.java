package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.AbstractDbKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.DbEncSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.DbKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IDbEncSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault.IDbKeyVault;

/**
 * Implementation of IKeyManager with default vaults. <br>
 * Both vaults will be implemented in databases.
 * 
 * @author MarkMilster
 *
 */
public class KeyManager implements IKeyManager {

	private static KeyManager instance;

	private IDbEncSymKeyVault encSymKeyVault;
	private IDbKeyVault pubKeyVault;

	/**
	 * Returns an Instance of this class
	 * 
	 * @return the instance of this class
	 */
	public static KeyManager getInstance() {
		if (instance == null) {
			instance = new KeyManager();
		}
		return KeyManager.instance;
	}

	/**
	 * Default Constructor.<br>
	 * Initializes the Vaults
	 */
	private KeyManager() {
		this.encSymKeyVault = new DbEncSymKeyVault();
		this.pubKeyVault = new DbKeyVault(AbstractDbKeyVault.Configuration.pubKeys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager#
	 * getPublicKey(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<KeyWrapper<PublicKey>> getPublicKey(List<Integer> id) {
		List<KeyWrapper<PublicKey>> pubKeys = new ArrayList<>();
		for (Integer i : id) {
			pubKeys.add((KeyWrapper<PublicKey>) this.pubKeyVault.getKey(i.intValue()));
		}
		return pubKeys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager#
	 * setPublicKey(java.lang.String)
	 */
	@Override
	public boolean setPublicKey(KeyWrapper<PublicKey> key) {
		return this.pubKeyVault.insertKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager#
	 * getEncSymKey(java.lang.String)
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
