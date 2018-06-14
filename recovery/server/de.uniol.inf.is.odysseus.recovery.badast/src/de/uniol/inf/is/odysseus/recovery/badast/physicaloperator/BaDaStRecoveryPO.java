package de.uniol.inf.is.odysseus.recovery.badast.physicaloperator;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.badast.subscriber.ISubscriber;
import de.uniol.inf.is.odysseus.badast.subscriber.ISubscriberController;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.recovery.badast.util.BaDaStRecoveryProtocolHandlerCreator;
import de.uniol.inf.is.odysseus.recovery.badast.util.BaDaStRecoveryTransferHandler;
import de.uniol.inf.is.odysseus.recovery.badast.util.BaDaStRecoveryTransferMode;
import de.uniol.inf.is.odysseus.recovery.badast.util.SubscriberControllerProvider;

/**
 * Operator to synchronize incoming elements with stored elements at BaDaSt.
 * <br />
 * <br />
 * The operator enhances the {@link BaDaStBackupPO}. Elements are retrieved from
 * BaDaSt and elements after the element, that has been loaded as operator state
 * will be transferred. Additionally, the transfer mode will be switched again
 * from BaDaSt to the original source, if it is possible without data loss.
 *
 * @author Michael Brand
 *
 */
public class BaDaStRecoveryPO<StreamObject extends IStreamObject<IMetaAttribute>> extends BaDaStBackupPO<StreamObject>
		implements ISubscriber {

	/**
	 * Max. number of elements from the original source to buffer.
	 */
	private static final int BUFFERSIZE = 1000000;

	/**
	 * The {@link ISubscriberController} used for the request.
	 */
	private ISubscriberController subController;

	/**
	 * The protocol handler to use in recovery mode.
	 */
	private IProtocolHandler<StreamObject> protocolHandler;

	/**
	 * The transfer handler for the objects from public subscribe system in
	 * recovery mode. Not for the objects from the source operator.
	 */
	private final BaDaStRecoveryTransferHandler<StreamObject> transferHandler;

	/**
	 * A {@link BaDaStRecoveryPO} can have different transfer modes: It can
	 * transfer elements from the original data source (live) or from BaDaSt
	 * (recovery).
	 */
	private BaDaStRecoveryTransferMode transferMode = BaDaStRecoveryTransferMode.NONE;

	/**
	 * The first element from the original data source after restart of the
	 * stream processing.
	 */
	private Optional<StreamObject> firstElementAfterRestart = Optional.absent();

	/**
	 * The last elements from the original source, which has been recognized.
	 */
	private final List<StreamObject> lastSeenElements = new ArrayList<>(BUFFERSIZE);

	/**
	 * {@link #lastSeenElements} may need to be locked to ensure that they are
	 * not cleared while {@link BaDaStRecoveryTransferHandler} works with them.
	 */
	private boolean lockOnLastSeenElements = false;

	/**
	 * Creates a new BaDaSt recovery operator.
	 *
	 * @param access
	 *            The access to the source, which is recorded by BaDaSt.
	 */
	public BaDaStRecoveryPO(AbstractAccessAO access) {
		super(access);
		this.subController = SubscriberControllerProvider.newInstance(access.getAccessAOName().toString(), 0, this, 0);
		this.transferHandler = new BaDaStRecoveryTransferHandler<>(this, this.subController);
		this.protocolHandler = BaDaStRecoveryProtocolHandlerCreator.createProtocolHandler(access, this.transferHandler);
	}

	/**
	 * Initializes the consumption from BaDaSt. Should be called after open or after setStateIniternal!
	 */
	private void init() {
		this.transferHandler.init();
		this.subController.start();
		// calls onNewMessage
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		init();
	}

	@Override
	public void setStateInternal(Serializable state) {
		super.setStateInternal(state);
		init();
	}

	/**
	 * Gets the transfer modes: It can transfer elements from the original data
	 * source (live) or from BaDaSt (recovery).
	 */
	public BaDaStRecoveryTransferMode getTransferMode() {
		synchronized (this.transferMode) {
			return this.transferMode;
		}
	}

	/**
	 * Sets the transfer modes: It can transfer elements from the original data
	 * source (live) or from BaDaSt (recovery).
	 */
	public void setTransferMode(BaDaStRecoveryTransferMode mode) {
		synchronized (this.transferMode) {
			this.transferMode = mode;
		}
	}

	/**
	 * Gets the first element from the original data source after restart of the
	 * stream processing.
	 */
	public Optional<StreamObject> getFirstElementAfterRestart() {
		return this.firstElementAfterRestart;
	}

	/**
	 * Gets the last elements from the original source, which has been
	 * recognized.
	 */
	public List<StreamObject> getLastSeenElements() {
		synchronized (this.lastSeenElements) {
			return this.lastSeenElements;
		}
	}

	/**
	 * Sets the lock on the list returned by {@link #getLastSeenElements()} to
	 * ensure that they are not cleared.
	 */
	public void lockOnLastSeenElements(boolean lock) {
		synchronized (this.lastSeenElements) {
			this.lockOnLastSeenElements = lock;
		}
	}

	@Override
	public void onNewMessage(ByteBuffer message, long offset) throws Throwable {
		this.protocolHandler.process(0, message);
		// calls this.transferHandler.transfer
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		if (!this.firstElementAfterRestart.isPresent()) {
			// First element, which is for sure not a duplicate
			this.firstElementAfterRestart = Optional.of(object);
		}

		synchronized (this.transferMode) {
			if (this.transferMode == BaDaStRecoveryTransferMode.LIVE) {
				// (4) transfer from the original source (the element from (1)
				// should be the first to transfer)
				this.transfer(object, port);
				return;
			} else if (this.transferMode == BaDaStRecoveryTransferMode.SWITCH
					&& object.equals(this.transferHandler.getFirstNoMoreTransferred())) {
				// (3) watch incoming elements from the original source until we
				// see
				// the element from (1)
				this.transfer(object, port);
				this.transferMode = BaDaStRecoveryTransferMode.LIVE;
				return;
			} else {
				// Do not transfer, because elements from publish subscribe
				// system
				// will be transfered with another transfer handler.
				while (this.lockOnLastSeenElements) {
					synchronized (this) {
						try {
							this.wait(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				synchronized (this.lastSeenElements) {
					if (this.lastSeenElements.size() == BUFFERSIZE) {
						this.lastSeenElements.clear();
					}
					this.lastSeenElements.add(object);
				}
			}
		}
	}

}