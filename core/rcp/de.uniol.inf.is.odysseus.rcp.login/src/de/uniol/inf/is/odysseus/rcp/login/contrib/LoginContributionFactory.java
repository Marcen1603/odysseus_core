package de.uniol.inf.is.odysseus.rcp.login.contrib;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;

public final class LoginContributionFactory {

	private static final Logger LOG = LoggerFactory.getLogger(LoginContributionFactory.class);
	
	public Optional<ILoginContribution> create( Class<? extends ILoginContribution> classToCreate ) {
		Preconditions.checkNotNull(classToCreate, "Login contribution class to create instance must not be null!");
		
		try {
			LOG.debug("Creating instance of {}", classToCreate);
			return Optional.<ILoginContribution>of(classToCreate.newInstance());
		} catch (InstantiationException | IllegalAccessException ex) {
			LOG.error("Could not create instance of login contribution class", ex);
			return Optional.absent();
		}
	}
	
	public Collection<ILoginContribution> create( Collection<Class<? extends ILoginContribution>> classesToCreate ) {
		Collection<ILoginContribution> contributions = Lists.newArrayList();
		
		LOG.debug("Creating {} login contributions", classesToCreate.size());
		for( Class<? extends ILoginContribution> classToCreate : classesToCreate ) {
			Optional<ILoginContribution> optContribution = create(classToCreate);
			if( optContribution.isPresent() ) {
				contributions.add(optContribution.get());
			}
		}
		
		return contributions;
	}
}
