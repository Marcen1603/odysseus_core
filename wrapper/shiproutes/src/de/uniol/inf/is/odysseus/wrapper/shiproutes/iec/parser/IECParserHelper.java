package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECDefaultWaypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECLeg;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECPosition;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRouteInfo;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECSchedule;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECScheduleElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECWaypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.GeometryType;

public class IECParserHelper {

	private static final Logger LOG = LoggerFactory.getLogger(IECParserHelper.class);
	
	public static void setDefaultWaypointAttributes(Attributes atts,
			IECDefaultWaypoint defaultWaypoint) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECDefaultWaypoint.RADIUS:
				defaultWaypoint.setRadius(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element DefaultWaypoint");
				break;
			}
		}
	}

	public static void setExtensionAttributes(Attributes atts,
			IECExtension extension) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECExtension.MANUFACTURER:
				extension.setManufacturer(value);
				break;
			case IECExtension.NAME:
				extension.setName(value);
				break;
			case IECExtension.VERSION:
				extension.setVersion(value);
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Extension");
				break;
			}
		}
	}

	public static void setLegAttributes(Attributes atts, IECLeg leg) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECLeg.DRAUGHT:
				leg.setDraught(Double.parseDouble(value));
				break;
			case IECLeg.DRAUGHT_AFT:
				leg.setDraughtAft(Double.parseDouble(value));
				break;
			case IECLeg.DRAUGHT_FORWARD:
				leg.setDraughtForward(Double.parseDouble(value));
				break;
			case IECLeg.DYNAMIC_UKC:
				leg.setDynamicUKC(Double.parseDouble(value));
				break;
			case IECLeg.GEOMETRY_TYPE:
				GeometryType type = GeometryType.parse(value);
				if (type != null)
					leg.setGeometryType(type);
				break;
			case IECLeg.LEG_INFO:
				leg.setLegInfo(value);
				break;
			case IECLeg.LEG_NOTE1:
				leg.setLegNote1(value);
				break;
			case IECLeg.LEG_NOTE2:
				leg.setLegNote2(value);
				break;
			case IECLeg.LEG_REPORT:
				leg.setLegReport(value);
				break;
			case IECLeg.MASTHEAD:
				leg.setMasthead(Double.parseDouble(value));
				break;
			case IECLeg.PORTSIDE_XTD:
				leg.setPortsideXTD(Double.parseDouble(value));
				break;
			case IECLeg.SAFETY_CONTOUR:
				leg.setSafetyContour(Double.parseDouble(value));
				break;
			case IECLeg.SAFETY_DEPTH:
				leg.setSafetyDepth(Double.parseDouble(value));
				break;
			case IECLeg.SPEED_MAX:
				leg.setSpeedMax(Double.parseDouble(value));
				break;
			case IECLeg.SPEED_MIN:
				leg.setSpeedMin(Double.parseDouble(value));
				break;
			case IECLeg.STARBOARD_XTD:
				leg.setStarboardXTD(Double.parseDouble(value));
				break;
			case IECLeg.STATIC_UKC:
				leg.setStaticUKC(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Leg");
				break;
			}
		}
	}

	public static void setPositionAttributes(Attributes atts, IECPosition position) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECPosition.LAT:
				position.setLatitude(Double.parseDouble(value));
				break;
			case IECPosition.LON:
				position.setLongitude(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Position");
				break;
			}
		}
	}

	public static void setRouteAttributes(Attributes atts, IECRoute route) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key.toLowerCase()) {
			case IECRoute.VERSION:
				route.setVersion(value);
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Route");
				break;
			}
		}
	}

	public static void setRouteInfoAttributes(Attributes atts,
			IECRouteInfo routeInfo) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECRouteInfo.OPTIMIZATION_METHOD:
				routeInfo.setOptimizationMethod(value);
				break;
			case IECRouteInfo.ROUTE_AUTHOR:
				routeInfo.setRouteAuthor(value);
				break;
			case IECRouteInfo.ROUTE_CHANGES_HISTORY:
				routeInfo.setRouteChangesHistory(value);
				break;
			case IECRouteInfo.ROUTE_NAME:
				routeInfo.setRouteName(value);
				break;
			case IECRouteInfo.ROUTE_STATUS:
				routeInfo.setRouteStatus(value);
				break;
			case IECRouteInfo.VALIDITY_PERIOD_START:
				Date validityPeriodStart = parseDateTime(value);
				if (validityPeriodStart != null)
					routeInfo.setValidityPeriodStart(validityPeriodStart);
				break;
			case IECRouteInfo.VALIDITY_PERIOD_STOP:
				Date validityPeriodStop = parseDateTime(value);
				if (validityPeriodStop != null)
					routeInfo.setValidityPeriodStop(validityPeriodStop);
				break;
			case IECRouteInfo.VESSEL_CARGO:
				routeInfo.setVesselCargo(Integer.parseInt(value));
				break;
			case IECRouteInfo.VESSEL_DISPLACEMENT:
				routeInfo.setVesselDisplacement(Integer.parseInt(value));
				break;
			case IECRouteInfo.VESSEL_GM:
				routeInfo.setVesselGM(Double.parseDouble(value));
				break;
			case IECRouteInfo.VESSEL_IMO:
				routeInfo.setVesselIMO(Integer.parseInt(value));
				break;
			case IECRouteInfo.VESSEL_MAX_ROLL:
				routeInfo.setVesselMaxRoll(Integer.parseInt(value));
				break;
			case IECRouteInfo.VESSEL_MAX_WAVE:
				routeInfo.setVesselMaxWave(Double.parseDouble(value));
				break;
			case IECRouteInfo.VESSEL_MAX_WIND:
				routeInfo.setVesselMaxWind(Double.parseDouble(value));
				break;
			case IECRouteInfo.VESSEL_MMSI:
				routeInfo.setVesselMMSI(Integer.parseInt(value));
				break;
			case IECRouteInfo.VESSEL_NAME:
				routeInfo.setVesselName(value);
				break;
			case IECRouteInfo.VESSEL_SERVICE_MAX:
				routeInfo.setVesselServiceMax(Double.parseDouble(value));
				break;
			case IECRouteInfo.VESSEL_SERVICE_MIN:
				routeInfo.setVesselServiceMin(Double.parseDouble(value));
				break;
			case IECRouteInfo.VESSEL_SPEED_MAX:
				routeInfo.setVesselSpeedMax(Double.parseDouble(value));
				break;
			case IECRouteInfo.VESSEL_VOYAGE:
				routeInfo.setVesselVoyage(Integer.parseInt(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element RouteInfo");
				break;
			}
		}
	}

	public static void setScheduleAttributes(Attributes atts, IECSchedule schedule) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECSchedule.ID:
				schedule.setId(Integer.parseInt(value));
				break;
			case IECSchedule.NAME:
				schedule.setName(value);
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Schedule");
				break;
			}
		}
	}

	public static void setScheduleElementAttributes(Attributes atts,
			IECScheduleElement scheduleElement) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECScheduleElement.ABS_FUEL_SAVE:
				scheduleElement.setAbsFuelSave(Double.parseDouble(value));
				break;
			case IECScheduleElement.CURRENT_DIRECTION:
				scheduleElement.setCurrentDirection(Double.parseDouble(value));
				break;
			case IECScheduleElement.CURRENT_SPEED:
				scheduleElement.setCurrentSpeed(Double.parseDouble(value));
				break;
			case IECScheduleElement.ETA:
				Date eta = parseDateTime(value);
				if (eta != null) scheduleElement.setEta(eta);
				break;
			case IECScheduleElement.ETA_WINDOW_AFTER:
				scheduleElement.setEtaWindowAfter(Double.parseDouble(value));
				break;
			case IECScheduleElement.ETA_WINDOW_BEFORE:
				scheduleElement.setEtaWindowBefore(Double.parseDouble(value));
				break;
			case IECScheduleElement.ETD:
				Date etd = parseDateTime(value);
				if (etd != null) scheduleElement.setEtd(etd);
				break;
			case IECScheduleElement.ETD_WINDOW_AFTER:
				scheduleElement.setEtdWindowAfter(Double.parseDouble(value));
				break;
			case IECScheduleElement.ETD_WINDOW_BEFORE:
				scheduleElement.setEtdWindowBefore(Double.parseDouble(value));
				break;
			case IECScheduleElement.FUEL:
				scheduleElement.setFuel(Double.parseDouble(value));
				break;
			case IECScheduleElement.NOTE:
				scheduleElement.setNote(value);
				break;
			case IECScheduleElement.PITCH:
				scheduleElement.setPitch(Integer.parseInt(value));
				break;
			case IECScheduleElement.REL_FUEL_SAVE:
				scheduleElement.setRelFuelSave(Double.parseDouble(value));
				break;
			case IECScheduleElement.RPM:
				scheduleElement.setRpm(Double.parseDouble(value));
				break;
			case IECScheduleElement.SPEED:
				scheduleElement.setSpeed(Double.parseDouble(value));
				break;
			case IECScheduleElement.SPEED_WINDOW:
				scheduleElement.setSpeedWindow(Double.parseDouble(value));
				break;
			case IECScheduleElement.STAY:
				scheduleElement.setStay(Double.parseDouble(value));
				break;
			case IECScheduleElement.TOTAL_LOSS:
				scheduleElement.setTotalLoss(Double.parseDouble(value));
				break;
			case IECScheduleElement.WAVE_LOSS:
				scheduleElement.setWaveLoss(Double.parseDouble(value));
				break;
			case IECScheduleElement.WAYPOINT_ID:
				scheduleElement.setWaypointID(Integer.parseInt(value));
				break;
			case IECScheduleElement.WIND_DIRECTION:
				scheduleElement.setWindDirection(Double.parseDouble(value));
				break;
			case IECScheduleElement.WIND_LOSS:
				scheduleElement.setWindLoss(Double.parseDouble(value));
				break;
			case IECScheduleElement.WIND_SPEED:
				scheduleElement.setWindSpeed(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element ScheduleElement");
				break;
			}
		}
	}

	public static void setWaypointAttributes(Attributes atts, IECWaypoint waypoint) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case IECWaypoint.ID:
				waypoint.setId(Integer.parseInt(value));
				break;
			case IECWaypoint.NAME:
				waypoint.setName(value);
				break;
			case IECWaypoint.RADIUS:
				waypoint.setRadius(Double.parseDouble(value));
				break;
			case IECWaypoint.REVISION:
				waypoint.setRevision(Integer.parseInt(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Waypoint");
				break;
			}
		}
	}

	private static Date parseDateTime(String value) {
		Date parsedDate = new Date();
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			if (value.charAt(value.length() - 1) == 'Z') {
				df.setTimeZone(TimeZone.getTimeZone("UTC"));
			}
			parsedDate = df.parse(value);
		} catch (Exception e2) {
			e2.printStackTrace();
			return null;
		}
		return parsedDate;
	}
}
