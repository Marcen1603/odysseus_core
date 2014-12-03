package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.impl.id.UUID.UUID;

/**
 * Class of messages to response to a {@link RecoveryAgreementMessage}.
 * 
 * @author Michael Brand
 */
public class RecoveryAgreementResponseMessage extends AbstractResponseMessage {

	/**
	 * Empty default constructor.
	 */
	public RecoveryAgreementResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery agreement acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryAgreementMessage}
	 *            message. <br />
	 *            Must be not null.
	 */
	public RecoveryAgreementResponseMessage(UUID uuid) {
		super(uuid);
	}

	/**
	 * Creates a new recovery agreement failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryAgreementMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public RecoveryAgreementResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}

}