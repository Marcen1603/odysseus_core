package de.uniol.inf.is.odysseus.recovery.duplicateelemination.physicaloperator;

import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

/**
 * Physical operator to be used instead of {@link SenderPO}, if duplicates (due
 * to a rollback) shall be removed. The operator writes elements on disk after
 * sending them (post-process, done by {@link SenderWithBackupPO}). If the
 * operator is in recovery mode, the last stored element is read from disk and
 * all elements, which arrive before that element, will be discarded (because
 * they are duplicates).
 *
 * @author Michael Brand
 */
public class SenderWithDuplicateEliminationPO<StreamObject extends IStreamObject<?>>
		extends SenderWithBackupPO<StreamObject> {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SenderWithDuplicateEliminationPO.class);

	/**
	 * This is the last stored element before system failure. All elements
	 * before (and the element itself) are duplicates.
	 */
	private StreamObject watermark;

	/**
	 * Shortcut: true, if {@link #watermark} has been reached.
	 */
	private boolean watermarkReached = false;

	/**
	 * Creates a new sender.
	 *
	 * @param protocolHandler
	 *            See {@link SenderPO#SenderPO(IProtocolHandler)}.
	 * @param sinkName
	 *            The name of the sink resource.
	 */
	@SuppressWarnings("unchecked")
	public SenderWithDuplicateEliminationPO(IProtocolHandler<StreamObject> protocolHandler, String sink) {
		super(protocolHandler, sink);

		// Read element from disk and store it
		try (FileInputStream fin = new FileInputStream(this.file);
				OsgiObjectInputStream ois = new OsgiObjectInputStream(fin)) {
			this.watermark = (StreamObject) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("Could not read outgoing element from backup file!", e);
		}
	}

	/**
	 * Creates a new sender as a copy of an existing one.
	 *
	 * @param other
	 *            The sender to clone.
	 */
	public SenderWithDuplicateEliminationPO(SenderWithDuplicateEliminationPO<StreamObject> other) {
		super(other);
		this.watermark = other.watermark;
		this.watermarkReached = other.watermarkReached;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(StreamObject object, int port) {
		if (this.watermarkReached) {
			super.process_next(object, port);
		} else if (object.equals((IStreamObject<IMetaAttribute>) this.watermark, false)
				&& equalTimeIntervals(object, this.watermark)) {
			this.watermarkReached = true;
		}
	}

	private boolean equalTimeIntervals(StreamObject object1, StreamObject object2) {
		if (!(object1.getMetadata() instanceof ITimeInterval) || !(object2.getMetadata() instanceof ITimeInterval)) {
			// No TI used. Hard to say whether such objects should be marked as
			// equals. But for me, they are more equals than not equals.
			return true;
		}
		ITimeInterval ti1 = (ITimeInterval) object1.getMetadata();
		ITimeInterval ti2 = (ITimeInterval) object2.getMetadata();
		// Calling equals on ti1 and ti2 does not work for combined meta
		// attributes
		return ti1.getStart().equals(ti2.getStart()) && ti1.getEnd().equals(ti2.getEnd());
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (this.watermarkReached) {
			super.processPunctuation(punctuation, port);
		}
	}

}