package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * Message to send the data of a PublicKey via JSON
 * 
 * @author MarkMilster
 *
 */
public class PublicKeyMessage implements Serializable {

	private static final long serialVersionUID = 8544302790146904313L;

	/**
	 * The List of PubKeyWrapper is stored as KeySpecs, because this contains
	 * the mathematical information about the key and you dont have trouble
	 * parsing an interface to jdbc
	 */
	private List<KeyWrapper<RSAPublicKeySpec>> publicKey;

	/**
	 * Default constructor for parsing in JSON
	 */
	public PublicKeyMessage() {
		this.publicKey = new ArrayList<>();
	}

	/**
	 * Constructor with List of publicKeys
	 * 
	 * @param publicKey
	 *            List of publicKeys to save in this message
	 */
	public PublicKeyMessage(List<KeyWrapper<PublicKey>> publicKey) {
		this.publicKey = new ArrayList<>();
		this.setPublicKey(publicKey);
	}

	/**
	 * Returns the list of publicKeys
	 * 
	 * @return the publicKey
	 */
	public List<KeyWrapper<PublicKey>> getPublicKey() {
		List<KeyWrapper<PublicKey>> pubKeys = new ArrayList<>();
		KeyFactory fact;
		KeyWrapper<PublicKey> wrapper = null;
		for (int i = 0; i < this.publicKey.size(); i++) {
			try {
				fact = KeyFactory.getInstance("RSA");
				PublicKey pbk = fact.generatePublic(this.publicKey.get(i).getKey());
				wrapper = new KeyWrapper<>();
				wrapper.acquireMetadata(this.publicKey.get(i));
				wrapper.setKey(pbk);
				pubKeys.add(wrapper);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		return pubKeys;
	}

	/**
	 * Sets the List of publicKeys
	 * 
	 * @param publicKey
	 *            the publicKey to set
	 */
	public void setPublicKey(List<KeyWrapper<PublicKey>> publicKey) {
		for (int i = 0; i < publicKey.size(); i++) {
			try {
				KeyFactory fact = KeyFactory.getInstance("RSA");
				RSAPublicKeySpec pks = fact.getKeySpec(publicKey.get(i).getKey(), RSAPublicKeySpec.class);
				KeyWrapper<RSAPublicKeySpec> wrap = new KeyWrapper<>();
				wrap.acquireMetadata(publicKey.get(i));
				wrap.setKey(pks);
				this.publicKey.add(wrap);
			} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

}
