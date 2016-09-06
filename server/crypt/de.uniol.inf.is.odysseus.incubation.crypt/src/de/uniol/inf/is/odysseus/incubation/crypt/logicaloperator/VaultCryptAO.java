/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * @author MarkMilster
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CRYPT_VAULT", doc = "Cryptographic functionality with Loading Key out of Vault", category = {
		LogicalOperatorCategory.ADVANCED })
public class VaultCryptAO extends AbstractCryptAO implements KeyVaultLoader, ReceiverVaultLoader {

	private static final long serialVersionUID = -2223999564377471940L;

	private int keyID;
	private int receiverKeyID;
	private int streamID;

	@Override
	public int getReceiverID() {
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

	public VaultCryptAO() {
		super();
	}

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
	@Parameter(type = IntegerParameter.class, name = "receiverID", optional = false)
	public void setReceiverID(int receiverID) {
		this.receiverKeyID = receiverID;
	}

	@Override
	public VaultCryptAO clone() {
		return new VaultCryptAO(this);
	}

}
