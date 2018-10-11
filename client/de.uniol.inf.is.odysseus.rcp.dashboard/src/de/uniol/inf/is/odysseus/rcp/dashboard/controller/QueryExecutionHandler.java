package de.uniol.inf.is.odysseus.rcp.dashboard.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractMultiSourceDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.GraphQueryFileProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.RunningQueryProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class QueryExecutionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(QueryExecutionHandler.class);

	private List<IFile> scriptFiles;
	private List<String[]> linesList;
	private List<String> queryNames;

	private Collection<Integer> queryIDs;
	private Collection<IPhysicalOperator> queryRoots;

	private IDashboardPart dashboardPart;

	public QueryExecutionHandler(IDashboardPart dashboardPart) {
		// Preconditions.checkNotNull(dashboardPart, "Dashboard part for execution must not be null!");

		// Create lists for queries
		scriptFiles = new ArrayList<>();
		linesList = new ArrayList<>();
		queryNames = new ArrayList<>();

		this.dashboardPart = dashboardPart;

		List<IDashboardPartQueryTextProvider> providers = new ArrayList<>();

		if (dashboardPart instanceof AbstractMultiSourceDashboardPart) {
			// We have more than one source
			providers = ((AbstractMultiSourceDashboardPart) dashboardPart).getQueryTextProviders();
		} else {
			// We have only one source
			IDashboardPartQueryTextProvider provider = dashboardPart.getQueryTextProvider();
			providers.add(provider);
		}

		for (IDashboardPartQueryTextProvider provider : providers) {

			if (provider instanceof ResourceFileQueryTextProvider) {
				ResourceFileQueryTextProvider resProvider = (ResourceFileQueryTextProvider) provider;
				this.scriptFiles.add(resProvider.getFile());
			} else if (provider instanceof SimpleQueryTextProvider) {
				SimpleQueryTextProvider simpleProvider = (SimpleQueryTextProvider) provider;
				ImmutableList<String> queryText = simpleProvider.getQueryText();
				this.linesList.add(queryText.toArray(new String[queryText.size()]));
			} else if (provider instanceof GraphQueryFileProvider) {
				GraphQueryFileProvider graphProvider = (GraphQueryFileProvider) provider;
				this.linesList.add(graphProvider.getQueryText().toArray(new String[0]));
			} else if (provider instanceof RunningQueryProvider) {
				RunningQueryProvider runningQueryProvider = (RunningQueryProvider) provider;
				this.queryNames.add(runningQueryProvider.getQueryName());
			} else {
				throw new RuntimeException("Unknown queryText provider: " + provider.getClass());
			}
		}

	}

	public void start() throws ControllerException {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();

		// Reset query-ids
		queryIDs = new ArrayList<>();

		try {

			// All queries by file
			int numScriptLines = -1;
			for (IFile scriptFile : this.scriptFiles) {
				List<String> lineList = FileUtil.read(scriptFile);
				linesList.add(lineList.toArray(new String[lineList.size()]));
				numScriptLines++;
			}

			// All queries by source
			int doneLinesCounter = 0;
			for (String[] lines : linesList) {
				String query = "";
				for (String line : lines) {
					query = query + System.lineSeparator() + line;
				}

				// Context depending on the source: scriptFile or no scriptFile
				Context context = null;

				// Context for a part with only one source
				if (doneLinesCounter <= numScriptLines) {
					// Context for a scriptFile
					context = ParserClientUtil.createRCPContext(this.scriptFiles.get(doneLinesCounter));
				} else {
					// Context for lines not from a scriptFile
					context = Context.empty();
				}

				for (String key : this.dashboardPart.getContextKeys()) {
					context.putOrReplace(key, this.dashboardPart.getContextValue(key).get());
				}

				Collection<Integer> ids = OdysseusRCPPlugIn.getExecutor().addQuery(query, "OdysseusScript", caller,
						context);
				queryIDs.addAll(ids);
				doneLinesCounter++;
			}

			// All queries by name
			for (String queryName : queryNames) {
				Optional<ILogicalQuery> optQuery = determineQueryByName(queryName, caller);
				if (!optQuery.isPresent()) {
					throw new ControllerException("Could not find query with name " + queryName + "!");
				}

				ILogicalQuery query = optQuery.get();
				int queryID = query.getID();

				queryIDs.add(queryID);
			}

			queryRoots = determineRoots(queryIDs);
		} catch (CoreException ex) {
			throw new ControllerException("Could not start query", ex);
		}
	}

	private static Optional<ILogicalQuery> determineQueryByName(String queryName, ISession session) {
		Collection<Integer> ids = OdysseusRCPPlugIn.getExecutor().getLogicalQueryIds(session);
		for (Integer id : ids) {
			ILogicalQuery query = OdysseusRCPPlugIn.getExecutor().getLogicalQueryById(id, session);
			if (queryName.equals(query.getName().toString()) || queryName.equals(query.getUser().getUser().getName() + "." + query.getName())) {
				return Optional.of(query);
			}
		}

		return Optional.absent();
	}

	private static Collection<IPhysicalOperator> determineRoots(Collection<Integer> queryIDs) {
		final Collection<IPhysicalOperator> roots = Lists.newArrayList();
		for (final Integer id : queryIDs) {
			for (final IPhysicalOperator rootOfQuery : DashboardPlugIn.getExecutor().getPhysicalRoots(id,
					OdysseusRCPPlugIn.getActiveSession())) {
				if (!(rootOfQuery instanceof DefaultStreamConnection)) {
					roots.add(rootOfQuery);
				}
			}
		}
		return roots;
	}

	public ImmutableCollection<IPhysicalOperator> getRoots() {
		if (queryRoots == null) {
			return ImmutableList.of();
		}
		return ImmutableList.copyOf(queryRoots);
	}

	public void stop() {

		if (queryIDs != null && !queryIDs.isEmpty()) {
			for (final Integer id : queryIDs) {
				try {
					DashboardPlugIn.getExecutor().removeQuery(id, OdysseusRCPPlugIn.getActiveSession());
				} catch (final Throwable t) {
					LOG.error("Exception during stopping/removing query {}.", id, t);
				}
			}

			queryIDs = null;
			queryRoots = null;
		}
	}

	public boolean isStarted() {
		return queryRoots != null;
	}
}
