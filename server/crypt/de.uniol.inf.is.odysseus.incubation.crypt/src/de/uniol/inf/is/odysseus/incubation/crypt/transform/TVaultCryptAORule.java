package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.client.IEncKeyReceiver;
import de.uniol.inf.is.odysseus.incubation.crypt.client.IPubKeyReceiver;
import de.uniol.inf.is.odysseus.incubation.crypt.client.KeyWebSocketClient;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultPrivKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.IPrivKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.ISymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.AbstractCryptAO;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.VaultCryptAO;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.SimpleCryptPO;
import de.uniol.inf.is.odysseus.incubation.crypt.util.HybridReceiverCryptor;

/**
 * @author MarkMilster
 *
 */
public class TVaultCryptAORule extends TAbstractCryptAORule<VaultCryptAO> implements IEncKeyReceiver, IPubKeyReceiver {

	private KeyWrapper<Key> keyWrapper;
	private VaultCryptAO cryptAO;
	private KeyWebSocketClient client;
	private TransformationConfiguration transformConfig;

	@Override
	public void execute(VaultCryptAO cryptAO, TransformationConfiguration transformConfig) {
		try {
			super.execute(cryptAO, transformConfig);
			this.cryptAO = cryptAO;
			this.transformConfig = transformConfig;
			client = KeyWebSocketClient.instance();
			if (cryptAO.getMode().equals(AbstractCryptAO.ENCRYPT_MODE)) {
				ISymKeyVault symKeyVault = new DefaultSymKeyVault();
				this.keyWrapper = symKeyVault.getSymKey(cryptAO.getKeyID());
				cryptor.setKey(keyWrapper.getKey());
				// TODO Nice to have: sicherstellen, dass nur zur db geschrieben
				// wird, wenn
				// erfolgreich (Konsistenz waren)
				client.sendGetPublicKeyMessage(cryptAO.getReceiverID());
			} else if (cryptAO.getMode().equals(AbstractCryptAO.DECRYPT_MODE)) {
				this.client.sendGetEncKeyMessage(this.cryptAO.getReceiverID(), this.cryptAO.getStreamID());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "VaultCryptAO -> SimpleCryptPO";
	}

	@Override
	public Class<? super VaultCryptAO> getConditionClass() {
		return VaultCryptAO.class;
	}

	@Override
	public void onPubKeyReceived(KeyWrapper<PublicKey> pubKeyWrapper) {
		// This is only in ENCRYPTION_MODE
		HybridReceiverCryptor receiverCryptor = new HybridReceiverCryptor(pubKeyWrapper.getKey().getAlgorithm());
		EncKeyWrapper encSymKeyWrapper = receiverCryptor.encryptSymKey(keyWrapper, pubKeyWrapper,
				cryptAO.getStreamID());
		// TODO ausblick: in keyManager keys zwischenspeichern
		client.sendEncKeyMessage(encSymKeyWrapper);
		// TODO Doku:
		// ich nutze doch die db (nicht securityPunctuation), weil der
		// key nur einmal genau beim
		// starten der anfrage geladen wird, und man nicht davon
		// ausgehen kann, dass gerade dann die punctuation ankommt
		this.startExecute();
	}

	@Override
	public void onEncKeyReceived(EncKeyWrapper encKey) {
		// EncKeyWrapper encSymKey =
		// keyManager.getEncSymKey(cryptAO.getReceiverID(),
		// cryptAO.getStreamID());
		// TODO an den keymanager anfrage ueber internet stellen
		IPrivKeyVault privKeyVault = new DefaultPrivKeyVault();
		KeyWrapper<PrivateKey> privKeyWrapper = privKeyVault.getPrivateKey(cryptAO.getReceiverID());
		HybridReceiverCryptor receiverCryptor = new HybridReceiverCryptor(privKeyWrapper.getKey().getAlgorithm());
		KeyWrapper<Key> symKey = receiverCryptor.decryptSymKey(encKey, privKeyWrapper);
		cryptor.setKey(symKey.getKey());
		this.startExecute();
	}

	private void startExecute() {
		SimpleCryptPO<?> cryptPO = new SimpleCryptPO<>(cryptor, inputSchema, restrictionList);
		defaultExecute(cryptAO, cryptPO, transformConfig, true, true);
	}

}
