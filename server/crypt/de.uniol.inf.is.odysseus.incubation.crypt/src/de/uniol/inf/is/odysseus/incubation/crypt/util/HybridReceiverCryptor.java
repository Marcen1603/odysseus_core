/**
 * 
 */
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
 * @author MarkMilster
 *
 */
public class HybridReceiverCryptor {

	private ICryptor receiverCryptor;

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

	public HybridReceiverCryptor(ICryptor asymCryptor) {
		this.receiverCryptor = asymCryptor;
	}

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
