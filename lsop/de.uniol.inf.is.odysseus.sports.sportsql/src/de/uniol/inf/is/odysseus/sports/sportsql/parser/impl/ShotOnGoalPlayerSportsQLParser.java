package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;

public class ShotOnGoalPlayerSportsQLParser implements ISportsQLParser {

	@SuppressWarnings("rawtypes")
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		// TODO Add all operators to this list

		// ---------------------
		// Access to the Streams
		// ---------------------

		// GameStream and MetaDataStream
		AccessAO gameAccess = OperatorBuildHelper.createGameStreamAccessAO();
		AccessAO metaDataAccess = OperatorBuildHelper
				.createMetaStreamAccessAO();

		// -------------------------------------------------------------------
		// First part
		// Filter the sensor data stream for events on the field and enrich it
		// with the metadata stream
		// -------------------------------------------------------------------

		// 1. Select for space
		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
				spaceParam, false, gameAccess);

		// 2. Enrich with the metastream
		EnrichAO enrichedStream = OperatorBuildHelper.createEnrichAO(
				"sensorid = sid", spaceSelect, metaDataAccess);

		// -------------------------------------------------------------------
		// Second part
		// Filter the sensor data stream for ball events
		// -------------------------------------------------------------------

		// 1. Select for ball
		SelectAO ballSelect = OperatorBuildHelper.createEntitySelectByName(
				OperatorBuildHelper.BALL_ENTITY, enrichedStream);

		// 2. Project for important variables
		List<String> ballProjectList = new ArrayList<String>();
		ballProjectList.add("ts");
		ballProjectList.add("x");
		ballProjectList.add("y");
		ballProjectList.add("z");
		ballProjectList.add("a");
		ProjectAO activeBall = OperatorBuildHelper.createProjectAO(
				ballProjectList, ballSelect);

		// -------------------------------------------------------------------
		// Third part
		// Shots begin with a minimal acceleration of 55 m/s
		// -------------------------------------------------------------------

		// 1. Map for acceleration
		List<SDFExpressionParameter> mapExpressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter mapParam1 = OperatorBuildHelper
				.createExpressionParameter("ts", "shot_ts", activeBall);
		SDFExpressionParameter mapParam2 = OperatorBuildHelper
				.createExpressionParameter("x", "shot_x", activeBall);
		SDFExpressionParameter mapParam3 = OperatorBuildHelper
				.createExpressionParameter("y", "shot_y", activeBall);
		SDFExpressionParameter mapParam4 = OperatorBuildHelper
				.createExpressionParameter("z", "shot_z", activeBall);
		SDFExpressionParameter mapParam5 = OperatorBuildHelper
				.createExpressionParameter(
						"eif(a >= ${minAcceleration}, 1, 0)", "accelerated",
						activeBall);
		mapExpressions.add(mapParam1);
		mapExpressions.add(mapParam2);
		mapExpressions.add(mapParam3);
		mapExpressions.add(mapParam4);
		mapExpressions.add(mapParam5);
		MapAO accelerationMap = OperatorBuildHelper.createMapAO(mapExpressions,
				activeBall, 0, 0);

		// 2. Changedetect
		List<String> attr = new ArrayList<String>();
		attr.add("accelerated");

		List<ILogicalOperator> attrOps = new ArrayList<ILogicalOperator>();
		attrOps.add(accelerationMap);

		List<SDFAttribute> changeAttributes = OperatorBuildHelper
				.createAttributeList(attr, attrOps);
		ChangeDetectAO accelerationChanged = OperatorBuildHelper
				.createChangeDetectAO(changeAttributes, 0, true,
						accelerationMap);

		// 3. Select for accelerated tuples
		SelectAO acceleratedSelect = OperatorBuildHelper.createSelectAO(
				"accelerated = 1", accelerationChanged);
		
		// -------------------------------------------------------------------
		// Fourth part
		// Filter the sensor data stream for player feet events
		// -------------------------------------------------------------------
		
		// 1. Select for the feet
		List<IPredicate> playerSelectAndPredicates = new ArrayList<IPredicate>();
		List<IPredicate> playerSelectOrPredicates = new ArrayList<IPredicate>();
		
		RelationalPredicate firstAndPredicate = OperatorBuildHelper.createRelationalPredicate("entity != " + OperatorBuildHelper.BALL_ENTITY);
		RelationalPredicate secondAndPredicate = OperatorBuildHelper.createRelationalPredicate("entity != " + OperatorBuildHelper.REFEREE_ENTITY);
		
		RelationalPredicate leftLegOrPredicate = OperatorBuildHelper.createRelationalPredicate("remark = " + OperatorBuildHelper.LEFT_LEG_REMARK);
		RelationalPredicate rightLegOrPredicate = OperatorBuildHelper.createRelationalPredicate("remark = " + OperatorBuildHelper.RIGHT_LEG_REMARK);
		playerSelectOrPredicates.add(leftLegOrPredicate);
		playerSelectOrPredicates.add(rightLegOrPredicate);
		
		IPredicate legOrPredicate = OperatorBuildHelper.createOrPredicate(playerSelectOrPredicates);
		playerSelectAndPredicates.add(firstAndPredicate);
		playerSelectAndPredicates.add(secondAndPredicate);
		playerSelectAndPredicates.add(legOrPredicate);
		
		IPredicate legAndPredicate = OperatorBuildHelper.createAndPredicate(playerSelectAndPredicates);
		
		SelectAO playerLegSelect = OperatorBuildHelper.createSelectAO(legAndPredicate, enrichedStream);		
		
		// 2. Project
		List<String> feetProjectList = new ArrayList<String>();
		ballProjectList.add("ts");
		ballProjectList.add("x");
		ballProjectList.add("y");
		ballProjectList.add("z");
		ballProjectList.add("entity_id");
		ballProjectList.add("team_id");
		ProjectAO players = OperatorBuildHelper.createProjectAO(
				feetProjectList, playerLegSelect);
		
		return null;
	}
}
