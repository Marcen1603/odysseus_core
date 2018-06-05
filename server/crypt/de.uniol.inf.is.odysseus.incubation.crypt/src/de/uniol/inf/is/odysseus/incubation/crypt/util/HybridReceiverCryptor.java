package de.uniol.inf.is.odysseus.incubation.crypt.util;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.CryptorFactory;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;

/**
 * Util class for keymanagement in a hybrid cryptographic environment
 * 
 * @author MarkMilster
 *
 */
public class HybridReceiverCryptor {

	private ICryptor receiverCryptor;

	/**
	 * Constructor who crypts the key with the given algorithm
	 * 
	 * @param algorithm
	 *            Algorithm to crypt the symmetric key with
	 */
	public HybridReceiverCryptor(String algorithm) {
		switch (algorithm) {
		case "RSA":
			this.receiverCryptor = CryptorFactory.createRSA();
			break;
		default:
			this.receiverCryptor = null;
			break;
		}
	}

	/**
	 * Constructor who crypts the key with the given Cryptor
	 * 
	 * @param asymCryptor
	 *            Cryptor tp crypt the symmetrik key with
	 */
	public HybridReceiverCryptor(ICryptor asymCryptor) {
		this.receiverCryptor = asymCryptor;
	}

	/**
	 * Encrypt a sym Key with the given Cryptor in this object.
	 * 
	 * @param symKey
	 *            The symKey to crypt
	 * @param receiverKey
	 *            the key to crypt the sym key with
	 * @param streamId
	 *            the id of the crypting the sym key was used for to identify
	 *            the crypted key
	 * @return the encrypted sym key
	 */
	public EncKeyWrapper encryptSymKey(KeyWrapper<Key> symKey, KeyWrapper<PublicKey> receiverKey, int streamId) {
		receiverCryptor.setKey(receiverKey.getKey());
		receiverCryptor.setMode(Cipher.ENCRYPT_MODE);
		receiverCryptor.init();
		receiverCryptor.crypt(symKey.getKey().getEncoded());
		EncKeyWrapper encKeyWrapper = new EncKeyWrapper();
		encKeyWrapper.acquireMetadata(symKey);
		encKeyWrapper.setKey(receiverCryptor.getCryptedMessage());
		encKeyWrapper.setAlgoritm(symKey.getKey().getAlgorithm());
		encKeyWrapper.setReceiverId(receiverKey.getId());
		encKeyWrapper.setStreamId(streamId);
		return encKeyWrapper;
	}

	/**
	 * Decrypt the encrypted symKey.
	 * 
	 * @param encKeyWrapper
	 *            The encrypted symKey to decrypt
	 * @param receiverKey
	 *            the key to decrypt the encrypted key with
	 * @return The symmetric key
	 */
	public KeyWrapper<Key> decryptSymKey(EncKeyWrapper encKeyWrapper, KeyWrapper<PrivateKey> receiverKey) {
		receiverCryptor.setKey(receiverKey.getKey());
		receiverCryptor.setMode(Cipher.DECRYPT_MODE);
		receiverCryptor.init();
		receiverCryptor.crypt(encKeyWrapper.getKey());
		KeyWrapper<Key> keyWrapper = new KeyWrapper<>();
		keyWrapper.acquireMetadata(encKeyWrapper);
		keyWrapper.setKey(new SecretKeySpec(receiverCryptor.getCryptedMessage(), encKeyWrapper.getAlgoritm()));
		return keyWrapper;
	}

}
