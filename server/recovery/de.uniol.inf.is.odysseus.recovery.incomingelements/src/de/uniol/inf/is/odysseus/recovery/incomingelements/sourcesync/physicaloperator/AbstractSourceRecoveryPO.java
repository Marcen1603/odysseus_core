package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.incomingelements.ISubscriber;
import de.uniol.inf.is.odysseus.recovery.incomingelements.ISubscriberController;
import de.uniol.inf.is.odysseus.recovery.incomingelements.SubscriberControllerFactory;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceRecoveryAO;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.IProtectionPointHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Logical operator to be placed directly after source access operators. <br />
 * <br />
 * It can operate in backup mode or in recovery mode.<br />
 * <br />
 * In backup mode, it transfers all incoming elements without delay. But it
 * adjusts it's offset as an {@code ISubscriber}: It reads stored elements from
 * a publish subscribe system and checks which offset the first element of
 * process_next has (initial state) and which offset the first element after a
 * protection point has. All elements with a lower index shall not be considered
 * for this operator/query in case of recovery, because they are either not
 * processed (initial state) or before the last protection point. <br />
 * <br />
 * In recovery mode, it acts as an {@code ISubscriber} and pushes data stream
 * elements from public subscribe system to it's next operators, until it gets
 * the same elements from both public subscribe system and original source.
 * Elements from the original source will be discarded in this time. But they
 * are not lost, since they are backed up by BaDaSt.
 * 
 * @author Michael Brand
 * 
 * @param <StreamObject>
 *            The type of stream elements to process.
 */
@SuppressWarnings(value = { "nls" })
public abstract class AbstractSourceRecoveryPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<StreamObject, StreamObject> implements IStatefulPO, IProtectionPointHandler {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 2075517070263761810L;

	/**
	 * The logger for this class.
	 */
	static final Logger cLog = LoggerFactory.getLogger(AbstractSourceRecoveryPO.class);

	/**
	 * Abstract transfer handler just to avoid the need to implement all
	 * methods.
	 * 
	 * @author Michael Brand
	 *
	 */
	protected abstract class AbstractSourceRecoveryTransferHandler implements ITransferHandler<StreamObject> {

		/**
		 * Method for both StreamObjects and punctuations.
		 */
		protected abstract void transfer_intern(IStreamable object, int port);

		@Override
		public void transfer(StreamObject object, int port) {
			transfer_intern(object, port);
		}

		@Override
		public void sendPunctuation(IPunctuation punctuation, int port) {
			transfer_intern(punctuation, port);
		}

		@Override
		public void transfer(StreamObject object) {
			transfer(object, 0);
		}

		@Override
		public void sendPunctuation(IPunctuation punctuation) {
			sendPunctuation(punctuation, 0);
		}

	}

	/**
	 * The access to the source, which is recorded by BaDaSt.
	 */
	protected final AbstractAccessAO mSourceAccess;

	/**
	 * The offset of the first element to recover.
	 */
	protected Long mOffset = new Long(0l);

	/**
	 * Dummy stream object, because you can not synchronize a null object (sync
	 * of {@link #lastSeenElement}).
	 */
	protected final IStreamObject<IMetaAttribute> dummyStreamObj = new IStreamObject<IMetaAttribute>() {

		private static final long serialVersionUID = -1298483066133422772L;

		@Override
		public IClone clone() {
			throw new NotImplementedException();
		}

		@Override
		public boolean isPunctuation() {
			throw new NotImplementedException();
		}

		@Override
		public IMetaAttribute getMetadata() {
			throw new NotImplementedException();
		}

		@Override
		public void setMetadata(IMetaAttribute metadata) {
			throw new NotImplementedException();
		}

		@Override
		public void setKeyValue(String name, Object content) {
			throw new NotImplementedException();
		}

		@Override
		public Object getKeyValue(String name) {
			throw new NotImplementedException();
		}

		@Override
		public boolean hasKeyValue(String name) {
			throw new NotImplementedException();
		}

		@Override
		public void setKeyValueMap(Map<String, Object> metaMap) {
			throw new NotImplementedException();
		}

		@Override
		public Map<String, Object> getGetValueMap() {
			throw new NotImplementedException();
		}

		@Override
		public boolean isTimeProgressMarker() {
			throw new NotImplementedException();
		}

		@Override
		public void setTimeProgressMarker(boolean timeProgressMarker) {
			throw new NotImplementedException();
		}

		@Override
		public IStreamObject<IMetaAttribute> merge(IStreamObject<IMetaAttribute> left,
				IStreamObject<IMetaAttribute> right, IMetadataMergeFunction<IMetaAttribute> metamerge, Order order) {
			throw new NotImplementedException();
		}

		@Override
		public IStreamable newInstance() {
			throw new NotImplementedException();
		}

		@Override
		public int restrictedHashCode(int[] attributeNumbers) {
			throw new NotImplementedException();
		}

		@Override
		public boolean equalsTolerance(Object o, double tolerance) {
			throw new NotImplementedException();
		}

		@Override
		public boolean equals(IStreamObject<IMetaAttribute> o, boolean compareMeta) {
			return super.equals(o);
		}

		@Override
		public String toString(boolean printMetadata) {
			return super.toString();
		}

	};

