package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Identifier;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.MSG_IVEF;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectDatas;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.TrackData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VoyageData;
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

public class ToIVEF025ConverterHelper {
	private static final double KTS_TO_MS = 0.51444444444;

	private static final Logger LOG = LoggerFactory
			.getLogger(ToIVEF025ConverterHelper.class);

	public static MSG_IVEF convertShipRouteToIVEF(RouteDataItem routeDataItem,
			StaticAndVoyageData staticAndVoyageData) {
		Route receivedRoute = routeDataItem.getRoute();

		List<Waypoint> receivedWaypoints = routeDataItem.getRoute()
				.getWaypoints();

		// Create IVEF Elements
		MSG_IVEF msg_vesselData = new MSG_IVEF();

		Header header = new Header();
		msg_vesselData.setHeader(header);

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		// iterate over waypoints
		for (Waypoint waypoint : receivedWaypoints) {
			ObjectData objectData = new ObjectData();

			// Pos Report
			TrackData trackData = new TrackData();
			if (waypoint.getID() != null) {
				trackData.setId(waypoint.getID());
				trackData.setSourceId(waypoint.getID().toString());
			}
			trackData.setUpdateTime(new Date());
			if (waypoint.getSpeed_kts() != null)
				trackData.setSOG(waypoint.getSpeed_kts() * KTS_TO_MS);
			trackData.setCOG(0.0);
			if (waypoint.getTurnradius_nm() != null)
				trackData.setROT(waypoint.getTurnradius_nm());
			if (waypoint.getLat_rad() != null && waypoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(waypoint.getLat_rad()));
				pos.setLong(Math.toDegrees(waypoint.getLon_rad()));
				trackData.addPos(pos);
			}
			objectData.setTrackData(trackData);

			VesselData vesselData = new VesselData();
			if (waypoint.getID() != null) {
				vesselData.setId(waypoint.getID());
				vesselData.setSourceId(waypoint.getID().toString());
			}
			if (receivedRoute.getRoute_label() != null)
				vesselData.setSourceName(receivedRoute.getRoute_label());

			Identifier identifier = new Identifier();
			if (staticAndVoyageData.getSourceMmsi() != null)
				identifier.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI().intValue());
			if (staticAndVoyageData.getImo() != null)
				identifier.setIMO(staticAndVoyageData.getImo().getIMO()
						.intValue());
			if (staticAndVoyageData.getShipName() != null)
				identifier.setName(staticAndVoyageData.getShipName());
			if (staticAndVoyageData.getCallsign() != null)
				identifier.setCallsign(staticAndVoyageData.getCallsign());
			vesselData.setIdentifier(identifier);
			objectData.addVesselData(vesselData);

			VoyageData voyageData = new VoyageData();
			if (waypoint.getID() != null) {
				voyageData.setId(waypoint.getID());
				voyageData.setSourceId(waypoint.getID().toString());
			}
			if (receivedRoute.getRoute_label() != null)
				voyageData.setSourceName(receivedRoute.getRoute_label());
			if (staticAndVoyageData.getDraught() != null)
				voyageData.setDraught(staticAndVoyageData.getDraught());

			de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint ivefWaypoint = new de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint();

			Date eta = new Date(waypoint.getETA() * 1000);
			ivefWaypoint.setETA(eta);
			voyageData.addWaypoint(ivefWaypoint);
			objectData.addVoyageData(voyageData);

