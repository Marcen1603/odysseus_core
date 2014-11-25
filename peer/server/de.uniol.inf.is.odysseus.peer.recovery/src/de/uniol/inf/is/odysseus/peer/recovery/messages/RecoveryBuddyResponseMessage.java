package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.impl.id.UUID.UUID;

/**
 * Class of messages to response to a {@link RecoveryBuddyMessage}.
 * 
 * @author Michael Brand
 */
public class RecoveryBuddyResponseMessage extends AbstractResponseMessage {

	/**
	 * Empty default constructor.
	 */
	public RecoveryBuddyResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery buddy acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryBuddyMessage}
	 *            message. <br />
	 *            Must be not null.
	 */
	public RecoveryBuddyResponseMessage(UUID uuid) {
		super(uuid);
	}

	/**
	 * Creates a new recovery buddy failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryBuddyMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public RecoveryBuddyResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}

}