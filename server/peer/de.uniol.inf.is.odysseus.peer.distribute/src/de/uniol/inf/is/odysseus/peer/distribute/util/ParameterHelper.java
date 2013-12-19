package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.DoForceLocalParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.DoMergeParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartAllocatorParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartModificatorParameter;
import de.uniol.inf.is.odysseus.peer.distribute.parameter.QueryPartitionerParameter;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartModificatorRegistry;
import de.uniol.inf.is.odysseus.peer.distribute.registry.QueryPartitionerRegistry;

public final class ParameterHelper {
	
	private static final boolean DO_FORCE_LOCAL_DEFAULT_VALUE = true;
	private static final boolean DO_MERGE_DEFAULT_VALUE = true;

	public static IQueryPartitioner determineQueryPartitioner(QueryBuildConfiguration config) throws QueryDistributionException {
		return QueryPartitionerRegistry.getInstance().get(getStringValueOfParameter(config, QueryPartitionerParameter.class, "Query partitioner"));
	}

	public static IQueryPartModificator determineQueryPartModificator(QueryBuildConfiguration config) throws QueryDistributionException {
		return QueryPartModificatorRegistry.getInstance().get(getStringValueOfParameter(config, QueryPartModificatorParameter.class, "Query part modificator"));
	}

	public static IQueryPartAllocator determineQueryPartAllocator(QueryBuildConfiguration config) throws QueryDistributionException {
		return QueryPartAllocatorRegistry.getInstance().get(getStringValueOfParameter(config, QueryPartAllocatorParameter.class, "Query part allocator"));
	}
	
	private static String getStringValueOfParameter( QueryBuildConfiguration config, Class<? extends IQueryBuildSetting<String>> settingType, String settingName ) throws QueryDistributionException {
		IQueryBuildSetting<String> stringSetting = config.get(settingType);
		if( stringSetting == null ) {
			throw new QueryDistributionException("Setting of " + settingName + " is not set but needed.");
		}
		return stringSetting.getValue();
	}
	
	public static List<String> determineQueryPartitionerParameters(QueryBuildConfiguration config) {
		QueryPartitionerParameter queryParameter = config.get(QueryPartitionerParameter.class);
		return queryParameter.getParameters();
	}
	
	public static List<String> determineQueryPartModificatorParameters(QueryBuildConfiguration config) {
		QueryPartModificatorParameter queryParameter = config.get(QueryPartModificatorParameter.class);
		return queryParameter.getParameters();
	}
	
	public static List<String> determineQueryPartAllocatorParameters(QueryBuildConfiguration config) {
		QueryPartAllocatorParameter queryParameter = config.get(QueryPartAllocatorParameter.class);
		return queryParameter.getParameters();
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
