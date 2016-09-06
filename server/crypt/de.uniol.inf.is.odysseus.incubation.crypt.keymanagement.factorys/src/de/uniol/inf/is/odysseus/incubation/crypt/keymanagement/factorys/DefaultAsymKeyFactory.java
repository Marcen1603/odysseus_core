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

public class DefaultAsymKeyFactory implements IAsymKeyFactory {

	private static DefaultAsymKeyFactory INSTANCE;

	public static final String[] ALGORITHMS = { "DiffieHellman", "DSA", "RSA", "EC" };

	private DefaultAsymKeyFactory() {

	}

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
		// TODO what todo, if you use an invalid algorithm?
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

		ASymKeyWrapper asymKeyWrapper = new ASymKeyWrapper();
		asymKeyWrapper.setPrivateKeyWrapper(privateKeyWrapper);
		asymKeyWrapper.setPublicKeyWrapper(publicKeyWrapper);
		return asymKeyWrapper;
	}

	// TODO die key factorys in extra bundle packen und auch ueber den server
	// den keymanager ansprechen --> keymanagerserver und
	// secretkeymanagerTrennen und client dafuer auch in extra bundle
	public void saveAsymKeys(ASymKeyWrapper keys) {
		// save public Key
		KeyManager.getInstance().setPublicKey(keys.getPublicKeyWrapper());
		// save private Key
		IPrivKeyVault privKeyVault = new DefaultPrivKeyVault();
		privKeyVault.setPrivateKey(keys.getPrivateKeyWrapper());
	}

}
