package de.uniol.inf.is.odysseus.recovery.querystate;

/**
 * Helper class for the {@link QueryStateRecoveryComponent}.
 * 
 * @author Michael Brand
 *
 */
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
	 * @param id
	 *            The id of the query.
	 */
	public static final String getTagQueryChanged(int id) {
		return TAG_QUERYSTATECHANGED + " " + id;
	}

	/**
	 * Gets the query id hidden in a query changed tag.
	 * 
	 * @param queryChangeTag
	 *            The query change tag.
	 * @return A query id or -1, if any error occurred.
	 */
	public static final int getQueryId(String queryChangeTag) {
		String strId = queryChangeTag.replace(TAG_QUERYSTATECHANGED, "").trim();
		try {
			return Integer.parseInt(strId);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * The comment for the system log entry for a removed query.
	 */
	public static final String COMMENT_QUERYREMOVED = "REMOVED";

}