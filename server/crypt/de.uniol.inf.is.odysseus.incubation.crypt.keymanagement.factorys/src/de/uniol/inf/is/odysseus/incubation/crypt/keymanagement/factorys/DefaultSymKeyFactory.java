package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import javax.crypto.KeyGenerator;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultSymKeyVault;

/**
 * Factory for SymKeys with some default settings.
 * 
 * @author MarkMilster
 *
 */
public class DefaultSymKeyFactory implements ISymKeyFactory {

	public static final String[] ALGORITHMS = { "AES", "ARCFOUR", "Blowfish", "DES", "DESede", "HmacMD5", "HmacSHA1",
			"HmacSHA224", "HmacSHA256", "HmacSHA384", "HmacSHA512", "RC2" };
	public static final int DEFAULT_ID = 0;

	private static DefaultSymKeyFactory INSTANCE;

	/**
	 * Default constructor
	 */
	private DefaultSymKeyFactory() {

	}

	/**
	 * Returns the instance of the DefaultSymKeyFactory
	 * 
	 * @return The instance of the DefaultSymKeyFactory
	 */
	public static DefaultSymKeyFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DefaultSymKeyFactory();
		}
		return INSTANCE;
	}

	@Override
	public KeyWrapper<Key> createSymKey(String algorithm, LocalDateTime valid, String comment) {
		Key key = null;
		try {
			key = KeyGenerator.getInstance(algorithm).generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		KeyWrapper<Key> keyWrapper = null;
		if (key != null) {
			keyWrapper = new KeyWrapper<>();
			DefaultSymKeyVault symKeyVault = new DefaultSymKeyVault();
			keyWrapper.setId(symKeyVault.getNextSymKey());
			keyWrapper.setKey(key);
			keyWrapper.setCreated(LocalDateTime.now());
			keyWrapper.setValid(valid);
			keyWrapper.setComment(comment);
		}
		return keyWrapper;
	}

}
