package de.uniol.inf.is.odysseus.recovery.duplicateelemination.physicaloperator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.recovery.RecoveryConfiguration;

/**
 * Physical operator to be used instead of {@link SenderPO}, if duplicates (due
 * to a rollback) shall be removed. The operator writes elements on disk after
 * sending them (post-process). Use {@link SenderWithDuplicateEliminationPO} to
 * elimate duplicates after a crash.
 * 
 * @author Michael Brand
 */
public class SenderWithBackupPO<StreamObject extends IStreamObject<?>> extends SenderPO<StreamObject> {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SenderWithBackupPO.class);

	/**
	 * The prefix of all file names, where outgoing elements are stored.
	 */
	private static final String FILE_PREFIX = "sink";

	/**
	 * The file ending (incl. dot) of all file names, where outgoing elements
	 * are stored.
	 */
	private static final String FILE_ENDING = ".sentElements";

	/**
	 * The used file to store outgoing elements.
	 */
	protected final File file;

	/**
	 * Creates a new sender.
	 * 
	 * @param protocolHandler
	 *            See {@link SenderPO#SenderPO(IProtocolHandler)}.
	 * @param sinkName
	 *            The name of the sink resource.
	 */
	public SenderWithBackupPO(IProtocolHandler<StreamObject> protocolHandler, String sink) {
		super(protocolHandler);
		this.file = new File(
				RecoveryConfiguration.getRecoveryDirectory() + File.separator + FILE_PREFIX + sink + FILE_ENDING);
	}

	/**
	 * Creates a new sender as a copy of an existing one.
	 * 
	 * @param other
	 *            The sender to clone.
	 */
	public SenderWithBackupPO(SenderWithBackupPO<StreamObject> other) {
		super(other);
		this.file = other.file;
	}

	/**
	 * Write the current element into {@link #file} (no append).
	 */
	private void write(StreamObject obj) {
		// Write the element on disk to be used as watermark for elimination of
		// duplicates
		try (FileOutputStream fout = new FileOutputStream(this.file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			oos.writeObject(obj);
		} catch (IOException e) {
			LOG.error("Could not write outgoing element!", e);
		}
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		super.process_next(object, port);
		write(object);
	}
	
	@Override
	protected void process_close() {
		super.process_close();
		this.file.delete();
	}

}