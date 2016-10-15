package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.PrivateKey;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

public interface IPrivKeyVault {
	
	public List<KeyWrapper<PrivateKey>> getPrivateKey(List<Integer> id);
	public boolean setPrivateKey(KeyWrapper<PrivateKey> privateKey);

}
