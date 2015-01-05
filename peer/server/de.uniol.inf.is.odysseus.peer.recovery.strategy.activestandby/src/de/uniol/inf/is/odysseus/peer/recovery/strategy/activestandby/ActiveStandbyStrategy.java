package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby;

import java.util.UUID;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.simplestrategy.SimpleRecoveryStrategy;

/**
 * The active standby strategy uses already existing replicas of the query parts to recover. <br />
 * It calls {@link SimpleRecoveryStrategy#recover(PeerID, UUID)} or
 * {@link SimpleRecoveryStrategy#recoverSingleQueryPart(PeerID, UUID, UUID)}.
 * 
 * @author Michael Brand
 *
 */
public class ActiveStandbyStrategy implements IRecoveryStrategy {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ActiveStandbyStrategy.class);

	/**
	 * The recovery allocator, if there is one bound.
	 */
	private static Optional<IRecoveryAllocator> cRecoveryAllocator = Optional.absent();

	/**
	 * Binds a recovery allocator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery allocator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryAllocator(IRecoveryAllocator serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryAllocator.isPresent() && cRecoveryAllocator.get().getName().equals("roundrobinwithlocal")) {
			// use local as default so do nothing here
		} else {
			cRecoveryAllocator = Optional.of(serv);
			LOG.debug("Bound {} as a recovery allocator.", serv.getClass().getSimpleName());
		}

	}

	/**
	 * Unbinds a recovery allocator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery allocator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryAllocator(IRecoveryAllocator serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryAllocator.isPresent() && cRecoveryAllocator.get() == serv) {

			cRecoveryAllocator = Optional.absent();
			LOG.debug("Unbound {} as a recovery allocator.", serv.getClass().getSimpleName());

		}

	}

	@Override
	public void setAllocator(IRecoveryAllocator allocator) {
		cRecoveryAllocator = Optional.of(allocator);
	}

	/**
	 * @see #getName()
	 */
	public static String getStrategyName() {
		return "activestandby";
	}

	@Override
	public String getName() {
		return getStrategyName();
	}

	@Override
	public void recover(PeerID failedPeer, UUID recoveryStateIdentifier) {
		new SimpleRecoveryStrategy().recover(failedPeer, recoveryStateIdentifier);
	}

	@Override
	public void recoverSingleQueryPart(PeerID failedPeer, UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier) {
		new SimpleRecoveryStrategy().recoverSingleQueryPart(failedPeer, recoveryStateIdentifier,
				recoverySubStateIdentifier);
	}

}
