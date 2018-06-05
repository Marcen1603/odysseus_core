package de.uniol.inf.is.odysseus.recovery.processingimage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;
import de.uniol.inf.is.odysseus.recovery.RecoveryConfiguration;

/**
 * Access to the files that contain the operator and queue states.
 * 
 * @author Michael Brand
 *
 */
public class ProcessingImageStore {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ProcessingImageStore.class);

	/**
	 * The prefix of all file names, where processing images are stored.
	 */
	private static final String FILEPREFIX = "query";

	/**
	 * The file ending (incl. dot) of all file names, where operator states are
	 * stored.
	 */
	private static final String OPERATOR_FILEENDING = ".operator";

	/**
	 * The file ending (incl. dot) of all file names, where queue states are
	 * stored.
	 */
	private static final String QUEUE_FILEENDING = ".queue";

	/**
	 * Gets the operator or queue file for a given query.
	 */
	private static File getFile(int queryId, boolean operator) {
		String name = RecoveryConfiguration.getRecoveryDirectory() + File.separator + FILEPREFIX + queryId;
		if (operator) {
			name += OPERATOR_FILEENDING;
		} else {
			name += QUEUE_FILEENDING;
		}
		return new File(name);
	}

	/**
	 * Stores the states of all {@link IStatefulPO}s of a query.
	 */
	public static void storeOperators(List<IStatefulPO> operators, int queryId) throws Exception {
		File file = getFile(queryId, true);
		try (FileOutputStream fout = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			for (int i = 0; i < operators.size(); i++) {
				IStatefulPO operator = operators.get(i);
				IOperatorState state = operator.getState();
				if (state != null) {
					oos.writeInt(i);
					oos.writeObject(operator.getState());
					LOG.debug("Wrote state of '{}' to file '{}'", operator, file.getName());
				}
			}
		}
	}

	/**
	 * Stores the state of all {@link ControllablePhysicalSubscription}s of a
	 * query.
	 */
	public static void storeQueues(List<ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?>> queues,
			int queryId) throws Exception {
		File file = getFile(queryId, false);
		try (FileOutputStream fout = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			for (int i = 0; i < queues.size(); i++) {
				ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?> queue = queues.get(i);
				@SuppressWarnings("rawtypes")
				List<IStreamObject> state = queue.getBufferedElements();
				if (state != null && !state.isEmpty()) {
					oos.writeInt(i);
					oos.writeObject(queue.getBufferedElements());
					LOG.debug("Wrote state of '{}' to file '{}'", queue, file.getName());
				}
			}
		}
	}

	/**
	 * Loads the states of all {@link IStatefulPO}s of a query.
	 */
	public static void loadOperators(List<IStatefulPO> operators, int queryId) throws Exception {
		File file = getFile(queryId, true);
		if (!file.exists()) {
			LOG.error("File '{}' to load operator states from does not exist!", file.getName());
			return;
		}
		try (FileInputStream fin = new FileInputStream(file);
				OsgiObjectInputStream ois = new OsgiObjectInputStream(fin)) {
			while (ois.available() > 0) {
				int i = ois.readInt();
				IStatefulPO operator = operators.get(i);
				operator.setState((Serializable) ois.readObject());
				LOG.debug("Loaded state of '{}' from file '{}'", operator, file.getName());
			}
		}
	}

	/**
	 * Loads the state of all {@link ControllablePhysicalSubscription}s of a
	 * query.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadQueues(List<ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?>> queues,
			int queryId) throws Exception {
		File file = getFile(queryId, false);
		if (!file.exists()) {
			LOG.error("File '{}' to load queue states from does not exist!", file.getName());
			return;
		}
		try (FileInputStream fin = new FileInputStream(file);
				OsgiObjectInputStream ois = new OsgiObjectInputStream(fin)) {
			while (ois.available() > 0) {
				int i = ois.readInt();
				ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?> queue = queues.get(i);
				queue.setBufferedElements((ImmutableList<IStreamObject>) ois.readObject());
				LOG.debug("Loaded state of '{}' from file '{}'", queue, file.getName());
			}
		}
	}

	public static void deleteProcessingImage(int queryId) throws Exception {
		File file = getFile(queryId, true);
		file.delete();
		LOG.debug("Deleted operator states file '{}'", file.getName());
		file = getFile(queryId, false);
		file.delete();
		LOG.debug("Deleted queue states file '{}'", file.getName());
	}

}