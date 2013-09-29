package de.uniol.inf.is.odysseus.p2p_new.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerAssignment;
import de.uniol.inf.is.odysseus.p2p_new.IPeerAssignmentProvider;

/**
 * A singleton peer assignment provider.
 * @author Michael Brand
 *
 */
public class PeerAssignmentProvider implements IPeerAssignmentProvider {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PeerAssignmentProvider.class);
	
	/**
	 * The single instance of this class.
	 */
	private static PeerAssignmentProvider instance;
	
	/**
	 * Mapping (name -> implementation) of all integrated peer assignment strategies.
	 */
	private Map<String, IPeerAssignment> peerAssignmentToNameMap = Maps.newHashMap();
	
	/**
	 * Returns the single instance of this class.
	 */
	public static PeerAssignmentProvider getInstance() {
		
		return instance;
		
	}
	
	/**
	 * Activates the peer assignment provider. <br />
	 * Called by OSGI.
	 */
	public void activate() {
		
		instance = this;
		
	}
	
	/**
	 * Deactivates the peer assignment provider. <br />
	 * Called by OSGI.
	 */
	public void deactivate() {
		
		instance = null;
		
	}
	
	/**
	 * Binds the referenced {@link IPeerAssignment}. <br />
	 * Called by OSGI-DS.
	 * 
	 * @see #unbindPeerAssignment(IPeerAssignment)
	 * @param paStrategy
	 *            An instance of an {@link IPeerAssignment} implementation.
	 * @author Michael Brand
	 */
	public final void bindPeerAssignment(IPeerAssignment paStrategy) {

		peerAssignmentToNameMap.put(paStrategy.getName(), paStrategy);
		LOG.debug("Peer assignment strategy bound '{}'", paStrategy.getName());

	}

	/**
	 * Unbinds an referenced {@link IPeerAssignment}, if <code>paStrategy</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * 
	 * @see #bindPeerAssignment(IPeerAssignment)
	 * @param paStrategy
	 *            An instance of an {@link IPeerAssignment} implementation.
	 * @author Michael Brand
	 */
	public final void unbindPeerAssignment(IPeerAssignment paStrategy) {

		String strategyName = paStrategy.getName();
		if(peerAssignmentToNameMap.containsKey(strategyName)) {

			peerAssignmentToNameMap.remove(strategyName);
			LOG.debug("Peer assignment strategy unbound '{}'", strategyName);

		}

	}

	@Override
	public ImmutableCollection<String> getPeerAssignmentNames() {
		
		return ImmutableSet.copyOf(peerAssignmentToNameMap.keySet());
		
	}

	@Override
	public Optional<IPeerAssignment> getPeerAssignment(String name) {
		
		return Optional.fromNullable(peerAssignmentToNameMap.get(name));
		
	}

}