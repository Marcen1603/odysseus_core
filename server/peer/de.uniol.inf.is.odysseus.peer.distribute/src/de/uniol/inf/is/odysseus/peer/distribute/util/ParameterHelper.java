package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.AbstractQueryDistributionParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.DoForceLocalParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.DoMergeParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.InterfaceNameParametersPair;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartAllocatorParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartModificatorParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartitionerParameter;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartModificatorRegistry;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartitionerRegistry;

public final class ParameterHelper {
	
	private static final boolean DO_FORCE_LOCAL_DEFAULT_VALUE = true;
	private static final boolean DO_MERGE_DEFAULT_VALUE = true;

	public static List<InterfaceParametersPair<IQueryPartitioner>> determineQueryPartitioner(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartitionerParameter.class, "Query partitioner");
		
		List<InterfaceParametersPair<IQueryPartitioner>> resultList = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultList.add(new InterfaceParametersPair<IQueryPartitioner>(QueryPartitionerRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultList;
	}

	public static List<InterfaceParametersPair<IQueryPartModificator>> determineQueryPartModificator(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartModificatorParameter.class, "Query part modificator");
		
		List<InterfaceParametersPair<IQueryPartModificator>> resultList = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultList.add(new InterfaceParametersPair<IQueryPartModificator>(QueryPartModificatorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultList;
	}

	public static List<InterfaceParametersPair<IQueryPartAllocator>> determineQueryPartAllocator(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartAllocatorParameter.class, "Query part allocator");
		
		List<InterfaceParametersPair<IQueryPartAllocator>> resultMap = Lists.newArrayList();
		for( InterfaceNameParametersPair pair : pairs ) {
			resultMap.add(new InterfaceParametersPair<IQueryPartAllocator>(QueryPartAllocatorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultMap;
	}
	
	private static List<InterfaceNameParametersPair> getPairsOfParameter( QueryBuildConfiguration config, Class<? extends AbstractQueryDistributionParameter> settingType, String settingName ) throws QueryDistributionException {
		AbstractQueryDistributionParameter setting = config.get(settingType);
		if( setting == null ) {
			throw new QueryDistributionException("Setting of " + settingName + " is not set but needed.");
		}
				
		return setting.getPairs();
	}
	
	public static boolean determineDoMerge(QueryBuildConfiguration config) {
		DoMergeParameter param = config.get(DoMergeParameter.class);
		if( param == null ) {
			return DO_MERGE_DEFAULT_VALUE;
		}
		return param.getValue();
	}
	
	public static boolean determineDoForceLocal(QueryBuildConfiguration config) {
		DoForceLocalParameter param = config.get(DoForceLocalParameter.class);
		if( param == null ) {
			return DO_FORCE_LOCAL_DEFAULT_VALUE;
		}
		return param.getValue();
	}
}
