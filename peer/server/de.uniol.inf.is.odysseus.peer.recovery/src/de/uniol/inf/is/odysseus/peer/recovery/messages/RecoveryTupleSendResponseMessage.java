package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.impl.id.UUID.UUID;

/**
 * Class of messages to response to a {@link RecoveryTupleSendMessage}.
 * 
 * @author Michael Brand
 */
public class RecoveryTupleSendResponseMessage extends AbstractResponseMessage {

	/**
	 * Empty default constructor.
	 */
	public RecoveryTupleSendResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery tuple send acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryTupleSendMessage}
	 *            message. <br />
	 *            Must be not null.
	 */
	public RecoveryTupleSendResponseMessage(UUID uuid) {
		super(uuid);
	}

	/**
	 * Creates a new recovery tuple send failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryTupleSendMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public RecoveryTupleSendResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}

}