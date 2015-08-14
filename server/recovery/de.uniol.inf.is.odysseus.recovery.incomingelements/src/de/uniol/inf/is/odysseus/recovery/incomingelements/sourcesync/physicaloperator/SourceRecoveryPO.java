package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.IKafkaConsumer;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.KafkaConsumerAccess;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceRecoveryAO;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * In recovery mode, it acts as a Kafka consumer and pushes data stream elements
 * from there to it's next operators, until it gets the same elements from both
 * Kafka and original source. Elements from the original source will be
 * discarded in this time. But they are not lost, since they are backed up by
 * BaDaSt.
 * 
 * @author Michael Brand
 * 
 * @param <StreamObject>
 *            The type of stream elements to process.
 */
public class SourceRecoveryPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractSourceRecoveryPO<StreamObject> {

	/**
	 * The protocol handler to use in recovery mode.
	 */
	private final IProtocolHandler<StreamObject> mRecoveryProtocolHandler;

	/**
	 * The access to the Kafka server to use in recovery mode.
	 */
	private KafkaConsumerAccess mRecoveryKafkaAccess;

	/**
	 * The handler of messages consumed from Kafka in recovery mode.
	 */
	private final IKafkaConsumer mRecoveryKafkaConsumer = new IKafkaConsumer() {

		@Override
		public void onNewMessage(ByteBuffer message, long offset)
				throws Throwable {
			SourceRecoveryPO.this.mRecoveryProtocolHandler.process(0, message);

		}

	};

	/**
	 * Transfer handler for the objects from Kafka server in recovery mode. Not
	 * for the objects from the source operator.
	 */
	private final ITransferHandler<StreamObject> mRecoveryTransferHandler = new AbstractSourceRecoveryTransferHandler() {

		@SuppressWarnings("unchecked")
		@Override
		public void transfer_intern(IStreamable object, int port) {
			if (object.isPunctuation()) {
				SourceRecoveryPO.this.sendPunctuation((IPunctuation) object,
						port);
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
		this.mRecoveryProtocolHandler.setTransfer(mRecoveryTransferHandler);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// offset should be loaded as operator state
		this.mNeedToAdjustOffset = false;
		this.mRecoveryKafkaAccess = new KafkaConsumerAccess(this.mSourceAccess
				.getAccessAOName().getResourceName(),
				this.mRecoveryKafkaConsumer, this.mOffset);
		this.mRecoveryKafkaAccess.start();
		super.process_open();
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		// Do not transfer, because elements from Kafka will be transfered with
		// another transfer handler.
		// XXX SourceRecovery: Shall it be possible to get live again?
		adjustOffsetIfNeeded(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Do not transfer, because elements from Kafka will be transfered with
		// another transfer handler.
		// XXX SourceRecovery: Shall it be possible to get live again?
		adjustOffsetIfNeeded(punctuation);
	}

}