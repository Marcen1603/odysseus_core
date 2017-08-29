package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * This class implements the LogicalOperator of VAULT_CRYPT. <br>
 * It will load the key out of a vault and encrypt it for some receivers. The
 * encrypted Key will be saved in a vault.
 * 
 * @author MarkMilster
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CRYPT_VAULT", doc = "Cryptographic functionality with Loading Key out of Vault and using hybrid cryptography.", category = {
		LogicalOperatorCategory.ADVANCED })
public class VaultCryptAO extends AbstractCryptAO implements KeyVaultLoader, ReceiverVaultLoader {

	private static final long serialVersionUID = -2223999564377471940L;

	private int keyID;
	private List<Integer> receiverKeyID;
	private int streamID;

	@Override
	public List<Integer> getReceiverID() {
		return this.receiverKeyID;
	}

	@Override
	public int getKeyID() {
		return this.keyID;
	}

	@Override
	public int getStreamID() {
		return this.streamID;
	}

	/**
	 * Default constructor.
	 */
	public VaultCryptAO() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param vaultCryptAO
	 *            The vaultCryptAO, which will be copied.
	 */
	public VaultCryptAO(VaultCryptAO vaultCryptAO) {
		super(vaultCryptAO);
		this.keyID = vaultCryptAO.keyID;
		this.receiverKeyID = vaultCryptAO.receiverKeyID;
		this.streamID = vaultCryptAO.streamID;
	}

	@Override
	@Parameter(type = IntegerParameter.class, name = "streamID", optional = false)
	public void setStreamID(int streamID) {
		this.streamID = streamID;
	}

	@Override
	@Parameter(type = IntegerParameter.class, name = "keyID", optional = true)
	public void setKeyID(int keyID) {
		this.keyID = keyID;
	}

	@Override
	@Parameter(type = IntegerParameter.class, name = "receiverID", optional = false, isList = true)
	public void setReceiverID(List<Integer> receiverID) {
		this.receiverKeyID = receiverID;
	}

	@Override
	public VaultCryptAO clone() {
		return new VaultCryptAO(this);
	}

}
