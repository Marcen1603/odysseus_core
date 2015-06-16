package de.uniol.inf.is.odysseus.multithreaded.interoperator.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.multithreaded.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.multithreaded.helper.FragmentationTypeHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorParameter;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.registry.MultithreadedTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class InterOperatorParallelizationPreParserKeyword extends
		AbstractPreParserKeyword {

	public enum DegreeOfParalleizationConstants {
		USERDEFINED, GLOBAL, AUTO;

		public static DegreeOfParalleizationConstants getConstantByName(
				String name) {
			for (DegreeOfParalleizationConstants parameter : DegreeOfParalleizationConstants
					.values()) {
				if (parameter.name().equalsIgnoreCase(name)) {
					return parameter;
				}
			}
			return null;
		}
	}

	public enum BufferSizeConstants {
		USERDEFINED, AUTO;

		public static BufferSizeConstants getConstantByName(String name) {
			for (BufferSizeConstants parameter : BufferSizeConstants.values()) {
				if (parameter.name().equalsIgnoreCase(name)) {
					return parameter;
				}
			}
			return null;
		}
	}

	public static final String KEYWORD = "INTEROPERATORPARALLELIZATION";

	private static final Logger LOG = LoggerFactory
			.getLogger(InterOperatorParallelizationPreParserKeyword.class);
	private static final int MIN_ATTRIBUTE_COUNT = 3;
	private static final int MAX_ATTRIBUTE_COUNT = 5;
	private static final String PATTERN = "<1..n Unique operator Ids> "
			+ "<degree of parallelization or GLOBAL or AUTO> "
			+ "<buffer-size or AUTO>" + "<multithreading-strategy (optional)> "
			+ "<fragmentation-type e.g. round robin (optional)> ";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Parameters for " + KEYWORD
					+ " are missing");
		}

		String[] splitted = parameter.trim().split(" ");

		if (splitted.length < MIN_ATTRIBUTE_COUNT
				|| splitted.length > MAX_ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(KEYWORD + " requires at least "
					+ MIN_ATTRIBUTE_COUNT + " and maximum "
					+ MAX_ATTRIBUTE_COUNT + "attributes: " + PATTERN + "!");
		}

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);

		// split parameters on whitespaces
		String[] keywordParameter = parameter.trim().split(" ");

		// create operator settings
		MultithreadedOperatorSettings operatorSettings = new MultithreadedOperatorSettings();

		// 1. parameter: operatorId's
		String[] splittedOperatorIDs = keywordParameter[0].trim().split(",");
		List<String> operatorIds = new ArrayList<String>(
				Arrays.asList(splittedOperatorIDs));
		LOG.debug("Multithreading for operators with id: "
				+ operatorIds.toString());

		// 2. parameter: degree of parallelization for defined operators
		String degreeOfParallelizationString = keywordParameter[1];
		try {
			int degreeOfParallelization = Integer
					.parseInt(degreeOfParallelizationString);
			if (degreeOfParallelization > PerformanceDetectionHelper
					.getNumberOfCores()) {
				LOG.warn("Degree of parallelization is greater than available cores");
			}
			operatorSettings
					.setDegreeOfParallelization(degreeOfParallelization);
		} catch (NumberFormatException e) {
			LOG.debug("Degree is no integer. Try to determine constant value");
			DegreeOfParalleizationConstants degreeOfParalleizationConstant = DegreeOfParalleizationConstants
					.getConstantByName(degreeOfParallelizationString);
			if (degreeOfParalleizationConstant != null
					&& degreeOfParalleizationConstant != DegreeOfParalleizationConstants.USERDEFINED) {
				operatorSettings
						.setDegreeConstant(degreeOfParalleizationConstant);
			} else {
				throw new OdysseusScriptException(
						"DegreeOfParallelization is not an integer or an valid constant: ");
			}
		}

		// 3. parameter: buffer-size
		String buffersizeString = keywordParameter[2];
		try {
			int buffersize = Integer.parseInt(buffersizeString);
			operatorSettings.setBufferSize(buffersize);
		} catch (NumberFormatException e) {
			LOG.debug("Buffersize is no integer. Try to determine constant value");
			BufferSizeConstants buffersizeConstant = BufferSizeConstants.getConstantByName(buffersizeString);
			if (buffersizeConstant != null && buffersizeConstant != BufferSizeConstants.USERDEFINED){
				operatorSettings.setBufferSizeConstant(buffersizeConstant);
			} else {
				throw new OdysseusScriptException(
						"Buffersize is not an integer or an valid constant: ");
			}
		}

		// 4. parameter (optional): multithreading-strategy
		if (keywordParameter.length >= 4) {
			String strategyName = keywordParameter[3];
			if (MultithreadedTransformationStrategyRegistry
					.isValidStrategyName(strategyName)) {
				operatorSettings.setMultithreadingStrategy(strategyName);
			} else {
				throw new OdysseusScriptException(
						"Value for Tranformation strategy is not valid");
			}
		}

		// 5. parameter (optional): fragmentation-type
		if (keywordParameter.length == 5) {
			String dataFragmentation = keywordParameter[4];
			if (FragmentationTypeHelper
					.isValidFragmentationType(dataFragmentation)) {
				operatorSettings.setFragementationType(dataFragmentation);
			} else {
				throw new OdysseusScriptException(
						"Value for fragmentation type is not valid");
			}

		}

		// get parameter from settings or create new one if not exists
		MultithreadedOperatorParameter mtOperatorParameter = null;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(MultithreadedOperatorParameter.class)) {
				mtOperatorParameter = (MultithreadedOperatorParameter) setting;
			}
		}
		if (mtOperatorParameter == null) {
			mtOperatorParameter = new MultithreadedOperatorParameter();
			settings.add(mtOperatorParameter);
		}

		// check if settings for one of the given operatorIds already exists
		for (String operatorId : operatorIds) {
			if (mtOperatorParameter.getOperatorIds().contains(operatorId)) {
				throw new OdysseusScriptException(
						"Multiple definition for operator with id: "
								+ operatorId);
			}
		}
		
		// add settings for each operatorId
		mtOperatorParameter.addSettingsForOperators(operatorIds,
				operatorSettings);

		return null;
	}
}
