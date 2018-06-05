package de.uniol.inf.is.odysseus.incubation.crypt.util;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import de.uniol.inf.is.odysseus.incubation.crypt.client.IEncKeyReceiver;
import de.uniol.inf.is.odysseus.incubation.crypt.client.IPubKeyReceiver;
import de.uniol.inf.is.odysseus.incubation.crypt.client.KeyWebSocketClient;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultPrivKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.IPrivKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.IReceiver;

/**
 * This class handles the receiving of EncKeys und PubKeys out of the
 * keymanagement server. It should be used in the client.<br>
 * This class parses the keys to a cryptographic provider to use it.
 * 
 * @author MarkMilster
 *
 */
public class KeyReceiverClient implements IEncKeyReceiver, IPubKeyReceiver {

	private IReceiver receiver;

	public IReceiver getReceiver() {
		return this.receiver;
	}

	/**
	 * Constructor with the given Receiver. The Receiver is the class, who uses
	 * the keys for crypting. It could be a cryptographic provider.
	 * 
	 * @param receiver
	 *            The cryptographic provider, who will use the keys
	 */
	public KeyReceiverClient(IReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public void onPubKeyReceived(List<KeyWrapper<PublicKey>> pubKeyWrapper) {
		// ENCRYPTION MODE
		for (int i = 0; i < pubKeyWrapper.size(); i++) {
			this.encryptAndSendKey(pubKeyWrapper.get(i));
		}
	}

	/**
	 * Encrypts a symKey and sends the encrypted key to the keymanagement
	 * server.
	 * 
	 * @param pubKeyWrapper
	 *            The key to encrypt the symKeyWith
	 */
	private void encryptAndSendKey(KeyWrapper<PublicKey> pubKeyWrapper) {
		try {
			if (this.receiver == null) {
				throw new Exception("The physical operator or a other receiver has to be set");
			}
			HybridReceiverCryptor receiverCryptor = new HybridReceiverCryptor(pubKeyWrapper.getKey().getAlgorithm());
			KeyWrapper<Key> symKey = this.receiver.getKeyWrapper();
			int streamId = this.receiver.getStreamId();
			EncKeyWrapper encSymKeyWrapper = receiverCryptor.encryptSymKey(symKey, pubKeyWrapper, streamId);
			KeyWebSocketClient client;
			client = KeyWebSocketClient.instance();
			client.sendEncKeyMessage(encSymKeyWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEncKeyReceived(EncKeyWrapper encKey, int receiverHash) {
		synchronized (this.receiver) {
			if (this.receiver.hashCode() == receiverHash) {
				IPrivKeyVault privKeyVault = new DefaultPrivKeyVault();
				List<Integer> receiverId = this.receiver.getReceiverId();
				KeyWrapper<PrivateKey> privKeyWrapper = privKeyVault.getPrivateKey(receiverId).get(0);
				HybridReceiverCryptor receiverCryptor = new HybridReceiverCryptor(
						privKeyWrapper.getKey().getAlgorithm());
				KeyWrapper<Key> symKey = receiverCryptor.decryptSymKey(encKey, privKeyWrapper);
				this.receiver.getCryptor().setKey(symKey.getKey());
				this.receiver.setKeyReceived(true);
				this.receiver.notifyAll();
			}
		}
	}

}
