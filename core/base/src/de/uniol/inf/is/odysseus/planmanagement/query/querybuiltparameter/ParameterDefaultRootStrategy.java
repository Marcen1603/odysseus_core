package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

/**
 * Since a query can have more than one root,
 * this class encapsulates strategies for
 * subscribing the default root to
 * the multiple roots of a query. For example:
 * 
 * A query has 3 roots. The default root of a
 * query should be an operator that prints
 * the results of the query to console. However,
 * should there be 3 different console printing
 * operators, one for each root of the query, or
 * should there be a single printing operator,
 * so that all the roots run into the same output
 * operator.
 * 
 * @author André Bolles
 *
 */
public class ParameterDefaultRootStrategy extends AbstractQueryBuildSetting<IDefaultRootStrategy> {

	public ParameterDefaultRootStrategy(IDefaultRootStrategy strategy){
		super(strategy);
	}
}
