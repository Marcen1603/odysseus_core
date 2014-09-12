package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Voyage;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.MSG_IVEF;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VoyageData;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECLeg;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECManual;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECPosition;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRouteInfo;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECSchedule;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECScheduleElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECSchedules;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECWaypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECWaypoints;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.GeometryType;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECExtensionTypes.PitchExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECExtensionTypes.RouteInfoExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECExtensionTypes.RpmExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.Pitch;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.Rpm;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionPlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionPoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteState;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Waypoint;

public class ToIECConverterHelper {

	private static final Logger LOG = LoggerFactory
			.getLogger(ToIECConverterHelper.class);

	public static IECRoute convertJSONShipRouteToIEC(RouteDataItem routeDataItem) {
		// get received objects
		Route receivedRoute = routeDataItem.getRoute();
		List<Waypoint> receivedWaypoints = routeDataItem.getRoute()
				.getWaypoints();

		IECRoute iec = new IECRoute();
		iec.setVersion("1.0"); // version is mandatory
		// Create route info
		IECRouteInfo routeInfo = new IECRouteInfo();
		if (receivedRoute.getRoute_label() != null)
			routeInfo.setRouteName(receivedRoute.getRoute_label());
		if (receivedRoute.getSignature() != null)
			routeInfo.setRouteAuthor(receivedRoute.getBaseSignature());
		if (receivedRoute.getRoute_ID() != null)
			routeInfo.setVesselVoyage(receivedRoute.getRoute_ID());
		// put route state into extension, because IEC do not provide more
		// than one value
		if (receivedRoute.getRoute_state() != null) {
			RouteState routeState = receivedRoute.getRoute_state();
			if (routeState.getHas_alarms() != null
					|| routeState.getHas_cautions() != null
					|| routeState.getHas_geometric_problems() != null
					|| routeState.getHas_warnings() != null) {
				IECExtension routeInfoExtension = new IECExtension();
				routeInfoExtension.setManufacturer(RouteInfoExtension
						.getManufacturer());
				routeInfoExtension.setName(RouteInfoExtension
						.getExtensionName());

				if (routeState.getHas_alarms() != null)
					routeInfoExtension.addExtensionValue(
							RouteInfoExtension.has_alarms.toString(),
							routeState.getHas_alarms());
				if (routeState.getHas_cautions() != null)
					routeInfoExtension.addExtensionValue(
							RouteInfoExtension.has_cautions.toString(),
							routeState.getHas_cautions());
				if (routeState.getHas_geometric_problems() != null)
					routeInfoExtension.addExtensionValue(
							RouteInfoExtension.has_geometric_problems
									.toString(), routeState
									.getHas_geometric_problems());
				if (routeState.getHas_warnings() != null)
					routeInfoExtension.addExtensionValue(
							RouteInfoExtension.has_warnings.toString(),
							routeState.getHas_warnings());

				routeInfo.addExtension(routeInfoExtension);
			}
		}
		iec.setRouteInfo(routeInfo);

		// waypoints
		IECWaypoints iecWaypoints = new IECWaypoints();
		IECSchedules iecSchedules = new IECSchedules();
		IECSchedule iecSchedule = new IECSchedule();
		iecSchedule.setId(-1); // Id is mandatory
		IECManual iecManual = new IECManual();
		iecSchedule.setManual(iecManual);

		for (Waypoint receivedWaypoint : receivedWaypoints) {
			// IEC waypoint and leg
			IECWaypoint iecWaypoint = new IECWaypoint();
			if (receivedWaypoint.getID() != null)
				iecWaypoint.setId(receivedWaypoint.getID());
			if (receivedWaypoint.getLabel() != null)
				iecWaypoint.setName(receivedWaypoint.getLabel());
			if (receivedWaypoint.getTurnradius_nm() != null)
				iecWaypoint.setRadius(receivedWaypoint.getTurnradius_nm());
			if (receivedWaypoint.getLat_rad() != null
					&& receivedWaypoint.getLon_rad() != null) {
				IECPosition iecPosition = new IECPosition();
				iecPosition.setLatitude(Math.toDegrees(receivedWaypoint
						.getLat_rad()));
				iecPosition.setLongitude(Math.toDegrees(receivedWaypoint
						.getLon_rad()));
				iecWaypoint.setPosition(iecPosition);
			}

			IECLeg iecLeg = new IECLeg();
			if (receivedWaypoint.getCrosstracklimit_stbd_m() != null)
				iecLeg.setStarboardXTD(Double.valueOf(receivedWaypoint
						.getCrosstracklimit_stbd_m()));
			if (receivedWaypoint.getCrosstracklimit_port_m() != null)
				iecLeg.setPortsideXTD(Double.valueOf(receivedWaypoint
						.getCrosstracklimit_port_m()));
			iecLeg.setGeometryType(GeometryType.Orthodrome);
			if (receivedWaypoint.getSpeedlimit_lower_kts() != null)
				iecLeg.setSpeedMin(receivedWaypoint.getSpeedlimit_lower_kts());
			if (receivedWaypoint.getSpeedlimit_upper_kts() != null)
				iecLeg.setSpeedMax(receivedWaypoint.getSpeedlimit_upper_kts());
			iecWaypoint.setLeg(iecLeg);
			iecWaypoints.addwaypoint(iecWaypoint);

			// IEC Schedule
			IECScheduleElement iecScheduleElement = new IECScheduleElement();
			if (receivedWaypoint.getID() != null)
				iecScheduleElement.setWaypointID(receivedWaypoint.getID());
			if (receivedWaypoint.getETA() != null)
				iecScheduleElement.setEta(new Date(
						receivedWaypoint.getETA() * 1000));
			if (receivedWaypoint.getSpeed_kts() != null)
				iecScheduleElement.setSpeed(receivedWaypoint.getSpeed_kts());
			iecManual.addScheduleElement(iecScheduleElement);
		}

		iecSchedules.addSchedule(iecSchedule);
		iec.setSchedules(iecSchedules);
		iec.setWaypoints(iecWaypoints);
		if (iec.isValid()) {
			return iec;
		} else {
			LOG.debug("IEC Element is invalid => not processed");
			return null;
		}
	}

