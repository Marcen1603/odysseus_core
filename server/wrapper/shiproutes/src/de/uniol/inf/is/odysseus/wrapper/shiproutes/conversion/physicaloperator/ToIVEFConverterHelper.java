package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.PosReport;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Voyage;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StaticAndVoyageData;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECManual;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRouteInfo;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECSchedule;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECScheduleElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECWaypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionPlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionPoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Waypoint;

public class ToIVEFConverterHelper {

	private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	private static final double KTS_TO_MS = 0.51444444444;

	private static final Logger LOG = LoggerFactory
			.getLogger(ToIVEFConverterHelper.class);

	public static MSG_VesselData convertShipRouteToIVEF(
			RouteDataItem routeDataItem, StaticAndVoyageData staticAndVoyageData) {
		Route receivedRoute = routeDataItem.getRoute();

		List<Waypoint> receivedWaypoints = routeDataItem.getRoute()
				.getWaypoints();

		// Create IVEF Elements
		MSG_VesselData msg_vesselData = new MSG_VesselData();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();

		// iterate over waypoints
		for (Waypoint waypoint : receivedWaypoints) {
			VesselData vesselData = new VesselData();

			// Pos Report
			PosReport posReport = new PosReport();
			if (waypoint.getID() != null)
				posReport.setId(waypoint.getID());
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

			// Static Data
			StaticData staticData = new StaticData();
			if (waypoint.getID() != null)
				staticData.setId(String.valueOf(waypoint.getID()));
			if (receivedRoute.getRoute_label() != null)
				staticData.setSourceName(receivedRoute.getRoute_label());
			if (receivedRoute.getRoute_ID() != null)
				staticData.setSource(receivedRoute.getRoute_ID());
			if (staticAndVoyageData.getSourceMmsi() != null)
				staticData.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI());
			if (staticAndVoyageData.getImo() != null)
				staticData.setIMO(staticAndVoyageData.getImo().getIMO());
			if (staticAndVoyageData.getShipName() != null)
				staticData.setShipName(staticAndVoyageData.getShipName());
			if (staticAndVoyageData.getCallsign() != null)
				staticData.setCallsign(staticAndVoyageData.getCallsign());
			staticData.setShipType(70);
			vesselData.addStaticData(staticData);

			// Voyage
			Voyage voyage = new Voyage();
			if (waypoint.getID() != null)
				voyage.setId(String.valueOf(waypoint.getID()));
			if (receivedRoute.getRoute_label() != null)
				voyage.setSourceName(receivedRoute.getRoute_label());
			if (receivedRoute.getRoute_ID() != null)
				voyage.setSource(receivedRoute.getRoute_ID());
			if (staticAndVoyageData.getDraught() != null)
				voyage.setDraught(staticAndVoyageData.getDraught());

			Date eta = new Date(waypoint.getETA() * 1000);
			DateFormat df = new SimpleDateFormat(PATTERN);
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			voyage.setETA(df.format(eta));
			vesselData.addVoyage(voyage);

			body.addVesselData(vesselData);
		}
		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

