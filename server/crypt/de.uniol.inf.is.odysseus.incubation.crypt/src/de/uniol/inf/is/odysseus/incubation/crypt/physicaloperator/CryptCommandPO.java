package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.crypt.client.KeyWebSocketClient;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptedPredicatePunctuation;
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
public class CryptCommandPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements IReceiver {

	protected ICryptor cryptor;
	protected Map<String, String> parameter;
	protected Map<String, String> cryptedParameter;
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
		return parameter;
	}

	/**
	 * Sets the map of parameters, which will be crypted.
	 * 
	 * @param parameter
	 *            The key represents the occurrence of the parameter. <br>
	 *            The value is the parameter, which will be crypted
	 */
	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
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
	public CryptCommandPO(ICryptor cryptor, int receiverId, int streamId, Map<String, String> parameter) {
		super();
		this.cryptor = cryptor;
		this.receiverId = receiverId;
		this.streamId = streamId;
		this.parameter = parameter;
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptCommandPO
	 *            The cryptCommandPO, which will be copied
	 */
	public CryptCommandPO(CryptCommandPO<T> cryptCommandPO) {
		super(cryptCommandPO);
		this.cryptor = cryptCommandPO.cryptor;
		this.receiverId = cryptCommandPO.receiverId;
		this.streamId = cryptCommandPO.streamId;
		this.parameter = cryptCommandPO.parameter;
		this.cryptedParameter = cryptCommandPO.cryptedParameter;
	}

	@Override
	protected void process_open() {
		try {
			client = KeyWebSocketClient.instance();
			KeyReceiverClient receiver = new KeyReceiverClient(this);
			client.addPubKeyReceiver(receiver);
			client.addEncKeyListener(receiver);
			// init key
			this.loadEncryptKey();
			this.cryptParameter();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		// TODO cryptParameters() in extra util Klasse auslagern?
		this.cryptedParameter = new HashMap<>();
		for (Entry<String, String> entry : this.parameter.entrySet()) {
			String key = entry.getKey();
			String param = entry.getValue();
			String crypted = this.cryptor.cryptBase64String(param);
			System.out.println(param + " crypted to: " + crypted);
			this.cryptedParameter.put(key, crypted);
			// TODO fire CryptPredicatesPunctuation
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		CryptedPredicatePunctuation predPunc = null;
		if (punctuation instanceof KeyPunctuation) {
			KeyPunctuation keypunc = (KeyPunctuation) punctuation;
			if (this.streamId == keypunc.getStreamId()) {
				for (Integer id : keypunc.getReceiverId()) {
					if (this.receiverId == id.intValue()) {
						// id is your own receiverID
						this.loadEncryptKey();
						this.cryptParameter();
						predPunc = CryptedPredicatePunctuation
								.createNewCryptedPredicatePunctuation(System.currentTimeMillis());
						predPunc.setCryptedPredicates(this.cryptedParameter);
						break;
					}
				}
			}
		}
		if (predPunc != null) {
			this.sendPunctuation(predPunc);
		}
		this.sendPunctuation(punctuation);
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
