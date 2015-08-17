package de.uniol.inf.is.odysseus.recovery.queuestate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;

/**
 * Helper class to queue operator states into a file. <br />
 * <br />
 * A queue state is the collection of buffered elements of a
 * {@link ControllablePhysicalSubscription},
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class QueueStateStore {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(QueueStateStore.class);

	/**
	 * The directory for queue state files.
	 */
	private static final File cRecoveryDir = new File(OdysseusBaseConfiguration.getHomeDir() + "recovery");

	static {
		if (!cRecoveryDir.isDirectory()) {
			cRecoveryDir.mkdir();
		}
	}

	/**
	 * The prefix of all file names, where queue states are stored.
	 */
	private static final String cFileNamePrefix = "query";

	/**
	 * The file ending (incl. dot) of all file names, where queue states are
	 * stored.
	 */
	private static final String cFileNameEnding = ".queue";

	/**
	 * Stores the states of all {@link ControllablePhysicalSubscription}s of a
	 * physical query.
	 * 
	 * @param queues
	 *            The queues, which states have to be stored.
	 * @param queryId
	 *            The id of the query (for identification).
	 * @throws IOException
	 *             if any error occurs.
	 */
	public static <K> void store(Collection<ControllablePhysicalSubscription<K>> queues, int queryId)
			throws IOException {
		File file = new File(cRecoveryDir + File.separator + cFileNamePrefix + queryId + cFileNameEnding);
		try (FileOutputStream fout = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			for (ControllablePhysicalSubscription<K> queue : queues) {
				oos.writeObject(queue.getBufferedElements());
				cLog.debug("Wrote state of '{}' to file '{}'", queue, file.getName());
			}
		}
	}

	/**
	 * Loads the states of all {@link ControllablePhysicalSubscription}s of a
	 * physical query.
	 * 
	 * @param queues
	 *            The queues, which states have to be loaded.
	 * @param queryId
	 *            The id of the query (for identification).
	 * @throws IOException
	 *             if any error occurs.
	 * @throws ClassNotFoundException
	 *             if the class of an object, which is read from file, can not
	 *             be found.
	 */
	public static <K> void load(Collection<ControllablePhysicalSubscription<K>> queues, int queryId)
			throws IOException, ClassNotFoundException {
		File file = new File(cRecoveryDir + File.separator + cFileNamePrefix + queryId + cFileNameEnding);
		if (!file.exists()) {
			cLog.error("File '{}' to load queue states from does not exist!", file.getName());
			return;
		}
		try (FileInputStream fin = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fin)) {
			for (ControllablePhysicalSubscription<K> queue : queues) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				ImmutableList<IStreamObject> state = (ImmutableList<IStreamObject>) ois.readObject();
				if (state == null) {
					throw new IOException("No state to load for queue " + queue);
				}
				queue.setBufferedElements(state);
				cLog.debug("Loaded state of '{}' from file '{}'", queue, file.getName());
			}
		}
	}

}