	public static MSG_VesselData convertManoeuvreToIVEF(
			ManoeuvrePlanDataItem manoeuvrePlanDataItem,
			StaticAndVoyageData staticAndVoyageData) {
		ManoeuvrePlan receivedMPlan = manoeuvrePlanDataItem.getMplan();
		List<ManoeuvrePoint> receivedManoeuvrePoints = receivedMPlan
				.getMpoints();

		// Create IVEF Elements
		MSG_VesselData msg_vesselData = new MSG_VesselData();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();

		int counter = 1;
		// Iterate over manoeuvre points
		for (ManoeuvrePoint manoeuvrePoint : receivedManoeuvrePoints) {

			VesselData vesselData = new VesselData();

			// Pos Report
			PosReport posReport = new PosReport();
			posReport.setId(counter);
			if (receivedMPlan.getMplan_ID() != null) {
				posReport.setSourceId(receivedMPlan.getMplan_ID());
			}
			posReport.setUpdateTime(new Date());

			if (manoeuvrePoint.getSog_long_kts() != null) {
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

			// Static Data
			StaticData staticData = new StaticData();
			staticData.setId(String.valueOf(counter));
			if (receivedMPlan.getMplan_label() != null)
				staticData.setSourceName(receivedMPlan.getMplan_label());
			if (receivedMPlan.getMplan_ID() != null) {
				staticData.setSource(receivedMPlan.getMplan_ID());
			}
			if (staticAndVoyageData.getSourceMmsi() != null)
				staticData.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI());
			if (staticAndVoyageData.getImo() != null)
				staticData.setIMO(staticAndVoyageData.getImo().getIMO());
			if (staticAndVoyageData.getShipName() != null)
				staticData.setShipName(staticAndVoyageData.getShipName());
			if (staticAndVoyageData.getCallsign() != null)
				staticData.setCallsign(staticAndVoyageData.getCallsign());

			staticData.setShipType(70);
			vesselData.addStaticData(staticData);

			// Voyage
			Voyage voyage = new Voyage();
			voyage.setId(String.valueOf(counter));
			if (receivedMPlan.getMplan_label() != null)
				voyage.setSourceName(receivedMPlan.getMplan_label());
			if (receivedMPlan.getMplan_ID() != null) {
				voyage.setSource(receivedMPlan.getMplan_ID());
			}
			if (staticAndVoyageData.getDraught() != null)
				voyage.setDraught(staticAndVoyageData.getDraught());
			vesselData.addVoyage(voyage);

			body.addVesselData(vesselData);

			counter++;
		}

		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

	public static MSG_VesselData convertPredictionToIVEF(
			PredictionDataItem predictionDataItem,
			StaticAndVoyageData staticAndVoyageData) {
		PredictionPlan predictionPlan = predictionDataItem.getMplan();
		List<PredictionPoint> predictionPoints = predictionPlan
				.getPred_points();

		// Create IVEF Elements
		MSG_VesselData msg_vesselData = new MSG_VesselData();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();

		int counter = 1;

		for (PredictionPoint predictionPoint : predictionPoints) {
			VesselData vesselData = new VesselData();

			// Pos Report
			PosReport posReport = new PosReport();
			posReport.setId(counter);
			posReport.setSourceId(counter);
			posReport.setUpdateTime(new Date());
			posReport.setSOG(0.0);
			if (predictionPoint.getCourse_over_ground_rad() != null)
				posReport.setCOG(Math.toDegrees(predictionPoint
						.getCourse_over_ground_rad()));
			posReport.setLost("no");
			if (predictionPoint.getHeading_rad() != null)
				posReport.setRateOfTurn(predictionPoint.getHeading_rad());
			if (predictionPoint.getLat_rad() != null
					&& predictionPoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(predictionPoint.getLat_rad()));
				pos.setLong(Math.toDegrees(predictionPoint.getLon_rad()));
				posReport.setPos(pos);
			}
			vesselData.setPosReport(posReport);

			// Static Data
			StaticData staticData = new StaticData();
			staticData.setId(String.valueOf(counter));
			staticData.setSourceName("Prediction");
			staticData.setSource(counter);
			if (staticAndVoyageData.getSourceMmsi() != null)
				staticData.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI());
			if (staticAndVoyageData.getImo() != null)
				staticData.setIMO(staticAndVoyageData.getImo().getIMO());
			if (staticAndVoyageData.getShipName() != null)
				staticData.setShipName(staticAndVoyageData.getShipName());
			if (staticAndVoyageData.getCallsign() != null)
				staticData.setCallsign(staticAndVoyageData.getCallsign());
			staticData.setShipType(70);
			vesselData.addStaticData(staticData);

			// Voyage
			Voyage voyage = new Voyage();
			voyage.setId(String.valueOf(counter));
			voyage.setSourceName("Prediction");
			voyage.setSource(counter);
			if (staticAndVoyageData.getDraught() != null)
				voyage.setDraught(staticAndVoyageData.getDraught());
			vesselData.addVoyage(voyage);

			body.addVesselData(vesselData);
			counter++;
		}

		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

