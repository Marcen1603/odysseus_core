package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.impl.id.UUID.UUID;

/**
 * Class of messages to response to a {@link RecoveryUpdatePipeMessage}.
 * 
 * @author Michael Brand
 */
public class RecoveryUpdatePipeResponseMessage extends AbstractResponseMessage {

	/**
	 * Empty default constructor.
	 */
	public RecoveryUpdatePipeResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery update pipe acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryUpdatePipeMessage}
	 *            message. <br />
	 *            Must be not null.
	 */
	public RecoveryUpdatePipeResponseMessage(UUID uuid) {
		super(uuid);
	}

	/**
	 * Creates a new recovery update pipe failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryUpdatePipeMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public RecoveryUpdatePipeResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}

}