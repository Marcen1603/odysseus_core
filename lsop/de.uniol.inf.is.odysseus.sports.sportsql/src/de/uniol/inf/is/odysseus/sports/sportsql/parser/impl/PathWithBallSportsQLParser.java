package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.PLAYER }, name = "pathwithball")
public class PathWithBallSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) throws SportsQLParseException {
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		String metadataSourceName = "metadata";
		String streamSourceName = "soccergame";
		
		long entityId = sportsQL.getEntityId();
		
		//Set defaults for parameters.
		int ballDatarate = 100;
		double accuracy = 300.0;
		double proximityToBall = 1000.0;
		
		//Set parameters when in sportsQL
		Map<String,String> parameters = sportsQL.getParameters();
		if(parameters.containsKey("datarate")) {
			try {
				ballDatarate = Integer.parseInt(parameters.get("datarate"));
			}
			catch (Exception e){
				throw new SportsQLParseException("Illegal value for datarate.");
			}
		}
		
		if(parameters.containsKey("accuracy")) {
			try {
				accuracy = Double.parseDouble(parameters.get("accuracy"));
			}
			catch (Exception e){
				throw new SportsQLParseException("Illegal value for accuracy (needs to be double!).");
			}
		}
		
		if(parameters.containsKey("proximity")) {
			try {
				proximityToBall = Double.parseDouble(parameters.get("proximity"));
			}
			catch (Exception e){
				throw new SportsQLParseException("Illegal value for proximity (needs to be double!).");
			}
		}
		
		
		
/*
		int upperleft_x = 0;
		int upperleft_y = 33965;
		int lowerleft_x = -50;
		int lowerleft_y = -33960;
		int upperright_x = 52477;
		int upperright_y = 33941;
		int lowerright_x = 52489;
		int lowerright_y = -33939;

		int min_x = lowerleft_x;
		int max_x = lowerright_x;
		int min_y = lowerleft_y;
		int max_y = upperleft_y;
*/
		
		
		ILogicalOperator source = OperatorBuildHelper.createAccessAO(streamSourceName);
		ILogicalOperator metadata =OperatorBuildHelper.createAccessAO(metadataSourceName);

		SportsQLTimeParameter timeParameter = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);

		//Filter by Time
		ILogicalOperator selectedTime = OperatorBuildHelper.createTimeSelect(
				timeParameter, source);
		allOperators.add(selectedTime);

		//Filter by Space
		ILogicalOperator selectedSpace = OperatorBuildHelper.createSpaceSelect(
				spaceParameter, selectedTime);
		allOperators.add(selectedSpace);

		ILogicalOperator enrichedStream = OperatorBuildHelper.createEnrichAO("sid=sensorid", selectedSpace,metadata);
		allOperators.add(enrichedStream);
		
		// /balls_filtered =
		// SELECT({predicate='entity = "ball"'},soccergame_after_start)
		ILogicalOperator ballsFiltered = OperatorBuildHelper.createEntitySelectByName("ball", enrichedStream);
		allOperators.add(ballsFiltered);

		/// ball_sample = SAMPLE({datarate=${datarate}},balls_filtered)
		ILogicalOperator sampledBall = OperatorBuildHelper.createSampleAO(
				ballDatarate, ballsFiltered);
		allOperators.add(sampledBall);
		
		// / MAP({EXPRESSIONS = [
		// /['ToPoint(x,y,z)','ball_pos']
		// /]}, sampled_ball)
		ArrayList<SDFExpressionParameter> parameterList = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter toPointParameter = OperatorBuildHelper
				.createExpressionParameter("ToPoint(x,y,z)", "ball_pos");
		parameterList.add(toPointParameter);
		ILogicalOperator ballpos = OperatorBuildHelper.createMapAO(
				parameterList, sampledBall, 0, 0);
		allOperators.add(ballpos);
		
		// player_stream = SELECT({predicate='entity_id = ${entity_id}'}, enriched_stream)
		ILogicalOperator playerStream = OperatorBuildHelper.createEntitySelect(entityId, enrichedStream);
		allOperators.add(playerStream);
		
		// /player_stream_changes = CHANGEDETECT({ATTR = ['x','y'],TOLERANCE =
				// ${accuracy}}, player_stream)
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("x");
		attributes.add("y");
		ILogicalOperator playerStreamChanges = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes), accuracy, playerStream);
		allOperators.add(playerStreamChanges);
		
		// /player_pos = MAP({EXPRESSIONS =
		// ['entity_id','sid','team_id','entity','x','y',['ToPoint(x,y,z)','player_pos']]},player_stream_changes)

		SDFExpressionParameter entIdParameter = OperatorBuildHelper
				.createExpressionParameter("entity_id", "entity_id");
		//Only needed to show entity on Map.
		SDFExpressionParameter sidParameter = OperatorBuildHelper
				.createExpressionParameter("sid", "sid");
		SDFExpressionParameter teamIdParameter = OperatorBuildHelper
				.createExpressionParameter("team_id", "team_id");
		SDFExpressionParameter entityParamter = OperatorBuildHelper
				.createExpressionParameter("entity", "entity");
		SDFExpressionParameter xParameter = OperatorBuildHelper
				.createExpressionParameter("x", "x");
		SDFExpressionParameter yParameter = OperatorBuildHelper
				.createExpressionParameter("y", "y");
		SDFExpressionParameter playerPosParameter = OperatorBuildHelper
				.createExpressionParameter("ToPoint(x,y,z)", "player_pos");
		ArrayList<SDFExpressionParameter> positionParameterList = new ArrayList<SDFExpressionParameter>();
		
		positionParameterList.add(entIdParameter);
		positionParameterList.add(sidParameter);
		positionParameterList.add(teamIdParameter);
		positionParameterList.add(entityParamter);
		positionParameterList.add(xParameter);
		positionParameterList.add(yParameter);
		positionParameterList.add(playerPosParameter);
		
		ILogicalOperator playerPosition = OperatorBuildHelper.createMapAO(positionParameterList, playerStreamChanges, 0, 0);
		allOperators.add(playerPosition);
		
		// /player_window = WINDOW({SIZE = 1, TYPE = 'TUPLE',ADVANCE = 1},
				// player_pos)
		ILogicalOperator playerWindow = OperatorBuildHelper.createTupleWindowAO(1, 1, playerPosition);
		allOperators.add(playerWindow);
		
		// /ball_window = WINDOW({SIZE = 1, TYPE = 'TUPLE',ADVANCE = 1},
		// ballpos)
		ILogicalOperator ballWindow = OperatorBuildHelper.createTupleWindowAO(1,1,ballpos);
		allOperators.add(ballWindow);
		
		// /out =
		// JOIN({PREDICATE='SpatialDistance(player_pos,ball_pos)<${proximity_to_ball}'},player_window,ball_window)
		ArrayList<String> joinPredicates = new ArrayList<String>();
		joinPredicates.add("SpatialDistance(player_pos,ball_pos)<" + proximityToBall);
		ILogicalOperator result = OperatorBuildHelper.createJoinAO(joinPredicates, playerWindow, ballWindow);
		allOperators.add(result);
		
		// Initialize all AOs
		OperatorBuildHelper.initializeOperators(allOperators);

		// Create plan
		ILogicalQuery query = new LogicalQuery();
		query.setLogicalPlan(result, true);
		
		return query;
	}
	

}
