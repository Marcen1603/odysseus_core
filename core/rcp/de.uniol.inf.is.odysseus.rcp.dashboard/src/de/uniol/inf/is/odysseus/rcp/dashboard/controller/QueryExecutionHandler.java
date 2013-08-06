package de.uniol.inf.is.odysseus.rcp.dashboard.controller;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class QueryExecutionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(QueryExecutionHandler.class);
	
	private final String[] lines;
	
	private Collection<Integer> queryIDs;
	private Collection<IPhysicalOperator> queryRoots;
	
	public QueryExecutionHandler( List<String> queryTextLines ) {
		Preconditions.checkNotNull(queryTextLines, "List of text lines must not be null!");
		Preconditions.checkArgument(!queryTextLines.isEmpty(), "List of text lines must not be empty!");
		
		lines = queryTextLines.toArray(new String[queryTextLines.size()]);
	}
	
	public void start() throws OdysseusScriptException {
		final IOdysseusScriptParser parser = DashboardPlugIn.getScriptParser();
		final ISession caller = OdysseusRCPPlugIn.getActiveSession();

		List<?> results = parser.execute(parser.parseScript(lines, caller), caller, null);
		queryIDs = getExecutedQueryIDs(results);
		queryRoots = determineRoots(queryIDs);
	}

	private static Collection<Integer> getExecutedQueryIDs(List<?> results) {
		final Collection<Integer> ids = Lists.newArrayList();

		for (final Object result : results) {
			if (result instanceof List) {
				@SuppressWarnings("rawtypes")
				final List list = (List) result;
				for (final Object obj : list) {
					if (obj instanceof Integer) {
						ids.add((Integer) obj);
					}
				}
			}
		}

		return ids;
	}

	private static Collection<IPhysicalOperator> determineRoots(Collection<Integer> queryIDs) {
		final Collection<IPhysicalOperator> roots = Lists.newArrayList();
		for (final Integer id : queryIDs) {
			for (final IPhysicalOperator rootOfQuery : DashboardPlugIn.getExecutor().getPhysicalRoots(id)) {
				if (!(rootOfQuery instanceof DefaultStreamConnection)) {
					roots.add(rootOfQuery);
				}
			}
		}
		return roots;
	}
	
	public ImmutableCollection<IPhysicalOperator> getRoots() {
		return ImmutableList.copyOf(queryRoots);
	}
	
	public void stop() {
		for (final Integer id : queryIDs) {
			try {
				DashboardPlugIn.getExecutor().removeQuery(id, OdysseusRCPPlugIn.getActiveSession());
			} catch (final Throwable t) {
				LOG.error("Exception during stopping query {}.", id, t);
			}
		}
		queryIDs = null;
	}
}
