package de.uniol.inf.is.odysseus.net.querydistribute.parameter;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.net.querydistribute.service.QueryDistributionPostProcessorRegistry;
import de.uniol.inf.is.odysseus.net.querydistribute.service.QueryDistributionPreProcessorRegistry;
import de.uniol.inf.is.odysseus.net.querydistribute.service.QueryPartAllocatorRegistry;
import de.uniol.inf.is.odysseus.net.querydistribute.service.QueryPartModificatorRegistry;
import de.uniol.inf.is.odysseus.net.querydistribute.service.QueryPartitionerRegistry;

public final class ParameterHelper {
	
	public static List<InterfaceParametersPair<IQueryPartitioner>> determineQueryPartitioners(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartitionerParameter.class, "Query partitioner");
		
		List<InterfaceParametersPair<IQueryPartitioner>> resultList = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultList.add(new InterfaceParametersPair<IQueryPartitioner>(QueryPartitionerRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultList;
	}

	public static List<InterfaceParametersPair<IQueryPartModificator>> determineQueryPartModificators(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartModificatorParameter.class, "Query part modificator", true);
		
		List<InterfaceParametersPair<IQueryPartModificator>> resultList = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultList.add(new InterfaceParametersPair<IQueryPartModificator>(QueryPartModificatorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultList;
	}

	public static List<InterfaceParametersPair<IQueryPartAllocator>> determineQueryPartAllocators(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartAllocatorParameter.class, "Query part allocator");
		
		List<InterfaceParametersPair<IQueryPartAllocator>> resultMap = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultMap.add(new InterfaceParametersPair<IQueryPartAllocator>(QueryPartAllocatorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultMap;
	}
	
	public static List<InterfaceParametersPair<IQueryDistributionPreProcessor>> determineQueryDistributionPreProcessors(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryDistributionPreProcessorParameter.class, "Query distribution preprocessor", true);
		
		List<InterfaceParametersPair<IQueryDistributionPreProcessor>> resultMap = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultMap.add(new InterfaceParametersPair<IQueryDistributionPreProcessor>(QueryDistributionPreProcessorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultMap;
	}
	
	public static List<InterfaceParametersPair<IQueryDistributionPostProcessor>> determineQueryDistributionPostProcessors(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryDistributionPostProcessorParameter.class, "Query distribution postprocessor", true);
		
		List<InterfaceParametersPair<IQueryDistributionPostProcessor>> resultMap = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultMap.add(new InterfaceParametersPair<IQueryDistributionPostProcessor>(QueryDistributionPostProcessorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultMap;
	}
	
	private static List<InterfaceNameParametersPair> getPairsOfParameter( QueryBuildConfiguration config, Class<? extends AbstractQueryDistributionParameter> settingType, String settingName ) throws QueryDistributionException {
		return getPairsOfParameter(config, settingType, settingName, false);
	}
	
	private static List<InterfaceNameParametersPair> getPairsOfParameter( QueryBuildConfiguration config, Class<? extends AbstractQueryDistributionParameter> settingType, String settingName, boolean isOptional ) throws QueryDistributionException {
		AbstractQueryDistributionParameter setting = config.get(settingType);
		if( setting == null ) {
			if( isOptional ) {
				return Lists.newArrayList();
			}
			
			throw new QueryDistributionException("Setting of " + settingName + " is not set but needed.");
		}
				
		return setting.getPairs();
	}
}