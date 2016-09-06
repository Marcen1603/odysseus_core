/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import de.uniol.inf.is.odysseus.incubation.crypt.util.ByteConverter;

/**
 * @author MarkMilster
 *
 */
public class Cryptor implements ICryptor {

	private Key key;
	private byte[] message;
	private Cipher cipher;
	private IvParameterSpec initVector;
	private byte[] cryptedMessage;
	private int mode;
	private String algorithm;

	public Cryptor(String algorithm) {
		this.setAlgorithm(algorithm);
	}

	public Cryptor(ICryptor cryptor) {
		this.setKey(cryptor.getKey());
		this.setMessage(cryptor.getMessage());
		this.setInitVector(cryptor.getInitVector());
		this.setMode(cryptor.getMode());
		this.setAlgorithm(cryptor.getAlgorithm());
	}

	@Override
	public void init(String algorithm, int mode, byte[] initVector, Key key) {
		this.setMode(mode);
		this.setKey(key);
		this.setInitVector(initVector);
		this.init();
	}

	@Override
	public void init() {
		try {
			this.cipher.init(this.mode, this.key, this.initVector);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		this.cryptedMessage = null;
	}

	@Override
	public void setKey(Key key) {
		this.key = key;
	}

	@Override
	public Key getKey() {
		return this.key;
	}

	@Override
	public void setInitVector(byte[] initVector) {
		this.initVector = new IvParameterSpec(initVector);
	}

	@Override
	public byte[] getInitVector() {
		return this.initVector.getIV();
	}

	@Override
	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public int getMode() {
		return this.mode;
	}

	@Override
	public void setMessage(byte[] message) {
		this.message = message;
	}

	@Override
	public byte[] getMessage() {
		return this.message;
	}

	@Override
	public byte[] getCryptedMessage() {
		return this.cryptedMessage;
	}

	@Override
	public Object cryptObject(Object object) {
		byte[] bytes = null;

		// convert to byteArray
		if (object instanceof byte[]) {
			bytes = (byte[]) object;
		} else {
			bytes = ByteConverter.objectToBytes(object);
		}

		// crypt
		byte[] crypted = this.crypt(bytes);
		Object fin = crypted;

		// encrypt: return byte[]; decrypt: return original Object
		if (this.getMode() == Cipher.DECRYPT_MODE) {
			fin = ByteConverter.bytesToObject(crypted);
		}
		return fin;
	}

	@Override
	public String cryptBase64String(String message) {
		byte[] encodedMessage = Base64.decodeBase64(message.toString());
		byte[] crypted = this.crypt(encodedMessage);
		String cryptedString = Base64.encodeBase64String(crypted);
		return cryptedString;
	}

	@Override
	public byte[] crypt() {
		try {
			this.cryptedMessage = this.cipher.doFinal(this.message);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return this.cryptedMessage;
	}

	@Override
	public byte[] crypt(byte[] message) {
		this.setMessage(message);
		return this.crypt();
	}

	@Override
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
		try {
			this.cipher = Cipher.getInstance(this.algorithm);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return algorithm;
	}

}
