package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
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

	@SuppressWarnings("unused")
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {
		///List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		// TODO Get Source.

		int entityId = 7;
		int ballDatarate = 100;
		double accuracy = 300.0;
		double proximity_to_ball = 1000.0;

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

		ILogicalOperator source = null;

		SportsQLTimeParameter timeParameter = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);

		//Filter by Time
		ILogicalOperator selectedTime = OperatorBuildHelper.createTimeSelect(
				timeParameter, source);

		//Filter by Space
		ILogicalOperator selectedSpace = OperatorBuildHelper.createSpaceSelect(
				spaceParameter, selectedTime);

		//TODO get Metadata.
		ILogicalOperator enrichedStream = OperatorBuildHelper.createEnrichAO("sid=sensorid", selectedSpace,null);
		
		// /balls_filtered =
		// SELECT({predicate='entity = "ball"'},soccergame_after_start)
		ILogicalOperator ballsFiltered = OperatorBuildHelper.createEntitySelectByName("ball", enrichedStream);
		

		/// ball_sample = SAMPLE({datarate=${datarate}},balls_filtered)
		ILogicalOperator sampledBall = OperatorBuildHelper.createSampleAO(
				ballDatarate, ballsFiltered);

		// / MAP({EXPRESSIONS = [
		// /['ToPoint(x,y,z)','ball_pos']
		// /]}, sampled_ball)
		ArrayList<SDFExpressionParameter> parameterList = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter toPointParameter = OperatorBuildHelper
				.createExpressionParameter("ToPoint(x,y,z)", "ball_pos");
		parameterList.add(toPointParameter);
		ILogicalOperator ballpos = OperatorBuildHelper.createMapAO(
				parameterList, sampledBall);

		
		// player_stream = SELECT({predicate='entity_id = ${entity_id}'}, enriched_stream)
		ILogicalOperator playerStream = OperatorBuildHelper.createEntitySelect(entityId, enrichedStream);
		
		// /player_stream_changes = CHANGEDETECT({ATTR = ['x','y'],TOLERANCE =
				// ${accuracy}}, player_stream)
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("x");
		attributes.add("y");
		ILogicalOperator playerStreamChanges = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes), accuracy, playerStream);
		
		// /player_pos = MAP({EXPRESSIONS =
		// ['entity_id','sid','team_id','entity','x','y',['ToPoint(x,y,z)','player_pos']]},player_stream_changes)

		// /player_window = WINDOW({SIZE = 1, TYPE = 'TUPLE',ADVANCE = 1},
		// player_pos)
		// /ball_window = WINDOW({SIZE = 1, TYPE = 'TUPLE',ADVANCE = 1},
		// ballpos)

		// /out =
		// JOIN({PREDICATE='SpatialDistance(player_pos,ball_pos)<${proximity_to_ball}'},player_window,ball_window)
		
		return null;
	}

}
