package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
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
	private final ITransferHandler<StreamObject> mRecoveryTransferHandler = new AbstractSourceRecoveryTransferHandler() {

		@SuppressWarnings("unchecked")
		@Override
		public void transfer_intern(IStreamable object, int port) {
			if (object.isPunctuation()) {
				SourceRecoveryPO.this.sendPunctuation((IPunctuation) object, port);
			} else {
				SourceRecoveryPO.this.transfer((StreamObject) object, port);
			}
		}

		@Override
		public String getName() {
			return SourceRecoveryPO.this.getName();
		}

	};

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

	@Override
	protected void process_open() throws OpenFailedException {
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