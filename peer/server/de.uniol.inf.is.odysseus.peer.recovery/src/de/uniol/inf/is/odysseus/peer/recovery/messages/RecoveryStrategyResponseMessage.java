package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.impl.id.UUID.UUID;

/**
 * Response Message for Recovery Strategy Messages.
 * 
 * @author Michael Brand
 * 
 */
public class RecoveryStrategyResponseMessage extends AbstractResponseMessage {
	/**
	 * Empty default constructor.
	 */
	public RecoveryStrategyResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery add query acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryStrategyMessage}
	 *            message. <br />
	 *            Must be not null.
	 */
	public RecoveryStrategyResponseMessage(UUID uuid) {
		super(uuid);
	}

	/**
	 * Creates a new recovery add query failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryStrategyMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public RecoveryStrategyResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}
}