package de.uniol.inf.is.odysseus.recovery.badast.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.badast.subscriber.ISubscriberController;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataInitializerAdapter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.recovery.TrustUpdatePunctuation;
import de.uniol.inf.is.odysseus.recovery.badast.physicaloperator.BaDaStRecoveryPO;

/**
 * Transfer handler for the objects from public subscribe system in recovery
 * mode. Not for the objects from the source operator.
 */
public class BaDaStRecoveryTransferHandler<StreamObject extends IStreamObject<IMetaAttribute>>
		implements ITransferHandler<StreamObject>, IMetadataInitializer<IMetaAttribute, StreamObject> {

	/**
	 * TimeInterval based comparator.
	 */
	private static final Comparator<IStreamObject<IMetaAttribute>> COMPARATOR = new Comparator<IStreamObject<IMetaAttribute>>() {

		@SuppressWarnings("unchecked")
		@Override
		public int compare(IStreamObject<IMetaAttribute> o1, IStreamObject<IMetaAttribute> o2) {
			IStreamObject<ITimeInterval> elem1 = (IStreamObject<ITimeInterval>) (IStreamObject<?>) o1;
			IStreamObject<ITimeInterval> elem2 = (IStreamObject<ITimeInterval>) (IStreamObject<?>) o2;
			return elem1.getMetadata().getStart().compareTo(elem2.getMetadata().getStart());
		}

	};

	/**
	 * Call {@link #evaluate(List, IStreamObject, int)} every DELAY
	 * milliseconds.
	 */
	private static final long DELAY = 10000;

	/**
	 * Initializer for the meta data. Needed to set the meta data correct for
	 * elements, which are sent by BaDaSt.
	 */
	private final IMetadataInitializer<IMetaAttribute, StreamObject> mMetaDataInitializer = new MetadataInitializerAdapter<>();

	/**
	 * The {@link BaDaStRecoveryPO} that uses this transfer handler.
	 */
	private final BaDaStRecoveryPO<StreamObject> recoveryPO;

	/**
	 * Consumes data, which is stored at BaDaSt.
	 */
	private final ISubscriberController subscriberController;

	/**
	 * {@link #evaluate(List, IStreamObject, int)} should not be called for
	 * every new element. It should be called every {@link #DELAY} milliseconds.
	 * If this field is false, it should be called for the next element.
	 */
	boolean timeToEvaluateDelta = true;

	/**
	 * The element that is the first one that has not been transferred. This is
	 * needed to switch from recovery mode to live mode (see
	 * {@link BaDaStRecoveryTransferMode}.
	 */
	private StreamObject firstNoMoreTransferred;

	/**
	 * The first element from the original data source after restart of the
	 * stream processing.
	 */
	private StreamObject firstElementFromSourceAfterRestart;

	/**
	 * Creates a new transfer handler for a given {@link BaDaStRecoveryPO} and a
	 * given, already started {@link ISubscriberController}.
	 */
	public BaDaStRecoveryTransferHandler(BaDaStRecoveryPO<StreamObject> recoveryPO,
			ISubscriberController subController) {
		super();
		this.recoveryPO = recoveryPO;
		this.subscriberController = subController;
	}

	/**
	 * Initializes the meta data, which are also set by the original data source
	 * access. <br />
	 * Must be called after creating the query (e.g., when the query is
	 * started). Otherwise receiverPO will be null.
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		IMetaAttribute metaDate = this.recoveryPO.getSourceAccess().getLocalMetaAttribute();
		if (metaDate != null) {
			this.setMetadataType(metaDate);
			if (TimeStampHelper.isTimeIntervalUsed(metaDate)) {
				IMetadataUpdater<?, ?> updater = null;
				MetadataUpdatePO<?, ?> updatePO = TimeStampHelper.searchMetaDataUpdatePO(this.recoveryPO);
				if (updatePO != null) {
					updater = TimeStampHelper.copyInitializer(updatePO);
				} else {
					ReceiverPO<?, ?> receiverPO = TimeStampHelper.searchReceiverPO(this.recoveryPO);
					if (receiverPO != null) {
						updater = TimeStampHelper.copyInitializer(receiverPO);
					}
				}
				if (updater != null) {
					this.addMetadataUpdater((IMetadataUpdater<IMetaAttribute, StreamObject>) updater);
				}
			}
		}
	}

	/**
	 * Gets the element that is the first one that has not been transferred.
	 * This is needed to switch from recovery mode to live mode (see
	 * {@link BaDaStRecoveryTransferMode}.
	 */
	public StreamObject getFirstNoMoreTransferred() {
		return this.firstNoMoreTransferred;
	}

	@Override
	public void transfer(StreamObject object, int sourceOutPort) {
		if (this.recoveryPO.getTransferMode() == BaDaStRecoveryTransferMode.SWITCH
				|| this.recoveryPO.getTransferMode() == BaDaStRecoveryTransferMode.LIVE) {
			// Already live processing.
			return;
		} else if (this.recoveryPO.getTransferMode() == BaDaStRecoveryTransferMode.NONE) {
			// Wait until we find the element that has been last processed
			// before the checkpoint in backup mode
			Optional<StreamObject> reference = this.recoveryPO.getLoadedElement();
			// if the reference is not present, there had been no checkpoint!
			if (!reference.isPresent() || object.equals(reference.get())) {
				this.recoveryPO.setTransferMode(BaDaStRecoveryTransferMode.RECOVERY);
			}
		} else {
			// Transfer from BaDaSt until we can go live again!

			if (this.firstElementFromSourceAfterRestart == null
					&& this.recoveryPO.getFirstElementAfterRestart().isPresent()) {
				this.firstElementFromSourceAfterRestart = this.recoveryPO.getFirstElementAfterRestart().get();
			}

			if (this.firstElementFromSourceAfterRestart == null) {
				this.recoveryPO.transfer(object, sourceOutPort);
				return;
			}

			int compareResult = COMPARATOR.compare(this.firstElementFromSourceAfterRestart, object);
			// == 0 for equal elements
			// > 0 if element from BaDaSt is older than the first element after
			// recovery from the original source
			// < 0 otherwise

			if (compareResult > 0) {
				this.recoveryPO.transfer(object, sourceOutPort);
			} else {
				if (compareResult == 0) {
					// no more duplicates from now on
					this.recoveryPO.sendPunctuation(
							new TrustUpdatePunctuation(((ITimeInterval) object.getMetadata()).getStart()),
							sourceOutPort);
				}
				if (this.timeToEvaluateDelta) {
					this.recoveryPO.lockOnLastSeenElements(true);
					List<StreamObject> lastSeenElements = this.recoveryPO.getLastSeenElements();
					if (!lastSeenElements.isEmpty()) {
						evaluate(lastSeenElements, object, sourceOutPort);
					} else {
						this.recoveryPO.transfer(object, sourceOutPort);
					}
					this.recoveryPO.lockOnLastSeenElements(false);
				}
			}
		}
	}

	/**
	 * Evaluates the delta of the time stamps from BaDaSt and original source as
	 * follows: <br />
	 * <br />
	 * if the oldest buffered element from original source is newer as the
	 * newest element from BaDaSt, BaDaSt did not catch up with the original
	 * source yet. Set {@link #timeToEvaluateDelta} in {@link #DELAY}
	 * milliseconds. <br />
	 * <br />
	 * else if the newest buffered element from original source is newer than
	 * the newest element from BaDaSt. Than, the newest element from BaDaSt must
	 * be within {@code objectsFromSource}. BaDaSt caught up with the original
	 * source. So (1) stop transferring from BaDaSt, (2) transfer all buffered
	 * elements from that element on, (3) transfer new elements from the
	 * original source, (4) stop consuming from BaDaSt. <br />
	 * <br />
	 * else: <br />
	 * BaDaSt caught up with the original source.Reaction:(1) keep the first
	 * element from BaDaSt in mind, which is not transferred anymore, (2) stop
	 * transferring from BaDaSt,(3) watch incoming elements from the original
	 * source until we see the element from (1), (4) transfer from the original
	 * source (the element from (1) should be the first to transfer), (5) stop
	 * consuming from BaDaSt.
	 *
	 * @param objectFromBaDaSt
	 *            The last seen element from BaDaSt.
	 * @param port
	 *            Port number at which {@code objectFromBaDaSt} arrived.
	 */
	private void evaluate(List<StreamObject> objectsFromSource, StreamObject objectFromBaDaSt, int port) {
		int delta = COMPARATOR.compare(objectsFromSource.get(0), objectFromBaDaSt);
		if (delta > 0) {
			// The oldest buffered element from original source is newer as
			// the newest element from BaDaSt.
			// BaDaSt did not catch up with the original source yet. Let's
			// wait some time and check again.
			this.timeToEvaluateDelta = false;
			this.recoveryPO.transfer(objectFromBaDaSt, port);
			new Timer("SourceRecoveryDeltaEvaluator", true).schedule(new TimerTask() {

				@Override
				public void run() {
					BaDaStRecoveryTransferHandler.this.timeToEvaluateDelta = true;
				}

			}, BaDaStRecoveryTransferHandler.DELAY);
			return;
		}

		delta = COMPARATOR.compare(objectsFromSource.get(objectsFromSource.size() - 1), objectFromBaDaSt);
		if (delta >= 0) {
			// The newest buffered element from original source is newer
			// than
			// the newest element from BaDaSt. The newest element from
			// BaDaSt must be within objectsFromSource.

			/*
			 * BaDaSt caught up with the original source. Let's (1) stop
			 * transferring from BaDaSt, (2) transfer all buffered elements from
			 * that element on, (3) transfer new elements from the original
			 * source, (4) stop consuming from BaDaSt.
			 */
			this.subscriberController.interrupt();
			int indexOfFirstToTransfer = Collections.binarySearch(objectsFromSource, objectFromBaDaSt, COMPARATOR);
			for (int i = indexOfFirstToTransfer; i < objectsFromSource.size(); i++) {
				this.recoveryPO.transfer(objectsFromSource.get(i), port);
			}
			this.recoveryPO.lockOnLastSeenElements(false);
			this.recoveryPO.setTransferMode(BaDaStRecoveryTransferMode.LIVE);
			objectsFromSource.clear();

		} else /* delta < 0 */ {
			// The newest buffered element from original source is older
			// than the newest element from BaDaSt.

			/*
			 * BaDaSt caught up with the original source. Let's (1) keep the
			 * first element from BaDaSt in mind, which is not transferred
			 * anymore, (2) stop transferring from BaDaSt, (3) watch incoming
			 * elements from the original source until we see the element from
			 * (1), (4) transfer from the original source (the element from (1)
			 * should be the first to transfer), (5) stop consuming from BaDaSt.
			 */
			this.firstNoMoreTransferred = objectFromBaDaSt;
			this.recoveryPO.setTransferMode(BaDaStRecoveryTransferMode.SWITCH);
			// (3) and (4) is done in SourceRecoveryPO.process_next
			this.subscriberController.interrupt();
		}
	}

	@Override
	public void transfer(StreamObject object) {
		transfer(object, 0);
	}

	@Override
	public void propagateDone() {
		this.recoveryPO.propagateDone();
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		this.recoveryPO.sendPunctuation(punctuation, 0);
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int outPort) {
		this.recoveryPO.sendPunctuation(punctuation);
	}

	@Override
	public String getName() {
		return this.recoveryPO.getName();
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

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

}