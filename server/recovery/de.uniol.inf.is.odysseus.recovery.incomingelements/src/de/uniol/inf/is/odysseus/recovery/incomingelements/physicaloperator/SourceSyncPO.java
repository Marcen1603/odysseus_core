package de.uniol.inf.is.odysseus.recovery.incomingelements.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.incomingelements.BaDaStSender;
import de.uniol.inf.is.odysseus.recovery.incomingelements.logicaloperator.SourceSyncAO;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * In the phase of backup, it controls a BaDaSt recorder by starting and closing
 * it. <br />
 * <br />
 * In the phase of recovery, it acts as a BaDaSt consumer and pushes data stream
 * elements from there into the DSMS. Elements from the original source will be
 * discarded in this time. But they are not lost, since this operator is also
 * always in backup mode.
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            The type of stream objects to process.
 */
public class SourceSyncPO<T extends IStreamObject<IMetaAttribute>> extends AbstractPipe<T, T> {

	/**
	 * The name of the BaDaSt recorder to use.
	 */
	private String mRecorder;

	/**
	 * Creates a new {@link SourceSyncPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	public SourceSyncPO(SourceSyncAO logical) {
		super();
		this.mRecorder = logical.getBaDaStRecorder();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !SourceSyncPO.class.isInstance(obj)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		SourceSyncPO<T> other = (SourceSyncPO<T>) obj;
		return this.mRecorder.equals(other.mRecorder);
	}

	@Override
	protected void process_open() {
		BaDaStSender.sendStartCommand(this.mRecorder);
	}

	@Override
	protected void process_close() {
		BaDaStSender.sendCloseCommand(this.mRecorder);
	}

	@Override
	protected void process_next(T object, int port) {
		// TODO implement SourceSyncPO.process_next
		transfer(object, port);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO implement SourceSyncPO.processPunctuation
		this.sendPunctuation(punctuation, port);
	}

}