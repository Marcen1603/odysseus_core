package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.PrivateKey;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

public interface IPrivKeyVault {
	
	public KeyWrapper<PrivateKey> getPrivateKey(int id);
	public boolean setPrivateKey(KeyWrapper<PrivateKey> privateKey);

}
