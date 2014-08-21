package de.uniol.inf.is.odysseus.peer.ddc;

/**
 * A missing DDC entry exception indicates, that the DDC does not contain an
 * entry for a given key.
 * 
 * @author Michael Brand
 *
 */
public class MissingDDCEntryException extends Exception {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 5742484928546118308L;

	/**
	 * Creates a new missing DDC entry exception for a given key.
	 * 
	 * @param key
	 *            The given key.
	 */
	public MissingDDCEntryException(String[] key) {

		super("Missing DDC entry for key '" + key + "'!");

	}

}