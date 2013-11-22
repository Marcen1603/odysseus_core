package de.uniol.inf.is.odysseus.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class CertificateStore {

	public static final Logger logger = LoggerFactory.getLogger(CertificateStore.class);

	public static final String SECURITY_PROVIDER = "BC";
	private static String CERTIFICATE_SIGNATURE_ALGORITHM = "SHA256WithRSA";
	
	public static X509Certificate certificate;

	public static void load() throws Exception {
		String dir = OdysseusConfiguration.get("security.dir");
		File certFile = new File(dir + File.separator + OdysseusConfiguration.get("security.default.certificate"));
		File privkeyFile = new File(dir + File.separator + OdysseusConfiguration.get("odysseus.default.private.pem"));
		String password = OdysseusConfiguration.get("security.default.privatekey.password");
		String applicationName = OdysseusConfiguration.get("security.applicationName");
		String organisation = OdysseusConfiguration.get("security.organization");

		if (!certFile.exists()) {
			logger.debug("There is no certificate at " + certFile.getAbsolutePath());
			if (privkeyFile.exists()) {
				throw new IllegalArgumentException("There is a private key but no certificate, but we need both! Cannot overwrite an existing private key, so delete or rename the private key to create a new pair!");
			}

			logger.debug("There is also no private key, so we creating a new key pair");
			createNewCertificate(applicationName, organisation, certFile, privkeyFile, password);
		} else {
			if (!privkeyFile.exists()) {
				throw new IllegalArgumentException("There is a certificate but no private key, but we need both! Cannot overwrite an existing certificate, so delete or rename the certificate to create a new pair!");
			}
			logger.debug("Loading the certificate from " + certFile.getAbsolutePath());
			InputStream is = new FileInputStream(certFile);
			CertificateFactory servercf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) servercf.generateCertificate(is);
			certificate = cert;
			// TODO: loading the private key from pem file!
			// save them in a pair and offer them to other bundles!
		}

	}

	public static Pair<KeyPair, X509Certificate> createNewCertificate(String applicationName, String organization, File certificateFile, File privateKeyFile, String password) throws Exception {

		String hostName = InetAddress.getLocalHost().getHostName();
		String applicationUri = "urn:" + hostName + ":" + applicationName;

		Pair<KeyPair, X509Certificate> keys = createApplicationInstanceCertificate(applicationName, organization, applicationUri, 3650, hostName);
		// keys.getCertificate().save(certFile);
		// keys.getPrivateKey().save(privKeyFile, password);
		return keys;

	}

	public static Pair<KeyPair, X509Certificate> createApplicationInstanceCertificate(String commonName, String organisation, String applicationUri, int validityTime, String... hostNames) throws IOException, GeneralSecurityException {
		if (applicationUri == null) {
			throw new NullPointerException("applicationUri must not be null");
		}

		KeyPair keyPair = generateKeyPair(1024);

		// The fields appear in reverse order in the final certificate!
		String name = "CN=" + commonName + (organisation == null ? "" : ", O=" + organisation) + ((hostNames == null || hostNames.length == 0) ? "" : ", DC=" + hostNames[0]);

		X509Certificate cert = generateCertificate(name, validityTime, applicationUri, keyPair, hostNames);
		Pair<KeyPair, X509Certificate> pair = new Pair<KeyPair, X509Certificate>(keyPair, cert);
		// Encapsulate Certificate and private key to CertificateKeyPair
		// return toKeyPair(cert, keyPair.getPrivate());
		return pair;
	}

	private static X509Certificate generateCertificate(String dn, int days, String applicationUri, KeyPair keys, String... hostNames) throws GeneralSecurityException, IOException {

		PrivateKey privkey = keys.getPrivate();
		PublicKey publicKey = keys.getPublic();

		return generateCertificate(dn, days, applicationUri, publicKey, privkey, hostNames);
	}

	private static X509Certificate generateCertificate(String dn, int days, String applicationUri, PublicKey publicKey, PrivateKey privateKey, String... hostNames) throws IOException, GeneralSecurityException {
		Calendar expiryTime = Calendar.getInstance();
		expiryTime.add(Calendar.DAY_OF_YEAR, days);
		Date from = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
		Date to = expiryTime.getTime();
		BigInteger sn = BigInteger.valueOf(System.currentTimeMillis());

		return generateCertificateWithBouncyCastle(dn, publicKey, privateKey, from, to, sn, applicationUri, hostNames);

	}

	private static X509Certificate generateCertificateWithBouncyCastle(String domainName, PublicKey publicKey, PrivateKey privateKey, Date from, Date to, BigInteger serial, String applicationUri, String... hostNames) throws IOException, GeneralSecurityException {

		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

		X509v3CertificateBuilder certBldr;
		AuthorityKeyIdentifier authorityKeyIdentifier;
		PrivateKey signerKey;
		X500Name dn = new X500Name(domainName);
		certBldr = new JcaX509v3CertificateBuilder(dn, serial, from, to, dn, publicKey);
		authorityKeyIdentifier = extUtils.createAuthorityKeyIdentifier(publicKey);
		signerKey = privateKey;

		certBldr.addExtension(X509Extension.authorityKeyIdentifier, false, authorityKeyIdentifier).addExtension(X509Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(publicKey)).addExtension(X509Extension.basicConstraints, false, new BasicConstraints(false)).addExtension(X509Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.nonRepudiation | KeyUsage.dataEncipherment | KeyUsage.keyCertSign));

		Vector<KeyPurposeId> extendedKeyUsages = new Vector<KeyPurposeId>();
		extendedKeyUsages.add(KeyPurposeId.id_kp_serverAuth);
		extendedKeyUsages.add(KeyPurposeId.id_kp_clientAuth);
		certBldr.addExtension(X509Extension.extendedKeyUsage, false, new ExtendedKeyUsage(extendedKeyUsages));
		// BC 1.49:
		// certBldr.addExtension(X509Extension.extendedKeyUsage, false,
		// new ExtendedKeyUsage(new KeyPurposeId[] {
		// KeyPurposeId.id_kp_serverAuth,
		// KeyPurposeId.id_kp_clientAuth }));
		// create the extension value

		// URI Name
		List<GeneralName> names = new ArrayList<GeneralName>();
		names.add(new GeneralName(GeneralName.uniformResourceIdentifier, applicationUri));

		// Add DNS name from ApplicationUri
		boolean hasDNSName = false;
		String uriHostName = null;
		try {
			String[] appUriParts = applicationUri.split("[:/]");
			if (appUriParts.length > 1) {
				uriHostName = appUriParts[1];
				if (!uriHostName.toLowerCase().equals("localhost")) {
					GeneralName dnsName = new GeneralName(GeneralName.dNSName, uriHostName);
					names.add(dnsName);
					hasDNSName = true;
				}
			}
		} catch (Exception e) {
			logger.warn("Cannot initialize DNS Name to Certificate from ApplicationUri" + applicationUri);
		}

		// Add other DNS Names
		List<GeneralName> ipAddressNames = new ArrayList<GeneralName>();
		if (hostNames != null)
			for (String hostName : hostNames) {
				boolean isIpAddress = hostName.matches("^[0-9.]+$");
				if (!hostName.equals(uriHostName) && !hostName.toLowerCase().equals("localhost")) {
					GeneralName dnsName = new GeneralName(hostName.matches("^[0-9.]+$") ? GeneralName.iPAddress : GeneralName.dNSName, hostName);
					if (isIpAddress)
						ipAddressNames.add(dnsName);
					else {
						names.add(dnsName);
						hasDNSName = true;
					}
				}
			}
		// Add IP Addresses, if no host names are defined
		if (!hasDNSName)
			for (GeneralName n : ipAddressNames)
				names.add(n);

		final GeneralNames subjectAltNames = new GeneralNames(names.toArray(new GeneralName[0]));
		certBldr.addExtension(X509Extension.subjectAlternativeName, false, subjectAltNames);

		// ***** generate certificate ***********/
		try {
			ContentSigner signer = new JcaContentSignerBuilder(CERTIFICATE_SIGNATURE_ALGORITHM).setProvider("BC").build(signerKey);
			return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
		} catch (OperatorCreationException e) {
			throw new GeneralSecurityException(e);
		}
	}

	public static KeyPair generateKeyPair(int keysize) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keysize);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

}
