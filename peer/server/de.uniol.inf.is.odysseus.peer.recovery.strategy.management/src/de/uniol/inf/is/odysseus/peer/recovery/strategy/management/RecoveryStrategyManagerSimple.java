package de.uniol.inf.is.odysseus.peer.recovery.strategy.management;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;

public class RecoveryStrategyManagerSimple implements IRecoveryStrategyManager {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyManagerSimple.class);

	
	// called by OSGi-DS
	public void activate() {
		LOG.debug("Simple recovery strategy manager activated.");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Simple recovery strategy manager deactivated.");
	}
	
	/**
	 * The collection for the recovery strategies, if there is one bound.
	 */
	private static Collection<IRecoveryStrategy> recoveryStrategies = Lists.newArrayList();
	
	/**
	 * Binds a recovery strategy. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery strategy to bind. <br />
	 * Must be not null.
	 */
	public static void bindRecoveryStrategy(IRecoveryStrategy serv) {
		
		Preconditions.checkNotNull(serv);
		recoveryStrategies.add(serv);
		LOG.debug("Bound {} as a recovery strategy.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a recovery strategy, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery strategy to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindRecoveryStrategy(IRecoveryStrategy serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (recoveryStrategies.contains(serv)) {		
			recoveryStrategies.remove(serv);
			LOG.debug("Unbound {} as a recovery strategy.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	@Override
	public void startRecovery(PeerID failedPeer) {
		if(recoveryStrategies.size() > 0){
			recoveryStrategies.iterator().next().recover(failedPeer);
		} else {
			LOG.error("No recovery strategy bound");
		}
		
	}

	@Override
	public String getName() {
		return "simple";
	}

}
