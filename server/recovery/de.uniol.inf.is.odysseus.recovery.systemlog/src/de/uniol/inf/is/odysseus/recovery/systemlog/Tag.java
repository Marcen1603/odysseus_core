package de.uniol.inf.is.odysseus.recovery.systemlog;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Optional;

//XXX SystemLogController: Not the best way to fill the permission
// mapping this way.
/**
 * Enumeration of all tags within system log entries and entities, which have
 * the permission to write those tags.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public enum Tag {

	/**
	 * The tag for the system log entry for a startup of Odysseus.
	 */
	STARTUP(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal.SystemStateLogger")) {

		@Override
		public String toString() {
			return "STARTUP";
		}

	},

	/**
	 * The tag for the system log entry for a shutdown of Odysseus.
	 */
	SHUTDOWN(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal.SystemStateLogger")) {

		@Override
		public String toString() {
			return "SHUTDOWN";
		}

	},

	/**
	 * The tag for the system log entry for a crash of Odysseus.
	 */
	CRASH_DETECTED(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal.CrashDetector")) {

		@Override
		public String toString() {
			return "CRASHDETECTED";
		}

	},

	/**
	 * The tag for system log entries identifying an added query.
	 */
	QUERY_ADDED(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.querystate.QueryStateRecoveryComponent")) {

		@Override
		public String toString() {
			return "QUERYADDED";
		}

	},

	/**
	 * The tag for system log entries identifying an added sink.
	 */
	SINK_ADDED(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.querystate.QueryStateRecoveryComponent")) {

		@Override
		public String toString() {
			return "SINKADDED";
		}

	},

	/**
	 * The tag for system log entries identifying an added source.
	 */
	SOURCE_ADDED(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.querystate.QueryStateRecoveryComponent")) {

		@Override
		public String toString() {
			return "SOURCEADDED";
		}

	},

	/**
	 * The tag for system log entries identifying a removed source.
	 */
	SOURCE_REMOVED(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.querystate.QueryStateRecoveryComponent")) {

		@Override
		public String toString() {
			return "SOURCEREMOVED";
		}

	},

	/**
	 * The tag for system log entries identifying a removed sink.
	 */
	SINK_REMOVED(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.querystate.QueryStateRecoveryComponent")) {

		@Override
		public String toString() {
			return "SINKREMOVED";
		}

	},

	/**
	 * The tag for system log entries identifying a changed query state.
	 */
	QUERYSTATE_CHANGED(Arrays.asList("de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog",
			"de.uniol.inf.is.odysseus.recovery.querystate.QueryStateRecoveryComponent")) {

		@Override
		public String toString() {
			return "QUERYSTATECHANGED";
		}

	};

	/**
	 * List of names. Each name represents an entity, which has the permission
	 * to write the tag.
	 */
	private final List<String> mGrantedNames;

	/**
	 * Gets all entities, which have the permission to write the tag.
	 * 
	 * @return List of names.
	 */
	public List<String> getGrantedNames() {
		return this.mGrantedNames;
	}

	/**
	 * Creates a new enum entry.
	 * 
	 * @param names
	 *            List of names. Each name represents an entity, which has the
	 *            permission to write the tag.
	 */
	private Tag(List<String> names) {
		this.mGrantedNames = names;
	}

	/**
	 * Gets an enum entry for a given tag.
	 * 
	 * @param tag
	 *            The string representation of the tag.
	 * @return The enum entry represented by the tag, if present.
	 */
	public static Optional<Tag> fromString(String tag) {
		for (Tag value : values()) {
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