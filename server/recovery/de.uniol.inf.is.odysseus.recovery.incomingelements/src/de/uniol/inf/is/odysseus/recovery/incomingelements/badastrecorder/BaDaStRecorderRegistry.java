package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

/**
 * Registry for BaDaSt recorders.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class BaDaStRecorderRegistry {

	/**
	 * All registered BaDaSt recorders (their names) mapped to the source names.
	 */
	private static final Map<String, String> cRecorders = Maps.newHashMap();
	
	// if Odysseus has been crashed, some recorders may exist, when Odysseus restarts
	static {
		Set<String> existingRecorders = BaDaStSender.sendLsRecordersComand();
		for(String recorder : existingRecorders) {
			// each recorder name has the pattern recordertype_sourcename
			register(recorder.split("_")[1].trim(), recorder);
		}
	}

	/**
	 * Registers a BaDaSt recorder for a given source.
	 * 
	 * @param sourcename
	 *            The name of the source.
	 * @param recorder
	 *            The name of the recorder.
	 * @return True, if {@code sourcename} and {@code recorder} are not null and
	 *         there is no other recorder registered for {@code sourcename}.
	 */
	public static boolean register(String sourcename, String recorder) {
		if (sourcename != null && recorder != null
				&& !cRecorders.containsKey(sourcename)) {
			cRecorders.put(sourcename, recorder);
			return true;
		}
		return false;
	}

	/**
	 * Removes the registration of a source (and it's recorder).
	 * 
	 * @param sourcename
	 *            The name of the source.
	 */
	public static void unregister(String sourcename) {
		if (sourcename != null) {
			cRecorders.remove(sourcename);
		}
	}

	/**
	 * Gets a registered BaDaSt recorder for a given source.
	 * 
	 * @param sourcename
	 *            The name of the source.
	 * @return The name of the registered BaDaSt recorder, if there is one
	 *         registered for {@code sourcename}.
	 */
	public static String getRecorder(String sourcename) {
		if (sourcename != null) {
			return cRecorders.get(sourcename);
		}
		return null;
	}

	/**
	 * Gets all recorded sources.
	 * 
	 * @return A set of source names, for which a BaDaSt recorder has been
	 *         registered.
	 */
	public static Set<String> getRecordedSources() {
		return cRecorders.keySet();
	}

}