package de.uniol.inf.is.odysseus.security.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

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

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class SecurityStore {

	private static SecurityStore instance;

	private static final Logger logger = LoggerFactory.getLogger(SecurityStore.class);

	private static final String SECURITY_PROVIDER = "BC";
	private static String CERTIFICATE_SIGNATURE_ALGORITHM = "SHA256WithRSA";
	private static final int VALIDITY_TIME = 3650;
	private static final String ALIAS_CERT_EXTENSION = "_CERTIFICATE";

	private KeyStore keystore;
	
	private KeyStore truststore;

	private String alias;

	private String password;

	private File pfxFile;

	private File derFile;
	
	private File tstFile;

	private SecurityStore() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static SecurityStore getInstance() {
		if (instance == null) {
			instance = new SecurityStore();
		}
		return instance;
	}

	public PrivateKey getDefaultPrivateKey() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		Key key = keystore.getKey(alias, password.toCharArray());
		return (PrivateKey) key;
	}

	public Certificate getDefaultCertificate() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		Certificate cert = keystore.getCertificate(alias + "_CERTIFICATE");
		return cert;
	}

	private void init() throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		String dir = OdysseusConfiguration.instance.get("security.dir");
		pfxFile = new File(dir + File.separator + OdysseusConfiguration.instance.get("security.default.pfx"));
		derFile = new File(dir + File.separator + OdysseusConfiguration.instance.get("security.default.der"));
		tstFile = new File(dir + File.separator + OdysseusConfiguration.instance.get("security.default.tst"));

		password = OdysseusConfiguration.instance.get("security.default.password");
		alias = OdysseusConfiguration.instance.get("security.default.pfx.alias");

		keystore = KeyStore.getInstance("PKCS12", SECURITY_PROVIDER);
		truststore = KeyStore.getInstance(KeyStore.getDefaultType());
		if (!pfxFile.exists()) {
			logger.debug("There is no personal information exchange (pfx) at " + pfxFile.getAbsolutePath());
			createKeystore(password, alias);
			pfxFile.getParentFile().mkdir();
			logger.debug("Saving keystore to: " + pfxFile.getAbsolutePath());
			FileOutputStream os = new FileOutputStream(pfxFile);
			keystore.store(os, password.toCharArray());
			os.close();
		}
		if (!tstFile.exists()) {
			truststore.load(null, password.toCharArray());
			tstFile.getParentFile().mkdir();
			logger.debug("Saving truststore to: " + tstFile.getAbsolutePath());
			FileOutputStream os = new FileOutputStream(tstFile);
			truststore.store(os, password.toCharArray());
			os.close();
		}
		logger.debug("Loading keystore from " + pfxFile.getAbsolutePath());
		keystore.load(new FileInputStream(pfxFile), password.toCharArray());
		
		logger.debug("Loading truststore from " + tstFile.getAbsolutePath());
		truststore.load(new FileInputStream(tstFile), password.toCharArray());
		
		logger.debug("Exporting the certificate to: " + derFile.getAbsolutePath());
		if (derFile.exists()) {
			logger.debug("Overwritting old certificate file at " + derFile.getAbsolutePath());
			derFile.delete();
		}
		Certificate cert = keystore.getCertificate(alias + ALIAS_CERT_EXTENSION);
		byte[] encBytes = cert.getEncoded();
		FileOutputStream os = new FileOutputStream(derFile);
		os.write(encBytes);
		os.close();

	}


	private void createKeystore(String password, String alias) throws Exception {
		keystore.load(null, password.toCharArray());

		String applicationName = OdysseusConfiguration.instance.get("security.applicationName");
		String organisation = OdysseusConfiguration.instance.get("security.organization");

		String hostName = InetAddress.getLocalHost().getHostName();
		String applicationUri = "urn:" + hostName + ":" + applicationName;

		logger.debug("generating a new key pair...");
		KeyPair keyPair = generateKeyPair(1024);

		// The fields appear in reverse order in the final certificate!
		String name = "CN=" + applicationName + (", O=" + organisation) + (", DC=" + hostName);
		logger.debug("generating the certificate from key pair...");
		X509Certificate cert = generateCertificate(name, VALIDITY_TIME, applicationUri, keyPair, hostName);
		logger.debug("save private key information into keystore");
		Certificate chain[] = { cert };
		keystore.setKeyEntry(alias, keyPair.getPrivate(), password.toCharArray(), chain);
		logger.debug("save certificate into keystore");
		keystore.setCertificateEntry(alias + ALIAS_CERT_EXTENSION, cert);
	}

	// public static void exportPrivateKey(File keystoreFile, String password)
	// throws Exception {
	// Security.addProvider(new
	// org.bouncycastle.jce.provider.BouncyCastleProvider());
	//
	// KeyStore keystore = KeyStore.getInstance("PKCS12", "BC");
	//
	// keystore.load(new FileInputStream(keystoreFile), password.toCharArray());
	// KeyPair keyPair = getPrivateKey(keystore, "mykeystore",
	// password.toCharArray());
	// PrivateKey privateKey = keyPair.getPrivate();
	//
	// FileWriter fw = new FileWriter("private.pem");
	// PEMWriter writer = new PEMWriter(fw);
	// writer.writeObject(privateKey);
	// writer.close();
	// }

	private X509Certificate generateCertificate(String dn, int days, String applicationUri, KeyPair keys, String... hostNames) throws IOException, GeneralSecurityException {
		PrivateKey privateKey = keys.getPrivate();
		PublicKey publicKey = keys.getPublic();
		Calendar expiryTime = Calendar.getInstance();
		expiryTime.add(Calendar.DAY_OF_YEAR, days);
		Date from = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
		Date to = expiryTime.getTime();
		BigInteger sn = BigInteger.valueOf(System.currentTimeMillis());

		return generateCertificateWithBouncyCastle(dn, publicKey, privateKey, from, to, sn, applicationUri, hostNames);

	}

	private X509Certificate generateCertificateWithBouncyCastle(String domainName, PublicKey publicKey, PrivateKey privateKey, Date from, Date to, BigInteger serial, String applicationUri, String... hostNames) throws IOException, GeneralSecurityException {

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

	private KeyPair generateKeyPair(int keysize) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keysize);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}
	
	public KeyManager[] getKeyManagers() {
		try {
		    KeyManagerFactory kmf  =  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		    kmf.init(keystore, password.toCharArray());
		    return kmf.getKeyManagers();
		} catch (Exception e) {
			throw new RuntimeException("Error while creating key managers", e);
		}
	}

	public TrustManager[] getTrustManagers() {
		try {
		    TrustManagerFactory kmf  =  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		    kmf.init(truststore);
		    return kmf.getTrustManagers();
		} catch (Exception e) {
			throw new RuntimeException("Error while creating trust managers", e);
		}
	}

}
