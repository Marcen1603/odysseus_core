package de.uniol.inf.is.odysseus.incubation.crypt.provider;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import de.uniol.inf.is.odysseus.incubation.crypt.jope.OPE;
import de.uniol.inf.is.odysseus.incubation.crypt.util.ByteConverter;

/**
 * This class is a cryptographic Provider with some util functions
 * 
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
	private boolean initialized = false;

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Constructs a Cryptor with the spcified algorithm
	 * 
	 * @param algorithm
	 *            The algorithm, which will be used for crypting
	 */
	public Cryptor(String algorithm) {
		this.setAlgorithm(algorithm);
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptor
	 *            The Cryptor, which will be copied
	 */
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
			this.initialized = true;
		} catch (Exception e) {
			// maybe this is no Problem, e.g. if you use a other Cryptographic
			// Engine
			e.printStackTrace();
		}
		this.cryptedMessage = null;
	}

	@Override
	public void setKey(Key key) {
		this.key = key;
		this.initialized = false;
	}

	@Override
	public Key getKey() {
		return this.key;
	}

	@Override
	public void setInitVector(byte[] initVector) {
		this.initVector = new IvParameterSpec(initVector);
		this.initialized = false;
	}

	@Override
	public byte[] getInitVector() {
		return this.initVector.getIV();
	}

	@Override
	public void setMode(int mode) {
		this.mode = mode;
		this.initialized = false;
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
		if (this.algorithm.equals(ICryptor.OPE_LONG_ALGORITHM)) {
			return this.cryptObjectOPE(object);
		} else {
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

	}

	/**
	 * Crypt a object with the OPE algorithm
	 * 
	 * @param object
	 *            The object to crypt
	 * @return The crypted object in Long representation
	 */
	private Long cryptObjectOPE(Object object) {
		return OPE.crypt(Long.valueOf(object.toString()), this.mode, this.key);
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
			if (this.isInitialized()) {
				this.cryptedMessage = this.cipher.doFinal(this.message);
			} else {
				throw new Exception("Cryptor was not initialized");
			}
		} catch (Exception e) {
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
			// maybe this is no Problem, e.g. if you use a other Cryptographic
			// Engine
			e.printStackTrace();
		}
	}

	public String getAlgorithm() {
		return algorithm;
	}

	@Override
	public Object cryptObjectViaString(Object object) {
		if (this.getMode() == Cipher.DECRYPT_MODE && object instanceof String) {
			// String to byte[]
			object = Base64.decodeBase64((String) object);
		}

		// returns:: Encrypting: encrypted byte[]; Decrypting: decrypted Object
		object = this.cryptObject(object);

		if (this.getMode() == Cipher.ENCRYPT_MODE && object instanceof byte[]) {
			// enc byte[] to enc String
			object = Base64.encodeBase64String((byte[]) object);
		}
		return object;
	}

}
