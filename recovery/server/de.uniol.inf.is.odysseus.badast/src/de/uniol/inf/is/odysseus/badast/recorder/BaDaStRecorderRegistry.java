package de.uniol.inf.is.odysseus.badast.recorder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.badast.BaDaStSender;

/**
 * Registry for BaDaSt recorders.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStRecorderRegistry {

	/**
	 * All registered BaDaSt recorders (their names) mapped to the source names.
	 */
	private static final Map<String, String> recorders = new HashMap<>();

	// if Odysseus has been crashed, some recorders may exist, when Odysseus
	// restarts
	static {
		if (BaDaStSender.isBaDaStServerAvailable()) {
			Set<String> existingRecorders = BaDaStSender.sendLsRecordersComand();
			for (String recorder : existingRecorders) {
				// each recorder name has the pattern recordertype_sourcename
				register(recorder.split("_")[1].trim(), recorder);
			}
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
		if (sourcename != null && recorder != null && !recorders.containsKey(sourcename)) {
			recorders.put(sourcename, recorder);
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
			recorders.remove(sourcename);
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
			return recorders.get(sourcename);
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
		return recorders.keySet();
	}

}