	public static IECRoute convertJSONPredictionToIEC(
			PredictionDataItem predictionDataItem) {
		PredictionPlan receivedPredictionPlan = predictionDataItem.getMplan();
		List<PredictionPoint> receivedPredictionPoints = receivedPredictionPlan
				.getPred_points();

		IECRoute iec = new IECRoute();
		iec.setVersion("1.0"); // version is mandatory

		// add route Info because it is also mandatory
		IECRouteInfo iecRouteInfo = new IECRouteInfo();
		iecRouteInfo.setRouteName("Prediction");
		iec.setRouteInfo(iecRouteInfo);

		// waypoints
		IECWaypoints iecWaypoints = new IECWaypoints();
		IECSchedules iecSchedules = new IECSchedules();
		IECSchedule iecSchedule = new IECSchedule();
		iecSchedule.setId(-1); // Id is mandatory
		IECManual iecManual = new IECManual();
		iecSchedule.setManual(iecManual);

		for (PredictionPoint receivedPPoint : receivedPredictionPoints) {
			// IEC waypoint and leg
			IECWaypoint iecWaypoint = new IECWaypoint();
			if (receivedPPoint.getID() != null)
				iecWaypoint.setId(receivedPPoint.getID());
			if (receivedPPoint.getHeading_rad() != null)
				iecWaypoint.setRadius(receivedPPoint.getHeading_rad());
			if (receivedPPoint.getLat_rad() != null
					&& receivedPPoint.getLon_rad() != null) {
				IECPosition iecPosition = new IECPosition();
				iecPosition.setLatitude(Math.toDegrees(receivedPPoint
						.getLat_rad()));
				iecPosition.setLongitude(Math.toDegrees(receivedPPoint
						.getLon_rad()));
				iecWaypoint.setPosition(iecPosition);
			}

			IECLeg iecLeg = new IECLeg();
			iecLeg.setGeometryType(GeometryType.Orthodrome);

			iecWaypoint.setLeg(iecLeg);
			iecWaypoints.addwaypoint(iecWaypoint);

			// IEC Schedule
			IECScheduleElement iecScheduleElement = new IECScheduleElement();
			if (receivedPPoint.getID() != null)
				iecScheduleElement.setWaypointID(receivedPPoint.getID());
			iecManual.addScheduleElement(iecScheduleElement);
		}

		iecSchedules.addSchedule(iecSchedule);
		iec.setSchedules(iecSchedules);
		iec.setWaypoints(iecWaypoints);

		if (iec.isValid()) {
			return iec;
		} else {
			LOG.debug("IEC Element is invalid => not processed");
			return null;
		}
	}