			objectDatas.addObjectData(objectData);
		}
		msg_vesselData.setBody(body);
		return msg_vesselData;
	}

	public static MSG_IVEF convertManoeuvreToIVEF(
			ManoeuvrePlanDataItem manoeuvreDataItem,
			StaticAndVoyageData staticAndVoyageData) {
		ManoeuvrePlan receivedMPlan = manoeuvreDataItem.getMplan();
		List<ManoeuvrePoint> receivedManoeuvrePoints = receivedMPlan
				.getMpoints();

		// Create IVEF Elements
		MSG_IVEF msg_ivef = new MSG_IVEF();

		Header header = new Header();
		msg_ivef.setHeader(header);

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		// Iterate over manoeuvre points
		for (ManoeuvrePoint manoeuvrePoint : receivedManoeuvrePoints) {

			ObjectData objectData = new ObjectData();

			TrackData trackData = new TrackData();
			if (manoeuvrePoint.getID() != null) {
				trackData.setId(manoeuvrePoint.getID());
				trackData.setSourceId(manoeuvrePoint.getID().toString());
			}
			trackData.setUpdateTime(new Date());

			if (manoeuvrePoint.getSog_long_kts() != null) {
				trackData.setSOG(manoeuvrePoint.getSog_long_kts() * KTS_TO_MS);
			}

			if (manoeuvrePoint.getCourse_over_ground_rad() != null)
				trackData.setCOG(Math.toDegrees(manoeuvrePoint
						.getCourse_over_ground_rad()));
			if (manoeuvrePoint.getHeading_rad() != null)
				trackData.setROT(manoeuvrePoint.getHeading_rad());
			if (manoeuvrePoint.getLat_rad() != null
					&& manoeuvrePoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(manoeuvrePoint.getLat_rad()));
				pos.setLong(Math.toDegrees(manoeuvrePoint.getLon_rad()));
				trackData.addPos(pos);
			}
			objectData.setTrackData(trackData);

			VesselData vesselData = new VesselData();
			if (manoeuvrePoint.getID() != null) {
				vesselData.setId(manoeuvrePoint.getID());
				vesselData.setSourceId(manoeuvrePoint.getID().toString());
			}
			if (receivedMPlan.getMplan_label() != null)
				vesselData.setSourceName(receivedMPlan.getMplan_label());

			Identifier identifier = new Identifier();
			if (staticAndVoyageData.getSourceMmsi() != null)
				identifier.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI().intValue());
			if (staticAndVoyageData.getImo() != null)
				identifier.setIMO(staticAndVoyageData.getImo().getIMO()
						.intValue());
			if (staticAndVoyageData.getShipName() != null)
				identifier.setName(staticAndVoyageData.getShipName());
			if (staticAndVoyageData.getCallsign() != null)
				identifier.setCallsign(staticAndVoyageData.getCallsign());
			vesselData.setIdentifier(identifier);

			objectData.addVesselData(vesselData);

			// Voyage
			VoyageData voyageData = new VoyageData();
			if (manoeuvrePoint.getID() != null) {
				voyageData.setId(manoeuvrePoint.getID());
				voyageData.setSourceId(manoeuvrePoint.getID().toString());
			}
			if (receivedMPlan.getMplan_label() != null)
				voyageData.setSourceName(receivedMPlan.getMplan_label());
			if (staticAndVoyageData.getDraught() != null)
				voyageData.setDraught(staticAndVoyageData.getDraught());
			objectData.addVoyageData(voyageData);

			objectDatas.addObjectData(objectData);
		}

		msg_ivef.setBody(body);
		return msg_ivef;
	}

	public static MSG_IVEF convertPredictionToIVEF(
			PredictionDataItem predictionDataItem,
			StaticAndVoyageData staticAndVoyageData) {
		PredictionPlan predictionPlan = predictionDataItem.getMplan();
		List<PredictionPoint> predictionPoints = predictionPlan
				.getPred_points();

		// Create IVEF Elements
		MSG_IVEF msg_ivef = new MSG_IVEF();

		Header header = new Header();
		msg_ivef.setHeader(header);

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		for (PredictionPoint predictionPoint : predictionPoints) {
			ObjectData objectData = new ObjectData();

			TrackData trackData = new TrackData();
			if (predictionPoint.getID() != null) {
				trackData.setId(predictionPoint.getID());
				trackData.setSourceId(predictionPoint.getID().toString());
			}
			trackData.setUpdateTime(new Date());
			trackData.setSOG(0.0);
			if (predictionPoint.getCourse_over_ground_rad() != null)
				trackData.setCOG(Math.toDegrees(predictionPoint
						.getCourse_over_ground_rad()));
			if (predictionPoint.getHeading_rad() != null)
				trackData.setROT(predictionPoint.getHeading_rad());
			if (predictionPoint.getLat_rad() != null
					&& predictionPoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(predictionPoint.getLat_rad()));
				pos.setLong(Math.toDegrees(predictionPoint.getLon_rad()));
				trackData.addPos(pos);
			}
			objectData.setTrackData(trackData);

			// Static Data
			VesselData vesselData = new VesselData();
			if (predictionPoint.getID() != null) {
				vesselData.setId(predictionPoint.getID());
				vesselData.setSourceId(predictionPoint.getID().toString());
			}
			vesselData.setSourceName("Prediction");

			Identifier identifier = new Identifier();
			if (staticAndVoyageData.getSourceMmsi() != null)
				identifier.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI().intValue());
			if (staticAndVoyageData.getImo() != null)
				identifier.setIMO(staticAndVoyageData.getImo().getIMO()
						.intValue());
			if (staticAndVoyageData.getShipName() != null)
				identifier.setName(staticAndVoyageData.getShipName());
			if (staticAndVoyageData.getCallsign() != null)
				identifier.setCallsign(staticAndVoyageData.getCallsign());
			vesselData.setIdentifier(identifier);
			objectData.addVesselData(vesselData);

			// Voyage
			VoyageData voyageData = new VoyageData();
			if (predictionPoint.getID() != null) {
				voyageData.setId(predictionPoint.getID());
				voyageData.setSourceId(predictionPoint.getID().toString());
			}
			voyageData.setSourceName("Prediction");
			if (staticAndVoyageData.getDraught() != null)
				voyageData.setDraught(staticAndVoyageData.getDraught());
			objectData.addVoyageData(voyageData);

			objectDatas.addObjectData(objectData);
		}

		msg_ivef.setBody(body);
		return msg_ivef;
	}

	public static MSG_IVEF convertIECToIVEF025(IECRoute iecRoute,
			StaticAndVoyageData staticAndVoyageData) {
		if (!iecRoute.isValid()) {
			LOG.debug("IEC Element is invalid");
			return null;
		}

		// Create IVEF Elements
		MSG_IVEF msg_ivef = new MSG_IVEF();

		Header header = new Header();
		msg_ivef.setHeader(header);

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		// Route Info
		IECRouteInfo iecRouteInfo = iecRoute.getRouteInfo();

		// Iterate over Waypoints
		for (IECWaypoint iecWaypoint : iecRoute.getWaypoints().getWaypoints()) {
			ObjectData objectData = new ObjectData();

			TrackData trackData = new TrackData();
			trackData.setId(iecWaypoint.getId());
			trackData.setSourceId(iecWaypoint.getId().toString());
			trackData.setUpdateTime(new Date());
			trackData.setCOG(0.0); // set to 0.0 because it is mandatory
			if (iecWaypoint.getRadius() != null)
				trackData.setROT(iecWaypoint.getRadius());
			if (iecWaypoint.getPosition() != null) {
				Pos pos = new Pos();
				pos.setLat(iecWaypoint.getPosition().getLatitude());
				pos.setLong(iecWaypoint.getPosition().getLongitude());
				trackData.addPos(pos);
			}
			objectData.setTrackData(trackData);

			VesselData vesselData = new VesselData();
			vesselData.setId(iecWaypoint.getId());
			vesselData.setSourceId(iecWaypoint.getId().toString());
			if (iecRouteInfo.getRouteName() != null)
				vesselData.setSourceName(iecRouteInfo.getRouteName());

			Identifier identifier = new Identifier();
			if (iecRouteInfo.getVesselName() != null) {
				identifier.setName(iecRouteInfo.getVesselName());
			} else if (staticAndVoyageData.getShipName() != null) {
				identifier.setName(staticAndVoyageData.getShipName());
			}

			if (iecRouteInfo.getVesselIMO() != null) {
				identifier.setIMO(iecRouteInfo.getVesselIMO());
			} else if (staticAndVoyageData.getImo() != null) {
				identifier.setIMO(staticAndVoyageData.getImo().getIMO()
						.intValue());
			}

			if (iecRouteInfo.getVesselMMSI() != null) {
				identifier.setMMSI(iecRouteInfo.getVesselMMSI());
			} else if (staticAndVoyageData.getSourceMmsi() != null) {
				identifier.setMMSI(staticAndVoyageData.getSourceMmsi()
						.getMMSI().intValue());
			}

			if (staticAndVoyageData.getCallsign() != null)
				identifier.setCallsign(staticAndVoyageData.getCallsign());
			vesselData.setIdentifier(identifier);
			objectData.addVesselData(vesselData);

			VoyageData voyageData = new VoyageData();
			voyageData.setId(iecWaypoint.getId());
			voyageData.setSourceId(iecWaypoint.getId().toString());
			if (iecRouteInfo.getRouteName() != null)
				voyageData.setSourceName(iecRouteInfo.getRouteName());
			if (staticAndVoyageData.getDraught() != null)
				voyageData.setDraught(staticAndVoyageData.getDraught());
			objectData.addVoyageData(voyageData);

			objectDatas.addObjectData(objectData);
		}

		for (IECSchedule schedule : iecRoute.getSchedules().getSchedules()) {
			if (schedule.getManual() != null) {
				IECManual manual = schedule.getManual();
				for (IECScheduleElement iecScheduleElement : manual
						.getScheduleElements()) {
					ObjectData vesselData = findObjectData(body,
							iecScheduleElement.getWaypointID());
					if (vesselData != null) {
						if (iecScheduleElement.getSpeed() != null)
							vesselData.getTrackData().setSOG(
									iecScheduleElement.getSpeed() * KTS_TO_MS);
						if (iecScheduleElement.getEta() != null) {
							vesselData.getVoyageDataAt(0).setETA(
									iecScheduleElement.getEta());
						}
					}
				}
			}
		}

		msg_ivef.setBody(body);
		return msg_ivef;
	}

	private static ObjectData findObjectData(Body body, Integer waypointID) {
		ObjectDatas objectDatas = body.getObjectDatas();
		for (int i = 0; i < objectDatas.countOfObjectDatas(); i++) {
			ObjectData objectData = objectDatas.getObjectDataAt(i);
			if (objectData.getTrackData() != null) {
				if (objectData.getTrackData().getId() == waypointID) {
					return objectData;
				}
			}
		}
		return null;
	}

}
