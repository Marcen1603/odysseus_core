package de.uniol.inf.is.odysseus.rcp.login.extension;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;

public class LoginContributionRegistry {

	private final List<Class<? extends ILoginContribution>> contributions = Lists.newArrayList();
	
	public void add( Class<? extends ILoginContribution> loginContribution ) {
		Preconditions.checkNotNull( loginContribution, "Login contribution to add must not be null!");
		Preconditions.checkArgument( !contains(loginContribution), "Login contribution '%s' is already added to registry", loginContribution);
		
		contributions.add(loginContribution);
	}
	
	public void remove( Class<? extends ILoginContribution> loginContribution ) {
		contributions.remove(loginContribution);
	}
	
	public boolean contains( Class<? extends ILoginContribution> loginContribution ) {
		Preconditions.checkNotNull(loginContribution, "Login contribution to check must not be null!");
		
		return contributions.contains(loginContribution);
	}
	
	public boolean isEmpty() {
		return contributions.isEmpty();
	}
	
	public ImmutableCollection<Class<? extends ILoginContribution>> getAllContributions() {
		return ImmutableList.copyOf(contributions);
	}
}