	/**
	 * The last element received in {@link #process_next(IStreamObject, int)}.
	 */
	@SuppressWarnings("unchecked")
	protected StreamObject lastSeenElement = (StreamObject) this.dummyStreamObj;

	/**
	 * True, if {@link #onProtectionPointReached()} is called, but
	 * {@link #getState()} has not been called yet.
	 */
	boolean mProtectionPointReached = false;

	/**
	 * True, if {@link #mOffset} is set, but {@link #onProtectionPointReached()}
	 * is not called yet.
	 */
	boolean mOffsetSet = false;

	/**
	 * Reference is the first element to be backed up. So the offset of
	 * {@link #mReference} stored on the publish subscribe system will be stored
	 * within {@link BaDaStAccessState}.
	 */
	IStreamable mReference;

	/**
	 * The offsets of the last element processed by
	 * {@link #onNewMessage(ByteBuffer, long)}. It is not a 1:1 mapping between
	 * data stream element and offset. Depends on the data handler.
	 */
	final LinkedList<Long> mCurrentOffsets = Lists.newLinkedList();

	/**
	 * The protocol handler to use in backup mode.
	 */
	final IProtocolHandler<StreamObject> mBackupProtocolHandler;

	/**
	 * The access to the publish subscribe system to use in backup mode.
	 */
	ISubscriberController mBackupSubscriberController;

	/**
	 * The handler of messages consumed from the publish subscribe system in
	 * backup mode.
	 */
	private final ISubscriber mBackupSubscriber = new ISubscriber() {

		@Override
		public void onNewMessage(ByteBuffer message, long offset) throws Throwable {
			synchronized (AbstractSourceRecoveryPO.this.mCurrentOffsets) {
				AbstractSourceRecoveryPO.this.mCurrentOffsets.add(new Long(offset));
				AbstractSourceRecoveryPO.this.mBackupProtocolHandler.process(0, message);
			}

		}

	};

	/**
	 * Transfer handler for the objects from the publish subscribe system in
	 * backup mode. Not for the objects from the source operator.
	 */
	private final ITransferHandler<StreamObject> mBackupTransferHandler = new AbstractSourceRecoveryTransferHandler() {

		@Override
		public void transfer_intern(IStreamable object, int port) {
			if (AbstractSourceRecoveryPO.this.mReference.equals(object)) {
				AbstractSourceRecoveryPO.this.mOffset = AbstractSourceRecoveryPO.this.mCurrentOffsets.getFirst();
				AbstractSourceRecoveryPO.this.mOffsetSet = true;
				cLog.debug("Calcluated BaDaSt offset as {}", AbstractSourceRecoveryPO.this.mOffset);
				synchronized (AbstractSourceRecoveryPO.this.lastSeenElement) {
					AbstractSourceRecoveryPO.this.lastSeenElement.notifyAll();
				}
				if (AbstractSourceRecoveryPO.this.mBackupSubscriberController != null) {
					AbstractSourceRecoveryPO.this.mBackupSubscriberController.interrupt();
					AbstractSourceRecoveryPO.this.mBackupSubscriberController = null;
				}
			}
			// At this point a complete tuple is read out. This means
			// that
			// the given tuple consists of all messages, which's offsets
			// are
			// in the list of current offsets.
			// Clear that list for the next tuple to check.
			AbstractSourceRecoveryPO.this.mCurrentOffsets.clear();
		}

		@Override
		public String getName() {
			return AbstractSourceRecoveryPO.this.getName();
		}

	};

