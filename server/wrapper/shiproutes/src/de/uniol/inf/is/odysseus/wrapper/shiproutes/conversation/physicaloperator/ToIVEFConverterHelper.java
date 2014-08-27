package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.PosReport;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Voyage;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRouteInfo;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECWaypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Waypoint;

public class ToIVEFConverterHelper {

	private static final double KTS_TO_MS = 0.51444444444;

	public static MSG_VesselData convertShipRouteToIVEF(
			RouteDataItem routeDataItem) {
		Route receivedRoute = routeDataItem.getRoute();
		Date currentDate = new Date();
		Long mmsi = currentDate.getTime();

		List<Waypoint> receivedWaypoints = routeDataItem.getRoute()
				.getWaypoints();

		// Create IVEF Elements
		MSG_VesselData msg_vesselData = new MSG_VesselData();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();

		for (Waypoint waypoint : receivedWaypoints) {
			VesselData vesselData = new VesselData();

			PosReport posReport = new PosReport();
			if (receivedRoute.getSignature() != null)
				posReport.setId(receivedRoute.getSignature());
			if (receivedRoute.getRoute_ID() != null)
				posReport.setSourceId(receivedRoute.getRoute_ID());
			posReport.setUpdateTime(new Date());
			if (waypoint.getSpeed_kts() != null)
				posReport.setSOG(waypoint.getSpeed_kts() * KTS_TO_MS);
			posReport.setCOG(0.0);
			posReport.setLost("no");
			if (waypoint.getTurnradius_nm() != null)
				posReport.setRateOfTurn(waypoint.getTurnradius_nm());
			if (waypoint.getLat_rad() != null && waypoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(waypoint.getLat_rad()));
				pos.setLong(Math.toDegrees(waypoint.getLon_rad()));
				posReport.setPos(pos);
			}
			vesselData.setPosReport(posReport);

			StaticData staticData = new StaticData();
			if (receivedRoute.getSignature() != null)
				staticData.setId(String.valueOf(receivedRoute.getSignature()));
			if (receivedRoute.getRoute_label() != null)
				staticData.setSourceName(receivedRoute.getRoute_label());
			if (receivedRoute.getRoute_ID() != null)
				staticData.setSource(receivedRoute.getRoute_ID());
			staticData.setMMSI(mmsi);
			vesselData.addStaticData(staticData);

			Voyage voyage = new Voyage();
			if (receivedRoute.getSignature() != null)
				voyage.setId(String.valueOf(receivedRoute.getSignature()));
			if (receivedRoute.getRoute_label() != null)
				voyage.setSourceName(receivedRoute.getRoute_label());
			if (receivedRoute.getRoute_ID() != null)
				voyage.setSource(receivedRoute.getRoute_ID());

			Date eta = new Date(waypoint.getETA() * 1000);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			voyage.setETA(df.format(eta));
			vesselData.addVoyage(voyage);

			body.addVesselData(vesselData);
		}
		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

	public static MSG_VesselData convertManoeuvreToIVEF(
			ManoeuvrePlanDataItem manoeuvrePlanDataItem) {
		ManoeuvrePlan receivedMPlan = manoeuvrePlanDataItem.getMplan();
		List<ManoeuvrePoint> receivedManoeuvrePoints = receivedMPlan
				.getMpoints();
		Date currentDate = new Date();
		Long mmsi = currentDate.getTime();

		int id = ((Double) (Math.random() * 999999999)).intValue();

		// Create IVEF Elements
		MSG_VesselData msg_vesselData = new MSG_VesselData();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();

		for (ManoeuvrePoint manoeuvrePoint : receivedManoeuvrePoints) {
			VesselData vesselData = new VesselData();

			PosReport posReport = new PosReport();
			posReport.setId(id);
			if (receivedMPlan.getMplan_ID() != null)
				posReport.setSourceId(receivedMPlan.getMplan_ID());
			posReport.setUpdateTime(new Date());

			if (manoeuvrePoint.getSog_long_kts() != null
					&& !manoeuvrePoint.getSog_long_kts()
							.equals(new Double(0.0))) {
				posReport.setSOG(manoeuvrePoint.getSog_long_kts() * KTS_TO_MS);
			}

			if (manoeuvrePoint.getCourse_over_ground_rad() != null)
				posReport.setCOG(Math.toDegrees(manoeuvrePoint
						.getCourse_over_ground_rad()));
			posReport.setLost("no");
			if (manoeuvrePoint.getHeading_rad() != null)
				posReport.setRateOfTurn(manoeuvrePoint.getHeading_rad());
			if (manoeuvrePoint.getLat_rad() != null
					&& manoeuvrePoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(manoeuvrePoint.getLat_rad()));
				pos.setLong(Math.toDegrees(manoeuvrePoint.getLon_rad()));
				posReport.setPos(pos);
			}
			vesselData.setPosReport(posReport);

			StaticData staticData = new StaticData();

			staticData.setId(String.valueOf(id));
			if (receivedMPlan.getMplan_label() != null)
				staticData.setSourceName(receivedMPlan.getMplan_label());
			if (receivedMPlan.getMplan_ID() != null)
				staticData.setSource(receivedMPlan.getMplan_ID());
			staticData.setMMSI(mmsi);
			vesselData.addStaticData(staticData);

			Voyage voyage = new Voyage();
			voyage.setId(String.valueOf(id));
			if (receivedMPlan.getMplan_label() != null)
				voyage.setSourceName(receivedMPlan.getMplan_label());
			if (receivedMPlan.getMplan_ID() != null)
				voyage.setSource(receivedMPlan.getMplan_ID());
			vesselData.addVoyage(voyage);

			body.addVesselData(vesselData);
		}

		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

	@SuppressWarnings("unused")
	public static MSG_VesselData convertIECToIVEF(IECRoute iecRoute) {

		// Create IVEF Elements
		MSG_VesselData msg_vesselData = new MSG_VesselData();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();

		// Route Info
		IECRouteInfo iecRouteInfo = iecRoute.getRouteInfo();
		
		// Waypoints
		for (IECWaypoint iecWaypoint : iecRoute.getWaypoints().getWaypoints()) {
			VesselData vesselData = new VesselData();

			PosReport posReport = new PosReport();
			vesselData.setPosReport(posReport);

			StaticData staticData = new StaticData();
			vesselData.addStaticData(staticData);

			Voyage voyage = new Voyage();
			vesselData.addVoyage(voyage);

			body.addVesselData(vesselData);
		}

		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

}
