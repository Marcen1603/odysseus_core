package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.Configuration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;

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

	/**
	 * Creates a new set of parameters. If some standard values are not set in
	 * <code>parameters</code> default values are used.
	 * 
	 * @param parameters
	 *            New parameter for this set.
	 */
	public QueryBuildConfiguration(IQueryBuildSetting[] parameters) {
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
			set(new ParameterBufferPlacementStrategy(null));
		}

		if (!contains(ParameterPriority.class)) {
			set(new ParameterPriority(0));
		}
		if(!contains(ParameterInstallMetadataListener.class)){
			set(new ParameterInstallMetadataListener(false));
		}
		if (!contains(ParameterDoRewrite.class)){
			set(ParameterDoRewrite.TRUE);
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
	public IBufferPlacementStrategy getBufferPlacementStrategy() {
		return (IBufferPlacementStrategy) get(
				ParameterBufferPlacementStrategy.class).getValue();
	}
	
	public boolean getParameterInstallMetadataListener(){
		return ((ParameterInstallMetadataListener)get(ParameterInstallMetadataListener.class)).getValue();
	}
	
	public IDefaultRootStrategy getDefaultRootStrategy(){
		return (IDefaultRootStrategy)get(ParameterDefaultRootStrategy.class).getValue();
	}
}
