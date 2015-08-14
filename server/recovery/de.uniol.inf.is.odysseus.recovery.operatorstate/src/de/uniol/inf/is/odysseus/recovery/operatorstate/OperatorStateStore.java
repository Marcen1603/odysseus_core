package de.uniol.inf.is.odysseus.recovery.operatorstate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;

/**
 * Helper class to store operator states into a file.
 * 
 * @author Michael Brand
 *
 */
public class OperatorStateStore {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(OperatorStateStore.class);
	
	/**
	 * The directory for operator state files.
	 */
	private static final File cRecoveryDir = new File(OdysseusBaseConfiguration
			.getHomeDir() + "recovery");
	
	static {
		if(!cRecoveryDir.isDirectory()) {
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
	private static final String cFileNameEnding = ".ops";

	/**
	 * Stores the states of all {@link IStatefulPO}s of a physical query.
	 * 
	 * @param operators
	 *            The operators, which states have to be stored.
	 * @param queryId
	 *            The id of the query (for identification).
	 * @throws IOException
	 *             if any error occurs.
	 */
	public static void store(Collection<IStatefulPO> operators, int queryId)
			throws IOException {
		File file = new File(cRecoveryDir + File.separator + cFileNamePrefix + queryId + cFileNameEnding);
		try (FileOutputStream fout = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			for (IStatefulPO operator : operators) {
				oos.writeObject(operator.getState());
				cLog.debug("Wrote state of '{}' to file '{}'", operator,
						file.getName());
			}
		}
	}
	
	// TODO implement OperatorStateStore.load

}