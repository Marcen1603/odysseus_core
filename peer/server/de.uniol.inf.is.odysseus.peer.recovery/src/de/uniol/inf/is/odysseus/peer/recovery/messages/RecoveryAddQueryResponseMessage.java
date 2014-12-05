package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.impl.id.UUID.UUID;

/**
 * Response Message for AddQuery Messages. The Response could be an ACK or a
 * fail. If the queryPart could not installed a fail response is send and the
 * query Part need to be reallocated. The response also contains most of the
 * information from the message.
 * 
 * @author ChrisToenjesDeye, Michael Brand
 * 
 */
public class RecoveryAddQueryResponseMessage  extends AbstractResponseMessage {
	/**
	 * Empty default constructor.
	 */
	public RecoveryAddQueryResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery add query acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryAddQueryMessage}
	 *            message. <br />
	 *            Must be not null.
	 */
	public RecoveryAddQueryResponseMessage(UUID uuid) {
		super(uuid);
	}

	/**
	 * Creates a new recovery add query failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryAddQueryMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public RecoveryAddQueryResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}
}