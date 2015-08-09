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
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.IKafkaConsumer;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.KafkaConsumerAccess;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.BaDaStAccessAO;

/**
 * Physical operator to be placed directly after source
 * access operators. <br />
 * <br />
 * It transfers all incoming elements without delay. But it adjusts it's offset
 * as a Kafka consumer: It reads stored elements from Kafka server and checks
 * which offset the first element of process_next has (means which index has
 * this element on the Kafka server). All elements with a lower index shall not
 * be considered for this operator/query in case of recovery, because they are
 * not processed. <br />
 * <br />
 * TODO this javaDoc is for the other PO In the case of recovery, it acts as a
 * Kafka consumer and pushes data stream elements from there into the DSMS.
 * Elements from the original source will be discarded in this time. But they
 * are not lost, since this operator is also always in backup mode.
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            The type of stream objects to process.
 */
public class BaDaStAccessPO<T extends IStreamObject<IMetaAttribute>> extends
		AbstractPipe<T, T> implements IStatefulPO, IKafkaConsumer {

	/**
	 * The access to the source, which is recorded by BaDaSt.
	 */
	private final AbstractAccessAO mSourceAccess;

	/**
	 * True, if the first element (or punctuation) from {@link #mSourceAccess}
	 * has been processed.
	 */
	private boolean mFirstElementProcessed = false;

	/**
	 * Reference is the first element to be backed up. So the index of
	 * {@link #mReference} stored on the Kafka server will be stored within
	 * {@link BaDaStAccessState}.
	 */
	private IStreamable mReference;

	/**
	 * The index of {@link #mReference} stored on the Kafka server.
	 */
	private Long mOffset = -1l;

	/**
	 * The index of the last element processed by
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
	 * Transfer handler for the objects from Kafka server. Not for the objects
	 * from the source operator.
	 */
	private final ITransferHandler<T> mTransferHandler = new ITransferHandler<T>() {

		/**
		 * Adjusts the offset as a Kafka Consumer. <br />
		 * <br />
		 * Reference should be the first element to be backed up. So the index
		 * of {@link #mReference} stored on the Kafka server will be stored
		 * within {@link BaDaStAccessState}. All elements with a lower offset
		 * are not relevant.
		 * 
		 * @see BaDaStAccessPO#adjustOffset(IStreamable)
		 * @param object
		 *            The currently read data stream element.
		 * 
		 */
		private void adjustOffset(IStreamable object) {
			if (BaDaStAccessPO.this.mReference.equals(object)) {
				BaDaStAccessPO.this.mOffset = BaDaStAccessPO.this.mCurrentOffset;
				BaDaStAccessPO.this.mKafkaAccess.interrupt();
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
			return BaDaStAccessPO.this.getName();
		}

	};

	/**
	 * Creates a new {@link BaDaStAccessPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	@SuppressWarnings("unchecked")
	// cast of data handler
	public BaDaStAccessPO(BaDaStAccessAO logical) {
		super();
		this.mSourceAccess = logical.getSource();
		this.mDataHandler = (IDataHandler<T>) DataHandlerRegistry
				.getDataHandler(this.mSourceAccess.getDataHandler(),
						this.mSourceAccess.getOutputSchema());
		OptionMap options = new OptionMap(this.mSourceAccess.getOptions());
		IAccessPattern access = IAccessPattern.PUSH;
		if (this.mSourceAccess.getWrapper().toLowerCase().contains("pull")) {
			// TODO Better way to determine the AccessPattern?
			access = IAccessPattern.PULL;
		}
		this.mProtocolHandler = (IProtocolHandler<T>) ProtocolHandlerRegistry
				.getInstance(this.mSourceAccess.getProtocolHandler(),
						ITransportDirection.IN, access, options,
						this.mDataHandler);
		this.mProtocolHandler.setTransfer(this.mTransferHandler);
		this.mKafkaAccess = new KafkaConsumerAccess(this.mSourceAccess
				.getAccessAOName().getResourceName(), this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !BaDaStAccessPO.class.isInstance(obj)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		BaDaStAccessPO<T> other = (BaDaStAccessPO<T>) obj;
		return this.mSourceAccess.equals(other.mSourceAccess);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.mFirstElementProcessed = false;
		super.process_open();
	}

	@Override
	protected void process_close() {
		this.mOffset = -1l;
		super.process_close();
	}

	@Override
	protected void process_next(T object, int port) {
		transfer(object, port);
		synchronized (this.mOffset) {
			if (!this.mFirstElementProcessed) {
				this.mFirstElementProcessed = true;
				if (this.mOffset == -1) {
					// first element not processed AND offset not -1 means
					// offset has been loaded as operator state
					adjustOffset(object);
				}
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation, port);
		synchronized (this.mOffset) {
			if (!this.mFirstElementProcessed) {
				this.mFirstElementProcessed = true;
				if (this.mOffset == -1) {
					// first element not processed AND offset not -1 means
					// offset has been loaded as operator state
					adjustOffset(punctuation);
				}
			}
		}
	}

	/**
	 * Adjusts the offset as a Kafka Consumer. <br />
	 * <br />
	 * Reference should be the first element to be backed up. So the index of
	 * {@link #mReference} stored on the Kafka server will be stored within
	 * {@link BaDaStAccessState}. All elements with a lower offset are not
	 * relevant.
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
		return new BaDaStAccessState(this.mOffset);
	}

	@Override
	public void setState(Serializable state) {
		this.mOffset = ((BaDaStAccessState) state).getOffset();
	}

	@Override
	public void onNewMessage(ByteBuffer message, long offset) throws Throwable {
		synchronized (this.mCurrentOffset) {
			this.mCurrentOffset = offset;
			this.mProtocolHandler.process(0, message);
		}
	}

}