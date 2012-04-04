package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;

public class LogicalQueryViewData implements IQueryViewData {

	private static final String LOGICAL_QUERY_STATUS = "<unknown>";
	
	private final int id;
	private final String status;
	private final int priority;
	private final String parser;
	private final String username;
	private final String queryText;
	
	public LogicalQueryViewData( ILogicalQuery logicalQuery ) {
		Preconditions.checkNotNull(logicalQuery, "LogicalQuery must not be null");
		
		id = logicalQuery.getID();
		status = LOGICAL_QUERY_STATUS;
		priority = logicalQuery.getPriority();
		parser = logicalQuery.getParserId();
		username = logicalQuery.getUser().getUser().getName();
		queryText = logicalQuery.getQueryText();
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public String getParserId() {
		return parser;
	}

	@Override
	public String getUserName() {
		return username;
	}

	@Override
	public String getQueryText() {
		return queryText;
	}

}
