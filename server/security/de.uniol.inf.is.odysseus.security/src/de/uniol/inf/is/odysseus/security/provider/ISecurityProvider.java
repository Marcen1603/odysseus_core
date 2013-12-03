package de.uniol.inf.is.odysseus.security.provider;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface ISecurityProvider {
	
	public PrivateKey getDefaultKey() throws GeneralSecurityException;
	public Certificate getDefaultCertificate() throws GeneralSecurityException;

}
