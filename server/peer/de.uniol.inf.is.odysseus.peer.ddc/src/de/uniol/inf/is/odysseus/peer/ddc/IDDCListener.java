package de.uniol.inf.is.odysseus.peer.ddc;

/**
 * A DDC listener will be notified, if the DDC changes.
 * 
 * @author Michael Brand
 *
 */
public interface IDDCListener {

	/**
	 * Listener method for added entries.
	 * 
	 * @param entry
	 *            The added entry.
	 */
	void ddcEntryAdded(DDCEntry entry);

	/**
	 * Listener method for removed entries.
	 * 
	 * @param entry
	 *            The removed entry.
	 */
	void ddcEntryRemoved(DDCEntry ddcEntry);

}