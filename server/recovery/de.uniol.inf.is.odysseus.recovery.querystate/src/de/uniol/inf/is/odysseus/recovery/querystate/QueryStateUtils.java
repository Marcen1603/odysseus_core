package de.uniol.inf.is.odysseus.recovery.querystate;

// TODO javaDoc
public class QueryStateUtils {

	/**
	 * The tag for the system log entry for an added query.
	 */
	public static final String TAG_QUERYADDED = "QUERYADDED";
	
	/**
	 * The prefix of the tag for the system log entry for an added query.
	 */
	public static final String TAG_QUERYSTATECHANGED = "QUERYSTATECHANGED";

	/**
	 * Gets the tag for the system log entry for a changed query state.
	 * 
	 * @param The
	 *            id of the query.
	 */
	public static final String getTagQueryChanged(int id) {
		return TAG_QUERYSTATECHANGED + " " + id;
	}

	/**
	 * The comment for the system log entry for a removed query.
	 */
	public static final String COMMENT_QUERYREMOVED = "REMOVED";
	
}