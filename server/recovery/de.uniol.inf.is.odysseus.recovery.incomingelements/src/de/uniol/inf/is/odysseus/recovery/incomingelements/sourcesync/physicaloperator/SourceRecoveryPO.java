package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataInitializerAdapter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.ISubscriber;
import de.uniol.inf.is.odysseus.recovery.incomingelements.ISubscriberController;
import de.uniol.inf.is.odysseus.recovery.incomingelements.SubscriberControllerFactory;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceRecoveryAO;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * It acts as an {@code ISubscriber} and pushes data stream elements from public
 * subscribe system to it's next operators, until it gets the same elements from
 * both public subscribe system and original source. Elements from the original
 * source will be discarded in this time. But they are not lost, since they are
 * backed up by BaDaSt.
 * 
 * @author Michael Brand
 * 
 * @param <StreamObject>
 *            The type of stream elements to process.
 */
public class SourceRecoveryPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractSourceRecoveryPO<StreamObject> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -2660605545656846436L;

	/**
	 * Compares the start time stamps
	 */
	private class Comparator implements java.util.Comparator<StreamObject> {

		/**
		 * Empty default constructor.
		 */
		public Comparator() {
		}

		@SuppressWarnings({ "cast", "rawtypes", "unchecked" })
		@Override
		public int compare(StreamObject o1, StreamObject o2) {
			IStreamObject<ITimeInterval> elem1 = (IStreamObject<ITimeInterval>) (IStreamObject) o1;
			IStreamObject<ITimeInterval> elem2 = (IStreamObject<ITimeInterval>) (IStreamObject) o2;
			return elem1.getMetadata().getStart().compareTo(elem2.getMetadata().getStart());
		}

	}

	/**
	 * Transfer handler for the objects from public subscribe system in recovery
	 * mode. Not for the objects from the source operator.
	 */
	private class RecoveryTransferHandler extends AbstractSourceRecoveryTransferHandler
			implements IMetadataInitializer<IMetaAttribute, StreamObject> {

		/**
		 * Initializer for the meta data. Needed to set the meta data correct
		 * for elements, which are sent by BaDaSt.
		 */
		private final IMetadataInitializer<IMetaAttribute, StreamObject> mMetaDataInitializer = new MetadataInitializerAdapter<>();

		/**
		 * Call {@link SourceRecoveryPO#delta(IStreamObject, IStreamObject)}
		 * every DELAY milliseconds.
		 */
		private static final long DELAY = 30000;

		/**
		 * {@link SourceRecoveryPO#delta(IStreamObject, IStreamObject)} should
		 * not be called for every new element. It should be called every
		 * {@link #DELAY} milliseconds. If this field is false, it should be
		 * called for the next element.
		 */
		boolean mTimeToEvaluateDelta = true;

		/**
		 * Empty default constructor.
		 */
		public RecoveryTransferHandler() {
			// Empty default constructor
		}

		@SuppressWarnings("unchecked")
		@Override
		public void transfer_intern(IStreamable object, int port) {
			if (!SourceRecoveryPO.this.mTransferFromBaDaSt) {
				return;
			} else if (object.isPunctuation()) {
				SourceRecoveryPO.this.sendPunctuation((IPunctuation) object, port);
			} else {
				StreamObject strObj = (StreamObject) object;
				try {
					strObj.setMetadata(getMetadataInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				updateMetadata(strObj);

				if (this.mTimeToEvaluateDelta) {
					synchronized (SourceRecoveryPO.this.mLastSeenElementsFromSource) {
						if (!SourceRecoveryPO.this.mLastSeenElementsFromSource.isEmpty()) {
							evaluate(SourceRecoveryPO.this.mLastSeenElementsFromSource, strObj, port);
						}
					}
				} else {
					SourceRecoveryPO.this.transfer(strObj, port);
				}
			}
		}

		/**
		 * Evaluates the delta of the time stamps from BaDaSt and original
		 * source as follows: <br />
		 * <br />
		 * if the oldest buffered element from original source is newer as the
		 * newest element from BaDaSt, BaDaSt did not catch up with the original
		 * source yet. Set {@link #mTimeToEvaluateDelta} in {@link #DELAY}
		 * milliseconds. <br />
		 * <br />
		 * else if the newest buffered element from original source is newer
		 * than the newest element from BaDaSt. Than, the newest element from
		 * BaDaSt must be within {@code objectsFromSource}. BaDaSt caught up
		 * with the original source. So (1) stop transferring from BaDaSt, (2)
		 * transfer all buffered elements from that element on, (3) transfer new
		 * elements from the original source, (4) stop consuming from BaDaSt.
		 * <br />
		 * <br />
		 * else: <br />
		 * BaDaSt caught up with the original source.Reaction:(1) keep the first
		 * element from BaDaSt in mind, which is not transferred anymore, (2)
		 * stop transferring from BaDaSt,(3) watch incoming elements from the
		 * original source until we see the element from (1), (4) transfer from
		 * the original source (the element from (1) should be the first to
		 * transfer), (5) stop consuming from BaDaSt.
		 * 
		 * @param objectsFromSource
		 *            The last elements from the original source, which has been
		 *            recognized.
		 * @param objectFromBaDaSt
		 *            The last seen element from BaDaSt.
		 * @param port
		 *            Port number at which {@code objectFromBaDaSt} arrived.
		 */

		private void evaluate(ArrayList<StreamObject> objectsFromSource, StreamObject objectFromBaDaSt, int port) {
			Comparator comp = new Comparator();
			int delta = comp.compare(objectsFromSource.get(0), objectFromBaDaSt);
			if (delta > 0) {
				// The oldest buffered element from original source is newer as
				// the newest element from BaDaSt.
				// BaDaSt did not catch up with the original source yet. Let's
				// wait some time and check again.
				this.mTimeToEvaluateDelta = false;
				SourceRecoveryPO.this.transfer(objectFromBaDaSt, port);
				new Timer("SourceRecoveryDeltaEvaluator", true).schedule(new TimerTask() {

					@Override
					public void run() {
						RecoveryTransferHandler.this.mTimeToEvaluateDelta = true;
					}
				}, RecoveryTransferHandler.DELAY);
				return;
			}

			delta = comp.compare(objectsFromSource.get(objectsFromSource.size() - 1), objectFromBaDaSt);
			if (delta >= 0) {
				// The newest buffered element from original source is newer
				// than
				// the newest element from BaDaSt. The newest element from
				// BaDaSt must be within objectsFromSource.

				/*
				 * BaDaSt caught up with the original source. Let's (1) stop
				 * transferring from BaDaSt, (2) transfer all buffered elements
				 * from that element on, (3) transfer new elements from the
				 * original source, (4) stop consuming from BaDaSt.
				 */
				SourceRecoveryPO.this.mTransferFromBaDaSt = false;
				int indexOfFirstToTransfer = Collections.binarySearch(objectsFromSource, objectFromBaDaSt, comp);
				for (int i = indexOfFirstToTransfer; i < objectsFromSource.size(); i++) {
					SourceRecoveryPO.this.transfer(objectsFromSource.get(i), port);
				}
				SourceRecoveryPO.this.mTransferFromSource = true;
				SourceRecoveryPO.this.mBackupSubscriberController.interrupt();
				objectsFromSource.clear();

			} else /* delta < 0 */ {
				// The newest buffered element from original source is older
				// than the newest element from BaDaSt.

				/*
				 * BaDaSt caught up with the original source. Let's (1) keep the
				 * first element from BaDaSt in mind, which is not transferred
				 * anymore, (2) stop transferring from BaDaSt, (3) watch
				 * incoming elements from the original source until we see the
				 * element from (1), (4) transfer from the original source (the
				 * element from (1) should be the first to transfer), (5) stop
				 * consuming from BaDaSt.
				 */
				SourceRecoveryPO.this.mFirstNotTransferredElementFromBaDaSt = Optional.fromNullable(objectFromBaDaSt);
				SourceRecoveryPO.this.mTransferFromBaDaSt = false;
				// (3) and (4) is done in SourceRecoveryPO.process_next
				SourceRecoveryPO.this.mBackupSubscriberController.interrupt();
			}
		}

		@Override
		public String getName() {
			return SourceRecoveryPO.this.getName();
		}

		@Override
		public void setMetadataType(IMetaAttribute type) {
			this.mMetaDataInitializer.setMetadataType(type);
		}

		@Override
		public IMetaAttribute getMetadataInstance() throws InstantiationException, IllegalAccessException {
			return this.mMetaDataInitializer.getMetadataInstance();
		}

		@Override
		public void addMetadataUpdater(IMetadataUpdater<IMetaAttribute, StreamObject> mFac) {
			this.mMetaDataInitializer.addMetadataUpdater(mFac);
		}

		@Override
		public void updateMetadata(StreamObject object) {
			this.mMetaDataInitializer.updateMetadata(object);
		}

	}

	/**
	 * The protocol handler to use in recovery mode.
	 */
	final IProtocolHandler<StreamObject> mRecoveryProtocolHandler;

	/**
	 * The access to the public subscribe system to use in recovery mode.
	 */
	private ISubscriberController mRecoverySubscriberController;

	/**
	 * The handler of messages consumed from public subscribe system in recovery
	 * mode.
	 */
	private final ISubscriber mRecoverySubscriber = new ISubscriber() {

		@Override
		public void onNewMessage(ByteBuffer message, long offset) throws Throwable {
			SourceRecoveryPO.this.mRecoveryProtocolHandler.process(0, message);

		}

	};

	/**
	 * Transfer handler for the objects from public subscribe system in recovery
	 * mode. Not for the objects from the source operator.
	 */
	private final RecoveryTransferHandler mRecoveryTransferHandler = new RecoveryTransferHandler();

	/**
	 * Max. number of elements from the original source to buffer.
	 */
	private static final int BUFFRSIZE = 1000;

	/**
	 * The last elements from the original source, which has been recognized.
	 */
	ArrayList<StreamObject> mLastSeenElementsFromSource = new ArrayList<>(BUFFRSIZE);

	/**
	 * The first element from BaDaSt, which is not transferred any more.
	 */
	Optional<StreamObject> mFirstNotTransferredElementFromBaDaSt = Optional.absent();

	/**
	 * True, if elements from BaDaSt shall be transferred.
	 */
	boolean mTransferFromBaDaSt = true;

	/**
	 * True, if elements from the original source shall be transferred.
	 */
	boolean mTransferFromSource = false;

	/**
	 * Creates a new {@link SourceRecoveryPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	public SourceRecoveryPO(SourceRecoveryAO logical) {
		super(logical);
		this.mRecoveryProtocolHandler = createProtocolHandler();
		this.mRecoveryProtocolHandler.setTransfer(this.mRecoveryTransferHandler);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		// Handle metadata
		IMetaAttribute metaDate = this.mSourceAccess.getLocalMetaAttribute();
		if (metaDate != null) {
			this.mRecoveryTransferHandler.setMetadataType(metaDate);
			if (TimeStampHelper.isTimeIntervalUsed(metaDate)) {
				IMetadataUpdater<?, ?> updater = null;
				MetadataUpdatePO<?, ?> updatePO = TimeStampHelper.searchMetaDataUpdatePO(this);
				if (updatePO != null) {
					updater = TimeStampHelper.copyInitializer(updatePO);
				} else {
					ReceiverPO<?, ?> receiverPO = TimeStampHelper.searchReceiverPO(this);
					if (receiverPO != null) {
						updater = TimeStampHelper.copyInitializer(receiverPO);
					}
				}
				if (updater != null) {
					this.mRecoveryTransferHandler
							.addMetadataUpdater((IMetadataUpdater<IMetaAttribute, StreamObject>) updater);
				}
			}
		}

		// offset should be loaded as operator state
		this.mNeedToAdjustOffset = false;
		this.mRecoverySubscriberController = SubscriberControllerFactory.createController(
				this.mSourceAccess.getAccessAOName(), this.mRecoverySubscriber, this.mOffset.longValue());
		this.mRecoverySubscriberController.start();
		super.process_open();
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		if (this.mTransferFromSource) {
			// (4) transfer from the original source (the element from (1)
			// should be the first to transfer)
			this.transfer(object, port);
		} else if (this.mTransferFromBaDaSt) {
			// Do not transfer, because elements from publish subscribe system
			// will be transfered with another transfer handler.
			synchronized (this.mLastSeenElementsFromSource) {
				if (this.mLastSeenElementsFromSource.size() == BUFFRSIZE) {
					this.mLastSeenElementsFromSource.clear();
				}
				this.mLastSeenElementsFromSource.add(object);
			}
		} else if (object.equals(this.mFirstNotTransferredElementFromBaDaSt)) {
			// (3) watch incoming elements from the original source until we see
			// the element from (1)
			this.mTransferFromSource = true;
		}
		adjustOffsetIfNeeded(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (this.mTransferFromSource) {
			// (4) transfer from the original source (the element from (1)
			// should be the first to transfer)
			this.sendPunctuation(punctuation, port);
		}

		// Otherwise do not transfer, because elements from publish subscribe
		// system will be transfered with another transfer handler.
		adjustOffsetIfNeeded(punctuation);
	}

}