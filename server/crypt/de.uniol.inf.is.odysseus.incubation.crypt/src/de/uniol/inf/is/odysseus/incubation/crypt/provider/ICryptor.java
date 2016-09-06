package de.uniol.inf.is.odysseus.incubation.crypt.provider;

import java.security.Key;

/**
 * @author MarkMilster
 *
 */
public interface ICryptor {

	public void init(String algorithm, int mode, byte[] initVector, Key key);
	public void init();

	public void setKey(Key key);
	public Key getKey();
	
	public void setInitVector(byte[] parameter);
	public byte[] getInitVector();
	
	public void setMode(int mode);
	public int getMode();
	
	public void setAlgorithm(String algorithm);
	public String getAlgorithm();
	
	public void setMessage(byte[] message);
	public byte[] getMessage();
	
	public byte[] getCryptedMessage();
	
	public byte[] crypt();
	public byte[] crypt(byte[] message);
	public String cryptBase64String(String message);
	public Object cryptObject(Object object);
	
}
