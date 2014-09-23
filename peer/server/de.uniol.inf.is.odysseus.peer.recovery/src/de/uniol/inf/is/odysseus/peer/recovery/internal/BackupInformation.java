package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jxta.peer.PeerID;

/**
 * Backup information of a shared query is a map of query parts (their pql
 * statements) to the allocated peer.
 * 
 * @author Michael Brand
 *
 */
public final class BackupInformation extends
		HashMap<PeerID, Collection<String>> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -1394696507182260061L;

	/**
	 * Creates empty backup information.
	 */
	public BackupInformation() {

		super();

	}

	/**
	 * Creates backup information from existing one.
	 * 
	 * @param information
	 *            The information to create from. <br />
	 *            Must not be null.
	 */
	public BackupInformation(Map<PeerID, Collection<String>> information) {

		super(information);

	}

}