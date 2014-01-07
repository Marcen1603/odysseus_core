package de.uniol.inf.is.odysseus.p2p_new;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

/**
 * The interface for peer assignment provider.
 * @author Michael Brand
 */
public interface IPeerAssignmentProvider {
	
	/**
	 * Returns an immutable collection of the names of all bind peer assignment strategies.
	 */
	public ImmutableCollection<String> getPeerAssignmentNames();
	
	/**
	 * Returns the peer assignment strategy given by name, if bound.
	 */
	public Optional<IPeerAssignment> getPeerAssignment(String name);

}