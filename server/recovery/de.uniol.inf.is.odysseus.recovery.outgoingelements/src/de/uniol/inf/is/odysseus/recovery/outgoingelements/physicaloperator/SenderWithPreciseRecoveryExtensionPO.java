package de.uniol.inf.is.odysseus.recovery.outgoingelements.physicaloperator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

/**
 * Physical operator to be used instead of {@link SenderPO}, if precise recovery
 * is used. The operator writes elements on disk after sending them
 * (post-process). This is the backup mode. If the operator is in recovery mode,
 * the last stored element is read from disk and all elements, which arrive
 * before that element, will be discarded (because they are duplicates).
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            See {@link SenderPO}.
 */
public class SenderWithPreciseRecoveryExtensionPO<T extends IStreamObject<?>> extends SenderPO<T> {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(SenderWithPreciseRecoveryExtensionPO.class);

	/**
	 * The directory for backup files.
	 */
	private static final File cRecoveryDir = new File(OdysseusBaseConfiguration.getHomeDir() + "recovery");

	static {
		if (!cRecoveryDir.isDirectory()) {
			cRecoveryDir.mkdir();
		}
	}

	/**
	 * The prefix of all file names, where outgoing elements are stored.
	 */
	private static final String cFileNamePrefix = "sink";

	/**
	 * The file ending (incl. dot) of all file names, where outgoing elements
	 * are stored.
	 */
	private static final String cFileNameEnding = ".sentElems";

	/**
	 * The used file to store outgoing elements.
	 */
	private final File file;

	/**
	 * True, if stored element shall be loaded and used as a watermark to
	 * discard duplicates.
	 */
	private final boolean recoveryMode;

	/**
	 * Creates a new sender.
	 * 
	 * @param protocolHandler
	 *            See {@link SenderPO#SenderPO(IProtocolHandler)}.
	 * @param senderAO
	 *            The logical operator.
	 * @param recovery
	 *            True, if stored element shall be loaded and used as a
	 *            watermark to discard duplicates.
	 */
	public SenderWithPreciseRecoveryExtensionPO(IProtocolHandler<T> protocolHandler, AbstractSenderAO senderAO,
			boolean recovery) {
		super(protocolHandler);
		this.recoveryMode = recovery;
		this.file = new File(cRecoveryDir + File.separator + cFileNamePrefix + senderAO.hashCode() + cFileNameEnding);
	}

	/**
	 * Creates a new sender as a copy of an existing one.
	 * 
	 * @param other
	 *            The sender to clone.
	 */
	public SenderWithPreciseRecoveryExtensionPO(SenderWithPreciseRecoveryExtensionPO<T> other) {
		super(other);
		this.recoveryMode = other.recoveryMode;
		this.file = other.file;
	}

	/**
	 * For recovery mode, this is the last stored element before system failure.
	 * All elements before (and the element itself) are duplicates.
	 */
	private Optional<IStreamable> watermark = Optional.empty();

	/**
	 * Shortcut: true, if {@link #watermark} has been reached.
	 */
	private boolean isWaterMarkReached = false;

	/**
	 * Shortcut: True, if {@link #watermark} is a punctuation.
	 */
	private boolean isWatermarkPunctuation;

	@Override
	protected void process_open() throws OpenFailedException {
		// Read element from disk and store it
		if (this.recoveryMode && this.file.exists()) {
			try (FileInputStream fin = new FileInputStream(this.file);
					OsgiObjectInputStream ois = new OsgiObjectInputStream(fin)) {
				this.watermark = Optional.of((IStreamable) ois.readObject());
				this.isWatermarkPunctuation = IPunctuation.class.isInstance(this.watermark);
			} catch (IOException | ClassNotFoundException e) {
				cLog.error("Could not read outgoing element from backup file!", e);
			}
		}
		super.process_open();
	}

	@Override
	protected void process_next(T object, int port) {
		if (!this.isWaterMarkReached && this.watermark.isPresent() && !this.isWatermarkPunctuation) {
			if (object.equals(this.watermark.get())) {
				this.isWaterMarkReached = true;
			}
			// element is duplicate
			return;
		}
		super.process_next(object, port);
		write(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (!this.isWaterMarkReached && this.watermark.isPresent() && this.isWatermarkPunctuation) {
			if (punctuation.equals(this.watermark.get())) {
				this.isWaterMarkReached = true;
			}
			// punctuation is duplicate
			return;
		}
		super.processPunctuation(punctuation, port);
		write(punctuation);
	}

	/**
	 * Write the current element or punctuation into {@link #file} (no append).
	 * 
	 * @param obj
	 *            The current element or punctuation.
	 */
	private void write(IStreamable obj) {
		// Write the element on disk to be used as watermark for elimination of
		// duplicates

		try (FileOutputStream fout = new FileOutputStream(this.file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			oos.writeObject(obj);
			if (cLog.isDebugEnabled()) {
				cLog.debug("Wrote outgoing element '{}' to file '{}'", obj, this.file.getName());
			}
		} catch (IOException e) {
			cLog.error("Could not write outgoing element!", e);
		}
	}

}