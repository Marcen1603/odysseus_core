package de.uniol.inf.is.odysseus.recovery.operatorstate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

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
	 * @param query
	 *            The logical query, which was transformed to the physical one.
	 * @throws IOException
	 *             if any error occurs.
	 */
	public static <K> void store(List<ControllablePhysicalSubscription<K>> queues, ILogicalQuery query)
			throws IOException {
		File file = new File(
				cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode() + cFileNameEnding);
		try (FileOutputStream fout = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			for (ControllablePhysicalSubscription<K> queue : queues) {
				queue.suspend();
				oos.writeObject(queue.getBufferedElements());
				queue.resume();
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
	 * @param query
	 *            The logical query, which was transformed to the physical one.
	 * @throws IOException
	 *             if any error occurs.
	 * @throws ClassNotFoundException
	 *             if the class of an object, which is read from file, can not
	 *             be found.
	 */
	public static <K> void load(List<ControllablePhysicalSubscription<K>> queues, ILogicalQuery query)
			throws IOException, ClassNotFoundException {
		File file = new File(
				cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode() + cFileNameEnding);
		if (!file.exists()) {
			cLog.error("File '{}' to load queue states from does not exist!", file.getName());
			return;
		}
		try (FileInputStream fin = new FileInputStream(file);
				OsgiObjectInputStream ois = new OsgiObjectInputStream(fin)) {
			for (ControllablePhysicalSubscription<K> queue : queues) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				ImmutableList<IStreamObject> state = (ImmutableList<IStreamObject>) ois.readObject();
				if (state == null) {
					throw new IOException("No state to load for queue " + queue);
				}
				queue.suspend();
				queue.setBufferedElements(state);
				queue.resume();
				cLog.debug("Loaded state of '{}' from file '{}'", queue, file.getName());
			}
		}
	}

	/**
	 * Creates a backup of the file, which contains the queue states for a given
	 * query. <br />
	 * The file has the same name but ends with "_old".
	 * 
	 * @param query
	 *            The logical query, which was transformed to the physical one.
	 * @throws IOException
	 *             if any error occurs.
	 * @throws ClassNotFoundException
	 *             if the class of an object, which is read from file, can not
	 *             be found.
	 */
	public static void backupFile(ILogicalQuery query) throws IOException, ClassNotFoundException {
		File fileToLoad = new File(
				cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode() + cFileNameEnding);
		File fileToStore = new File(cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode()
				+ "_old" + cFileNameEnding);
		if (!fileToLoad.exists()) {
			cLog.error("File '{}' to load queue states from does not exist!", fileToLoad.getName());
			return;
		}
		try (FileInputStream fin = new FileInputStream(fileToLoad);
				OsgiObjectInputStream ois = new OsgiObjectInputStream(fin);
				FileOutputStream fout = new FileOutputStream(fileToStore);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			oos.writeObject(ois.readObject());
			cLog.debug("Saved queue states of query '{}' in file '{}'", new Integer(query.getID()),
					fileToStore.getName());
		}
	}

	/**
	 * Deletes the file, which contains the queue states for a given query.
	 * 
	 * @param query
	 *            The logical query, which was transformed to the physical one.
	 */
	public static void deleteFile(ILogicalQuery query) {
		File file = new File(
				cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode() + cFileNameEnding);
		file.delete();
	}

}