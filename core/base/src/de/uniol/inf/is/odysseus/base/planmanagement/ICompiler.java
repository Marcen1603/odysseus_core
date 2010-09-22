package de.uniol.inf.is.odysseus.base.planmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.usermanagement.User;

/**
 * Describes an object which provides methods for processing queries. Fassade to
 * combine transforming, rewriting and translating of queries. Used for
 * OSGi-services.
 * 
 * @author Wolf Bauer, Tobias Witt
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
	public List<IQuery> translateQuery(String query, String parserID, User user)
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
	 * Rewrites a logical plan.
	 * 
	 * @param logicalAlgebraList
	 *            logical plan which should be rewrited.
	 * @param rules
	 *            Contains the name of the rules to use. Other rules will not be
	 *            used.
	 * 
	 * @return rewrited logical plan.
	 */
	public ILogicalOperator restructPlan(ILogicalOperator logicalPlan,
			Set<String> rulesToUse);

	/**
	 * Creates semantically equivalent alternative plans.
	 * 
	 * @param logicalPlan
	 *            logical plan for which alternatives should be generated.
	 * @param rules
	 *            Contains the name of the rules to use. Other rules will not be
	 *            used.
	 * @return list of possible alternatives, excluding the given plan.
	 */
	public List<ILogicalOperator> createAlternativePlans(
			ILogicalOperator logicalPlan, Set<String> rulesToUse);

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
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator logicalPlan,
			TransformationConfiguration transformationConfiguration)
			throws TransformationException;

	/**
	 * Transforms a logical plan into several semantically equivalent physical
	 * representations.
	 * 
	 * @param logicalPlan
	 *            logical plan which should be transformed.
	 * @param transformationConfiguration
	 *            {@link TransformationConfiguration} for the transformation
	 *            module.
	 * @return list of physical representations for the logical plan.
	 * each list element is again a list of physical operators. This is
	 * because a query can have more than one root. So each query alternative
	 * is represented by a list of physical operators, the roots of the query.
	 * @throws TransformationException
	 *             An {@link Exception} which occurs during transformation the
	 *             query.
	 */
	public List<List<IPhysicalOperator>> transformWithAlternatives(
			ILogicalOperator logicalPlan,
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
	
	/**
	 * Returns true if an transformation service is bound
	 */
	
	public boolean isTransformationBound();

	/**
	 * Returns true if a rewrite service is bound	
	*/
	public boolean isRewriteBound();
	
	/**
	 * Registration of Compiler Event Listener  
	 */
	
	public void addCompilerListener(ICompilerListener listener);
	public void removeCompilerListener(ICompilerListener listener);
	
}