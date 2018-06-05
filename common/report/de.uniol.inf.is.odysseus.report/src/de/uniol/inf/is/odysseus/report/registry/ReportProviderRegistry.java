package de.uniol.inf.is.odysseus.report.registry;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.report.IReportProvider;

public final class ReportProviderRegistry {

	private final List<IReportProvider> providers = Lists.newArrayList();

	public void addReportProvider(IReportProvider provider) {
		Preconditions.checkNotNull(provider, "Provider to add must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(provider.getTitle()), "Title of report provider %s must not be null or empty!", provider.getClass());
		
		synchronized (providers) {
			if (!providers.contains(provider)) {
				providers.add(provider);
			}
		}
	}

	public void removeReportProvider(IReportProvider provider) {
		Preconditions.checkNotNull(provider, "Provider to remove must not be null!");

		synchronized (providers) {
			providers.remove(provider);
		}
	}

	public ImmutableList<IReportProvider> getSortedReportProviders() {
		synchronized( providers ) {
			Collections.sort(providers, ReportProviderComparator.INSTANCE);
	
			return ImmutableList.copyOf(providers);
		}
	}
}
