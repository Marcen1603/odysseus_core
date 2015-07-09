package de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo;

import java.util.List;

/**
 * All information needed to recover a query added event (no source
 * definitions). <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and {@link #fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public class QueryAddedEntryInfo extends AbstractQueryAddedInfo {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -5645818017726810840L;

	/**
	 * The ids of all queries added by the event.
	 */
	public List<Integer> ids;

}