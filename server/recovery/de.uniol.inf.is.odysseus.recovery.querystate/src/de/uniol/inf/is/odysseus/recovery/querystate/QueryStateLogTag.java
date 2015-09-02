package de.uniol.inf.is.odysseus.recovery.querystate;

import com.google.common.base.Optional;

/**
 * Enumeration of all tags within system log entries used to backup query
 * states.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public enum QueryStateLogTag {

	/**
	 * The tag for system log entries identifying an added query.
	 */
	QUERY_ADDED {

		@Override
		public String toString() {
			return "QUERYADDED";
		}

	},

	/**
	 * The tag for system log entries identifying an added sink.
	 */
	SINK_ADDED {

		@Override
		public String toString() {
			return "SINKADDED";
		}

	},

	/**
	 * The tag for system log entries identifying an added source.
	 */
	SOURCE_ADDED {

		@Override
		public String toString() {
			return "SOURCEADDED";
		}

	},

	/**
	 * The tag for system log entries identifying a removed source.
	 */
	SOURCE_REMOVED {

		@Override
		public String toString() {
			return "SOURCEREMOVED";
		}

	},

	/**
	 * The tag for system log entries identifying a removed sink.
	 */
	SINK_REMOVED {

		@Override
		public String toString() {
			return "SINKREMOVED";
		}

	},

	/**
	 * The tag for system log entries identifying a changed query state.
	 */
	QUERYSTATE_CHANGED {

		@Override
		public String toString() {
			return "QUERYSTATECHANGED";
		}

	};

	/**
	 * Gets an enum entry for a given tag.
	 * 
	 * @param tag
	 *            The string representation of the tag.
	 * @return The enum entry represented by the tag, if present.
	 */
	public static Optional<QueryStateLogTag> fromString(String tag) {
		for (QueryStateLogTag value : values()) {
			if (value.toString().equals(tag)) {
				return Optional.of(value);
			}
		}
		return Optional.absent();
	}

	/**
	 * Checks, if there is an enum entry for a given tag.
	 * 
	 * @param tag
	 *            The string representation of the tag.
	 * @return True, if there is an enum entry represented by the tag.
	 */
	public static boolean containsTag(String tag) {
		return fromString(tag).isPresent();
	}

}