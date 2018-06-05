package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator;

import java.security.Key;
import java.util.List;

import javax.crypto.Cipher;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.incubation.crypt.client.KeyWebSocketClient;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.ISymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.KeyPunctuation;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;
import de.uniol.inf.is.odysseus.incubation.crypt.util.KeyReceiverClient;

/**
 * The physical Operator which, will crypt Datastreams and load the keys out of
 * vaults.
 * 
 * @author MarkMilster
 *
 */
public class VaultCryptPO<T extends IStreamObject<?>> extends SimpleCryptPO<T> implements IReceiver {

	private int keyId;

	/**
	 * At DECRYPT MODE your own receiverID is at position 0. <br>
	 * The other ids are only used at ENCRYPT MODE.
	 */
	private List<Integer> receiverId;

	/**
	 * Returns the receiverIDs
	 * 
	 * @return the receiverId
	 */
	public List<Integer> getReceiverId() {
		return receiverId;
	}

	private int streamId;

	/**
	 * Returns the streamIDs
	 * 
	 * @return the streamId
	 */
	public int getStreamId() {
		return streamId;
	}

	private boolean keyReceived = false;

	/**
	 * Return, if the keyis Received
	 * 
	 * @return the keyReceived
	 */
	public boolean isKeyReceived() {
		return keyReceived;
	}

	public void setKeyReceived(boolean keyReceived) {
		this.keyReceived = keyReceived;
	}

	private KeyWebSocketClient client;

	/**
	 * Returns the Client, which will be used for communication with the
	 * keymanagement server.
	 * 
	 * @return the client
	 */
	public KeyWebSocketClient getClient() {
		return client;
	}

	private KeyWrapper<Key> keyWrapper;

	public KeyWrapper<Key> getKeyWrapper() {
		return keyWrapper;
	}

	/**
	 * Constructor.
	 * 
	 * @param cryptor
	 *            The cryptor, which will be used for crypting the datastream
	 * @param inputSchema
	 *            The inputSchema of the dataStream
	 * @param restrictionList
	 *            The restrictionList of the Attributes, which will be crypted
	 * @param keyId
	 *            the id of the symmetric key, which will be used
	 * @param receiverId
	 *            the ids of the receiver with asymmetric keys
	 * @param streamId
	 *            the id of the crypting
	 * @param punctuationDelay
	 *            the amount of elements to delay the punctuation
	 */
	public VaultCryptPO(ICryptor cryptor, List<SDFAttribute> inputSchema, List<SDFAttribute> restrictionList, int keyId,
			List<Integer> receiverId, int streamId, Integer punctuationDelay) {
		super(cryptor, inputSchema, restrictionList, punctuationDelay);
		this.keyId = keyId;
		this.receiverId = receiverId;
		this.streamId = streamId;
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptPO
	 *            The VaultCryptPO to copy
	 */
	public VaultCryptPO(VaultCryptPO<T> cryptPO) {
		super(cryptPO);
		this.keyId = cryptPO.keyId;
		this.receiverId = cryptPO.receiverId;
		this.streamId = cryptPO.streamId;
	}

	@Override
	protected void process_open() {
		try {
			this.client = KeyWebSocketClient.instance();
			KeyReceiverClient receiver = new KeyReceiverClient(this);
			// init key
			if (this.cryptor.getMode() == Cipher.ENCRYPT_MODE) {
				client.addPubKeyReceiver(receiver);
				this.loadEncryptKey();
				client.sendGetPublicKeyMessage(this.receiverId);
			} else if (this.cryptor.getMode() == Cipher.DECRYPT_MODE) {
				client.addEncKeyListener(receiver);
				this.loadDecryptKey();
			} else {
				throw new Exception("Invallid Mode!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.cryptor.getMode() == Cipher.ENCRYPT_MODE) {
			this.sendNewKeyPunctuation();
		}
	}

	/**
	 * Loads the key out of the vault
	 */
	private void loadEncryptKey() {
		// load sym key
		ISymKeyVault symKeyVault = new DefaultSymKeyVault();
		this.keyWrapper = symKeyVault.getSymKey(this.keyId);
		cryptor.setKey(keyWrapper.getKey());
		if (!this.cryptor.isInitialized()) {
			try {
				this.cryptor.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads the key out of the vault
	 */
	protected void loadDecryptKey() {
		synchronized (this) {
			if (this.receiverId != null && this.receiverId.get(0) != null && this.client != null
					&& this.client.getEncKeyListener() != null) {
				client.sendGetEncKeyMessage(receiverId.get(0), streamId, this.hashCode());
				// the encKeyReceiver will set the new key here
				keyReceived = false;
				while (!keyReceived) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (!this.cryptor.isInitialized()) {
					// a new key was set
					this.cryptor.init();
				}
			}

		}
	}

	/**
	 * Sends a new keyPunctuation with the metadata of this operator
	 */
	protected void sendNewKeyPunctuation() {
		KeyPunctuation punctuation = KeyPunctuation.createNewKeyPunctuation(System.currentTimeMillis());
		for (Integer id : this.receiverId) {
			punctuation.addReceiverId(id);
		}
		punctuation.setStreamId(this.streamId);
		this.sendPunctuation(punctuation);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof KeyPunctuation && this.cryptor.getMode() == Cipher.DECRYPT_MODE) {
			// only in DECRYPT MODE
			// search for your own receiver id and load the new key
			KeyPunctuation keypunc = (KeyPunctuation) punctuation;
			if (this.streamId == keypunc.getStreamId()) {
				for (Integer id : keypunc.getReceiverId()) {
					if (this.receiverId.get(0).equals(id)) {
						// id is your own receiverID
						this.loadDecryptKey();
						break;
					}
				}
			}
		}
		super.processPunctuation(punctuation, port);
	}

}
