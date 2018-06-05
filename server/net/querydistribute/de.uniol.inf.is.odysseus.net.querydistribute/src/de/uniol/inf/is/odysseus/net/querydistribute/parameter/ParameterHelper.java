package de.uniol.inf.is.odysseus.net.querydistribute.parameter;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
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

	private static final String DEFAULT_PREPROCESSORS_CONFIG_KEY = "net.querydistribute.preprocess";
	private static final String DEFAULT_QUERY_PARTITIONER_CONFIG_KEY = "net.querydistribute.partition";
	private static final String DEFAULT_QUERY_PART_MODIFICATOR_CONFIG_KEY = "net.querydistribute.modification";
	private static final String DEFAULT_QUERY_PART_ALLOCATOR_CONFIG_KEY = "net.querydistribute.allocation";
	private static final String DEFAULT_POSTPROCESSORS_CONFIG_KEY = "net.querydistribute.postprocess";

	private static final String DEFAULT_QUERY_PARTITIONER_NAME = "querycloud";
	private static final String DEFAULT_QUERY_PART_ALLOCATOR_NAME = "querycount";
	private static final String DEFAULT_QUERY_PART_POST_PROCESSOR_NAME = "merge";

	public static List<InterfaceParametersPair<IQueryPartitioner>> determineQueryPartitioners(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartitionerParameter.class);

		if (pairs.isEmpty()) {
			pairs.add(determineDefaultQueryPartitioner());
		}

		List<InterfaceParametersPair<IQueryPartitioner>> resultList = Lists.newArrayList();
		for (InterfaceNameParametersPair pair : pairs) {
			resultList.add(new InterfaceParametersPair<IQueryPartitioner>(QueryPartitionerRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultList;
	}

	private static InterfaceNameParametersPair determineDefaultQueryPartitioner() throws QueryDistributionException {
		String defaultPartitionerFromConfig = OdysseusNetConfiguration.get(DEFAULT_QUERY_PARTITIONER_CONFIG_KEY, DEFAULT_QUERY_PARTITIONER_NAME);
		if (!QueryPartitionerRegistry.getInstance().contains(defaultPartitionerFromConfig)) {
			throw new QueryDistributionException("Could not find default query partitioner " + defaultPartitionerFromConfig);
		}

		return new InterfaceNameParametersPair(defaultPartitionerFromConfig, Lists.newArrayList());
	}

	public static List<InterfaceParametersPair<IQueryPartModificator>> determineQueryPartModificators(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartModificatorParameter.class);

		if (pairs.isEmpty()) {
			List<InterfaceNameParametersPair> defaultModificators = determineDefaultQueryPartModificators();
			if (!defaultModificators.isEmpty()) {
				pairs.addAll(defaultModificators);
			}
		}

		List<InterfaceParametersPair<IQueryPartModificator>> resultList = Lists.newArrayList();
		for (InterfaceNameParametersPair pair : pairs) {
			resultList.add(new InterfaceParametersPair<IQueryPartModificator>(QueryPartModificatorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultList;
	}

	private static List<InterfaceNameParametersPair> determineDefaultQueryPartModificators() throws QueryDistributionException {
		Optional<String> optDefaultModificators = OdysseusNetConfiguration.get(DEFAULT_QUERY_PART_MODIFICATOR_CONFIG_KEY);
		List<InterfaceNameParametersPair> result = Lists.newArrayList();
		if (optDefaultModificators.isPresent()) {
			String defaultModificators = optDefaultModificators.get();
			if (!Strings.isNullOrEmpty(defaultModificators)) {
				String[] defaultModificatorsArray = defaultModificators.split(",");
				for (String defaultModificator : defaultModificatorsArray) {
					if (!QueryPartModificatorRegistry.getInstance().contains(defaultModificator)) {
						throw new QueryDistributionException("Could not find query part modificator " + defaultModificator);
					}

					result.add(new InterfaceNameParametersPair(defaultModificator, Lists.newArrayList()));
				}
			}
		}

		return result;
	}

	public static List<InterfaceParametersPair<IQueryPartAllocator>> determineQueryPartAllocators(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryPartAllocatorParameter.class);

		if (pairs.isEmpty()) {
			pairs.add(determineDefaultQueryPartAllocator());
		}

		List<InterfaceParametersPair<IQueryPartAllocator>> resultMap = Lists.newArrayList();
		for (InterfaceNameParametersPair pair : pairs) {
			resultMap.add(new InterfaceParametersPair<IQueryPartAllocator>(QueryPartAllocatorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultMap;
	}

	private static InterfaceNameParametersPair determineDefaultQueryPartAllocator() throws QueryDistributionException {
		String defaultAllocatorFromConfig = OdysseusNetConfiguration.get(DEFAULT_QUERY_PART_ALLOCATOR_CONFIG_KEY, DEFAULT_QUERY_PART_ALLOCATOR_NAME);
		if (!QueryPartAllocatorRegistry.getInstance().contains(defaultAllocatorFromConfig)) {
			throw new QueryDistributionException("Could not find default query part allocator " + defaultAllocatorFromConfig);
		}

		return new InterfaceNameParametersPair(defaultAllocatorFromConfig, Lists.newArrayList());
	}

	public static List<InterfaceParametersPair<IQueryDistributionPreProcessor>> determineQueryDistributionPreProcessors(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryDistributionPreProcessorParameter.class);

		if (pairs.isEmpty()) {
			List<InterfaceNameParametersPair> defaultPreprocessors = determineDefaultPreProcessors();
			if (!defaultPreprocessors.isEmpty()) {
				pairs.addAll(defaultPreprocessors);
			}
		}

		List<InterfaceParametersPair<IQueryDistributionPreProcessor>> resultMap = Lists.newArrayList();
		for (InterfaceNameParametersPair pair : pairs) {
			resultMap.add(new InterfaceParametersPair<IQueryDistributionPreProcessor>(QueryDistributionPreProcessorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultMap;
	}

	private static List<InterfaceNameParametersPair> determineDefaultPreProcessors() throws QueryDistributionException {
		Optional<String> optDefaultPreProcessors = OdysseusNetConfiguration.get(DEFAULT_PREPROCESSORS_CONFIG_KEY);
		List<InterfaceNameParametersPair> result = Lists.newArrayList();
		if (optDefaultPreProcessors.isPresent()) {
			String defaultPreProcessors = optDefaultPreProcessors.get();
			if (!Strings.isNullOrEmpty(defaultPreProcessors)) {
				String[] defaultPreProcessorsArray = defaultPreProcessors.split(",");
				for (String defaultPreProcessor : defaultPreProcessorsArray) {
					if (!QueryDistributionPreProcessorRegistry.getInstance().contains(defaultPreProcessor)) {
						throw new QueryDistributionException("Could not find query distribution preprocessor " + defaultPreProcessor);
					}

					result.add(new InterfaceNameParametersPair(defaultPreProcessor, Lists.newArrayList()));
				}
			}
		}

		return result;
	}

	public static List<InterfaceParametersPair<IQueryDistributionPostProcessor>> determineQueryDistributionPostProcessors(QueryBuildConfiguration config) throws QueryDistributionException {
		List<InterfaceNameParametersPair> pairs = getPairsOfParameter(config, QueryDistributionPostProcessorParameter.class);

		if (pairs.isEmpty()) {
			List<InterfaceNameParametersPair> defaultPostProcessors = determineDefaultPostProcessors();
			if (!defaultPostProcessors.isEmpty()) {
				pairs.addAll(defaultPostProcessors);
			}
		}

		List<InterfaceParametersPair<IQueryDistributionPostProcessor>> resultMap = Lists.newArrayList();
		for (InterfaceNameParametersPair pair : pairs) {
			resultMap.add(new InterfaceParametersPair<IQueryDistributionPostProcessor>(QueryDistributionPostProcessorRegistry.getInstance().get(pair.getName()), pair.getParameters()));
		}
		return resultMap;
	}

	private static List<InterfaceNameParametersPair> determineDefaultPostProcessors() throws QueryDistributionException {
		String defaultPostProcessors = OdysseusNetConfiguration.get(DEFAULT_POSTPROCESSORS_CONFIG_KEY, DEFAULT_QUERY_PART_POST_PROCESSOR_NAME);
		List<InterfaceNameParametersPair> result = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(defaultPostProcessors)) {
			String[] defaultPostProcessorsArray = defaultPostProcessors.split(",");
			for (String defaultPostProcessor : defaultPostProcessorsArray) {
				if (!QueryDistributionPostProcessorRegistry.getInstance().contains(defaultPostProcessor)) {
					throw new QueryDistributionException("Could not find query distribution postprocessor " + defaultPostProcessor);
				}

				result.add(new InterfaceNameParametersPair(defaultPostProcessor, Lists.newArrayList()));
			}
		}

		return result;
	}

	private static List<InterfaceNameParametersPair> getPairsOfParameter(QueryBuildConfiguration config, Class<? extends AbstractQueryDistributionParameter> settingType) {
		AbstractQueryDistributionParameter setting = config.get(settingType);
		if (setting == null) {
			return Lists.newArrayList();
		}

		return setting.getPairs();
	}
}