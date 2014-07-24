package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLIntegerParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLDoubleParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Parser for SportsQL:
 * Query: Path with Ball.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * {
 * "statisticType": "player",
 *   "gameType": "soccer",
 *   "entityId": 16,
 *   "name": "pathwithball",
 *   "parameters": {
 *       "time": {
 *           "start": 10753295594424116,
 *           "end" : 9999999999999999,   
 *       }
 *    "space": {
 *      	"startx":-50,
 *          "starty":-33960
 *      	"endx":52489
 *     		"endy":33965
 *   }
 *  }
 *}
 * 
 * @author Carsten Cordes
 *
 */
@SportsQL(
		gameTypes = { GameType.SOCCER }, 
		statisticTypes = { StatisticType.PLAYER },
		name = "pathwithball",
		parameters = { 
				@SportsQLParameter(name = "datarate", parameterClass = SportsQLIntegerParameter.class, mandatory = false),
				@SportsQLParameter(name = "accuracy", parameterClass = SportsQLDoubleParameter.class, mandatory = false),
				@SportsQLParameter(name = "proximity", parameterClass = SportsQLDoubleParameter.class, mandatory = false),
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
				@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false)
				}				
		)

public class PathWithBallSportsQLParser implements ISportsQLParser {

	@Override
	
	/**
	 * Parses SportsQL 
	 * @Parameter sportsQL Query to parse.
	 * @throws SportsQLParseException
	 */
	public ILogicalQuery parse(SportsQLQuery sportsQL) throws SportsQLParseException {
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		//TODO remove hardcoded information.
		
		/**
		 * Source name of Metadata Stream.
		 */
		String metadataSourceName = "metadata";
		/**
		 * Source name of Game Stream.
		 */
		String streamSourceName = "soccergame";
		
		/**
		 * Entity id we look for.
		 */
		long entityId = sportsQL.getEntityId();
		
		
		/**
		 * Datarate to reduce ball load (only ever n'th tuple is regarded)
		 * Default: 100 (Can be set with optional parameter 'datarate')
		 */
		int ballDatarate = 100;
		
		/**
		 * Accuracy (in mm): To reduce load. Movements with distance < accuracy are not processed further.
		 * Default: 300 (Can be set with optional parameter 'accuracy')
		 */
		double accuracy = 300.0;
		
		/**
		 * Proximity to ball (in mm): Which distance counts as ball possession?
		 * Default: 1000 (Can be set with optional parameter 'proximity')
		 */
		double proximityToBall = 1000.0;
		
		//Set parameters when in sportsQL
		
		Map<String,ISportsQLParameter> parameters = sportsQL.getParameters();
		if(parameters.containsKey("datarate")) {
			try {
				ballDatarate = ((SportsQLIntegerParameter)parameters.get("datarate")).getValue();
			}
			catch (Exception e){
				throw new SportsQLParseException("Illegal value for datarate.");
			}
		}
		
		if(parameters.containsKey("accuracy")) {
			try {
				accuracy = ((SportsQLDoubleParameter)parameters.get("accuracy")).getValue();
			}
			catch (Exception e){
				throw new SportsQLParseException("Illegal value for accuracy (needs to be double!).");
			}
		}
		
		if(parameters.containsKey("proximity")) {
			try {
				proximityToBall = ((SportsQLDoubleParameter)parameters.get("proximity")).getValue();
			}
			catch (Exception e){
				throw new SportsQLParseException("Illegal value for proximity (needs to be double!).");
			}
		}
		
		//Fetch time and Space parameters from Query.
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper.getTimeParameter(sportsQL);;
		SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper.getSpaceParameter(sportsQL);;

		
		///Beginning of QueryPlan.
		
		//Create AccessAOs for sources.
		ILogicalOperator source = OperatorBuildHelper.createAccessAO(streamSourceName);
		ILogicalOperator metadata =OperatorBuildHelper.createAccessAO(metadataSourceName);

		//Filter by Time
		ILogicalOperator selectedTime = OperatorBuildHelper.createTimeSelect(
				timeParameter, source);
		allOperators.add(selectedTime);

		//Filter by Space
		ILogicalOperator selectedSpace = OperatorBuildHelper.createSpaceSelect(
				spaceParameter, false, selectedTime);
		allOperators.add(selectedSpace);

		//Enrich game Stream with metadata.
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
				.createExpressionParameter("ToPoint(x,y,z)", "ball_pos", sampledBall);
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
		ILogicalOperator playerStreamChanges = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes,playerStream), accuracy, playerStream);
		allOperators.add(playerStreamChanges);
		
		// /player_pos = MAP({EXPRESSIONS =
		// ['entity_id','sid','team_id','entity','x','y',['ToPoint(x,y,z)','player_pos']]},player_stream_changes)

		SDFExpressionParameter entIdParameter = OperatorBuildHelper
				.createExpressionParameter("entity_id", "entity_id", playerStreamChanges);
		//Only needed to show entity on Map.
		SDFExpressionParameter sidParameter = OperatorBuildHelper
				.createExpressionParameter("sid", "sid", playerStreamChanges);
		SDFExpressionParameter teamIdParameter = OperatorBuildHelper
				.createExpressionParameter("team_id", "team_id", playerStreamChanges);
		SDFExpressionParameter entityParamter = OperatorBuildHelper
				.createExpressionParameter("entity", "entity", playerStreamChanges);
		SDFExpressionParameter xParameter = OperatorBuildHelper
				.createExpressionParameter("x", "x", playerStreamChanges);
		SDFExpressionParameter yParameter = OperatorBuildHelper
				.createExpressionParameter("y", "y", playerStreamChanges);
		SDFExpressionParameter playerPosParameter = OperatorBuildHelper
				.createExpressionParameter("ToPoint(x,y,z)", "player_pos", playerStreamChanges);
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
		
		return OperatorBuildHelper.finishQuery(result, allOperators, sportsQL.getName());
	}
	

}
