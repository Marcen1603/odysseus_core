package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret;

import java.security.Key;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

public interface ISymKeyVault {
	
	public KeyWrapper<Key> getSymKey(int id);
	public boolean setSymKey(KeyWrapper<Key> key);

}
