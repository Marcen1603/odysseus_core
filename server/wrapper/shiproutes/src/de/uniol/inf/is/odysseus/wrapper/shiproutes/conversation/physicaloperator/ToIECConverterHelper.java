package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator;

import java.util.List;
import java.util.Date;

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
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Waypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.Pitch;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.Rpm;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteState;

public class ToIECConverterHelper {

	public static IECRoute convertShipRouteToIEC(RouteDataItem routeDataItem) {
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
			routeInfo.setRouteAuthor(String.valueOf(receivedRoute
					.getSignature()));
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
				routeInfoExtension.setManufacturer("OFFIS");
				routeInfoExtension.setName("EXT_AdditionalValues");

				if (routeState.getHas_alarms() != null)
					routeInfoExtension.addExtensionValue("hasAlarms",
							routeState.getHas_alarms());
				if (routeState.getHas_cautions() != null)
					routeInfoExtension.addExtensionValue("hasCautions",
							routeState.getHas_cautions());
				if (routeState.getHas_geometric_problems() != null)
					routeInfoExtension.addExtensionValue("hasGeoProblems",
							routeState.getHas_geometric_problems());
				if (routeState.getHas_warnings() != null)
					routeInfoExtension.addExtensionValue("hasWarnings",
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
				iecScheduleElement.setEta(new Date((long) receivedWaypoint
						.getETA() * 1000));
			if (receivedWaypoint.getSpeed_kts() != null)
				iecScheduleElement.setSpeed(receivedWaypoint.getSpeed_kts());
			iecManual.addScheduleElement(iecScheduleElement);
		}

		iecSchedules.addSchedule(iecSchedule);
		iec.setSchedules(iecSchedules);
		iec.setWaypoints(iecWaypoints);
	
		return iec;
	}

	public static IECRoute convertPredictionToIEC(
			PredictionDataItem predictionDataItem) {
		IECRoute route = new IECRoute();

		return route;
	}

	public static IECRoute convertManoeuvreToIEC(
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
				iecScheduleElement.setWindDirection(receivedMPoint
						.getWind_dir_rad());
			if (receivedMPoint.getCurrent_speed_kts() != null)
				iecScheduleElement.setCurrentSpeed(receivedMPoint
						.getCurrent_speed_kts());
			if (receivedMPoint.getCurrent_dir_rad() != null)
				iecScheduleElement.setCurrentDirection(receivedMPoint
						.getCurrent_dir_rad());

			// put rpm command into extension, because IEC do not provide more
			// than one value
			if (receivedMPoint.getRpm_command() != null) {
				Rpm rpm_command = receivedMPoint.getRpm_command();

				if (rpm_command.getRpm1_cmd_rpm() != null
						|| rpm_command.getRpm2_cmd_rpm() != null
						|| rpm_command.getRpm3_cmd_rpm() != null
						|| rpm_command.getRpm4_cmd_rpm() != null) {
					IECExtension rpmExtension = new IECExtension();
					rpmExtension.setManufacturer("OFFIS");
					rpmExtension.setName("EXT_RPM_MultipleEngines");
					if (rpm_command.getRpm1_cmd_rpm() != null)
						rpmExtension.addExtensionValue("rpm1_cmd",
								rpm_command.getRpm1_cmd_rpm());
					if (rpm_command.getRpm2_cmd_rpm() != null)
						rpmExtension.addExtensionValue("rpm2_cmd",
								rpm_command.getRpm2_cmd_rpm());
					if (rpm_command.getRpm3_cmd_rpm() != null)
						rpmExtension.addExtensionValue("rpm3_cmd",
								rpm_command.getRpm3_cmd_rpm());
					if (rpm_command.getRpm4_cmd_rpm() != null)
						rpmExtension.addExtensionValue("rpm4_cmd",
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
					rpmExtension.setManufacturer("OFFIS");
					rpmExtension.setName("EXT_PITCH_MultipleValues");
					if (pitch_command.getPitch1_cmd_perc() != null)
						rpmExtension.addExtensionValue("pitch1_cmd",
								pitch_command.getPitch1_cmd_perc());
					if (pitch_command.getPitch2_cmd_perc() != null)
						rpmExtension.addExtensionValue("pitch2_cmd",
								pitch_command.getPitch2_cmd_perc());
					if (pitch_command.getPitch3_cmd_perc() != null)
						rpmExtension.addExtensionValue("pitch3_cmd",
								pitch_command.getPitch3_cmd_perc());
					if (pitch_command.getPitch4_cmd_perc() != null)
						rpmExtension.addExtensionValue("pitch4_cmd",
								pitch_command.getPitch4_cmd_perc());
					iecScheduleElement.addExtension(rpmExtension);
				}
			}

			iecManual.addScheduleElement(iecScheduleElement);
		}

		iecSchedules.addSchedule(iecSchedule);
		iec.setSchedules(iecSchedules);
		iec.setWaypoints(iecWaypoints);

		return iec;
	}
}
