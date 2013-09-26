package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
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
	 * Returns the names of all used sources within a collection of operators.
	 */
	public static Collection<String> getSourceNames(Collection<ILogicalOperator> operators) {
		
		Preconditions.checkNotNull(operators);
		
		// The return value
		Collection<String> sourceNames = Lists.newArrayList();
		
		// Collect all StreamAOs and AccessAOs.
		for(ILogicalOperator operator : operators) {
		
			if(operator instanceof StreamAO)
				sourceNames.add(((StreamAO) operator).getStreamname().toString());
			else if(operator instanceof AbstractAccessAO)
				sourceNames.add(((AbstractAccessAO) operator).getName());
			
		}
		
		return sourceNames;
		
	}
	
	/**
	 * Returns the names of all used sources within a logical plan.
	 */
	public static Collection<String> getSourceNames(ILogicalOperator logicalPlan) {
		
		Preconditions.checkNotNull(logicalPlan);
		
		// A collection of all operators within the logical plan
		Collection<ILogicalOperator> operators = Lists.newArrayList();
		
		// Collect all operators within the logical plan
		RestructHelper.collectOperators(logicalPlan, operators);
		
		return getSourceNames(operators);
		
	}
	
	/**
	 * Returns a collection of the names of all used sources within a list of queries.
	 */
	public static Collection<String> getSourceNames(List<ILogicalQuery> queries) {
		
		Preconditions.checkNotNull(queries);
		Preconditions.checkArgument(!queries.isEmpty());
		
		// The return value
		Collection<String> allSourceNames = Lists.newArrayList();
		
		for(ILogicalQuery query : queries) {
			
			// Collect all StreamAOs and AccessAOs of the query
			Collection<String> sourceNames = getSourceNames(query.getLogicalPlan());
			
			// Add a sourceName to the return value only if it hasn't been done before.
			for(String sourceName : sourceNames) {
				
				if(!allSourceNames.contains(sourceName))
					allSourceNames.add(sourceName);
				
			}
			
		}
		
		return allSourceNames;
		
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
	public static Optional<Pair<String,IDataFragmentation>> parseFromString(String strFragmentationStrategy, IServerExecutor executor) {
		
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