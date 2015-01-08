package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.SpaceUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.model.Space;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceUnit;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLStringParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Parser for SportsQL: Query: sprints.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * { "statisticType": "player", "gameType": "soccer", "entityId": 16, "name":
 * "sprints", "parameters": { "time": { "time": "game" }, "space": { "space":
 * "right_half" }, "output": { "output": "path" }, "speed": { "speed": "trot" }
 * } }
 * 
 * @author Simon Küspert, Pascal Schmedt
 * 
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.PLAYER }, name = "sprints", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false),
		@SportsQLParameter(name = "speed", parameterClass = SportsQLStringParameter.class, mandatory = false),
		@SportsQLParameter(name = "output", parameterClass = SportsQLStringParameter.class, mandatory = false), })
public class SprintsPlayerSportsQLParser implements ISportsQLParser {

	private static final String MICROMS_TO_KMH_FACTOR = "(1000000 / 3.6)";
	private static final Long MILLISECONDS_TO_PICOSECONDS = 1000000000L;
	private static final String STANDING_SPEED_UPPERBORDER = "1";
	private static final String TROT_SPEED_UPPERBORDER = "11";
	private static final String LOW_SPEED_UPPERBORDER = "14";
	private static final String MEDIUM_SPEED_UPPERBORDER = "17";
	private static final String HIGH_SPEED_UPPERBORDER = "24";
	
	// Output
	public static final String OUTPUT_PARAMETER_OVERVIEW = "overview";
	public static final String OUTPUT_PARAMETER_DETAIL = "detail";
	public static final String OUTPUT_PARAMETER_PATH = "path";

	// Speed
	public static final String SPEED_PARAMETER_STANDING = "standing";
	public static final String SPEED_PARAMETER_TROT = "trot";
	public static final String SPEED_PARAMETER_LOW = "low";
	public static final String SPEED_PARAMETER_MEDIUM = "medium";
	public static final String SPEED_PARAMETER_HIGH = "high";
	public static final String SPEED_PARAMETER_SPRINT = "sprint";
	
	// Attributes
	private static String ATTRIBUTE_second = "second";
	private List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

	@Override
	public ILogicalQuery parse(ISession session,SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO(session);
		StreamAO metadataStreamAO = OperatorBuildHelper
				.createMetadataStreamAO(session);
		SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);

		// 1. GameStream: Time Parameter
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		SelectAO gameTimeSelect = OperatorBuildHelper.createTimeMapAndSelect(
				timeParameter, soccerGameStreamAO);
		allOperators.add(gameTimeSelect);

		// 2. GameStream: Project
		List<String> streamAttributes = new ArrayList<String>();
		streamAttributes.add(IntermediateSchemaAttributes.ENTITY_ID);
		streamAttributes.add(ATTRIBUTE_second);
		streamAttributes.add(IntermediateSchemaAttributes.X);
		streamAttributes.add(IntermediateSchemaAttributes.Y);
		streamAttributes.add(IntermediateSchemaAttributes.V);

		ProjectAO streamProject = OperatorBuildHelper.createProjectAO(
				streamAttributes, gameTimeSelect);
		allOperators.add(streamProject);

		// 3. MetadataStream: Select
		SelectAO metadataSelect = OperatorBuildHelper.createEntityIDSelect(
				sportsQL.getEntityId(), metadataStreamAO);
		allOperators.add(metadataSelect);

		// 4. Enrich
		EnrichAO dataStreamEnrichAO = OperatorBuildHelper.createEnrichAO(
				IntermediateSchemaAttributes.ENTITY_ID + "= sid",
				streamProject, metadataSelect);
		allOperators.add(dataStreamEnrichAO);

		// 5. Project
		List<String> streamAttributes2 = new ArrayList<String>();
		streamAttributes2.add(ATTRIBUTE_second);
		streamAttributes2.add(IntermediateSchemaAttributes.X);
		streamAttributes2.add(IntermediateSchemaAttributes.Y);
		streamAttributes2.add(IntermediateSchemaAttributes.V);

		ProjectAO streamProject2 = OperatorBuildHelper.createProjectAO(
				streamAttributes2, dataStreamEnrichAO);
		allOperators.add(streamProject2);

