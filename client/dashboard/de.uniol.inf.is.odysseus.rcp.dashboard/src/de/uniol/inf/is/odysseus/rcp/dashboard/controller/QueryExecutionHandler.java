package de.uniol.inf.is.odysseus.rcp.dashboard.controller;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
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

	private final IDashboardPart dashboardPart;

	private IFile scriptFile;
	private String[] lines;
	private String queryName;

	private Collection<Integer> queryIDs;
	private Collection<IPhysicalOperator> queryRoots;

	public QueryExecutionHandler(IDashboardPart dashboardPart) {
		Preconditions.checkNotNull(dashboardPart, "Dashboard part for execution must not be null!");

		this.dashboardPart = dashboardPart;
		IDashboardPartQueryTextProvider provider = dashboardPart.getQueryTextProvider();
		if (provider instanceof ResourceFileQueryTextProvider) {
			ResourceFileQueryTextProvider resProvider = (ResourceFileQueryTextProvider) provider;

			this.scriptFile = resProvider.getFile();
			this.lines = null;
			this.queryName = null;
		} else if (provider instanceof SimpleQueryTextProvider) {
			SimpleQueryTextProvider simpleProvider = (SimpleQueryTextProvider) provider;

			this.scriptFile = null;
			this.queryName = null;

			ImmutableList<String> queryText = simpleProvider.getQueryText();
			this.lines = queryText.toArray(new String[queryText.size()]);
		} else if (provider instanceof GraphQueryFileProvider) {
			GraphQueryFileProvider graphProvider = (GraphQueryFileProvider) provider;

			this.scriptFile = null;
			this.queryName = null;

			this.lines = graphProvider.getQueryText().toArray(new String[0]);
		} else if (provider instanceof RunningQueryProvider) {
			RunningQueryProvider runningQueryProvider = (RunningQueryProvider) provider;

			this.scriptFile = null;
			this.lines = null;
			this.queryName = runningQueryProvider.getQueryName();
		} else {
			throw new RuntimeException("Unknown queryText provider: " + provider.getClass());
		}

	}

	public void start() throws ControllerException {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();

		try {
			if (lines == null && scriptFile != null) {
				List<String> lineList = FileUtil.read(scriptFile);
				lines = lineList.toArray(new String[lineList.size()]);
			}

			if (lines != null) {
				String query = "";
				for (String line : lines) {
					query = query + System.lineSeparator() + line;
				}
				Context context = createContext(scriptFile, dashboardPart);
				Collection<Integer> ids = OdysseusRCPPlugIn.getExecutor().addQuery(query, "OdysseusScript", caller, "Standard", context);
				queryIDs = ids;
			} else {
				Optional<ILogicalQuery> optQuery = determineQueryByName(queryName, caller);
				if (!optQuery.isPresent()) {
					throw new ControllerException("Could not find query with name " + queryName + "!");
				}

				ILogicalQuery query = optQuery.get();
				int queryID = query.getID();

				queryIDs = Lists.newArrayList(queryID);
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
			if (queryName.equals(query.getName())) {
				return Optional.of(query);
			}
		}

		return Optional.absent();
	}

	private static Context createContext(IFile scriptFile, IDashboardPart part) {
		Context context = scriptFile != null ? ParserClientUtil.createRCPContext(scriptFile) : Context.empty();
		for (String key : part.getContextKeys()) {
			context.putOrReplace(key, part.getContextValue(key).get());
		}
		return context;
	}

	private static Collection<IPhysicalOperator> determineRoots(Collection<Integer> queryIDs) {
		final Collection<IPhysicalOperator> roots = Lists.newArrayList();
		for (final Integer id : queryIDs) {
			for (final IPhysicalOperator rootOfQuery : DashboardPlugIn.getExecutor().getPhysicalRoots(id, OdysseusRCPPlugIn.getActiveSession())) {
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
		queryIDs = null;
		queryRoots = null;
		
		if (!Strings.isNullOrEmpty(queryName)) {
			return;
		}

		for (final Integer id : queryIDs) {
			try {
				DashboardPlugIn.getExecutor().removeQuery(id, OdysseusRCPPlugIn.getActiveSession());
			} catch (final Throwable t) {
				LOG.error("Exception during stopping/removing query {}.", id, t);
			}
		}
	}

	public boolean isStarted() {
		return queryRoots != null;
	}
}
