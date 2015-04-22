/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.planmanagement;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.plangeneration.IPlanGenerator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Describes an object which provides methods for processing queries. Fassade to
 * combine transforming, rewriting and translating of queries. Used for
 * OSGi-services.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
public interface ICompiler extends IInfoProvider, IRewrite, IPlanGenerator {
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
	public List<IExecutorCommand> translateQuery(String query, String parserID, ISession user, IDataDictionary dd, Context context, IMetaAttribute metaAttribute)
			throws QueryParseException;

	public List<IPhysicalQuery> translateAndTransformQuery(String query, String parserID, ISession user, IDataDictionary dd,
			TransformationConfiguration transformationConfiguration, Context context)
			throws QueryParseException, TransformationException;
//	/**
//	 * Creates semantically equivalent alternative plans.
//	 * 
//	 * @param logicalPlan
//	 *            logical plan for which alternatives should be generated.
//	 * @param conf 
//	 * 			 OptimizationConfiguration
//	 * @return list of possible alternatives, excluding the given plan.
//	 */
//	public List<ILogicalOperator> createAlternativePlans(
//			ILogicalOperator logicalPlan, OptimizationConfiguration conf);

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
//	public ArrayList<IPhysicalOperator> transform(ILogicalOperator logicalPlan,
//			TransformationConfiguration transformationConfiguration, User caller)
//			throws TransformationException;

	/**
	 * Transforms a query where the logical plan is set into a physical representation that will
	 * be set in the query object
	 * 
	 * @param ILogicalQuery
	 *            containing a logical plan which should be transformed.
	 * @param transformationConfiguration
	 *            {@link TransformationConfiguration} for the transformation
	 *            module.
	 * @param caller 
	 * 			the user who wants to execute this operation
	 * @throws TransformationException
	 *             An {@link Exception} which occurs during transformation the
	 *             query.
	 */
	public IPhysicalQuery transform(ILogicalQuery query, TransformationConfiguration transformationConfiguration, ISession caller, IDataDictionary dd) throws TransformationException;

//	/**
//	 * Transforms a logical plan into several semantically equivalent physical
//	 * representations.
//	 * 
//	 * @param logicalPlan
//	 *            logical plan which should be transformed.
//	 * @param transformationConfiguration
//	 *            {@link TransformationConfiguration} for the transformation
//	 *            module.
//	 * @return list of physical representations for the logical plan.
//	 * each list element is again a list of physical operators. This is
//	 * because a query can have more than one root. So each query alternative
//	 * is represented by a list of physical operators, the roots of the query.
//	 * @throws TransformationException
//	 *             An {@link Exception} which occurs during transformation the
//	 *             query.
//	 */
//	public List<List<IPhysicalOperator>> transformWithAlternatives(
//			ILogicalOperator logicalPlan,
//			TransformationConfiguration transformationConfiguration, User caller)
//			throws TransformationException;

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
	
	/**
	 * A list of tokens like static key words for a certain query parser that
	 * can be, e.g. used for visualization. 
	 * @param queryParser
	 * @return a parser dependent key-multiple-values map
	 */
	public Map<String, List<String>> getQueryParserTokens(String parserID, ISession user);
	
	public List<String> getQueryParserSuggestions(String parserID, String hint, ISession user);
	
}
