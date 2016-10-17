package de.uniol.inf.is.odysseus.incubation.crypt.provider;

import java.security.Key;

/**
 * This interface provides a cryptographic provider. It will controll the
 * cryptographic engine
 * 
 * @author MarkMilster
 *
 */
public interface ICryptor {
	
	public static final String OPE_LONG_ALGORITHM = "OPELONG";

	/**
	 * Initializes the cryptographic engine with the metadata given in the
	 * parameters
	 * 
	 * @param algorithm
	 *            The algorithm, which will be used for crypting
	 * @param mode
	 *            The mode, which will be used for crypting
	 * @param initVector
	 *            The initVector, which will be used for crypting
	 * @param key
	 *            The key, which will be used for crypting
	 */
	public void init(String algorithm, int mode, byte[] initVector, Key key);

	/**
	 * Initializes the cryptographic engine with the metadata already set
	 */
	public void init();

	/**
	 * Checks, if the cryptographic engine is already initialized
	 * 
	 * @return true, if the cryptographic engine is initialized <br>
	 *         false, otheriwse
	 */
	public boolean isInitialized();

	/**
	 * Sets the key
	 * 
	 * @param key
	 *            the key to set
	 */
	public void setKey(Key key);

	/**
	 * Returns the key
	 * 
	 * @return the key
	 */
	public Key getKey();

	/**
	 * Sets the initVector
	 * 
	 * @param parameter
	 *            the initVector to set
	 */
	public void setInitVector(byte[] initVector);

	/**
	 * Returns the initVector
	 * 
	 * @return the initVector
	 */
	public byte[] getInitVector();

	/**
	 * Sets the mode
	 * 
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(int mode);

	/**
	 * Returns the mode
	 * 
	 * @return the mode
	 */
	public int getMode();

	/**
	 * Sets the algorithm
	 * 
	 * @param algorithm
	 *            the algorithm to set
	 */
	public void setAlgorithm(String algorithm);

	/**
	 * Returns the algorithm
	 * 
	 * @return the algorithm
	 */
	public String getAlgorithm();

	/**
	 * Sets the message, which will be crypted
	 * 
	 * @param message
	 *            the messate to set in cleatext
	 */
	public void setMessage(byte[] message);

	/**
	 * Returns the message
	 * 
	 * @return the message
	 */
	public byte[] getMessage();

	/**
	 * Retursns the crypted message
	 * 
	 * @return the crypted Message
	 */
	public byte[] getCryptedMessage();

	/**
	 * Do the crypting with the message already set
	 * 
	 * @return the crypted Message
	 */
	public byte[] crypt();

	/**
	 * Crypt this message
	 * 
	 * @param message
	 *            the message to crypt
	 * @return the crypted message
	 */
	public byte[] crypt(byte[] message);

	/**
	 * Crypting a String
	 * 
	 * @param message
	 *            the string to crypt
	 * @return the crypted String
	 */
	public String cryptBase64String(String message);

	/**
	 * Crypt any object. <br>
	 * If you decrypt the Object, it will be the same class as before the
	 * crypting
	 * 
	 * @param object
	 *            the object to crypt
	 * @return the crypted object
	 */
	public Object cryptObject(Object object);
	
	/**
	 * Encrypting any object to a Base64String and Decrypting it back to the original Object.
	 * 
	 * @param object the object to crypt
	 * @return the crypted object; at encrypting a String; at decrypting the original object
	 */
	public Object cryptObjectViaString(Object object);

}
