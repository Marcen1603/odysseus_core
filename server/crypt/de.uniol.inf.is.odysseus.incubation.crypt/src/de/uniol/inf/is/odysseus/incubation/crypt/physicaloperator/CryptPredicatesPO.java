package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.crypt.client.KeyWebSocketClient;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptPunctuation;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptedPredicatesPunctuation;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptedValue;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.KeyPunctuation;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;
import de.uniol.inf.is.odysseus.incubation.crypt.util.KeyReceiverClient;

/**
 * This is the physical Operator, to crypt Parameters for querying on crypted
 * Data streams. The data tuples will not be modiefied.
 * 
 * @author MarkMilster
 *
 */
public class CryptPredicatesPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements IReceiver {

	protected ICryptor cryptor;
	protected Map<String, String> originalPredicate;
	protected Map<String, CryptedValue> cryptedPredicate = new HashMap<>();
	protected int receiverId;
	protected int streamId;

	private KeyWrapper<Key> keyWrapper;
	private boolean keyReceived = false;
	private KeyWebSocketClient client;

	/**
	 * Returns the client, which will be used for communication with the
	 * keymanagement server.
	 * 
	 * @return the client
	 */
	public KeyWebSocketClient getClient() {
		return client;
	}

	/**
	 * Returns the Cryptor, which will be used for crypting the parameters.
	 * 
	 * @return the cryptor
	 */
	public ICryptor getCryptor() {
		return cryptor;
	}

	/**
	 * Sets the Cryptor, which will be used for crypting the parameters.
	 * 
	 * @param cryptor
	 *            the cryptor to set
	 */
	public void setCryptor(ICryptor cryptor) {
		this.cryptor = cryptor;
	}

	/**
	 * Returns the map of parameters, which will be crypted.
	 * 
	 * @return The key represents the occurrence of the parameter. <br>
	 *         The value is the parameter, which will be crypted
	 */
	public Map<String, String> getParameter() {
		return originalPredicate;
	}

	/**
	 * Sets the map of parameters, which will be crypted.
	 * 
	 * @param parameter
	 *            The key represents the occurrence of the parameter. <br>
	 *            The value is the parameter, which will be crypted
	 */
	public void setParameter(Map<String, String> parameter) {
		this.originalPredicate = parameter;
	}

	/**
	 * Constructor, which will create an operator with the specific parameters.
	 * 
	 * @param cryptor
	 *            The cryptor, which will be used for crypting the parameters
	 * @param receiverId
	 *            The id of this receiver, which will specify the asymmetric
	 *            keys
	 * @param streamId
	 *            The id of the crypting, for which the parameters will be used
	 * @param parameter
	 *            The parameters, which will be crypted
	 */
	public CryptPredicatesPO(ICryptor cryptor, int receiverId, int streamId, Map<String, String> predicate) {
		super();
		this.cryptor = cryptor;
		this.receiverId = receiverId;
		this.streamId = streamId;
		this.originalPredicate = predicate;
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptPredicatesPO
	 *            The cryptPredicatesPO, which will be copied
	 */
	public CryptPredicatesPO(CryptPredicatesPO<T> cryptPredicatesPO) {
		super(cryptPredicatesPO);
		this.cryptor = cryptPredicatesPO.cryptor;
		this.receiverId = cryptPredicatesPO.receiverId;
		this.streamId = cryptPredicatesPO.streamId;
		this.originalPredicate = cryptPredicatesPO.originalPredicate;
		this.cryptedPredicate = cryptPredicatesPO.cryptedPredicate;
	}

	@Override
	protected void process_open() {
		try {
			client = KeyWebSocketClient.instance();
			KeyReceiverClient receiver = new KeyReceiverClient(this);
			client.addPubKeyReceiver(receiver);
			client.addEncKeyListener(receiver);
			this.loadEncryptKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.cryptParameter();
		this.sendNewPunctuation(new PointInTime(System.currentTimeMillis()));
	}

	/**
	 * Loads the key out of the vault
	 */
	protected void loadEncryptKey() {
		synchronized (this) {
			if (this.client != null && this.client.getEncKeyListener() != null) {
				client.sendGetEncKeyMessage(receiverId, streamId, this.hashCode());
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
	 * Crypts the specified parameters.
	 */
	protected void cryptParameter() {
		for (Entry<String, String> entry : this.originalPredicate.entrySet()) {
			String oldKey = entry.getKey();
			Object originalValue = entry.getValue();
			Object crypted = this.cryptor.cryptObjectViaString(originalValue);
			System.out.println(originalValue + " crypted to: " + crypted.toString());
			CryptedValue oldCryptedValue = this.cryptedPredicate.get(oldKey);
			String oldCryptedString = null;
			String newKey = null;
			if (oldCryptedValue == null) {
				oldCryptedString = originalValue.toString();
				newKey = oldKey;
			} else {
				oldCryptedString = oldCryptedValue.getNewValue();
				newKey = oldKey.replaceAll(oldCryptedValue.getOldValue(), oldCryptedString);
			}
			CryptedValue newCryptedValue = new CryptedValue(oldCryptedString, crypted.toString());
			this.cryptedPredicate.put(newKey, newCryptedValue);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof KeyPunctuation) {
			KeyPunctuation keypunc = (KeyPunctuation) punctuation;
			if (this.streamId == keypunc.getStreamId()) {
				for (Integer id : keypunc.getReceiverId()) {
					if (this.receiverId == id.intValue()) {
						// id is your own receiverID
						try {
							this.loadEncryptKey();
							this.cryptParameter();
							this.sendNewPunctuation(punctuation.getTime());
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else if (punctuation instanceof CryptPunctuation) {
			if (this.cryptor.isInitialized()) {
				this.sendNewPunctuation(punctuation.getTime());
			}
		}
		this.sendPunctuation(punctuation);
	}

	/**
	 * Creates and sends a new CryptedPredicatePunctuation<br>
	 * The punctuation contains the actual cryptedPredicates Map
	 */
	private void sendNewPunctuation(PointInTime point) {
		CryptedPredicatesPunctuation predPunc = CryptedPredicatesPunctuation
				.createNewCryptedPredicatesPunctuation(point);
		predPunc.setCryptedPredicates((HashMap<String, CryptedValue>) this.cryptedPredicate);
		this.sendPunctuation(predPunc);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		this.transfer(object);
	}

	@Override
	public KeyWrapper<Key> getKeyWrapper() {
		return this.keyWrapper;
	}

	@Override
	public int getStreamId() {
		return this.streamId;
	}

	@Override
	public List<Integer> getReceiverId() {
		List<Integer> list = new ArrayList<>();
		list.add(this.receiverId);
		return list;
	}

	@Override
	public void setKeyReceived(boolean keyReceived) {
		this.keyReceived = keyReceived;
	}

}
