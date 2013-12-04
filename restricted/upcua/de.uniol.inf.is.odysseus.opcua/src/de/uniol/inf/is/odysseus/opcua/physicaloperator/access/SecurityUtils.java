package de.uniol.inf.is.odysseus.opcua.physicaloperator.access;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.opcfoundation.ua.transport.security.Cert;

public class SecurityUtils {

	public static void createKeyPairs(String password, String storeAlias, String pfxDestination, String certDestionation) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		System.out.println("create RSA generator");
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		System.out.println("create random generator");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		System.out.println("set seed");
		random.setSeed(System.currentTimeMillis());
		System.out.println("create key pair");
		java.security.KeyPair pair = keyGen.generateKeyPair();		
		System.out.println("key pair created");

		// issuer - the certificate issuer
		X500Name name = new X500Name("DN=Odysseus");
		// serial - the certificate serial number
		BigInteger serial = new BigInteger("1");
		// notBefore - the date before which the certificate is not valid
		Date notBefore = new Date(System.currentTimeMillis() - 50000);
		// notAfter - the date after which the certificate is not valid
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 12, 31);
		Date dateOfExiry = cal.getTime();
		// subject - the certificate subject
		X500Name subject = new X500Name("CN=Odysseus Test Certificate");
		// publicKeyInfo - the info structure for the public key to be
		// associated with this certificate.
		SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pair.getPublic().getEncoded());
		X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(name, serial, notBefore, dateOfExiry, subject, publicKeyInfo);

		System.out.println("create certificate");
		ContentSigner sigGen = new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider(BouncyCastleProvider.PROVIDER_NAME).build(pair.getPrivate());
		X509Certificate cert = new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(certBuilder.build(sigGen));
		System.out.println("check if valid...");
		cert.checkValidity(new Date());
		System.out.println("ok, and varify with public key");
		cert.verify(cert.getPublicKey());
		System.out.println("all ok, save into keystore");

		KeyStore store = KeyStore.getInstance("pkcs12");

		store.load(null, password.toCharArray());
		Certificate[] chain = { cert };
		store.setKeyEntry(storeAlias, pair.getPrivate(), password.toCharArray(), chain);
		if(!pfxDestination.endsWith("pfx")){
			pfxDestination = pfxDestination+".pfx";
		}
		FileOutputStream fos = new FileOutputStream(pfxDestination);
		store.store(fos, password.toCharArray());
		Cert uaCert = new Cert((X509Certificate) cert);
		if (!certDestionation.endsWith("der")) {
			certDestionation = certDestionation + ".der";
		}
		uaCert.save(new File(certDestionation));
	}

}
