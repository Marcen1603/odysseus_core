package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public abstract class AbstractMultiSourceDashboardPart extends AbstractDashboardPart {

	private List<IDashboardPartQueryTextProvider> queryTextProviders = new ArrayList<>();
	private Map<Object, Map<String, String>> contextMap = Maps.newHashMap();

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Preconditions.checkNotNull(provider,
				"QueryTextProvider for DashboardPart must not be null!");
		addQueryTextProvider(provider);
	}

	public void addQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
		this.queryTextProviders.add(provider);
	}

	public List<IDashboardPartQueryTextProvider> getQueryTextProviders() {
		return new ArrayList<IDashboardPartQueryTextProvider>(this.queryTextProviders);
	}

	// We need a new interface implementation for the context values as each
	// source has its own context values

	public void addContext(Object sourceKey, String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "value for context map must not be null or empty!");

		Map<String, String> context = getContextMap(sourceKey);
		context.put(key, value);
	}

	private Map<String, String> getContextMap(Object key) {
		Map<String, String> context = this.contextMap.get(key);

		if (context == null) {
			context = new HashMap<String, String>();
			this.contextMap.put(key, context);
		}

		return context;
	}

	public Optional<String> getContextValue(Object sourceKey, String key) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");

		return Optional.fromNullable(getContextMap(sourceKey).get(key));
	}

	public ImmutableCollection<String> getContextKeys(Object sourceKey) {
		return ImmutableSet.copyOf(getContextMap(sourceKey).keySet());
	}

	public void removeContext(Object sourceKey, String key) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");
		getContextMap(sourceKey).remove(key);
	}

}
