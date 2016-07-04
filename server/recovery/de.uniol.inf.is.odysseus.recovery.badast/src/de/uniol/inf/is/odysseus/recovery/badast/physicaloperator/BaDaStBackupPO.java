package de.uniol.inf.is.odysseus.recovery.badast.physicaloperator;

import java.io.Serializable;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.badast.util.BaDaStSyncState;

/**
 * Operator to synchronize incoming elements with stored elements at BaDaSt.
 * <br />
 * <br />
 * The operator transfers incoming elements without delay and keeps the last
 * transfered one in mind. If a checkpoint is reached, that element is stored as
 * operator state, because only newer elements shall be recovered.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStBackupPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<StreamObject, StreamObject> implements IStatefulPO {

	/**
	 * The access to the source, which is recorded by BaDaSt.
	 */
	private final AbstractAccessAO sourceAccess;

	/**
	 * The last transferred element.
	 */
	private Optional<StreamObject> lastTransferredElement = Optional.absent();

	/**
	 * The element, that has been loaded as operator state.
	 */
	private Optional<StreamObject> loadedElement = Optional.absent();

	/**
	 * Creates a new BaDaSt backup operator.
	 * 
	 * @param access
	 *            The access to the source, which is recorded by BaDaSt.
	 */
	public BaDaStBackupPO(AbstractAccessAO access) {
		this.sourceAccess = access;
	}

	/**
	 * Gets the access to the source, which is recorded by BaDaSt.
	 */
	public AbstractAccessAO getSourceAccess() {
		return this.sourceAccess;
	}

	/**
	 * Gets the element, that has been loaded as operator state.
	 */
	public Optional<StreamObject> getLoadedElement() {
		synchronized (this.loadedElement) {
			return this.loadedElement;
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !BaDaStBackupPO.class.isInstance(obj)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		BaDaStBackupPO<StreamObject> other = (BaDaStBackupPO<StreamObject>) obj;
		return this.sourceAccess.equals(other.sourceAccess);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation, port);
	}

	@Override
	public IOperatorState getState() {
		synchronized (this.lastTransferredElement) {
			return new BaDaStSyncState<StreamObject>(this.lastTransferredElement.get());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setStateInternal(Serializable state) {
		synchronized (this.lastTransferredElement) {
			StreamObject element = ((BaDaStSyncState<StreamObject>) state).getReference();
			this.lastTransferredElement = Optional.of(element);
			this.loadedElement = Optional.of(element);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		transfer(object, port);
	}

	@Override
	public void transfer(StreamObject object, int sourceOutPort) {
		super.transfer(object, sourceOutPort);
		synchronized (this.lastTransferredElement) {
			this.lastTransferredElement = Optional.of(object);
		}
	}

}