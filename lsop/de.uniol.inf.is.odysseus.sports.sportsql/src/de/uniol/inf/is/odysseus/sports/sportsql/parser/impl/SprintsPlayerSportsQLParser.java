package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.MetadataAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Parser for SportsQL: Query: game time.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * { "statisticType": "player", 
 * "gameType": "soccer", 
 * "name": "sprints", 
 * "entity_ID": "16",
 * "parameters": { 
 * 		"time": { 
 * 			"time": "game" 
 * 		} 
 * } 
 * };
 * 
 * @author Simon Küspert, Pascal Schmedt
 * 
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.PLAYER }, name = "sprints", parameters = { @SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false) })
public class SprintsPlayerSportsQLParser implements ISportsQLParser {

	private static final String MICROMS_TO_KMH_FACTOR = "(1000000 / 3.6)";
	private static final Long MILLISECONDS_TO_PICOSECONDS = 1000000000L;
	private static final String STANDING_SPEED_UPPERBORDER = "1";
	private static final String TROT_SPEED_UPPERBORDER = "11";
	private static final String LOW_SPEED_UPPERBORDER = "14";
	private static final String MEDIUM_SPEED_UPPERBORDER = "17";
	private static final String HIGH_SPEED_UPPERBORDER = "24";

	// Attributes
	private static String ATTRIBUTE_second = "second";

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();
		StreamAO metadataStreamAO = OperatorBuildHelper
				.createMetadataStreamAO();

		// 1. Time Parameter
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		SelectAO gameTimeSelect = OperatorBuildHelper.createTimeMapAndSelect(
				timeParameter, soccerGameStreamAO);
		allOperators.add(gameTimeSelect);

		// 2. Project
		List<String> streamAttributes = new ArrayList<String>();
		streamAttributes.add(SoccerGameAttributes.SID);
		streamAttributes.add(ATTRIBUTE_second);
		streamAttributes.add(SoccerGameAttributes.X);
		streamAttributes.add(SoccerGameAttributes.Y);
		streamAttributes.add(SoccerGameAttributes.V);

		ProjectAO streamProject = OperatorBuildHelper.createProjectAO(
				streamAttributes, gameTimeSelect);
		allOperators.add(streamProject);

		// 3. Select Metadata
		SelectAO metadataSelect = OperatorBuildHelper.createEntityIDSelect(
				sportsQL.getEntityId(), metadataStreamAO);
		allOperators.add(metadataSelect);

		// 4. Enrich
		EnrichAO dataStreamEnrichAO = OperatorBuildHelper.createEnrichAO(
				SoccerGameAttributes.SID + "=" + MetadataAttributes.SENSOR_ID,
				streamProject, metadataSelect);
		allOperators.add(dataStreamEnrichAO);

		// 4.1 Project
		List<String> streamAttributes2 = new ArrayList<String>();
		streamAttributes2.add(ATTRIBUTE_second);
		streamAttributes2.add(SoccerGameAttributes.X);
		streamAttributes2.add(SoccerGameAttributes.Y);
		streamAttributes2.add(SoccerGameAttributes.V);

		ProjectAO streamProject2 = OperatorBuildHelper.createProjectAO(
				streamAttributes2, dataStreamEnrichAO);
		allOperators.add(streamProject2);

		// 4.2 TimeWindow
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAO(1*MILLISECONDS_TO_PICOSECONDS, 1*MILLISECONDS_TO_PICOSECONDS,
				"SECONDS", streamProject2);
		allOperators.add(timeWindow);

		// 4.3 Aggregate
		List<String> functions = new ArrayList<String>();
		functions.add("AVG");
		functions.add("MAX");
		functions.add("LAST");
		functions.add("LAST");

		List<String> inputAttributeNames = new ArrayList<String>();
		inputAttributeNames.add(SoccerGameAttributes.V);
		inputAttributeNames.add(ATTRIBUTE_second);
		inputAttributeNames.add(SoccerGameAttributes.X);
		inputAttributeNames.add(SoccerGameAttributes.Y);

		List<String> outputAttributeNames = new ArrayList<String>();
		outputAttributeNames.add(SoccerGameAttributes.V);
		outputAttributeNames.add(ATTRIBUTE_second);
		outputAttributeNames.add(SoccerGameAttributes.X);
		outputAttributeNames.add(SoccerGameAttributes.Y);

