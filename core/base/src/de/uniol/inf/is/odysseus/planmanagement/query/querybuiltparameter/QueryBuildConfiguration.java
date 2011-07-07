/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.Configuration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterAllowRestructuringOfCurrentPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterShareSimilarOperators;

/**
 * QueryBuildConfiguration provides a set of {@link IQueryBuildSetting}.
 * Each standard Parameter has a default value. This class also provides some
 * methods for a simple access to some standard parameters.
 * 
 * These parameters are used for creating queries.
 * 
 * @author Wolf Bauer
 * 
 */
public class QueryBuildConfiguration extends
		Configuration<IQueryBuildSetting<?>> {

	
	private static final long serialVersionUID = -5018054053257854891L;

	/**
	 * Creates a new set of parameters. If some standard values are not set in
	 * <code>parameters</code> default values are used.
	 * 
	 * @param parameters
	 *            New parameter for this set.
	 */
	public QueryBuildConfiguration(IQueryBuildSetting<?>[] parameters) {
		super(parameters);

		if (!contains(ParameterTransformationConfiguration.class)) {
			set(new ParameterTransformationConfiguration(null));
		}

		// if no default root is set, 
		// we need no default root strategy
		if (!contains(ParameterDefaultRoot.class)) {
			set(new ParameterDefaultRoot(null));
			set(new ParameterDefaultRootStrategy(null));
		}
		// if there is a default root, but no default root
		// strategy, we add a clone strategy, that clones
		// the default root and adds a copy to each root of
		// the query
		else if(!contains(ParameterDefaultRootStrategy.class)){
			set(new ParameterDefaultRootStrategy(new CloneDefaultRootStrategy()));
		}
			

		if (!contains(ParameterParserID.class)) {
			set(new ParameterParserID(""));
		}

		if (!contains(ParameterBufferPlacementStrategy.class)) {
			set(new ParameterBufferPlacementStrategy());
		}

		if (!contains(ParameterPriority.class)) {
			set(new ParameterPriority(0));
		}
		if (!contains(ParameterDoRewrite.class)){
			set(ParameterDoRewrite.TRUE);
		}
		if (!contains(ParameterPerformQuerySharing.class)){
			set(ParameterPerformQuerySharing.FALSE);
		}
		if (!contains(ParameterAllowRestructuringOfCurrentPlan.class)){
			set(ParameterAllowRestructuringOfCurrentPlan.FALSE);
		}
		if (!contains(ParameterShareSimilarOperators.class)) {
			set(ParameterShareSimilarOperators.FALSE);
		}
		
	}

	/**
	 * Creates a new set of parameters with default values.
	 */
	public QueryBuildConfiguration() {
		this(null);
	}

	/**
	 * Returns a physical root for the physical plan of a query.
	 * 
	 * @return A physical root for the physical plan of a query.
	 */
	public IPhysicalOperator getDefaultRoot() {
		return (IPhysicalOperator) get(ParameterDefaultRoot.class).getValue();
	}
	
	public int getDefaultRootInPort() {
		ParameterDefaultRoot parameter = get(ParameterDefaultRoot.class);
		if (parameter == null) {
			return 0;
		}
		return parameter.getPort();
	}

	/**
	 * Returns a priority for the query.
	 * 
	 * @return A priority for the query.
	 */
	public Integer getPriority() {
		return (Integer) get(ParameterPriority.class).getValue();
	}

	/**
	 * Returns a {@link TransformationConfiguration} for the query.
	 * 
	 * @return A {@link TransformationConfiguration} for the query.
	 */
	public TransformationConfiguration getTransformationConfiguration() {
		return (TransformationConfiguration) get(
				ParameterTransformationConfiguration.class).getValue();
	}

	/**
	 * Returns an ID of the parser which should be used for translating the
	 * query.
	 * 
	 * @return An ID of the parser which should be used for translating the
	 *         query.
	 */
	public String getParserID() {
		return (String) get(ParameterParserID.class).getValue();
	}

	/**
	 * Returns an {@link IBufferPlacementStrategy} for creating the query.
	 * 
	 * @return An {@link IBufferPlacementStrategy} for creating the query.
	 */
	public ParameterBufferPlacementStrategy getBufferPlacementParameter() {
		return get(ParameterBufferPlacementStrategy.class);
	}
	
	public IBufferPlacementStrategy getBufferPlacementStrategy(){
		return getBufferPlacementParameter().getValue();
	}
	
	public IDefaultRootStrategy getDefaultRootStrategy(){
		return (IDefaultRootStrategy)get(ParameterDefaultRootStrategy.class).getValue();
	}
}
