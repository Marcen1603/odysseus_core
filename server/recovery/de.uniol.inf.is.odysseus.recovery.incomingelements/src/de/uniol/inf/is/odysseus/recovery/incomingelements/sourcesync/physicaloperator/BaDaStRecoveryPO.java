package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.KafkaConsumerAccess;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.BaDaStAccessAO;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * It acts as a Kafka consumer and pushes data stream elements from there to
 * it's next operators, until it gets the same elements from both Kafka and
 * original source. Elements from the original source will be discarded in this
 * time. But they are not lost, since they are backed up again by BaDaSt.
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            The type of stream objects to process.
 */
public class BaDaStRecoveryPO<T extends IStreamObject<IMetaAttribute>> extends
		AbstractBaDaStAccessPO<T> implements ITransferHandler<T> {

	/**
	 * True, if recovery is finished and if we can process live again.
	 */
	private boolean mLiveProcessing = false;

	/**
	 * Reference is the last element delivered by the source. Used to check, if
	 * we can continue with live processing.
	 */
	private IStreamable mReference;

	/**
	 * Helper to synchronize a block, because {@link #mReference} can be null.
	 */
	private final Object mSyncHelper = new Object();

	/**
	 * Transfer handler for the objects from Kafka server. Not for the objects
	 * from the source operator.
	 */
	private final ITransferHandler<T> mTransferHandler = new ITransferHandler<T>() {

		@Override
		public void transfer(T object, int sourceOutPort) {
			BaDaStRecoveryPO.this.transfer(object, sourceOutPort);
			BaDaStRecoveryPO.this.checkLifeState(object);
		}

		@Override
		public void transfer(T object) {
			transfer(object, 0);
		}

		@Override
		public void sendPunctuation(IPunctuation punctuation) {
			sendPunctuation(punctuation, 0);
		}

		@Override
		public void sendPunctuation(IPunctuation punctuation, int outPort) {
			BaDaStRecoveryPO.this.sendPunctuation(punctuation, outPort);
			BaDaStRecoveryPO.this.checkLifeState(punctuation);
		}

		@Override
		public String getName() {
			return BaDaStRecoveryPO.this.getName();
		}

	};

	/**
	 * Creates a new {@link BaDaStRecoveryPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	public BaDaStRecoveryPO(BaDaStAccessAO logical) {
		super(logical);
		this.mProtocolHandler.setTransfer(this.mTransferHandler);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !BaDaStRecoveryPO.class.isInstance(obj)) {
			return false;
		}
		return super.isSemanticallyEqual(obj);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.mKafkaAccess = new KafkaConsumerAccess(this.mSourceName, this,
				this.mOffset);
		this.mKafkaAccess.start();
		super.process_open();
	}

	@Override
	protected void process_next(T object, int port) {
		transferIfLive(object, port);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transferIfLive(punctuation, port);
	}

	@Override
	public void onNewMessage(ByteBuffer message, long offset) throws Throwable {
		this.mProtocolHandler.process(0, message);
	}

	/**
	 * Transfers an object, which comes from the original source, only if
	 * {@link #mLiveProcessing}. Otherwise it's gonna be the new
	 * {@link #mReference}.
	 * 
	 * @param object
	 *            The object from the original source.
	 * @param port
	 *            The input port.
	 */
	@SuppressWarnings("unchecked")
	// cast from IStreamable to T
	private void transferIfLive(IStreamable object, int port) {
		// TODO Live mode after recovery: Need to be tested, if it is possible
		// to get live again.
		if (this.mLiveProcessing) {
			if (object.isPunctuation()) {
				this.sendPunctuation((IPunctuation) object, port);
			} else {
				this.transfer((T) object, port);
			}
		} else {
			// this comes from original source and is discarded in recovery
			// mode,
			// because elements from Kafka will be transfered
			synchronized (this.mSyncHelper) {
				this.mReference = object;
			}
		}
	}

	/**
	 * Compares an element delivered by Kafka with the latest element delivered
	 * from the original source.
	 * 
	 * @param object
	 *            The element delivered by Kafka.
	 */
	private void checkLifeState(IStreamable object) {
		synchronized (BaDaStRecoveryPO.this.mSyncHelper) {
			if (BaDaStRecoveryPO.this.mReference.equals(object)) {
				// TODO is it a realistic scenario that both are equal?
				// We can go live now
				BaDaStRecoveryPO.this.mLiveProcessing = false;
				BaDaStRecoveryPO.this.mKafkaAccess.interrupt();
			}
		}
	}

}