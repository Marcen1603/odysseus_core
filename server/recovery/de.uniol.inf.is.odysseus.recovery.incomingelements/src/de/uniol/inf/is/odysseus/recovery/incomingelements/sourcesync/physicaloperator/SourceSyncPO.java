package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.io.Serializable;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.BaDaStSender;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.IKafkaConsumer;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.KafkaConsumerAccess;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceSyncAO;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * In the case of backup, it TODO update javaDoc <br />
 * <br />
 * In the case of recovery, it acts as a BaDaSt consumer and pushes data stream
 * elements from there into the DSMS. Elements from the original source will be
 * discarded in this time. But they are not lost, since this operator is also
 * always in backup mode.
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            The type of stream objects to process.
 */
public class SourceSyncPO<T extends IStreamObject<IMetaAttribute>> extends
		AbstractPipe<T, T> implements IStatefulPO, IKafkaConsumer {

	/**
	 * The name of the source to synchronize.
	 */
	private final String mSourcename;

	/**
	 * The name of the BaDaSt recorder to use.
	 */
	private final String mRecorder;

	/**
	 * True, if the first element (or punctuation) has been processed.
	 */
	private boolean mFirstElementProcessed = false;

	/**
	 * Reference should be the first element to be backed up. So the
	 * corresponding offset will be stored in the state of this operator.
	 */
	private IStreamable mReference;

	/**
	 * The corresponding offset of {@link #mReference}.
	 */
	private Long mOffset = -1l;

	/**
	 * The corresponding offset of the last element processed by
	 * {@link #onNewMessage(ByteBuffer, long)}.
	 */
	private Long mCurrentOffset = -1l;

	/**
	 * The data handler to use.
	 */
	private final IDataHandler<T> mDataHandler;

	/**
	 * The protocol handler to use.
	 */
	private final IProtocolHandler<T> mProtocolHandler;

	/**
	 * The access to the Kafka server as consumer.
	 */
	private final KafkaConsumerAccess mKafkaAccess;

	/**
	 * Transfer handler for the objects from Kafka. Not for the objects from the
	 * source operator.
	 */
	private final ITransferHandler<T> mTransferHandler = new ITransferHandler<T>() {

		/**
		 * Adjusts the offset as a Kafka Consumer.
		 * 
		 * @see SourceSyncPO#adjustOffset(IStreamable)
		 * @param object
		 *            The currently read data stream element.
		 * 
		 */
		private void adjustOffset(IStreamable object) {
			if (SourceSyncPO.this.mReference.equals(object)) {
				SourceSyncPO.this.mOffset = SourceSyncPO.this.mCurrentOffset;
				SourceSyncPO.this.mKafkaAccess.interrupt();
			}
		}

		@Override
		public void transfer(T object, int sourceOutPort) {
			adjustOffset(object);
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
			adjustOffset(punctuation);
		}

		@Override
		public String getName() {
			return SourceSyncPO.this.getName();
		}

	};

	/**
	 * Creates a new {@link SourceSyncPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	@SuppressWarnings("unchecked")
	// cast of data handler
	public SourceSyncPO(SourceSyncAO logical) {
		super();
		this.mRecorder = logical.getBaDaStRecorder();
		AccessAO sourceAccess = logical.getSource();
		this.mSourcename = sourceAccess.getAccessAOName().getResourceName();
		this.mDataHandler = (IDataHandler<T>) DataHandlerRegistry
				.getDataHandler(sourceAccess.getDataHandler(),
						sourceAccess.getOutputSchema());
		OptionMap options = new OptionMap(sourceAccess.getOptions());
		IAccessPattern access = IAccessPattern.PUSH;
		if (sourceAccess.getWrapper().toLowerCase().contains("pull")) {
			// TODO not so nice!
			access = IAccessPattern.PULL;
		}
		this.mProtocolHandler = (IProtocolHandler<T>) ProtocolHandlerRegistry
				.getInstance(sourceAccess.getProtocolHandler(),
						ITransportDirection.IN, access, options,
						this.mDataHandler);
		this.mProtocolHandler.setTransfer(this.mTransferHandler);
		this.mKafkaAccess = new KafkaConsumerAccess(this.mSourcename, this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_close() {
		// TODO workaround. Recorder should be closed, if the last query using
		// it is removed.
		BaDaStSender.sendCloseCommand(this.mRecorder);
		this.mFirstElementProcessed = false;
		this.mOffset = -1l;
		this.mReference = null;
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
	protected void process_next(T object, int port) {
		transfer(object, port);
		synchronized (this.mOffset) {
			if (!this.mFirstElementProcessed) {
				this.mFirstElementProcessed = true;
				adjustOffset(object);
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation, port);
		synchronized (this.mOffset) {
			if (!this.mFirstElementProcessed) {
				this.mFirstElementProcessed = true;
				adjustOffset(punctuation);
			}
		}
	}

	/**
	 * Adjusts the offset as a Kafka Consumer. <br />
	 * <br />
	 * Reference should be the first element to be backed up. So the
	 * corresponding offset will be stored in the state of this operator. All
	 * elements with a lower offset are not relevant.
	 * 
	 * @see #onNewMessage(ByteBuffer, long)
	 * @param reference
	 *            The first element to process.
	 * 
	 */
	private void adjustOffset(IStreamable reference) {
		this.mReference = reference;
		this.mKafkaAccess.start();
	}

	@Override
	public IOperatorState getState() {
		return new SourceSyncPOState(this.mOffset);
	}

	@Override
	public void setState(Serializable state) {
		this.mOffset = ((SourceSyncPOState) state).getOffset();
	}

	@Override
	public void onNewMessage(ByteBuffer message, long offset) throws Throwable {
		synchronized (this.mCurrentOffset) {
			this.mCurrentOffset = offset;
			// TODO caller id is what?
			this.mProtocolHandler.process(0, message);
		}
	}

}