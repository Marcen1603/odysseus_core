package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
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
		 * Empty default constructor.
		 */
		public RecoveryTransferHandler() {
			// Empty default constructor
		}

		@SuppressWarnings("unchecked")
		@Override
		public void transfer_intern(IStreamable object, int port) {
			if (object.isPunctuation()) {
				SourceRecoveryPO.this.sendPunctuation((IPunctuation) object, port);
			} else {
				StreamObject strObj = (StreamObject) object;
				try {
					strObj.setMetadata(getMetadataInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				updateMetadata(strObj);
				SourceRecoveryPO.this.transfer(strObj, port);
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
		// Do not transfer, because elements from publish subscribe system will
		// be transfered with
		// another transfer handler.
		// XXX SourceRecovery: Shall it be possible to get live again?
		adjustOffsetIfNeeded(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Do not transfer, because elements from publish subscribe system will
		// be transfered with
		// another transfer handler.
		// XXX SourceRecovery: Shall it be possible to get live again?
		adjustOffsetIfNeeded(punctuation);
	}

}