	public static MSG_VesselData convertIECToIVEF(IECRoute iecRoute,
			StaticAndVoyageData staticAndVoyageData) {
		if (!iecRoute.isValid()) {
			LOG.debug("IEC Element is invalid");
			return null;
		}

		// Create IVEF Elements
		MSG_VesselData msg_vesselData = new MSG_VesselData();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();

		// Route Info
		IECRouteInfo iecRouteInfo = iecRoute.getRouteInfo();

		// Iterate over Waypoints
		for (IECWaypoint iecWaypoint : iecRoute.getWaypoints().getWaypoints()) {
			VesselData vesselData = new VesselData();

			// Pos Report
			PosReport posReport = new PosReport();
			posReport.setId(iecWaypoint.getId());
			posReport.setSourceId(iecWaypoint.getId());
			posReport.setUpdateTime(new Date());
			posReport.setCOG(0.0); // set to 0.0 because it is mandatory
			posReport.setLost("no");
			if (iecWaypoint.getRadius() != null)
				posReport.setRateOfTurn(iecWaypoint.getRadius());
			if (iecWaypoint.getPosition() != null) {
				Pos pos = new Pos();
				pos.setLat(iecWaypoint.getPosition().getLatitude());
				pos.setLong(iecWaypoint.getPosition().getLongitude());
				posReport.setPos(pos);
			}
			vesselData.setPosReport(posReport);

			// Static Data
			StaticData staticData = new StaticData();
			staticData.setId(String.valueOf(iecWaypoint.getId()));
			staticData.setSource(iecWaypoint.getId());
			if (iecRouteInfo.getRouteName() != null)
				staticData.setSourceName(iecRouteInfo.getRouteName());

			if (iecRouteInfo.getVesselName() != null) {
				staticData.setShipName(iecRouteInfo.getVesselName());
			} else if (staticAndVoyageData.getShipName() != null) {
				staticData.setShipName(staticAndVoyageData.getShipName());
			}

			if (iecRouteInfo.getVesselIMO() != null) {
				staticData.setIMO(iecRouteInfo.getVesselIMO().longValue());
			} else if (staticAndVoyageData.getImo() != null) {
				staticData.setIMO(staticAndVoyageData.getImo().getIMO());
			}

			if (iecRouteInfo.getVesselMMSI() != null) {
				staticData.setMMSI(iecRouteInfo.getVesselMMSI().longValue());
			} else if (staticAndVoyageData.getSourceMmsi() != null) {
				staticData.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI());
			}
			staticData.setShipType(70); // cargo vessel

			if (staticAndVoyageData.getCallsign() != null)
				staticData.setCallsign(staticAndVoyageData.getCallsign());
			vesselData.addStaticData(staticData);

			// Voyage
			Voyage voyage = new Voyage();
			voyage.setId(String.valueOf(iecWaypoint.getId()));
			voyage.setSource(iecWaypoint.getId());
			if (iecRouteInfo.getRouteName() != null)
				voyage.setSourceName(iecRouteInfo.getRouteName());
			if (staticAndVoyageData.getDraught() != null)
				voyage.setDraught(staticAndVoyageData.getDraught());
			vesselData.addVoyage(voyage);

			body.addVesselData(vesselData);
		}

		for (IECSchedule schedule : iecRoute.getSchedules().getSchedules()) {
			if (schedule.getManual() != null) {
				IECManual manual = schedule.getManual();
				for (IECScheduleElement iecScheduleElement : manual
						.getScheduleElements()) {
					VesselData vesselData = findVesselData(body,
							iecScheduleElement.getWaypointID());
					if (vesselData != null) {
						if (iecScheduleElement.getSpeed() != null)
							vesselData.getPosReport().setSOG(
									iecScheduleElement.getSpeed() * KTS_TO_MS);
						if (iecScheduleElement.getEta() != null) {
							Date eta = iecScheduleElement.getEta();
							DateFormat df = new SimpleDateFormat(PATTERN);
							df.setTimeZone(TimeZone.getTimeZone("UTC"));
							vesselData.getVoyageAt(0).setETA(df.format(eta));
						}
					}
				}
			}
		}

		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

	private static VesselData findVesselData(Body body, Integer waypointID) {
		for (int i = 0; i < body.countOfVesselDatas(); i++) {
			VesselData vesselData = body.getVesselDataAt(i);
			if (vesselData.getPosReport() != null) {
				if (vesselData.getPosReport().getId() == waypointID) {
					return vesselData;
				}
			}
		}
		return null;
	}

}