		AggregateAO aggregate = OperatorBuildHelper.createAggregateAO(
				functions, null, inputAttributeNames, outputAttributeNames,
				null, timeWindow);
		allOperators.add(aggregate);

		// 4.4 MAP
		ArrayList<SDFExpressionParameter> mapExpressions = new ArrayList<SDFExpressionParameter>();
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, aggregate));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter("v / "
				+ MICROMS_TO_KMH_FACTOR, SoccerGameAttributes.V, aggregate));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.X, aggregate));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.Y, aggregate));

		MapAO toKmhMap = OperatorBuildHelper.createMapAO(mapExpressions,
				aggregate, 0, 0);
		allOperators.add(toKmhMap);

		// 4.5 Statemap
		List<SDFExpressionParameter> statemapExpressions = new ArrayList<SDFExpressionParameter>();
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.V, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.X, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.Y, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " <= "
						+ STANDING_SPEED_UPPERBORDER + ", 1, 0)",
				"Standing_Time", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " <= "
						+ STANDING_SPEED_UPPERBORDER + ", "
						+ SoccerGameAttributes.V + " /3.6, 0)",
				"Standing_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ STANDING_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ TROT_SPEED_UPPERBORDER + ", 1, 0)", "Trot_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ STANDING_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ TROT_SPEED_UPPERBORDER + ", "
						+ SoccerGameAttributes.V + " /3.6, 0)",
				"Trot_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ TROT_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ LOW_SPEED_UPPERBORDER + ", 1, 0)", "Low_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ TROT_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ LOW_SPEED_UPPERBORDER + ", " + SoccerGameAttributes.V
						+ " /3.6, 0)", "Low_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + SoccerGameAttributes.V + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", 1, 0)", "Medium_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + SoccerGameAttributes.V + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", "
						+ SoccerGameAttributes.V + " /3.6, 0)",
				"Medium_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ MEDIUM_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "High_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ MEDIUM_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", "
						+ SoccerGameAttributes.V + " /3.6, 0)",
				"High_Distance", toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "Sprint_Time",
				toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ HIGH_SPEED_UPPERBORDER + ", "
						+ SoccerGameAttributes.V + " /3.6, 0)",
				"Sprint_Distance", toKmhMap));

		StateMapAO divideSpeedStateMapAO = OperatorBuildHelper
				.createStateMapAO(statemapExpressions, "", toKmhMap);
		allOperators.add(divideSpeedStateMapAO);

		// 4.6 TimeWindow
		// TODO: size anpassen (aus dem ddc)
		TimeWindowAO timeWindow2 = OperatorBuildHelper.createTimeWindowAO(120*MILLISECONDS_TO_PICOSECONDS,
				"MINUTES", divideSpeedStateMapAO);
		allOperators.add(timeWindow2);

		// 4.7 Aggregate
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
		inputAttributeNames2.add(SoccerGameAttributes.V);
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
		outputAttributeNames2.add(SoccerGameAttributes.V);
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
				null, timeWindow2);
		allOperators.add(aggregate2);

		// 4.6.1 ChangeDetect
		List<String> speedClassificationChangeDetectAttributes = new ArrayList<String>();
		speedClassificationChangeDetectAttributes.add("Standing_Time");
		speedClassificationChangeDetectAttributes.add("Trot_Time");
		speedClassificationChangeDetectAttributes.add("Low_Time");
		speedClassificationChangeDetectAttributes.add("Medium_Time");
		speedClassificationChangeDetectAttributes.add("High_Time");
		speedClassificationChangeDetectAttributes.add("Sprint_Time");

		List<String> ballVelocityChangeDetectGroupByAttributes = new ArrayList<String>();
		ballVelocityChangeDetectGroupByAttributes.add(SoccerGameAttributes.SID);

		ChangeDetectAO speedClassificationChangeDetect = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						speedClassificationChangeDetectAttributes,
						divideSpeedStateMapAO), 0, divideSpeedStateMapAO);
		allOperators.add(speedClassificationChangeDetect);

		// 4.6.2 Statemap
		List<SDFExpressionParameter> statemapExpressions2 = new ArrayList<SDFExpressionParameter>();
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.V, speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " <= "
						+ STANDING_SPEED_UPPERBORDER + ", 1, 0)",
				"Standing_Count", speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ STANDING_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ TROT_SPEED_UPPERBORDER + ", 1, 0)", "Trot_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ TROT_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ LOW_SPEED_UPPERBORDER + ", 1, 0)", "Low_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + SoccerGameAttributes.V + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", 1, 0)", "Medium_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ MEDIUM_SPEED_UPPERBORDER + " AND "
						+ SoccerGameAttributes.V + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "High_Count",
				speedClassificationChangeDetect));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.V + " > "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)", "Sprint_Count",
				speedClassificationChangeDetect));

		StateMapAO countStateMapAO = OperatorBuildHelper.createStateMapAO(
				statemapExpressions2, "", speedClassificationChangeDetect);
		allOperators.add(countStateMapAO);

		// 4.6.3 TimeWindow
		// TODO: size anpassen (aus dem ddc)
		TimeWindowAO timeWindow3 = OperatorBuildHelper.createTimeWindowAO(120*MILLISECONDS_TO_PICOSECONDS,
				"MINUTES", countStateMapAO);
		allOperators.add(timeWindow3);

		// 4.6.4 Aggregate
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
		inputAttributeNames3.add(SoccerGameAttributes.V);
		inputAttributeNames3.add("Standing_Count");
		inputAttributeNames3.add("Trot_Count");
		inputAttributeNames3.add("Low_Count");
		inputAttributeNames3.add("Medium_Count");
		inputAttributeNames3.add("High_Count");
		inputAttributeNames3.add("Sprint_Count");

		List<String> outputAttributeNames3 = new ArrayList<String>();
		outputAttributeNames3.add(ATTRIBUTE_second);
		outputAttributeNames3.add(SoccerGameAttributes.V);
		outputAttributeNames3.add("Standing_Count");
		outputAttributeNames3.add("Trot_Count");
		outputAttributeNames3.add("Low_Count");
		outputAttributeNames3.add("Medium_Count");
		outputAttributeNames3.add("High_Count");
		outputAttributeNames3.add("Sprint_Count");

		AggregateAO aggregate3 = OperatorBuildHelper.createAggregateAO(
				functions3, null, inputAttributeNames3, outputAttributeNames3,
				null, timeWindow3);
		allOperators.add(aggregate3);

		// 4.6.2 Statemap
		List<SDFExpressionParameter> statemapExpressions3 = new ArrayList<SDFExpressionParameter>();
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_second, divideSpeedStateMapAO));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"eif("+ SoccerGameAttributes.V + "<= "+ STANDING_SPEED_UPPERBORDER + ", \"Standing\", eif("+ SoccerGameAttributes.V + " > "+ STANDING_SPEED_UPPERBORDER + " AND "+ SoccerGameAttributes.V + " <= "+ TROT_SPEED_UPPERBORDER + ", \"Trot\", eif("+ SoccerGameAttributes.V + " > "+ TROT_SPEED_UPPERBORDER + " AND "+ SoccerGameAttributes.V + " <= "+ LOW_SPEED_UPPERBORDER + ", \"Low\", eif("+ SoccerGameAttributes.V + " > "+ LOW_SPEED_UPPERBORDER + " AND "+ SoccerGameAttributes.V + " <= "+ MEDIUM_SPEED_UPPERBORDER + ", \"Medium\", eif("+ SoccerGameAttributes.V + " > "+ MEDIUM_SPEED_UPPERBORDER + " AND "+ SoccerGameAttributes.V + " <="+ HIGH_SPEED_UPPERBORDER + ", \"High\", \"Sprint\")))))", 
				"Classification",
						divideSpeedStateMapAO));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"__last_1." + SoccerGameAttributes.X,
				"Start_x", divideSpeedStateMapAO));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"__last_1." + SoccerGameAttributes.Y,
				"Start_y", divideSpeedStateMapAO));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"__last_1." + SoccerGameAttributes.X,
				"End_x", divideSpeedStateMapAO));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"__last_1." + SoccerGameAttributes.Y,
				"End_y", divideSpeedStateMapAO));

		StateMapAO pathStateMapAO = OperatorBuildHelper.createStateMapAO(
				statemapExpressions3, "", divideSpeedStateMapAO);
		allOperators.add(pathStateMapAO);
		
		// 5. Finish
		return OperatorBuildHelper.finishQuery(pathStateMapAO,
				allOperators, sportsQL.getName());
	}

}