	/**
	 * Creates a new {@link AbstractSourceRecoveryPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	public AbstractSourceRecoveryPO(SourceRecoveryAO logical) {
		super();
		this.mSourceAccess = logical.getSource();
		this.mBackupProtocolHandler = createProtocolHandler();
		this.mBackupProtocolHandler.setTransfer(this.mBackupTransferHandler);
		logical.getProtectionPointManager().addHandler(this);
	}

	/**
	 * Creates the protocol handler to use.
	 */
	@SuppressWarnings("unchecked")
	protected IProtocolHandler<StreamObject> createProtocolHandler() {
		IStreamObjectDataHandler<StreamObject> dataHandler = (IStreamObjectDataHandler<StreamObject>) DataHandlerRegistry
				.getStreamObjectDataHandler(this.mSourceAccess.getDataHandler(), this.mSourceAccess.getOutputSchema());
		OptionMap options = new OptionMap(this.mSourceAccess.getOptions());
		IAccessPattern access = IAccessPattern.PUSH;
		if (this.mSourceAccess.getWrapper().toLowerCase().contains("pull")) {
			// XXX AccessPattern: Better way to determine the AccessPattern?
			access = IAccessPattern.PULL;
		}
		return (IProtocolHandler<StreamObject>) ProtocolHandlerRegistry.getInstance(
				this.mSourceAccess.getProtocolHandler(), ITransportDirection.IN, access, options, dataHandler);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !SourceBackupPO.class.isInstance(obj)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		AbstractSourceRecoveryPO<StreamObject> other = (AbstractSourceRecoveryPO<StreamObject>) obj;
		return this.mSourceAccess.equals(other.mSourceAccess);
	}

	@Override
	protected void process_close() {
		this.mOffset = new Long(0l);
		if (this.mBackupSubscriberController != null) {
			this.mBackupSubscriberController.interrupt();
		}
		super.process_close();
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		this.lastSeenElement = object;
	}

	/**
	 * Adjusts the offset as an {@code ISubscriber}, if {@code object} is the
	 * first element to be processed by this operator.
	 */
	protected void adjustOffset() {
		synchronized (this.mOffset) {
			if (this.lastSeenElement != this.dummyStreamObj) {
				@SuppressWarnings("unchecked")
				StreamObject clone = (StreamObject) this.lastSeenElement.clone();
				clone.setMetadata(null);
				this.mReference = clone;
				// ISubscriberController has to be deleted and new created for
				// every protection point reaching, because otherwise an
				// IllegalThreadStateException occurs.
				if (this.mBackupSubscriberController != null) {
					this.mBackupSubscriberController.interrupt();
					this.mBackupSubscriberController = null;
				}
				this.mBackupSubscriberController = SubscriberControllerFactory.createController(
						this.mSourceAccess.getAccessAOName(), this.mBackupSubscriber, this.mOffset.longValue());
				this.mBackupSubscriberController.start();
			}
		}

	}

	@Override
	public IOperatorState getState() {
		synchronized (this.lastSeenElement) {
			while (!this.mProtectionPointReached || !this.mOffsetSet) {
				try {
					this.lastSeenElement.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.mProtectionPointReached = false;
			return new SourceRecoveryState(this.mOffset, this.lastSeenElement);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable state) {
		synchronized (this.mOffset) {
			SourceRecoveryState srState = (SourceRecoveryState) state;
			this.mOffset = srState.getOffset();
			this.lastSeenElement = (StreamObject) srState.getLastSeenElement();
		}
	}

	@Override
	public void onProtectionPointReached() throws Exception {
		this.mOffsetSet = false;
		this.mProtectionPointReached = true;
		this.adjustOffset();
	}

}