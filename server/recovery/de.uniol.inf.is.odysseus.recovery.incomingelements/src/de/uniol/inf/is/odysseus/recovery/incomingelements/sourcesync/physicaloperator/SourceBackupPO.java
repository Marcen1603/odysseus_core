package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceRecoveryAO;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * It transfers all incoming elements without delay. But it adjusts it's offset
 * as a Kafka consumer: It reads stored elements from Kafka server and checks
 * which offset the first element of process_next has (initial state) and which
 * offset the first element after a protection point has. All elements with a
 * lower index shall not be considered for this operator/query in case of
 * recovery, because they are either not processed (initial state) or before the
 * last protection point.
 * 
 * @author Michael Brand
 * 
 * @param <StreamObject>
 *            The type of stream elements to process.
 */
public class SourceBackupPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractSourceRecoveryPO<StreamObject> {

	/**
	 * Creates a new {@link SourceBackupPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	public SourceBackupPO(SourceRecoveryAO logical) {
		super(logical);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.mNeedToAdjustOffset = false;
		super.process_open();
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		transfer(object, port);
		adjustOffsetIfNeeded(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation, port);
		adjustOffsetIfNeeded(punctuation);
	}

}