package de.uniol.inf.is.odysseus.peer.distribute.registry;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.distribute.IDistributionChecker;

public final class DistributionCheckerRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(DistributionCheckerRegistry.class);
	
	private static DistributionCheckerRegistry instance;
	
	private final Collection<IDistributionChecker> checkers = Lists.newArrayList();

	public static DistributionCheckerRegistry getInstance() {
		if( instance == null ) {
			instance = new DistributionCheckerRegistry();
		}
		return instance;
	}
	
	public void add( IDistributionChecker checker ) {
		Preconditions.checkNotNull(checker, "Distribution checker to add to registry must not be null!");
		Preconditions.checkArgument(!contains(checker), "Distribution checker %s already registered", checker.getClass());
		
		checkers.add(checker);
		LOG.debug("Distribution checker {} added", checker.getClass());
	}
	
	public void remove( IDistributionChecker checker ) {
		Preconditions.checkNotNull(checker, "Distribution checker to remove from registry must not be null!");
		
		if(checkers.contains(checker)) {
			checkers.remove(checker);
			LOG.debug("Distribution checker {} removed", checker.getClass());
		} else {
			LOG.warn("Tried to remove distribution checker which was not added before: {}", checker.getClass());
		}
	}
	
	public boolean contains( IDistributionChecker checker ) {
		Preconditions.checkNotNull(checker, "Distribution checker to check existence in registry must not be null!");
		
		return checkers.contains(checker);
	}
	
	public ImmutableCollection<IDistributionChecker> getAll() {
		return ImmutableList.copyOf(checkers);
	}
}
