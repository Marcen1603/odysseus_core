package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.impl.id.UUID.UUID;

/**
 * The acknowledge message for backup information.
 * 
 * @see BackupInformationMessage
 * @author Michael Brand
 *
 */
public class BackupInformationResponseMessage extends AbstractResponseMessage {

	/**
	 * Empty default constructor.
	 */
	public BackupInformationResponseMessage() {

		// Empty default constructor.

	}

	/**
	 * Creates a new backup information acknowledge message.
	 * 
	 * @param id
	 *            The id of the received message. <br />
	 *            Must be not null.
	 */
	public BackupInformationResponseMessage(UUID id) {
		super(id);
	}
	
	/**
	 * Creates a new backup information failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryUpdatePipeMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public BackupInformationResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}

}