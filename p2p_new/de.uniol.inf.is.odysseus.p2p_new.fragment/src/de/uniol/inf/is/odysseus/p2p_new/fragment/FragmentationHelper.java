package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterFragmentationType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;

/**
 * Utilities for the usage of the interface {@link IDataFragmentation}.
 * @author Michael Brand
 */
public class FragmentationHelper {
	
	/**
	 * The string representation of {@link #MIN_NUM_FRAGMENTS_VALUE}.
	 */
	public static final String MIN_NUM_FRAGMENTS_PARAM = "min";
	
	/**
	 * The minimum number of fragmentats.
	 */
	public static final int MIN_NUM_FRAGMENTS_VALUE = 2;
	
	/**
	 * The string representation of {@link #MAX_NUM_FRAGMENTS_VALUE}.
	 */
	public static final String MAX_NUM_FRAGMENTS_PARAM = "max";
	
	/**
	 * The maximum degree of fragmentation.
	 */
	public static final int MAX_NUM_FRAGMENTS_VALUE = Integer.MAX_VALUE;
	
	/**
	 * A list of operator classes which instances shall be part of the query part of data reunion.
	 */
	public static final Class<?>[] OPERATOR_CLASSES_DATAREUNION_PART = {
		FileSinkAO.class, 
		CSVFileSink.class,
		CalcLatencyAO.class,
	};
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FragmentationHelper.class);
	
	/**
	 * TODO javaDoc update
	 * Determines the degree of parallelism as the minimum of the degree given by the user and the 
	 * number of available peers.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @param remotePeerIDs A list of all available peers.
	 * @return The degree of parallelism.
	 */
	public static int determineNumberOfFragments(QueryBuildConfiguration parameters, 
			Collection<PeerID> remotePeerIDs) {
		
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(remotePeerIDs);
		Preconditions.checkArgument(!remotePeerIDs.isEmpty());
		
		// The return value
		int numberOfFragments = 1;
		
		if(!parameters.get(ParameterDoDataFragmentation.class).getValue() || !parameters.contains(ParameterFragmentationType.class))
			return numberOfFragments;
		
		// The separator for the parameters
		final String separator = " ";
		
		// The arguments of the parameter ParameterFragmentationType
		final String[] strParameters = 
				parameters.get(ParameterFragmentationType.class).getValue().split(separator);
		
		// The wanted number of fragments by the user
		int wantedNumberOfFragments = MIN_NUM_FRAGMENTS_VALUE;
		
		// Read out the wanted number of fragments
		if(strParameters.length > 1) {
			
			if(strParameters[1].toLowerCase().equals(MIN_NUM_FRAGMENTS_PARAM))
				wantedNumberOfFragments = MIN_NUM_FRAGMENTS_VALUE;
			else if(strParameters[1].toLowerCase().equals(MAX_NUM_FRAGMENTS_PARAM))
				wantedNumberOfFragments = MAX_NUM_FRAGMENTS_VALUE;
			else try {
			
				wantedNumberOfFragments = Integer.parseInt(strParameters[1]);
				if(wantedNumberOfFragments < MIN_NUM_FRAGMENTS_VALUE) {
					
					LOG.warn("{} is an invalid number of fragments. Number of fragments settet to {}", 
							strParameters[1], MIN_NUM_FRAGMENTS_VALUE);
					wantedNumberOfFragments = MIN_NUM_FRAGMENTS_VALUE;
					
				}
				
			} catch(NumberFormatException e) {
				
				e.printStackTrace();
				LOG.error("Could not parse {} to an integer. Number of fragments set to {}", 
						strParameters[1], MIN_NUM_FRAGMENTS_VALUE);
				
			}
			
		}
		
		numberOfFragments = Math.min(wantedNumberOfFragments, Math.max(remotePeerIDs.size(), MIN_NUM_FRAGMENTS_VALUE));
		LOG.debug("Number of fragments set to '{}'.", numberOfFragments);
		
		return numberOfFragments;
		
	}
	
	/**
	 * TODO javaDoc update
	 * Determine the fragmentation strategy given by the parameters.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @param executor The {@link IServerExecutor} calling.
	 * @param numberOfFragments The degree of parallelism which is also the number of fragments.
	 * @return A pair of source name and fragmentation strategy for that source, if any is given by 
	 * the user.
	 */
	public static Optional<Pair<String, IDataFragmentation>> determineFragmentationStrategy(
			QueryBuildConfiguration parameters, IServerExecutor executor, int numberOfFragments) {
		
		// The return value
		Optional<Pair<String, IDataFragmentation>> fragmentationStrategy = 
				FragmentationHelper.parseFromConfiguration(parameters, executor);
		if(fragmentationStrategy.isPresent())
			LOG.debug("Using '{}' as fragmentation strategy for the source '{}'.", 
					fragmentationStrategy.get().getE2().getName(), fragmentationStrategy.get().getE1());
		else LOG.debug("Using replication for all sources.");
		
		// Check the number of fragments if fragmentation is selected
		if(fragmentationStrategy.isPresent() && numberOfFragments < 2) {
			
			LOG.warn("Number of fragments must be at least 2 to use data fragmentation. " +
					"Turned off data fragmentation.");
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
	public static Optional<Pair<String,IDataFragmentation>> parseFromConfiguration(
			QueryBuildConfiguration parameters, IServerExecutor executor) {
		
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(executor);
		
		if(!parameters.get(ParameterDoDataFragmentation.class).getValue()) {
			
			// ParameterDoDataFragmentation is setted to false
			return Optional.absent();
			
		} else if(!parameters.contains(ParameterFragmentationType.class)) {
			
			// Missing parameter ParameterFragmentationType
			LOG.warn("No fragmentation strategy defined.");
			return Optional.absent();
			
		} else return parseFromString(parameters.get(ParameterFragmentationType.class).getValue(), 
				executor);
		
	}
	
	/**
	 * Parses an string expression into a fragmentation strategy.
	 * @param strFragmentationStrategy The string to be parsed. <br />
	 * String must be as follows: "strategyName sourceName"
	 * @param executor The executor calling.
	 * @return A pair of the source name and the fragmentation strategy or null.
	 */
	private static Optional<Pair<String,IDataFragmentation>> parseFromString(
			String strFragmentationStrategy, IServerExecutor executor) {
		
		Preconditions.checkNotNull(strFragmentationStrategy);
		Preconditions.checkNotNull(executor);
		
		// The separator of the string
		final String separator = " ";
		
		// The parameters for the strategy
		String[] fragmentationStrategyParameters = strFragmentationStrategy.split(separator);
		
		Preconditions.checkArgument(fragmentationStrategyParameters.length > 2);
		
		// The name of the strategy
		String fragmentationStrategyName = fragmentationStrategyParameters[0];
		
		// The name of the source to be fragmented
		String sourceName = fragmentationStrategyParameters[2];
		
		// The data fragmentation strategy
		Optional<IDataFragmentation> fragmentationStrategy =
				executor.getDataFragmentation(fragmentationStrategyName);	
	
		if(fragmentationStrategy.isPresent())
			return Optional.of(new Pair<String, IDataFragmentation>(sourceName, 
					fragmentationStrategy.get()));
		return Optional.absent();
		
	}

}