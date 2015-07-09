package de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo;

/**
 * All information needed to recover a source definition added event. <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and {@link #fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public class SourceAddedEntryInfo extends AbstractQueryAddedInfo {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -9052770140496906254L;
	
	// No further fields.
}