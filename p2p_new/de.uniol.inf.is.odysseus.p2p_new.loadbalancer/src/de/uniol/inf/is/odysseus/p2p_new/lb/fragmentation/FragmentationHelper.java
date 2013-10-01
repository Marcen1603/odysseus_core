package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterFragmentationType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * Utilities for the usage of the interface {@link IDataFragmentation}.
 * @author Michael Brand
 */
public class FragmentationHelper {
	
	/**
	 * A list of operator classes which instances shall be part of the query part of data reunion. <br />
	 * It should only contain sinks.
	 */
	public static final Class<?>[] OPERATOR_CLASSES_DATAREUNION_PART = {
		FileSinkAO.class
	};
	
	/**
	 * The identifier for {@link ILogicalOperator#setDestinationName(String)}, 
	 * which marks that the operator belongs to the reunion part of the query.
	 */
	public static final String REUNION_DESTINATION_NAME = "reunion";
	
	/**
	 * The identifier for {@link ILogicalOperator#setDestinationName(String)}, 
	 * which marks that the operator belongs to the fragmentation part of the query.
	 */
	public static final String FRAGMENTATION_DESTINATION_NAME = "fragmentation";
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FragmentationHelper.class);
	
	/**
	 * Determine the fragmentation strategy given by the parameters.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @param executor The {@link IServerExecutor} calling.
	 * @param degreeOfParallelism The degree of parallelism which is also the number of fragments.
	 * @return A pair of source name and fragmentation strategy for that source, if any is given by the user.
	 */
	public static Optional<Pair<String, IDataFragmentation>> determineFragmentationStrategy(QueryBuildConfiguration parameters, IServerExecutor executor, 
			int degreeOfParallelism) {
		
		// The return value
		Optional<Pair<String, IDataFragmentation>> fragmentationStrategy = 
				FragmentationHelper.parseFromConfiguration(parameters, executor);
		if(fragmentationStrategy.isPresent())
			LOG.debug("Using '{}' as fragmentation strategy for the source '{}'.", 
					fragmentationStrategy.get().getE2().getName(), fragmentationStrategy.get().getE1());
		else LOG.debug("Using replication for all sources.");
		
		// Check the number of fragments if fragmentation is selected
		if(fragmentationStrategy.isPresent() && degreeOfParallelism < 2) {
			
			LOG.warn("Degree of parallelism must be at least 2 to use data fragmentation. Turned off data fragmentation.");
			fragmentationStrategy = Optional.absent();
			
		}
		
		return fragmentationStrategy;
		
	}
	
	/**
	 * Parses an parameter of the {@link QueryBuildConfiguration} into a fragmentation strategy.
	 * @param parameters The {@link QueryBuildConfiguration} to be parsed.
	 * @param executor The executor calling.
	 * @return A pair of the source name and the fragmentation strategy or null.
	 */
	public static Optional<Pair<String,IDataFragmentation>> parseFromConfiguration(QueryBuildConfiguration parameters, IServerExecutor executor) {
		
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(executor);
		
		if(!parameters.get(ParameterDoDataFragmentation.class).getValue()) {
			
			// ParameterDoDataFragmentation is setted to false
			return Optional.absent();
			
		} else if(!parameters.contains(ParameterFragmentationType.class)) {
			
			// Missing parameter ParameterFragmentationType
			LOG.warn("No fragmentation strategy defined.");
			return Optional.absent();
			
		} else return parseFromString(parameters.get(ParameterFragmentationType.class).getValue(), executor);
		
	}
	
	/**
	 * Parses an string expression into a fragmentation strategy.
	 * @param strFragmentationStrategy The string to be parsed. <br />
	 * String must be as follows: "strategyName sourceName"
	 * @param executor The executor calling.
	 * @return A pair of the source name and the fragmentation strategy or null.
	 */
	private static Optional<Pair<String,IDataFragmentation>> parseFromString(String strFragmentationStrategy, IServerExecutor executor) {
		
		Preconditions.checkNotNull(strFragmentationStrategy);
		Preconditions.checkNotNull(executor);
		
		// The separator of the string
		final String separator = " ";
		
		// The parameters for the strategy
		String[] fragmentationStrategyParameters = strFragmentationStrategy.split(separator);
		
		Preconditions.checkArgument(fragmentationStrategyParameters.length > 1);
		
		// The name of the strategy
		String fragmentationStrategyName = fragmentationStrategyParameters[0];
		
		// The name of the source to be fragmented
		String sourceName = fragmentationStrategyParameters[1];
		
		// The data fragmentation strategy
		Optional<IDataFragmentation> fragmentationStrategy = executor.getDataFragmentation(fragmentationStrategyName);	
	
		if(fragmentationStrategy.isPresent())
			return Optional.of(new Pair<String, IDataFragmentation>(sourceName, fragmentationStrategy.get()));
		return Optional.absent();
		
	}

}