package de.uniol.inf.is.odysseus.recovery.operatorstate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

/**
 * Helper class to store operator states into a file.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class OperatorStateStore {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(OperatorStateStore.class);

	/**
	 * The directory for operator state files.
	 */
	private static final File cRecoveryDir = new File(OdysseusBaseConfiguration.getHomeDir() + "recovery");

	static {
		if (!cRecoveryDir.isDirectory()) {
			cRecoveryDir.mkdir();
		}
	}

	/**
	 * The prefix of all file names, where operator states are stored.
	 */
	private static final String cFileNamePrefix = "query";

	/**
	 * The file ending (incl. dot) of all file names, where operator states are
	 * stored.
	 */
	private static final String cFileNameEnding = ".operator";

	/**
	 * Stores the states of all {@link IStatefulPO}s of a physical query.
	 * 
	 * @param operators
	 *            The operators, which states have to be stored.
	 * @param query
	 *            The logical query, which was transformed to the physical one.
	 * @throws IOException
	 *             if any error occurs.
	 */
	public static void store(List<IStatefulPO> operators, ILogicalQuery query) throws IOException {
		File file = new File(
				cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode() + cFileNameEnding);
		try (FileOutputStream fout = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			for (IStatefulPO operator : operators) {
				startBuffering((ISink<?>) operator);
				oos.writeObject(operator.getState());
				cLog.debug("Wrote state of '{}' to file '{}'", operator, file.getName());
				stopBuffering((ISink<?>) operator);
			}
		}
	}

	/***
	 * Starts buffering before a given Operator.
	 * 
	 * @param operator
	 *            The given operator.
	 */
	private static void startBuffering(ISink<?> operator) {
		for (AbstractPhysicalSubscription<?> sub : operator.getSubscribedToSource()) {
			for (AbstractPhysicalSubscription<?> subTo : ((ISource<?>) sub.getTarget()).getSubscriptions()) {
				if (subTo.getTarget() != operator) {
					continue;
				} else if (subTo instanceof ControllablePhysicalSubscription) {
					((ControllablePhysicalSubscription<?>) subTo).suspend();
				}
			}
		}
	}

	/***
	 * Stops buffering before a given Operator.
	 * 
	 * @param operator
	 *            The given operator.
	 */
	private static void stopBuffering(ISink<?> operator) {
		for (AbstractPhysicalSubscription<?> sub : operator.getSubscribedToSource()) {
			for (AbstractPhysicalSubscription<?> subTo : ((ISource<?>) sub.getTarget()).getSubscriptions()) {
				if (subTo.getTarget() != operator) {
					continue;
				} else if (subTo instanceof ControllablePhysicalSubscription) {
					((ControllablePhysicalSubscription<?>) subTo).resume();
				}
			}
		}
	}

	/**
	 * Loads the states of all {@link IStatefulPO}s of a physical query.
	 * 
	 * @param operators
	 *            The operators, which states have to be loaded.
	 * @param query
	 *            The logical query, which was transformed to the physical one.
	 * @throws IOException
	 *             if any error occurs.
	 * @throws ClassNotFoundException
	 *             if the class of an object, which is read from file, can not
	 *             be found.
	 */
	public static void load(List<IStatefulPO> operators, ILogicalQuery query)
			throws IOException, ClassNotFoundException {
		File file = new File(
				cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode() + cFileNameEnding);
		if (!file.exists()) {
			cLog.error("File '{}' to load operator states from does not exist!", file.getName());
			return;
		}
		try (FileInputStream fin = new FileInputStream(file);
				OsgiObjectInputStream ois = new OsgiObjectInputStream(fin)) {
			for (IStatefulPO operator : operators) {
				Serializable state = (Serializable) ois.readObject();
				if (state == null) {
					throw new IOException("No state to load for operator " + operator);
				}
				startBuffering((ISink<?>) operator);
				operator.setState(state);
				cLog.debug("Loaded state of '{}' from file '{}'", operator, file.getName());
				stopBuffering((ISink<?>) operator);
			}
		}
	}

	/**
	 * Creates a backup of the file, which contains the operator states for a
	 * given query. <br />
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
			cLog.error("File '{}' to load operator states from does not exist!", fileToLoad.getName());
			return;
		}
		try (FileInputStream fin = new FileInputStream(fileToLoad);
				OsgiObjectInputStream ois = new OsgiObjectInputStream(fin);
				FileOutputStream fout = new FileOutputStream(fileToStore);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			oos.writeObject(ois.readObject());
			cLog.debug("Saved operator states of query '{}' in file '{}'", new Integer(query.getID()),
					fileToStore.getName());
		}
	}

	/**
	 * Deletes the file, which contains the operator states for a given query.
	 * 
	 * @param query
	 *            The query.
	 */
	public static void deleteFile(ILogicalQuery query) {
		File file = new File(
				cRecoveryDir + File.separator + cFileNamePrefix + query.getQueryText().hashCode() + cFileNameEnding);
		file.delete();
	}

}