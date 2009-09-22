package de.uniol.inf.is.odysseus.base.planmanagement;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;

/**
 * Describes an object which provides methods for processing queries. Fassade to
 * combine transforming, rewriting and translating of queries. Used for
 * OSGi-services.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ICompiler extends IInfoProvider {
	/**
	 * Translates a query represented as a string into a logical plan. Multiple
	 * queries could be represented by a ";"-separated string.
	 * 
	 * @param query
	 *            ";"-separated string which represents the queries.
	 * @param parserID
	 *            Id of the parser for translating the query.
	 * @return For each ";"-separated query will be a new logical root created.
	 *         A List of all created roots will be returned.
	 * @throws QueryParseException
	 *             An {@link Exception} which occurs during parsing the query.
	 */
	public List<ILogicalOperator> translateQuery(String query, String parserID)
			throws QueryParseException;

	/**
	 * Rewrites a logical plan.
	 * 
	 * @param logicalAlgebraList
	 *            logical plan which should be rewrited.
	 * @return rewrited logical plan.
	 */
	public ILogicalOperator restructPlan(ILogicalOperator logicalAlgebraList);

	/**
	 * Transforms a logical plan into a physical representation.
	 * 
	 * @param logicalPlan
	 *            logical plan which should be transformed.
	 * @param transformationConfiguration
	 *            {@link TransformationConfiguration} for the transformation
	 *            module.
	 * @return The physical representation for the logical plan.
	 * @throws TransformationException
	 *             An {@link Exception} which occurs during transformation the
	 *             query.
	 */
	public IPhysicalOperator transform(ILogicalOperator logicalPlan,
			TransformationConfiguration transformationConfiguration)
			throws TransformationException;

	/**
	 * Returns a list of all supported query parsers. These parser could be used
	 * for translating queries.
	 * 
	 * @return List of all supported query parsers. These parser could be used
	 *         for translating queries.
	 */
	public Set<String> getSupportedQueryParser();
}