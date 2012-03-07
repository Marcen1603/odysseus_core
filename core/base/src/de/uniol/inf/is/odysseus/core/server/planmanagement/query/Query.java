package de.uniol.inf.is.odysseus.core.server.planmanagement.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * 
 * @author Marco Grawunder
 *
 * @deprecated Use LogicalQuery
 */
@Deprecated
public class Query extends LogicalQuery {


	private static final long serialVersionUID = -7846033747726522915L;

	transient protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(LogicalQuery.class);
		}
		return _logger;
	}

	public Query() {
		this("", null, null);
	}


	/**
	 * Creates a query based on a parser ID and {@link QueryBuildConfiguration}
	 * 
	 * @param parserID
	 *            ID of the parser to use
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public Query(String parserID, QueryBuildConfiguration parameters) {
		this(parserID, null, parameters);
	}

	/**
	 * Creates a query based on a logical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param logicalPlan
	 *            logical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public Query(ILogicalOperator logicalPlan, QueryBuildConfiguration parameters) {
		this("", logicalPlan, parameters);
	}

	/**
	 * Creates a query based on a parserID, a logical plan, a physical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param parserID
	 *            logical operator plan
	 * @param logicalPlan
	 *            logical operator plan
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	protected Query(String parserID, ILogicalOperator logicalPlan, QueryBuildConfiguration parameters) {
		super(parserID, logicalPlan, parameters!=null?(parameters.getPriority()!=null?parameters.getPriority():0):0);
	}

}
