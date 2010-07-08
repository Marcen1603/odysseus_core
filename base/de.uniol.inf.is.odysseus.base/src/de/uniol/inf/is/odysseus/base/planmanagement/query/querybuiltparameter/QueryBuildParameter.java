package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AbstractTypeSafeMap;

/**
 * QueryBuildParameter provides a set of {@link AbstractQueryBuildParameter}.
 * Each standard Parameter has a default value. This class also provides some
 * methods for a simple access to some standard parameters.
 * 
 * These parameters are used for creating queries.
 * 
 * @author Wolf Bauer
 * 
 */
public class QueryBuildParameter extends
		AbstractTypeSafeMap<AbstractQueryBuildParameter<?>> {

	/**
	 * Creates a new set of parameters. If some standard values are not set in
	 * <code>parameters</code> default values are used.
	 * 
	 * @param parameters
	 *            New parameter for this set.
	 */
	public QueryBuildParameter(AbstractQueryBuildParameter<?>[] parameters) {
		super(parameters);

		if (!contains(ParameterTransformationConfiguration.class)) {
			set(new ParameterTransformationConfiguration(null));
		}

		if (!contains(ParameterDefaultRoot.class)) {
			set(new ParameterDefaultRoot(null));
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
	}

	/**
	 * Creates a new set of parameters with default values.
	 */
	public QueryBuildParameter() {
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
}