	public static IECRoute convertJSONManoeuvreToIEC(
			ManoeuvrePlanDataItem manoeuvrePlanDataItem) {
		ManoeuvrePlan receivedMPlan = manoeuvrePlanDataItem.getMplan();
		List<ManoeuvrePoint> receivedManoeuvrePoints = receivedMPlan
				.getMpoints();

		IECRoute iec = new IECRoute();
		iec.setVersion("1.0"); // version is mandatory

		// Create route info
		IECRouteInfo routeInfo = new IECRouteInfo();
		if (receivedMPlan.getMplan_label() != null)
			routeInfo.setRouteName(receivedMPlan.getMplan_label());
		if (receivedMPlan.getMplan_ID() != null)
			routeInfo.setVesselVoyage(receivedMPlan.getMplan_ID());
		iec.setRouteInfo(routeInfo);

		// waypoints
		IECWaypoints iecWaypoints = new IECWaypoints();
		IECSchedules iecSchedules = new IECSchedules();
		IECSchedule iecSchedule = new IECSchedule();
		iecSchedule.setId(-1); // Id is mandatory
		IECManual iecManual = new IECManual();
		iecSchedule.setManual(iecManual);

		for (ManoeuvrePoint receivedMPoint : receivedManoeuvrePoints) {
			// IEC Waypoint
			IECWaypoint iecWaypoint = new IECWaypoint();
			if (receivedMPoint.getID() != null)
				iecWaypoint.setId(receivedMPoint.getID());
			if (receivedMPoint.getLabel() != null)
				iecWaypoint.setName(receivedMPoint.getLabel());
			if (receivedMPoint.getHeading_rad() != null)
				iecWaypoint.setRadius(receivedMPoint.getHeading_rad());
			if (receivedMPoint.getLat_rad() != null
					&& receivedMPoint.getLon_rad() != null) {
				IECPosition iecPosition = new IECPosition();
				iecPosition.setLatitude(Math.toDegrees(receivedMPoint
						.getLat_rad()));
				iecPosition.setLongitude(Math.toDegrees(receivedMPoint
						.getLon_rad()));
				iecWaypoint.setPosition(iecPosition);
			}

			// IEC Leg
			IECLeg iecLeg = new IECLeg();
			iecLeg.setGeometryType(GeometryType.Orthodrome);
			iecWaypoint.setLeg(iecLeg);
			iecWaypoints.addwaypoint(iecWaypoint);

			// IEC Schedule
			IECScheduleElement iecScheduleElement = new IECScheduleElement();
			if (receivedMPoint.getID() != null)
				iecScheduleElement.setWaypointID(receivedMPoint.getID());
			if (receivedMPoint.getWind_speed_kts() != null)
				iecScheduleElement.setWindSpeed(receivedMPoint
						.getWind_speed_kts());
			if (receivedMPoint.getWind_dir_rad() != null)
				iecScheduleElement.setWindDirection(Math
						.toDegrees(receivedMPoint.getWind_dir_rad()));
			if (receivedMPoint.getSog_long_kts() != null)
				iecScheduleElement.setSpeed(receivedMPoint
						.getSog_long_kts());
			if (receivedMPoint.getCurrent_dir_rad() != null)
				iecScheduleElement.setCurrentDirection(Math
						.toDegrees(receivedMPoint.getCurrent_dir_rad()));

			// put rpm command into extension, because IEC do not provide more
			// than one value
			if (receivedMPoint.getRpm_command() != null) {
				Rpm rpm_command = receivedMPoint.getRpm_command();

				if (rpm_command.getRpm1_cmd_rpm() != null
						|| rpm_command.getRpm2_cmd_rpm() != null
						|| rpm_command.getRpm3_cmd_rpm() != null
						|| rpm_command.getRpm4_cmd_rpm() != null) {
					IECExtension rpmExtension = new IECExtension();
					rpmExtension
							.setManufacturer(RpmExtension.getManufacturer());
					rpmExtension.setName(RpmExtension.getExtensionName());
					if (rpm_command.getRpm1_cmd_rpm() != null)
						iecScheduleElement
								.setRpm(rpm_command.getRpm1_cmd_rpm());
					if (rpm_command.getRpm2_cmd_rpm() != null)
						rpmExtension.addExtensionValue(
								RpmExtension.rpm2_cmd.toString(),
								rpm_command.getRpm2_cmd_rpm());
					if (rpm_command.getRpm3_cmd_rpm() != null)
						rpmExtension.addExtensionValue(
								RpmExtension.rpm3_cmd.toString(),
								rpm_command.getRpm3_cmd_rpm());
					if (rpm_command.getRpm4_cmd_rpm() != null)
						rpmExtension.addExtensionValue(
								RpmExtension.rpm4_cmd.toString(),
								rpm_command.getRpm4_cmd_rpm());
					iecScheduleElement.addExtension(rpmExtension);
				}
			}

			// put pitch command into extension, because IEC do not provide
			// more than one value
			if (receivedMPoint.getPitch_command() != null) {
				Pitch pitch_command = receivedMPoint.getPitch_command();

				if (pitch_command.getPitch1_cmd_perc() != null
						|| pitch_command.getPitch2_cmd_perc() != null
						|| pitch_command.getPitch3_cmd_perc() != null
						|| pitch_command.getPitch4_cmd_perc() != null) {
					IECExtension rpmExtension = new IECExtension();
					rpmExtension.setManufacturer(PitchExtension
							.getManufacturer());
					rpmExtension.setName(PitchExtension.getExtensionName());
					if (pitch_command.getPitch1_cmd_perc() != null) {

					}
					iecScheduleElement.setPitch(pitch_command
							.getPitch1_cmd_perc().intValue());
					if (pitch_command.getPitch2_cmd_perc() != null)
						rpmExtension.addExtensionValue(
								PitchExtension.pitch2_cmd.toString(),
								pitch_command.getPitch2_cmd_perc());
					if (pitch_command.getPitch3_cmd_perc() != null)
						rpmExtension.addExtensionValue(
								PitchExtension.pitch3_cmd.toString(),
								pitch_command.getPitch3_cmd_perc());
					if (pitch_command.getPitch4_cmd_perc() != null)
						rpmExtension.addExtensionValue(
								PitchExtension.pitch4_cmd.toString(),
								pitch_command.getPitch4_cmd_perc());
					iecScheduleElement.addExtension(rpmExtension);
				}
			}

			iecManual.addScheduleElement(iecScheduleElement);
		}

		iecSchedules.addSchedule(iecSchedule);
		iec.setSchedules(iecSchedules);
		iec.setWaypoints(iecWaypoints);

		if (iec.isValid()) {
			return iec;
		} else {
			LOG.debug("IEC Element is invalid => not processed");
			return null;
		}
	}

