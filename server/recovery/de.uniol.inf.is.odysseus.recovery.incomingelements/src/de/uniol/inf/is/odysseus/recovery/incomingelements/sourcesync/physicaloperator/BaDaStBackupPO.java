package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import com.google.common.collect.Lists;

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
 * It transfers all incoming elements without delay. But it adjusts it's offset
 * as a Kafka consumer: It reads stored elements from Kafka server and checks
 * which offset the first element of process_next has (means which index has
 * this element on the Kafka server). All elements with a lower index shall not
 * be considered for this operator/query in case of recovery, because they are
 * not processed.
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            The type of stream objects to process.
 */
public class BaDaStBackupPO<T extends IStreamObject<IMetaAttribute>> extends
		AbstractBaDaStAccessPO<T> {

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
	 * TThe indices/offsets of the last element processed by
	 * {@link #onNewMessage(ByteBuffer, long)}. It is not a 1:1 mapping between
	 * data stream element and offset. Depends on the data handler.
	 */
	private final LinkedList<Long> mCurrentOffsets = Lists.newLinkedList();

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
		 * @see BaDaStBackupPO#adjustOffset(IStreamable)
		 * @param object
		 *            The currently read data stream element.
		 * 
		 */
		private void adjustOffset(IStreamable object) {
			if (BaDaStBackupPO.this.mReference.equals(object)) {
				BaDaStBackupPO.this.mOffset = BaDaStBackupPO.this.mCurrentOffsets
						.getFirst();
				if (BaDaStBackupPO.this.mKafkaAccess != null) {
					BaDaStBackupPO.this.mKafkaAccess.interrupt();
				}
			} else {
				// At this point a complete tuple is read out. This means that
				// the given tuple consists of all messages, which's offsets are
				// in the list of current offsets.
				// Clear that list for the next tuple to check.
				BaDaStBackupPO.this.mCurrentOffsets.clear();
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
			return BaDaStBackupPO.this.getName();
		}

	};

	/**
	 * Creates a new {@link BaDaStBackupPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	public BaDaStBackupPO(BaDaStAccessAO logical) {
		super(logical);
		this.mProtocolHandler.setTransfer(this.mTransferHandler);
		this.mKafkaAccess = new KafkaConsumerAccess(this.mSourceName, this);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !BaDaStBackupPO.class.isInstance(obj)) {
			return false;
		}
		return super.isSemanticallyEqual(obj);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.mFirstElementProcessed = false;
		super.process_open();
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
	public void onNewMessage(ByteBuffer message, long offset) throws Throwable {
		synchronized (this.mCurrentOffsets) {
			this.mCurrentOffsets.add(offset);
			this.mProtocolHandler.process(0, message);
		}
	}

}