package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Identifier;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.MSG_IVEF;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectDatas;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Pos;
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
	private static final Logger LOG = LoggerFactory
			.getLogger(ToIVEF025ConverterHelper.class);

	public static MSG_IVEF convertShipRouteToIVEF(RouteDataItem routeDataItem,
			StaticAndVoyageData staticAndVoyageData) {
		Route receivedRoute = routeDataItem.getRoute();

		List<Waypoint> receivedWaypoints = routeDataItem.getRoute()
				.getWaypoints();

		// Create IVEF Elements
		MSG_IVEF msg_ivef = new MSG_IVEF();
		msg_ivef.setHeader(createIVEFHeader());

		int id = (int) (Math.random() * 999999999);

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		ObjectData objectData = new ObjectData();

		VesselData vesselData = new VesselData();

		vesselData.setId(id);
		vesselData.setSourceId(String.valueOf(id));

		if (receivedRoute.getRoute_label() != null)
			vesselData.setSourceName(receivedRoute.getRoute_label());

		Identifier identifier = new Identifier();
		if (staticAndVoyageData.getSourceMmsi() != null)
			identifier.setMMSI(staticAndVoyageData.getSourceMmsi().getMMSI()
					.intValue());
		if (staticAndVoyageData.getImo() != null)
			identifier.setIMO(staticAndVoyageData.getImo().getIMO().intValue());
		if (staticAndVoyageData.getShipName() != null)
			identifier.setName(staticAndVoyageData.getShipName());
		if (staticAndVoyageData.getCallsign() != null)
			identifier.setCallsign(staticAndVoyageData.getCallsign());
		vesselData.setIdentifier(identifier);
		objectData.addVesselData(vesselData);

		VoyageData voyageData = new VoyageData();
		voyageData.setId(id);
		voyageData.setSourceId(String.valueOf(id));

		if (receivedRoute.getRoute_label() != null)
			voyageData.setSourceName(receivedRoute.getRoute_label());
		if (staticAndVoyageData.getDraught() != null)
			voyageData.setDraught(staticAndVoyageData.getDraught());

		// iterate over waypoints
		for (Waypoint waypoint : receivedWaypoints) {
			de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint ivefWaypoint = new de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint();
			if (waypoint.getLat_rad() != null && waypoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(waypoint.getLat_rad()));
				pos.setLong(Math.toDegrees(waypoint.getLon_rad()));
				ivefWaypoint.setPos(pos);
			}
			Date eta = new Date(waypoint.getETA() * 1000);
			ivefWaypoint.setETA(eta);
			voyageData.addWaypoint(ivefWaypoint);
		}

		objectData.addVoyageData(voyageData);

		objectDatas.addObjectData(objectData);
		msg_ivef.setBody(body);
		return msg_ivef;
	}

	private static Header createIVEFHeader() {
		Header header = new Header();
		header.setMsgRefId(UUID.randomUUID().toString());
		header.setVersion("0.2.5");
		return header;
	}

	public static MSG_IVEF convertManoeuvreToIVEF(
			ManoeuvrePlanDataItem manoeuvreDataItem,
			StaticAndVoyageData staticAndVoyageData) {
		ManoeuvrePlan receivedMPlan = manoeuvreDataItem.getMplan();
		List<ManoeuvrePoint> receivedManoeuvrePoints = receivedMPlan
				.getMpoints();

		// Create IVEF Elements
		MSG_IVEF msg_ivef = new MSG_IVEF();
		msg_ivef.setHeader(createIVEFHeader());

		int id = (int) (Math.random() * 999999999);

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		ObjectData objectData = new ObjectData();

		VesselData vesselData = new VesselData();

		vesselData.setId(id);
		vesselData.setSourceId(String.valueOf(id));

		if (receivedMPlan.getMplan_label() != null)
			vesselData.setSourceName(receivedMPlan.getMplan_label());

		Identifier identifier = new Identifier();
		if (staticAndVoyageData.getSourceMmsi() != null)
			identifier.setMMSI(staticAndVoyageData.getSourceMmsi().getMMSI()
					.intValue());
		if (staticAndVoyageData.getImo() != null)
			identifier.setIMO(staticAndVoyageData.getImo().getIMO().intValue());
		if (staticAndVoyageData.getShipName() != null)
			identifier.setName(staticAndVoyageData.getShipName());
		if (staticAndVoyageData.getCallsign() != null)
			identifier.setCallsign(staticAndVoyageData.getCallsign());
		vesselData.setIdentifier(identifier);

		objectData.addVesselData(vesselData);

		// Voyage
		VoyageData voyageData = new VoyageData();

		voyageData.setId(id);
		voyageData.setSourceId(String.valueOf(id));

		if (receivedMPlan.getMplan_label() != null)
			voyageData.setSourceName(receivedMPlan.getMplan_label());
		if (staticAndVoyageData.getDraught() != null)
			voyageData.setDraught(staticAndVoyageData.getDraught());

		// Iterate over manoeuvre points
		for (ManoeuvrePoint manoeuvrePoint : receivedManoeuvrePoints) {

			de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint ivefWaypoint = new de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint();
			if (manoeuvrePoint.getLat_rad() != null
					&& manoeuvrePoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(manoeuvrePoint.getLat_rad()));
				pos.setLong(Math.toDegrees(manoeuvrePoint.getLon_rad()));
				ivefWaypoint.setPos(pos);
			}
		}
		objectData.addVoyageData(voyageData);
		objectDatas.addObjectData(objectData);

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
		msg_ivef.setHeader(createIVEFHeader());

		int id = (int) (Math.random() * 999999999);

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		ObjectData objectData = new ObjectData();

		// Static Data
		VesselData vesselData = new VesselData();

		vesselData.setId(id);
		vesselData.setSourceId(String.valueOf(id));

		vesselData.setSourceName("Prediction");

		Identifier identifier = new Identifier();
		if (staticAndVoyageData.getSourceMmsi() != null)
			identifier.setMMSI(staticAndVoyageData.getSourceMmsi().getMMSI()
					.intValue());
		if (staticAndVoyageData.getImo() != null)
			identifier.setIMO(staticAndVoyageData.getImo().getIMO().intValue());
		if (staticAndVoyageData.getShipName() != null)
			identifier.setName(staticAndVoyageData.getShipName());
		if (staticAndVoyageData.getCallsign() != null)
			identifier.setCallsign(staticAndVoyageData.getCallsign());
		vesselData.setIdentifier(identifier);
		objectData.addVesselData(vesselData);

		// Voyage
		VoyageData voyageData = new VoyageData();

		voyageData.setId(id);
		voyageData.setSourceId(String.valueOf(id));

		voyageData.setSourceName("Prediction");
		if (staticAndVoyageData.getDraught() != null)
			voyageData.setDraught(staticAndVoyageData.getDraught());
		for (PredictionPoint predictionPoint : predictionPoints) {
			de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint ivefWaypoint = new de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint();
			if (predictionPoint.getLat_rad() != null
					&& predictionPoint.getLon_rad() != null) {
				Pos pos = new Pos();
				pos.setLat(Math.toDegrees(predictionPoint.getLat_rad()));
				pos.setLong(Math.toDegrees(predictionPoint.getLon_rad()));
				ivefWaypoint.setPos(pos);
			}
		}
		objectData.addVoyageData(voyageData);
		objectDatas.addObjectData(objectData);

		msg_ivef.setBody(body);
		return msg_ivef;
	}

	public static MSG_IVEF convertIECToIVEF025(IECRoute iecRoute,
			StaticAndVoyageData staticAndVoyageData) {
		if (!iecRoute.isValid()) {
			LOG.debug("IEC Element is invalid");
			return null;
		}

		int id = (int) (Math.random() * 999999999);

		// Create IVEF Elements
		MSG_IVEF msg_ivef = new MSG_IVEF();

		msg_ivef.setHeader(createIVEFHeader());

		Body body = new Body();
		ObjectDatas objectDatas = new ObjectDatas();
		body.setObjectDatas(objectDatas);

		// Route Info
		IECRouteInfo iecRouteInfo = iecRoute.getRouteInfo();

		// Iterate over Waypoints

		ObjectData objectData = new ObjectData();

		VesselData vesselData = new VesselData();
		vesselData.setId(id);
		vesselData.setSourceId(String.valueOf(id));
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
			identifier.setIMO(staticAndVoyageData.getImo().getIMO().intValue());
		}

		if (iecRouteInfo.getVesselMMSI() != null) {
			identifier.setMMSI(iecRouteInfo.getVesselMMSI());
		} else if (staticAndVoyageData.getSourceMmsi() != null) {
			identifier.setMMSI(staticAndVoyageData.getSourceMmsi().getMMSI()
					.intValue());
		}

		if (staticAndVoyageData.getCallsign() != null)
			identifier.setCallsign(staticAndVoyageData.getCallsign());
		vesselData.setIdentifier(identifier);
		objectData.addVesselData(vesselData);

		VoyageData voyageData = new VoyageData();
		voyageData.setId(id);
		voyageData.setSourceId(String.valueOf(id));
		if (iecRouteInfo.getRouteName() != null)
			voyageData.setSourceName(iecRouteInfo.getRouteName());
		if (staticAndVoyageData.getDraught() != null)
			voyageData.setDraught(staticAndVoyageData.getDraught());

		if (iecRoute.getSchedules().getSchedules().size() > 0) {
			IECSchedule schedule = iecRoute.getSchedules().getSchedules()
					.get(0);
			if (schedule.getManual() != null) {
				IECManual manual = schedule.getManual();
				if (manual.getScheduleElements().size() > 0) {
					IECScheduleElement iecScheduleElement = manual
							.getScheduleElements().get(
									manual.getScheduleElements().size() - 1);
					voyageData.setETA(iecScheduleElement.getEta());
				}
			}
		}

		for (IECWaypoint iecWaypoint : iecRoute.getWaypoints().getWaypoints()) {
			de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint waypoint = new de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint();
			if (iecWaypoint.getPosition() != null) {
				Pos pos = new Pos();
				pos.setLat(iecWaypoint.getPosition().getLatitude());
				pos.setLong(iecWaypoint.getPosition().getLongitude());
				waypoint.setPos(pos);

				if (iecRouteInfo.getRouteName() != null)
					waypoint.setName(iecRouteInfo.getRouteName());
			}
			voyageData.addWaypoint(waypoint);
		}

		objectData.addVoyageData(voyageData);

		objectDatas.addObjectData(objectData);
		msg_ivef.setBody(body);
		return msg_ivef;
	}
}
