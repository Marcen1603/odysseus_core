package de.uniol.inf.is.odysseus.security.store;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import de.uniol.inf.is.odysseus.security.provider.ISecurityProvider;

public class SecurityProvider implements ISecurityProvider {

	@Override
	public PrivateKey getDefaultKey() throws GeneralSecurityException{
		return SecurityStore.getInstance().getDefaultPrivateKey();
	}

	@Override
	public Certificate getDefaultCertificate() throws GeneralSecurityException{
		return SecurityStore.getInstance().getDefaultCertificate();
	}

}
