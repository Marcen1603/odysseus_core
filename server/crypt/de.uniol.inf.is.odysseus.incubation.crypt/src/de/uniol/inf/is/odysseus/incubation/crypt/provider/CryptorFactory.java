package de.uniol.inf.is.odysseus.incubation.crypt.provider;

import java.io.UnsupportedEncodingException;

import javax.crypto.spec.SecretKeySpec;

public class CryptorFactory {

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
	
	public static Cryptor createRSA() {
		//RSA has no initVector
		return new Cryptor("RSA/ECB/PKCS1Padding");
	}

}