	public static List<IECRoute> convertIVEF015ToIEC(MSG_VesselData msg_VesselData) {
		Map<Integer, IECRoute> iecRoutes = new HashMap<Integer, IECRoute>();
		int countOfVesselDatas = msg_VesselData.getBody().countOfVesselDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfVesselDatas; i++) {
			VesselData vesselData = msg_VesselData.getBody().getVesselDataAt(i);
			if (vesselData.getPosReport() != null) {
				IECRoute iecRoute = null;
				if (iecRoutes.containsKey(vesselData.getPosReport().getId())) {
					iecRoute = iecRoutes.get(vesselData.getPosReport().getId());
				} else {
					iecRoute = new IECRoute();
					iecRoute.setVersion("1.0");
					IECRouteInfo iecRouteInfo = new IECRouteInfo();
					if (vesselData.countOfStaticDatas() > 0) {
						StaticData staticData = vesselData.getStaticDataAt(0);
						iecRouteInfo.setRouteName(staticData.getShipName()
								+ "_route");
						iecRouteInfo.setVesselName(staticData.getShipName());
						iecRouteInfo.setVesselMMSI(staticData.getMMSI()
								.intValue()); // Long?
						iecRouteInfo.setVesselIMO(staticData.getIMO()
								.intValue()); // Long?
					}
					iecRoute.setRouteInfo(iecRouteInfo);

					IECWaypoints iecWaypoints = new IECWaypoints();
					IECSchedules iecSchedules = new IECSchedules();
					IECSchedule iecSchedule = new IECSchedule();
					iecSchedule.setId(-1); // Id is mandatory
					IECManual iecManual = new IECManual();
					iecSchedule.setManual(iecManual);
					iecSchedules.addSchedule(iecSchedule);
					iecRoute.setSchedules(iecSchedules);
					iecRoute.setWaypoints(iecWaypoints);

					iecRoutes.put(vesselData.getPosReport().getId(), iecRoute);
				}

				IECWaypoint iecWaypoint = new IECWaypoint();
				iecWaypoint.setId(idCounter);
				if (vesselData.getPosReport().getPos() != null) {
					Pos pos = vesselData.getPosReport().getPos();
					IECPosition iecPosition = new IECPosition();
					iecPosition.setLatitude(pos.getLat());
					iecPosition.setLongitude(pos.getLong());
					iecWaypoint.setPosition(iecPosition);
				}

				IECLeg iecLeg = new IECLeg();
				iecLeg.setGeometryType(GeometryType.Orthodrome);
				if (vesselData.countOfVoyages() > 0) {
					Voyage voyage = vesselData.getVoyageAt(0);
					iecLeg.setDraughtForward(voyage.getDraught());
					iecLeg.setDraughtAft(voyage.getDraught());
				}
				iecWaypoint.setLeg(iecLeg);
				iecRoute.getWaypoints().addwaypoint(iecWaypoint);

				IECScheduleElement iecScheduleElement = new IECScheduleElement();
				iecScheduleElement.setWaypointID(idCounter);
				iecScheduleElement.setSpeed(vesselData.getPosReport().getSOG());
				iecScheduleElement.setCurrentDirection(vesselData
						.getPosReport().getCOG());
				iecRoute.getSchedules().getSchedules().get(0).getManual()
						.addScheduleElement(iecScheduleElement);
				idCounter++;
			}
		}

