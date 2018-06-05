/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.xafero.turjumaan.core.sdk.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.xafero.turjumaan.core.sdk.api.ICertificateLoader;

/**
 * The default implementation of a certificate loader.
 */
public class KeyStoreLoader implements ICertificateLoader {

	/** The key store. */
	private final KeyStore keyStore;

	/** The alias. */
	private final String alias;

	/** The certificate. */
	private X509Certificate certificate;

	/** The key pair. */
	private KeyPair keyPair;

	/**
	 * Instantiates a new key store loader.
	 *
	 * @param alias
	 *            the alias
	 */
	public KeyStoreLoader(String alias) {
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			this.alias = alias;
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Load from class loader by name and password.
	 *
	 * @param name
	 *            the name
	 * @param password
	 *            the password
	 * @throws KeyStoreLoaderException
	 *             the key store loader exception
	 */
	public void loadFromResource(String name, char[] password) throws KeyStoreLoaderException {
		loadFromResource(getClass(), name, password);
	}

	/**
	 * Load from class by name and password.
	 *
	 * @param clazz
	 *            the class
	 * @param name
	 *            the name
	 * @param password
	 *            the password
	 * @throws KeyStoreLoaderException
	 *             the key store loader exception
	 */
	public void loadFromResource(Class<?> clazz, String name, char[] password) throws KeyStoreLoaderException {
		loadFromResource(clazz.getClassLoader(), name, password);
	}

	/**
	 * Load from class loader by name and password.
	 *
	 * @param classLoader
	 *            the class loader
	 * @param name
	 *            the name
	 * @param password
	 *            the password
	 * @throws KeyStoreLoaderException
	 *             the key store loader exception
	 */
	public void loadFromResource(ClassLoader classLoader, String name, char[] password) throws KeyStoreLoaderException {
		loadFromStream(classLoader.getResourceAsStream(name), password);
	}

	/**
	 * Load from file by name and password.
	 *
	 * @param name
	 *            the name
	 * @param password
	 *            the password
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws KeyStoreLoaderException
	 *             the key store loader exception
	 */
	public void loadFromFile(String name, char[] password) throws FileNotFoundException, KeyStoreLoaderException {
		loadFromFile(new File(name), password);
	}

	/**
	 * Load from file by password.
	 *
	 * @param file
	 *            the file
	 * @param password
	 *            the password
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws KeyStoreLoaderException
	 *             the key store loader exception
	 */
	public void loadFromFile(File file, char[] password) throws FileNotFoundException, KeyStoreLoaderException {
		loadFromStream(new FileInputStream(file), password);
	}

	/**
	 * Load from stream by password.
	 *
	 * @param stream
	 *            the stream
	 * @param password
	 *            the password
	 * @throws KeyStoreLoaderException
	 *             the key store loader exception
	 */
	public void loadFromStream(InputStream stream, char[] password) throws KeyStoreLoaderException {
		try {
			keyStore.load(stream, password);
			certificate = (X509Certificate) keyStore.getCertificate(alias);
			PublicKey pubKey = certificate.getPublicKey();
			PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, password);
			keyPair = new KeyPair(pubKey, privKey);
		} catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException
				| UnrecoverableKeyException e) {
			throw new KeyStoreLoaderException(alias, e);
		}
	}

	@Override
	public X509Certificate getCertificate() {
		return certificate;
	}

	@Override
	public KeyPair getKeyPair() {
		return keyPair;
	}
}