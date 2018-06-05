package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * Some DashboardParts, such as a Map, can handle more than one source. To
 * handle such DashboadParts, they implement this abstract class. The Wizard
 * distinguishes between the normal and this implementation.
 * 
 * @author Tobias Brandt
 *
 */
public abstract class AbstractMultiSourceDashboardPart extends AbstractDashboardPart {

	private List<IDashboardPartQueryTextProvider> queryTextProviders = new ArrayList<>();

	/**
	 * Adds the queryTextProvider to the list. Instead of setting the
	 * queryTextProvider, it is added. If you call this method twice, two
	 * providers will be in the list.
	 * 
	 * Does not call the super method. This method should not be used if you
	 * know it is a MultiSourceDashboadPart. Use {@code addQueryTextProvider}
	 * instead.
	 */
	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Preconditions.checkNotNull(provider,
				"QueryTextProvider for DashboardPart must not be null!");
		addQueryTextProvider(provider);
	}

	/**
	 * Adds a queryTextProvider to the list
	 * 
	 * @param provider
	 *            The provider to add to the list
	 */
	public void addQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
		this.queryTextProviders.add(provider);
	}

	/**
	 * The list of all queryTextProviders for this DashboardPart.
	 * 
	 * @return A copy of the list of all queryTextProviders
	 */
	public List<IDashboardPartQueryTextProvider> getQueryTextProviders() {
		return new ArrayList<IDashboardPartQueryTextProvider>(this.queryTextProviders);
	}
}