		// 6. TimeWindow
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAO(
				1 * MILLISECONDS_TO_PICOSECONDS,
				1 * MILLISECONDS_TO_PICOSECONDS, "SECONDS", streamProject2);
		allOperators.add(timeWindow);

		// 7. Aggregate
		List<String> functions = new ArrayList<String>();
		functions.add("AVG");
		functions.add("MAX");
		functions.add("LAST");
		functions.add("LAST");

		List<String> inputAttributeNames = new ArrayList<String>();
		inputAttributeNames.add(IntermediateSchemaAttributes.V);
		inputAttributeNames.add(ATTRIBUTE_second);
		inputAttributeNames.add(IntermediateSchemaAttributes.X);
		inputAttributeNames.add(IntermediateSchemaAttributes.Y);

		List<String> outputAttributeNames = new ArrayList<String>();
		outputAttributeNames.add(IntermediateSchemaAttributes.V);
		outputAttributeNames.add(ATTRIBUTE_second);
		outputAttributeNames.add(IntermediateSchemaAttributes.X);
		outputAttributeNames.add(IntermediateSchemaAttributes.Y);

		AggregateAO aggregate = OperatorBuildHelper.createAggregateAO(
				functions, null, inputAttributeNames, outputAttributeNames,
				null, timeWindow,-1);
		allOperators.add(aggregate);

