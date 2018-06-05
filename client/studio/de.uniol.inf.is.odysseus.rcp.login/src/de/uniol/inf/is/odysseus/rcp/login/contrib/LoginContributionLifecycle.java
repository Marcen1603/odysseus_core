package de.uniol.inf.is.odysseus.rcp.login.contrib;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;

public class LoginContributionLifecycle {

	private static final Logger LOG = LoggerFactory.getLogger(LoginContributionLifecycle.class);
	
	private final Collection<ILoginContribution> contributions;
	
	public LoginContributionLifecycle( Collection<ILoginContribution> contributions ) {
		Preconditions.checkNotNull(contributions, "Collection of login contributions for lifecycle must not be null!");
		Preconditions.checkArgument(!contributions.isEmpty(), "Collection of login contributions for lifecycle must not be empty!");
		
		LOG.debug("Creating lifecyle for {} login contributions", contributions.size());
		this.contributions = sortByPriorityAsc(contributions);
	}
	
	private static Collection<ILoginContribution> sortByPriorityAsc(Collection<ILoginContribution> contributions) {
		List<ILoginContribution> sorted = Lists.newArrayList(contributions);
		Collections.sort(sorted, new Comparator<ILoginContribution>() {
			@Override
			public int compare(ILoginContribution a, ILoginContribution b) {
				return Integer.compare(a.getPriority(), b.getPriority());
			}
		});
		Collections.reverse(sorted);
		return sorted;
	}

	public void onInitAll() {
		LOG.debug("Init all login contributions");
		for( ILoginContribution contribution : contributions ) {
			tryOnInit(contribution);
		}
	}
	
	private static void tryOnInit(ILoginContribution contribution) {
		try {
			contribution.onInit();
		} catch( Throwable t ) {
			LOG.error("Exception during initializing login contribution {}", contribution, t);
		}
	}

	public void onLoadAll( Map<String, String> savedConfig ) {
		Preconditions.checkNotNull(savedConfig, "Saved config must not be null");
		LOG.debug("Load config in all login contributions");
		
		for( ILoginContribution contribution : contributions ) {
			Map<String, String> configClone = deepCloneMap(savedConfig);
			
			tryOnLoad(contribution, configClone);
		}
	}
	
	private static Map<String, String> deepCloneMap(Map<String, String> map) {
		Map<String, String> mapCopy = Maps.newHashMap();
		
		for( String key : map.keySet()) {
			mapCopy.put(key, map.get(key));
		}
		
		return mapCopy;
	}
	
	private static void tryOnLoad(ILoginContribution contribution, Map<String, String> config) {
		try {
			contribution.onLoad(config);
		} catch( Throwable t ) {
			LOG.error("Exception during loading configuration for login contribution {}", contribution, t);
		}
	}

	public boolean areAllValid() {
		for( ILoginContribution contribution : contributions ) {
			if( !tryIsValid(contribution) ) {
				return false;
			}
		}
		
		return true;
	}

	private static boolean tryIsValid(ILoginContribution contribution) {
		try {
			return contribution.isValid();
		} catch( Throwable t ) {
			LOG.error("Exception during validating login contribution {}", contribution, t);
			
			return false;
		}
	}
	
	public Map<String, String> onSaveAll() {
		Map<String, String> config = Maps.newHashMap();
		LOG.debug("Save config for all login contributions");
		
		for( ILoginContribution contribution: contributions ) {
			config.putAll(tryOnSave(contribution));
		}
		
		return config;
	}

	private static Map<String, String> tryOnSave(ILoginContribution contribution) {
		try {
			return contribution.onSave();
		} catch( Throwable t ) {
			LOG.error("Exception during saving in login contribution {}", contribution, t );
			
			return Maps.newHashMap();
		}
	}
	
	public boolean onFinishAll() {
		LOG.debug("Finishing all login contributions");
		
		Collection<ILoginContribution> contributionsCopy = Lists.newArrayList(contributions);
		boolean allFinished = true;
		for( ILoginContribution contribution : contributions ) {
			allFinished &= tryOnFinish(contribution, contributionsCopy);
		}
		
		LOG.debug("Finish = {}", allFinished);
		return allFinished;
	}

	private boolean tryOnFinish(ILoginContribution contribution, Collection<ILoginContribution> otherContributions) {
		try {
			return contribution.onFinish(otherContributions);
		} catch( Throwable t ) {
			LOG.error("Exception during finishing contribution", contribution, t);
			return false;
		}
	}
}
