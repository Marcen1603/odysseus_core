package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.KeyManager;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.ASymKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultPrivKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.IPrivKeyVault;

/**
 * Factory, to create ASymKey with some Default Setting
 * 
 * @author MarkMilster
 *
 */
public class DefaultAsymKeyFactory implements IAsymKeyFactory {

	private static DefaultAsymKeyFactory INSTANCE;

	public static final String[] ALGORITHMS = { "DiffieHellman", "DSA", "RSA", "EC" };

	/**
	 * Default constructor
	 */
	private DefaultAsymKeyFactory() {

	}

	/**
	 * Returns the instance of this Factory
	 * 
	 * @return The instance
	 */
	public static DefaultAsymKeyFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DefaultAsymKeyFactory();
		}
		return INSTANCE;
	}

	@Override
	public ASymKeyWrapper createAsymKeyPair(String algorithm, int size, LocalDateTime valid, String comment) {
		KeyPairGenerator keygen = null;
		try {
			keygen = KeyPairGenerator.getInstance(algorithm);
			keygen.initialize(size);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		ASymKeyWrapper asymKeyWrapper = null;
		try {
			KeyPair keyPair = keygen.genKeyPair();

			LocalDateTime generated = LocalDateTime.now();
			KeyWrapper<PublicKey> publicKeyWrapper = new KeyWrapper<>();
			publicKeyWrapper.setKey(keyPair.getPublic());
			publicKeyWrapper.setId(KeyManager.getInstance().getNextAsymKeyId());
			publicKeyWrapper.setCreated(generated);
			publicKeyWrapper.setValid(valid);
			publicKeyWrapper.setComment(comment);

			KeyWrapper<PrivateKey> privateKeyWrapper = new KeyWrapper<>();
			privateKeyWrapper.acquireMetadata(publicKeyWrapper);
			privateKeyWrapper.setKey(keyPair.getPrivate());

			asymKeyWrapper = new ASymKeyWrapper();
			asymKeyWrapper.setPrivateKeyWrapper(privateKeyWrapper);
			asymKeyWrapper.setPublicKeyWrapper(publicKeyWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return asymKeyWrapper;
	}

	/**
	 * Store both parts of the keys (public and private) to the default vaults.
	 * <br>
	 * The PublicKey will be stored with the KeyManager.<br>
	 * The PrivateKey will be stored in the DefaultPrivKeyVault.
	 * 
	 * @param keys
	 */
	public void saveAsymKeys(ASymKeyWrapper keys) {
		// save public Key
		KeyManager.getInstance().setPublicKey(keys.getPublicKeyWrapper());
		// save private Key
		IPrivKeyVault privKeyVault = new DefaultPrivKeyVault();
		privKeyVault.setPrivateKey(keys.getPrivateKeyWrapper());
	}

}
