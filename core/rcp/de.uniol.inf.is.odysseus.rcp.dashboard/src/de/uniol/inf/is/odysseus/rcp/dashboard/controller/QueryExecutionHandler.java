package de.uniol.inf.is.odysseus.rcp.dashboard.controller;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class QueryExecutionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(QueryExecutionHandler.class);
	
	private IFile scriptFile;
	private String[] lines;
	
	private Collection<Integer> queryIDs;
	private Collection<IPhysicalOperator> queryRoots;
	
	public QueryExecutionHandler( IDashboardPartQueryTextProvider provider ) {
		Preconditions.checkNotNull(provider, "Text provider for execution must not be null!");
		
		if( provider instanceof ResourceFileQueryTextProvider ) {
			ResourceFileQueryTextProvider resProvider = (ResourceFileQueryTextProvider)provider;
			
			this.scriptFile = resProvider.getFile();
			this.lines = null;
		} else if( provider instanceof SimpleQueryTextProvider ) {
			SimpleQueryTextProvider simpleProvider = (SimpleQueryTextProvider)provider;
			
			this.scriptFile = null;
			
			ImmutableList<String> queryText = simpleProvider.getQueryText();
			this.lines = queryText.toArray(new String[queryText.size()]);
		}
		
	}
	
	public void start() throws OdysseusScriptException {
		final IOdysseusScriptParser parser = DashboardPlugIn.getScriptParser();
		final ISession caller = OdysseusRCPPlugIn.getActiveSession();

	
		
		try {
			if( lines == null ) {
				List<String> lineList = FileUtil.read(scriptFile);
				lines = lineList.toArray(new String[lineList.size()]);
			}

			if( scriptFile != null ) {
				lines = ParserClientUtil.replaceClientReplacements(lines, scriptFile);
			}
			//TODO: you cannot access the parser directly!!
			List<?> results = parser.parseAndExecute(lines, caller, null, Context.empty());
			
			queryIDs = getExecutedQueryIDs(results);
			queryRoots = determineRoots(queryIDs);
		} catch (CoreException ex) {
			throw new OdysseusScriptException("Could not start query", ex);
		}
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
		if( queryRoots == null ) {
			return ImmutableList.of();
		}
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
		queryRoots = null;
	}
	
	public boolean isStarted() {
		return queryRoots != null;
	}
}
