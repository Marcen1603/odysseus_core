package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator;

import java.util.List;
import java.util.Date;

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
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;

public class ToIECConverterHelper {

	public static IECRoute convertShipRouteToIEC(RouteDataItem routeDataItem) {
		// get received objects
		Route receivedRoute = routeDataItem.getRoute();
		List<Waypoint> receivedWaypoints = routeDataItem.getRoute()
				.getWaypoints();

		IECRoute route = new IECRoute();

		// Create route info
		IECRouteInfo routeInfo = new IECRouteInfo();
		if (receivedRoute.getRoute_label() != null)
			routeInfo.setRouteName(receivedRoute.getRoute_label());
		if (receivedRoute.getSignature() != null)
			routeInfo.setRouteAuthor(String.valueOf(receivedRoute
					.getSignature()));
		if (receivedRoute.getRoute_ID() != null)
			routeInfo.setVesselVoyage(receivedRoute.getRoute_ID());
		route.setRouteInfo(routeInfo);

		// waypoints
		IECWaypoints iecWaypoints = new IECWaypoints();
		IECSchedules iecSchedules = new IECSchedules();
		IECSchedule iecSchedule = new IECSchedule();
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
				iecPosition.setLatitude(receivedWaypoint.getLat_rad());
				iecPosition.setLongitude(receivedWaypoint.getLon_rad());
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
		route.setSchedules(iecSchedules);
		route.setWaypoints(iecWaypoints);

		return route;
	}

	public static IECRoute convertPredictionToIEC(
			PredictionDataItem predictionDataItem) {
		IECRoute route = new IECRoute();

		return route;
	}

	public static IECRoute convertManoeuvreToIEC(
			ManoeuvrePlanDataItem manoeuvrePlanDataItem) {
		IECRoute route = new IECRoute();

		return route;
	}
}