		return new ArrayList<IECRoute>(iecRoutes.values());
	}

	public static List<IECRoute> convertIVEF025ToIEC(MSG_IVEF msg_VesselData) {
		Map<Integer, IECRoute> iecRoutes = new HashMap<Integer, IECRoute>();
		int countOfObjectDatas = msg_VesselData.getBody().getObjectDatas().countOfObjectDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfObjectDatas; i++) {
			ObjectData objectData = msg_VesselData.getBody().getObjectDatas().getObjectDataAt(i);
			if (objectData.getTrackData() != null) {
				IECRoute iecRoute = null;
				if (iecRoutes.containsKey(objectData.getTrackData().getId())) {
					iecRoute = iecRoutes.get(objectData.getTrackData().getId());
				} else {
					iecRoute = new IECRoute();
					iecRoute.setVersion("1.0");
					IECRouteInfo iecRouteInfo = new IECRouteInfo();
					if (objectData.countOfVesselDatas() > 0) {
						de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VesselData vesselData = objectData.getVesselDataAt(0);
						if (vesselData.getIdentifier() != null){
							iecRouteInfo.setRouteName(vesselData.getIdentifier().getName()
									+ "_route");
							iecRouteInfo.setVesselName(vesselData.getIdentifier().getName());
							iecRouteInfo.setVesselMMSI(vesselData.getIdentifier().getMMSI()); 
							iecRouteInfo.setVesselIMO(vesselData.getIdentifier().getIMO());					
						}
						
					}
					iecRoute.setRouteInfo(iecRouteInfo);

					IECWaypoints iecWaypoints = new IECWaypoints();
					IECSchedules iecSchedules = new IECSchedules();
					IECSchedule iecSchedule = new IECSchedule();
					iecSchedule.setId(-1); // Id is mandatory
					IECManual iecManual = new IECManual();
					iecSchedule.setManual(iecManual);
					iecSchedules.addSchedule(iecSchedule);
					iecRoute.setSchedules(iecSchedules);
					iecRoute.setWaypoints(iecWaypoints);

					iecRoutes.put(objectData.getTrackData().getId(), iecRoute);
				}

				IECWaypoint iecWaypoint = new IECWaypoint();
				iecWaypoint.setId(idCounter);
				if (objectData.getTrackData().countOfPoss() > 0) {
					de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Pos pos = objectData.getTrackData().getPosAt(0);
					IECPosition iecPosition = new IECPosition();
					iecPosition.setLatitude(pos.getLat());
					iecPosition.setLongitude(pos.getLong());
					iecWaypoint.setPosition(iecPosition);
				}

				IECLeg iecLeg = new IECLeg();
				iecLeg.setGeometryType(GeometryType.Orthodrome);
				if (objectData.countOfVoyageDatas() > 0) {
					VoyageData voyageData = objectData.getVoyageDataAt(0);
					iecLeg.setDraughtForward(voyageData.getDraught());
					iecLeg.setDraughtAft(voyageData.getDraught());
				}
				iecWaypoint.setLeg(iecLeg);
				iecRoute.getWaypoints().addwaypoint(iecWaypoint);

				IECScheduleElement iecScheduleElement = new IECScheduleElement();
				iecScheduleElement.setWaypointID(idCounter);
				iecScheduleElement.setSpeed(objectData.getTrackData().getSOG());
				iecScheduleElement.setCurrentDirection(objectData
						.getTrackData().getCOG());
				iecRoute.getSchedules().getSchedules().get(0).getManual()
						.addScheduleElement(iecScheduleElement);
				idCounter++;
			}
		}

		return new ArrayList<IECRoute>(iecRoutes.values());
	}
}
