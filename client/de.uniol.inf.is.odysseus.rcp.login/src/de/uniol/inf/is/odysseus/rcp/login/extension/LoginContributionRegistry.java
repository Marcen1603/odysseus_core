package de.uniol.inf.is.odysseus.rcp.login.extension;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;

public class LoginContributionRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(LoginContributionRegistry.class);
	
	private final List<Class<? extends ILoginContribution>> contributions = Lists.newArrayList();
	
	public void add( Class<? extends ILoginContribution> loginContribution ) {
		// Preconditions.checkNotNull( loginContribution, "Login contribution to add must not be null!");
		// Preconditions.checkArgument( !contains(loginContribution), "Login contribution '%s' is already added to registry", loginContribution);
		
		LOG.debug("Adding {} to login contribution registry", loginContribution);
		contributions.add(loginContribution);
	}
	
	public void remove( Class<? extends ILoginContribution> loginContribution ) {
		LOG.debug("Adding {} to login contribution registry", loginContribution);

		contributions.remove(loginContribution);
	}
	
	public boolean contains( Class<? extends ILoginContribution> loginContribution ) {
		// Preconditions.checkNotNull(loginContribution, "Login contribution to check must not be null!");
		
		return contributions.contains(loginContribution);
	}
	
	public boolean isEmpty() {
		return contributions.isEmpty();
	}
	
	public ImmutableCollection<Class<? extends ILoginContribution>> getAllContributions() {
		return ImmutableList.copyOf(contributions);
	}
}