		// 8. MAP
		ArrayList<SDFExpressionParameter> mapExpressions = new ArrayList<SDFExpressionParameter>();
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, aggregate));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter("v / "
				+ MICROMS_TO_KMH_FACTOR, IntermediateSchemaAttributes.V, aggregate));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.X, aggregate));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.Y, aggregate));

		MapAO toKmhMap = OperatorBuildHelper.createMapAO(mapExpressions,
				aggregate, 0, 0, false);
		allOperators.add(toKmhMap);

		// 9. Statemap
		List<SDFExpressionParameter> statemapExpressions = new ArrayList<SDFExpressionParameter>();
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.V, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.X, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.Y, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " <= "
						+ STANDING_SPEED_UPPERBORDER + ", 1, 0)",
				"Standing_Time", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " <= "
						+ STANDING_SPEED_UPPERBORDER + ", "
						+ IntermediateSchemaAttributes.V + " /3.6, 0)",
				"Standing_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ STANDING_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ TROT_SPEED_UPPERBORDER + ", 1, 0)", "Trot_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ STANDING_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ TROT_SPEED_UPPERBORDER + ", "
						+ IntermediateSchemaAttributes.V + " /3.6, 0)",
				"Trot_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ TROT_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ LOW_SPEED_UPPERBORDER + ", 1, 0)", "Low_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ TROT_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ LOW_SPEED_UPPERBORDER + ", " + IntermediateSchemaAttributes.V
						+ " /3.6, 0)", "Low_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + IntermediateSchemaAttributes.V + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", 1, 0)", "Medium_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + IntermediateSchemaAttributes.V + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", "
						+ IntermediateSchemaAttributes.V + " /3.6, 0)",
				"Medium_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ MEDIUM_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "High_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ MEDIUM_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", "
						+ IntermediateSchemaAttributes.V + " /3.6, 0)",
				"High_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "Sprint_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ HIGH_SPEED_UPPERBORDER + ", "
						+ IntermediateSchemaAttributes.V + " /3.6, 0)",
				"Sprint_Distance", toKmhMap));

		StateMapAO divideSpeedStateMapAO = OperatorBuildHelper
				.createStateMapAO(statemapExpressions, "", toKmhMap);
		allOperators.add(divideSpeedStateMapAO);

		// 10. Output
		Map<String, ISportsQLParameter> parameters = sportsQL.getParameters();
		SportsQLStringParameter outputParameter = (SportsQLStringParameter) parameters
				.get("output");
		SportsQLStringParameter speedParameter = (SportsQLStringParameter) parameters
				.get("speed");

		ILogicalOperator topSource;
		if (outputParameter != null) {
			if (outputParameter.getValue().equals("path")) {
				// Output: Path
				topSource = createPathOutputAO(divideSpeedStateMapAO,
						spaceParameter);
				topSource = createSpeedPathSelectAO(speedParameter, topSource);
			} else if (outputParameter.getValue().equals("detail")) {
				// Output: Detail
				topSource = createDetailOutputAO(divideSpeedStateMapAO,
						spaceParameter);
				topSource = createSpeedDetailProjectAO(speedParameter,
						topSource);
			} else {
				// Output: Overview
				topSource = createOverviewOutputAO(divideSpeedStateMapAO,
						spaceParameter);
				topSource = createSpeedOverviewProjectAO(speedParameter,
						topSource);
			}
		} else {
			// Output: Overview
			topSource = createOverviewOutputAO(divideSpeedStateMapAO,
					spaceParameter);
			topSource = createSpeedOverviewProjectAO(speedParameter, topSource);
		}

		// 11. Finish
		return OperatorBuildHelper.finishQuery(topSource, allOperators,
				sportsQL.getDisplayName());
	}

	// Overview: SpeedParameter Project
	private ILogicalOperator createSpeedOverviewProjectAO(
			SportsQLStringParameter speedParameter, ILogicalOperator topSource) {
		if (speedParameter != null) {
			ProjectAO overviewProject = null;
			List<String> overviewAttributes = new ArrayList<String>();
			List<String> overviewChangeDetectAttributes = new ArrayList<String>();

			if (speedParameter.getValue().equals(
					SPEED_PARAMETER_STANDING)) {

				overviewAttributes.add(ATTRIBUTE_second);
				overviewAttributes.add("Standing_Time");
				overviewAttributes.add("Standing_Distance");

				overviewProject = OperatorBuildHelper.createProjectAO(
						overviewAttributes, topSource);

				overviewChangeDetectAttributes.add("Standing_Time");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_TROT)) {

				overviewAttributes.add(ATTRIBUTE_second);
				overviewAttributes.add("Trot_Time");
				overviewAttributes.add("Trot_Distance");

				overviewProject = OperatorBuildHelper.createProjectAO(
						overviewAttributes, topSource);

				overviewChangeDetectAttributes.add("Trot_Time");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_LOW)) {

				overviewAttributes.add(ATTRIBUTE_second);
				overviewAttributes.add("Low_Time");
				overviewAttributes.add("Low_Distance");

				overviewProject = OperatorBuildHelper.createProjectAO(
						overviewAttributes, topSource);

				overviewChangeDetectAttributes.add("Low_Time");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_MEDIUM)) {

				overviewAttributes.add(ATTRIBUTE_second);
				overviewAttributes.add("Medium_Time");
				overviewAttributes.add("Medium_Distance");

				overviewProject = OperatorBuildHelper.createProjectAO(
						overviewAttributes, topSource);

				overviewChangeDetectAttributes.add("Medium_Time");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_HIGH)) {

				overviewAttributes.add(ATTRIBUTE_second);
				overviewAttributes.add("High_Time");
				overviewAttributes.add("High_Distance");

				overviewProject = OperatorBuildHelper.createProjectAO(
						overviewAttributes, topSource);

				overviewChangeDetectAttributes.add("High_Time");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_SPRINT)) {

				overviewAttributes.add(ATTRIBUTE_second);
				overviewAttributes.add("Sprint_Time");
				overviewAttributes.add("Sprint_Distance");

				overviewProject = OperatorBuildHelper.createProjectAO(
						overviewAttributes, topSource);

				overviewChangeDetectAttributes.add("Sprint_Time");

			}
			allOperators.add(overviewProject);

			ChangeDetectAO overviewChangeDetect = OperatorBuildHelper
					.createChangeDetectAO(OperatorBuildHelper
							.createAttributeList(
									overviewChangeDetectAttributes,
									overviewProject), 0, overviewProject);

			allOperators.add(overviewChangeDetect);

			return overviewChangeDetect;
		}
		return topSource;

	}

	// Detail: SpeedParameter Project
	private ILogicalOperator createSpeedDetailProjectAO(
			SportsQLStringParameter speedParameter, ILogicalOperator topSource) {
		if (speedParameter != null) {
			ProjectAO detailProject = null;
			List<String> detailAttributes = new ArrayList<String>();
			List<String> detailChangeDetectAttributes = new ArrayList<String>();

			if (speedParameter.getValue().equals(
					SPEED_PARAMETER_STANDING)) {

				detailAttributes.add(ATTRIBUTE_second);
				detailAttributes.add("Standing_Count");

				detailProject = OperatorBuildHelper.createProjectAO(
						detailAttributes, topSource);

				detailChangeDetectAttributes.add("Standing_Count");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_TROT)) {

				detailAttributes.add(ATTRIBUTE_second);
				detailAttributes.add("Trot_Count");

				detailProject = OperatorBuildHelper.createProjectAO(
						detailAttributes, topSource);

				detailChangeDetectAttributes.add("Trot_Count");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_LOW)) {

				detailAttributes.add(ATTRIBUTE_second);
				detailAttributes.add("Low_Count");

				detailProject = OperatorBuildHelper.createProjectAO(
						detailAttributes, topSource);

				detailChangeDetectAttributes.add("Low_Count");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_MEDIUM)) {

				detailAttributes.add(ATTRIBUTE_second);
				detailAttributes.add("Medium_Count");

				detailProject = OperatorBuildHelper.createProjectAO(
						detailAttributes, topSource);

				detailChangeDetectAttributes.add("Medium_Count");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_HIGH)) {

				detailAttributes.add(ATTRIBUTE_second);
				detailAttributes.add("High_Count");

				detailProject = OperatorBuildHelper.createProjectAO(
						detailAttributes, topSource);

				detailChangeDetectAttributes.add("High_Count");

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_SPRINT)) {

				detailAttributes.add(ATTRIBUTE_second);
				detailAttributes.add("Sprint_Count");

				detailProject = OperatorBuildHelper.createProjectAO(
						detailAttributes, topSource);

				detailChangeDetectAttributes.add("Sprint_Count");

			}

			allOperators.add(detailProject);

			ChangeDetectAO detailChangeDetect = OperatorBuildHelper
					.createChangeDetectAO(OperatorBuildHelper
							.createAttributeList(detailChangeDetectAttributes,
									detailProject), 0, detailProject);

			allOperators.add(detailChangeDetect);

			return detailChangeDetect;
		}

		return topSource;
	}

	// Path: SpeedParameter Select
	private ILogicalOperator createSpeedPathSelectAO(
			SportsQLStringParameter speedParameter, ILogicalOperator topSource) {
		if (speedParameter != null) {
			SelectAO pathSelect;
			ArrayList<String> pathSelectPredicates = new ArrayList<String>();

			if (speedParameter.getValue().equals(
					SPEED_PARAMETER_STANDING)) {

				pathSelectPredicates.add("Classification = 'Standing'");
				pathSelect = OperatorBuildHelper.createSelectAO(
						pathSelectPredicates, topSource);

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_TROT)) {

				pathSelectPredicates.add("Classification = 'Trot'");
				pathSelect = OperatorBuildHelper.createSelectAO(
						pathSelectPredicates, topSource);

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_LOW)) {

				pathSelectPredicates.add("Classification = 'Low'");
				pathSelect = OperatorBuildHelper.createSelectAO(
						pathSelectPredicates, topSource);

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_MEDIUM)) {

				pathSelectPredicates.add("Classification = 'Medium'");
				pathSelect = OperatorBuildHelper.createSelectAO(
						pathSelectPredicates, topSource);

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_HIGH)) {

				pathSelectPredicates.add("Classification = 'High'");
				pathSelect = OperatorBuildHelper.createSelectAO(
						pathSelectPredicates, topSource);

			} else if (speedParameter.getValue().equals(
					SPEED_PARAMETER_SPRINT)) {

				pathSelectPredicates.add("Classification = 'Sprint'");
				pathSelect = OperatorBuildHelper.createSelectAO(
						pathSelectPredicates, topSource);

			} else {
				return topSource;
			}
			allOperators.add(pathSelect);

			return pathSelect;
		}
		return topSource;
	}

	// create Overview Output
	private ILogicalOperator createOverviewOutputAO(ILogicalOperator source,
			SportsQLSpaceParameter spaceParameter) throws NumberFormatException, MissingDDCEntryException {

		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
				spaceParameter, false, source);
		allOperators.add(spaceSelect);

		// Overview: TimeWindow
		// TODO: size anpassen (aus dem ddc)
		TimeWindowAO timeWindow2 = OperatorBuildHelper.createTimeWindowAO(
				120 * MILLISECONDS_TO_PICOSECONDS, "MINUTES", spaceSelect);

		allOperators.add(timeWindow2);

		// Overview: Aggregate
		List<String> functions2 = new ArrayList<String>();
		functions2.add("LAST");
		functions2.add("LAST");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");

		List<String> inputAttributeNames2 = new ArrayList<String>();
		inputAttributeNames2.add(ATTRIBUTE_second);
		inputAttributeNames2.add(IntermediateSchemaAttributes.V);
		inputAttributeNames2.add("Standing_Time");
		inputAttributeNames2.add("Standing_Distance");
		inputAttributeNames2.add("Trot_Time");
		inputAttributeNames2.add("Trot_Distance");
		inputAttributeNames2.add("Low_Time");
		inputAttributeNames2.add("Low_Distance");
		inputAttributeNames2.add("Medium_Time");
		inputAttributeNames2.add("Medium_Distance");
		inputAttributeNames2.add("High_Time");
		inputAttributeNames2.add("High_Distance");
		inputAttributeNames2.add("Sprint_Time");
		inputAttributeNames2.add("Sprint_Distance");

		List<String> outputAttributeNames2 = new ArrayList<String>();
		outputAttributeNames2.add(ATTRIBUTE_second);
		outputAttributeNames2.add(IntermediateSchemaAttributes.V);
		outputAttributeNames2.add("Standing_Time");
		outputAttributeNames2.add("Standing_Distance");
		outputAttributeNames2.add("Trot_Time");
		outputAttributeNames2.add("Trot_Distance");
		outputAttributeNames2.add("Low_Time");
		outputAttributeNames2.add("Low_Distance");
		outputAttributeNames2.add("Medium_Time");
		outputAttributeNames2.add("Medium_Distance");
		outputAttributeNames2.add("High_Time");
		outputAttributeNames2.add("High_Distance");
		outputAttributeNames2.add("Sprint_Time");
		outputAttributeNames2.add("Sprint_Distance");

		AggregateAO aggregate2 = OperatorBuildHelper.createAggregateAO(
				functions2, null, inputAttributeNames2, outputAttributeNames2,
				null, timeWindow2, -1);

		allOperators.add(aggregate2);

		return aggregate2;
	}

	// create Detail Output
	private ILogicalOperator createDetailOutputAO(ILogicalOperator source,
			SportsQLSpaceParameter spaceParameter) throws NumberFormatException, MissingDDCEntryException {

		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
				spaceParameter, false, source);
		allOperators.add(spaceSelect);
		// Detail: ChangeDetect
		List<String> speedClassificationChangeDetectAttributes = new ArrayList<String>();
		speedClassificationChangeDetectAttributes.add("Standing_Time");
		speedClassificationChangeDetectAttributes.add("Trot_Time");
		speedClassificationChangeDetectAttributes.add("Low_Time");
		speedClassificationChangeDetectAttributes.add("Medium_Time");
		speedClassificationChangeDetectAttributes.add("High_Time");
		speedClassificationChangeDetectAttributes.add("Sprint_Time");

		List<String> ballVelocityChangeDetectGroupByAttributes = new ArrayList<String>();
		ballVelocityChangeDetectGroupByAttributes.add(IntermediateSchemaAttributes.ENTITY_ID);

		ChangeDetectAO speedClassificationChangeDetect = OperatorBuildHelper
				.createChangeDetectAO(
						OperatorBuildHelper.createAttributeList(
								speedClassificationChangeDetectAttributes,
								spaceSelect), 0, spaceSelect);

		allOperators.add(speedClassificationChangeDetect);

		// Detail: Statemap
		List<SDFExpressionParameter> statemapExpressions2 = new ArrayList<SDFExpressionParameter>();
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.V, speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " <= "
						+ STANDING_SPEED_UPPERBORDER + ", 1, 0)",
				"Standing_Count", speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ STANDING_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ TROT_SPEED_UPPERBORDER + ", 1, 0)", "Trot_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ TROT_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ LOW_SPEED_UPPERBORDER + ", 1, 0)", "Low_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + IntermediateSchemaAttributes.V + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", 1, 0)", "Medium_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ MEDIUM_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "High_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + " > "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "Sprint_Count",
				speedClassificationChangeDetect));

		StateMapAO countStateMapAO = OperatorBuildHelper.createStateMapAO(
				statemapExpressions2, "", speedClassificationChangeDetect);

		allOperators.add(countStateMapAO);

		// Detail: TimeWindow
		// TODO: size anpassen (aus dem ddc)
		TimeWindowAO timeWindow3 = OperatorBuildHelper.createTimeWindowAO(
				120 * MILLISECONDS_TO_PICOSECONDS, "MINUTES", countStateMapAO);

		allOperators.add(timeWindow3);

		// Detail: Aggregate
		List<String> functions3 = new ArrayList<String>();
		functions3.add("LAST");
		functions3.add("LAST");
		functions3.add("SUM");
		functions3.add("SUM");
		functions3.add("SUM");
		functions3.add("SUM");
		functions3.add("SUM");
		functions3.add("SUM");

		List<String> inputAttributeNames3 = new ArrayList<String>();
		inputAttributeNames3.add(ATTRIBUTE_second);
		inputAttributeNames3.add(IntermediateSchemaAttributes.V);
		inputAttributeNames3.add("Standing_Count");
		inputAttributeNames3.add("Trot_Count");
		inputAttributeNames3.add("Low_Count");
		inputAttributeNames3.add("Medium_Count");
		inputAttributeNames3.add("High_Count");
		inputAttributeNames3.add("Sprint_Count");

		List<String> outputAttributeNames3 = new ArrayList<String>();
		outputAttributeNames3.add(ATTRIBUTE_second);
		outputAttributeNames3.add(IntermediateSchemaAttributes.V);
		outputAttributeNames3.add("Standing_Count");
		outputAttributeNames3.add("Trot_Count");
		outputAttributeNames3.add("Low_Count");
		outputAttributeNames3.add("Medium_Count");
		outputAttributeNames3.add("High_Count");
		outputAttributeNames3.add("Sprint_Count");

		AggregateAO aggregate3 = OperatorBuildHelper.createAggregateAO(
				functions3, null, inputAttributeNames3, outputAttributeNames3,
				null, timeWindow3, -1);

		allOperators.add(aggregate3);

		return aggregate3;

	}

	// create Path Output
	private ILogicalOperator createPathOutputAO(ILogicalOperator source,
			SportsQLSpaceParameter spaceParameter) throws NumberFormatException, MissingDDCEntryException {
		// path: Statemap
		List<SDFExpressionParameter> statemapExpressions3 = new ArrayList<SDFExpressionParameter>();
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, source));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.V + "<= "
						+ STANDING_SPEED_UPPERBORDER + ", \"Standing\", eif("
						+ IntermediateSchemaAttributes.V + " > "
						+ STANDING_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ TROT_SPEED_UPPERBORDER + ", \"Trot\", eif("
						+ IntermediateSchemaAttributes.V + " > "
						+ TROT_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ LOW_SPEED_UPPERBORDER + ", \"Low\", eif("
						+ IntermediateSchemaAttributes.V + " > "
						+ LOW_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", \"Medium\", eif("
						+ IntermediateSchemaAttributes.V + " > "
						+ MEDIUM_SPEED_UPPERBORDER + " AND "
						+ IntermediateSchemaAttributes.V + " <="
						+ HIGH_SPEED_UPPERBORDER
						+ ", \"High\", \"Sprint\")))))", "Classification",
				source));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"__last_1." + IntermediateSchemaAttributes.X, "Start_x", source));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"__last_1." + IntermediateSchemaAttributes.Y, "Start_y", source));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.X, "End_x", source));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.Y, "End_y", source));

		StateMapAO pathStateMapAO = OperatorBuildHelper.createStateMapAO(
				statemapExpressions3, "", source);

		allOperators.add(pathStateMapAO);

		// Space Select
		ILogicalOperator spaceSelect = createSpaceSelectAO(pathStateMapAO,
				spaceParameter);
		allOperators.add(spaceSelect);

		return spaceSelect;

	}

	// create Space Select
	@SuppressWarnings("rawtypes")
	private ILogicalOperator createSpaceSelectAO(ILogicalOperator source,
			SportsQLSpaceParameter spaceParameter) throws NumberFormatException, MissingDDCEntryException {

		SpaceUnit unit = spaceParameter.getUnit();
		if (unit == null) {
			unit = SpaceUnit.millimeters;
		}

		double startX = SpaceUnitHelper.getMillimeters(spaceParameter.getStartx(),
				unit);
		double startY = SpaceUnitHelper.getMillimeters(spaceParameter.getStarty(),
				unit);
		double endX = SpaceUnitHelper.getMillimeters(spaceParameter.getEndx(),
				unit);
		double endY = SpaceUnitHelper.getMillimeters(spaceParameter.getEndy(),
				unit);

		if (spaceParameter.getSpace() != null) {
			Space space = AbstractSportsDDCAccess.getSpace(spaceParameter.getSpace());
			startX = space.getXMin();
			startY = space.getYMin();
			endX = space.getXMax();
			endY = space.getYMax();
		}

		String firstPredicateString = "Start_x >= " + startX;
		String secondPredicateString = "Start_x <= " + endX;
		String thirdPredicateString = "Start_y >= " + startY;
		String fourthPredicateString = "Start_y <= " + endY;

		String fifthPredicateString = "End_x >= " + startX;
		String sixthPredicateString = "End_x <= " + endX;
		String seventhPredicateString = "End_y >= " + startY;
		String eighthPredicateString = "End_y <= " + endY;

		SelectAO spaceSelect = new SelectAO();

		// Create Predicates from Strings

		SDFExpression firstPredicateExpression = new SDFExpression(
				firstPredicateString, MEP.getInstance());
		RelationalPredicate firstPredicate = new RelationalPredicate(
				firstPredicateExpression);

		SDFExpression secondPredicateExpression = new SDFExpression(
				secondPredicateString, MEP.getInstance());
		RelationalPredicate secondPredicate = new RelationalPredicate(
				secondPredicateExpression);

		SDFExpression thirdPredicateExpression = new SDFExpression(
				thirdPredicateString, MEP.getInstance());
		RelationalPredicate thirdPredicate = new RelationalPredicate(
				thirdPredicateExpression);

		SDFExpression fourthPredicateExpression = new SDFExpression(
				fourthPredicateString, MEP.getInstance());
		RelationalPredicate fourthPredicate = new RelationalPredicate(
				fourthPredicateExpression);

		SDFExpression fifthPredicateExpression = new SDFExpression(
				fifthPredicateString, MEP.getInstance());
		RelationalPredicate fifthPredicate = new RelationalPredicate(
				fifthPredicateExpression);

		SDFExpression sixthPredicateExpression = new SDFExpression(
				sixthPredicateString, MEP.getInstance());
		RelationalPredicate sixthPredicate = new RelationalPredicate(
				sixthPredicateExpression);

		SDFExpression seventhPredicateExpression = new SDFExpression(
				seventhPredicateString, MEP.getInstance());
		RelationalPredicate seventhPredicate = new RelationalPredicate(
				seventhPredicateExpression);

		SDFExpression eighthPredicateExpression = new SDFExpression(
				eighthPredicateString, MEP.getInstance());
		RelationalPredicate eighthPredicate = new RelationalPredicate(
				eighthPredicateExpression);

		IPredicate firstAndPredicate = ComplexPredicateHelper
				.createAndPredicate(firstPredicate, secondPredicate);
		IPredicate secondAndPredicate = ComplexPredicateHelper
				.createAndPredicate(thirdPredicate, fourthPredicate);
		IPredicate thirdAndPredicate = ComplexPredicateHelper
				.createAndPredicate(fifthPredicate, sixthPredicate);
		IPredicate fourthAndPredicate = ComplexPredicateHelper
				.createAndPredicate(seventhPredicate, eighthPredicate);
		IPredicate firstHalfAndPredicate = ComplexPredicateHelper
				.createAndPredicate(firstAndPredicate, secondAndPredicate);
		IPredicate secondHalfAndPredicate = ComplexPredicateHelper
				.createAndPredicate(thirdAndPredicate, fourthAndPredicate);
		IPredicate fullAndPredicate = ComplexPredicateHelper
				.createAndPredicate(firstHalfAndPredicate,
						secondHalfAndPredicate);

		spaceSelect.setPredicate(fullAndPredicate);

		spaceSelect.subscribeTo(source, source.getOutputSchema());

		return spaceSelect;
	}
}
