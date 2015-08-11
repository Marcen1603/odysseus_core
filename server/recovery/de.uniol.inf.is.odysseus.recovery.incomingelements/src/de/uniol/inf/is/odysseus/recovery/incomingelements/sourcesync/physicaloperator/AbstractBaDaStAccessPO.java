package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.IKafkaConsumer;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.KafkaConsumerAccess;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.BaDaStAccessAO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.transform.TBaDaStAccessAORule;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * It can operate in backup or recovery mode, set by {@link TBaDaStAccessAORule}
 * and {@link BaDaStAccessAO#isInRecoveryMode()}.<br />
 * <br />
 * In backup mode ({@link BaDaStBackupPO}), it transfers all incoming elements
 * without delay. But it adjusts it's offset as a Kafka consumer: It reads
 * stored elements from Kafka server and checks which offset the first element
 * of process_next has (means which index has this element on the Kafka server).
 * All elements with a lower index shall not be considered for this
 * operator/query in case of recovery, because they are not processed. <br />
 * <br />
 * In recovery mode ({@link BaDaStRecoveryPO}), it acts as a Kafka consumer and
 * pushes data stream elements from there to it's next operators, until it gets
 * the same elements from both Kafka and original source. Elements from the
 * original source will be discarded in this time. But they are not lost, since
 * they are backed up again by BaDaSt.
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            The type of stream objects to process.
 */
public abstract class AbstractBaDaStAccessPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> implements IStatefulPO, IKafkaConsumer {

	/**
	 * The access to the source, which is recorded by BaDaSt.
	 */
	protected final AbstractAccessAO mSourceAccess;

	/**
	 * The name of the source.
	 */
	protected final String mSourceName;

	/**
	 * The index of {@link #mReference} stored on the Kafka server.
	 */
	protected Long mOffset = -1l;

	/**
	 * The data handler to use.
	 */
	protected final IDataHandler<T> mDataHandler;

	/**
	 * The protocol handler to use.
	 */
	protected final IProtocolHandler<T> mProtocolHandler;

	/**
	 * The access to the Kafka server as consumer.
	 */
	protected KafkaConsumerAccess mKafkaAccess;

	/**
	 * Creates a new {@link AbstractBaDaStAccessPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	@SuppressWarnings("unchecked")
	// cast of data handler
	public AbstractBaDaStAccessPO(BaDaStAccessAO logical) {
		super();
		this.mSourceAccess = logical.getSource();
		this.mSourceName = this.mSourceAccess.getAccessAOName()
				.getResourceName();
		this.mDataHandler = (IDataHandler<T>) DataHandlerRegistry
				.getDataHandler(this.mSourceAccess.getDataHandler(),
						this.mSourceAccess.getOutputSchema());
		OptionMap options = new OptionMap(this.mSourceAccess.getOptions());
		IAccessPattern access = IAccessPattern.PUSH;
		if (this.mSourceAccess.getWrapper().toLowerCase().contains("pull")) {
			// XXX AccessPattern: Better way to determine the AccessPattern?
			access = IAccessPattern.PULL;
		}
		this.mProtocolHandler = (IProtocolHandler<T>) ProtocolHandlerRegistry
				.getInstance(this.mSourceAccess.getProtocolHandler(),
						ITransportDirection.IN, access, options,
						this.mDataHandler);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !AbstractBaDaStAccessPO.class.isInstance(obj)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		AbstractBaDaStAccessPO<T> other = (AbstractBaDaStAccessPO<T>) obj;
		return this.mSourceAccess.equals(other.mSourceAccess);
	}

	@Override
	protected void process_close() {
		this.mOffset = -1l;
		if (this.mKafkaAccess != null) {
			AbstractBaDaStAccessPO.this.mKafkaAccess.interrupt();
		}
		super.process_close();
	}

	@Override
	public IOperatorState getState() {
		return new BaDaStAccessState(this.mOffset);
	}

	@Override
	public void setState(Serializable state) {
		this.mOffset = ((BaDaStAccessState) state).getOffset();
	}

}