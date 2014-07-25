package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;

public class ShotOnGoalPlayerSportsQLParser implements ISportsQLParser {

	@SuppressWarnings("unused")
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
		List<SDFAttribute> changeAttributes = new ArrayList<SDFAttribute>();
		// SDFAttribute accelerationAttribute = new SDFAttribute(toCopyFrom,
		// newDatatype)
		ChangeDetectAO accelerationChanged = OperatorBuildHelper
				.createChangeDetectAO(changeAttributes, 0, accelerationMap);

		return null;
	}
}
