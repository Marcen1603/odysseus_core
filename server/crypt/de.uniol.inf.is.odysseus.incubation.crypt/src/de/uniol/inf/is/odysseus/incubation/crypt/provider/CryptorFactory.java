package de.uniol.inf.is.odysseus.incubation.crypt.provider;

import java.io.UnsupportedEncodingException;

import javax.crypto.spec.SecretKeySpec;

/**
 * This class is a factory for Cryptors
 * 
 * @author MarkMilster
 *
 */
public class CryptorFactory {

	/**
	 * Creates a Cryptor with AESCFB algorithm and some bad default settings
	 * <br>
	 * It should only be used for testing.
	 * 
	 * @return The created Cryptor
	 */
	public static Cryptor createAESCFB() {
		Cryptor cryptor = new Cryptor("AES/CFB/PKCS5PADDING");
		try {
			cryptor.setKey(new SecretKeySpec("1234567890123456".getBytes("UTF-8"), "AES"));
			cryptor.setInitVector("1234567890123456".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return cryptor;
	}

	/**
	 * Creates a Cryptor with RSAECB algorithm and some default settings
	 * 
	 * @return The created Cryptor
	 */
	public static Cryptor createRSA() {
		// RSA has no initVector
		return new Cryptor("RSA/ECB/PKCS1Padding");
	}

}