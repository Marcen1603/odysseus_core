package de.uniol.inf.is.odysseus.wrapper.iec.parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import de.uniol.inf.is.odysseus.wrapper.iec.element.DefaultWaypoint;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Extension;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Leg;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Position;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Route;
import de.uniol.inf.is.odysseus.wrapper.iec.element.RouteInfo;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Schedule;
import de.uniol.inf.is.odysseus.wrapper.iec.element.ScheduleElement;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Waypoint;
import de.uniol.inf.is.odysseus.wrapper.iec.enums.GeometryType;

public class IECParserHelper {

	private static final Logger LOG = LoggerFactory.getLogger(IECParserHelper.class);
	
	public static void setDefaultWaypointAttributes(Attributes atts,
			DefaultWaypoint defaultWaypoint) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case DefaultWaypoint.RADIUS:
				defaultWaypoint.setRadius(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element DefaultWaypoint");
				break;
			}
		}
	}

	public static void setExtensionAttributes(Attributes atts,
			Extension extension) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case Extension.MANUFACTURER:
				extension.setManufacturer(value);
				break;
			case Extension.NAME:
				extension.setName(value);
				break;
			case Extension.VERSION:
				extension.setVersion(value);
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Extension");
				break;
			}
		}
	}

	public static void setLegAttributes(Attributes atts, Leg leg) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case Leg.DRAUGHT:
				leg.setDraught(Double.parseDouble(value));
				break;
			case Leg.DRAUGHT_AFT:
				leg.setDraughtAft(Double.parseDouble(value));
				break;
			case Leg.DRAUGHT_FORWARD:
				leg.setDraughtForward(Double.parseDouble(value));
				break;
			case Leg.DYNAMIC_UKC:
				leg.setDynamicUKC(Double.parseDouble(value));
				break;
			case Leg.GEOMETRY_TYPE:
				GeometryType type = GeometryType.parse(value);
				if (type != null)
					leg.setGeometryType(type);
				break;
			case Leg.LEG_INFO:
				leg.setLegInfo(value);
				break;
			case Leg.LEG_NOTE1:
				leg.setLegNote1(value);
				break;
			case Leg.LEG_NOTE2:
				leg.setLegNote2(value);
				break;
			case Leg.LEG_REPORT:
				leg.setLegReport(value);
				break;
			case Leg.MASTHEAD:
				leg.setMasthead(Double.parseDouble(value));
				break;
			case Leg.PORTSIDE_XTD:
				leg.setPortsideXTD(Double.parseDouble(value));
				break;
			case Leg.SAFETY_CONTOUR:
				leg.setSafetyContour(Double.parseDouble(value));
				break;
			case Leg.SAFETY_DEPTH:
				leg.setSafetyDepth(Double.parseDouble(value));
				break;
			case Leg.SPEED_MAX:
				leg.setSpeedMax(Double.parseDouble(value));
				break;
			case Leg.SPEED_MIN:
				leg.setSpeedMin(Double.parseDouble(value));
				break;
			case Leg.STARBOARD_XTD:
				leg.setStarboardXTD(Double.parseDouble(value));
				break;
			case Leg.STATIC_UKC:
				leg.setStaticUKC(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Leg");
				break;
			}
		}
	}

	public static void setPositionAttributes(Attributes atts, Position position) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case Position.LAT:
				position.setLatitude(Double.parseDouble(value));
				break;
			case Position.LON:
				position.setLongitude(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Position");
				break;
			}
		}
	}

	public static void setRouteAttributes(Attributes atts, Route route) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key.toLowerCase()) {
			case Route.VERSION:
				route.setVersion(value);
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Route");
				break;
			}
		}
	}

	public static void setRouteInfoAttributes(Attributes atts,
			RouteInfo routeInfo) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case RouteInfo.OPTIMIZATION_METHOD:
				routeInfo.setOptimizationMethod(value);
				break;
			case RouteInfo.ROUTE_AUTHOR:
				routeInfo.setRouteAuthor(value);
				break;
			case RouteInfo.ROUTE_CHANGES_HISTORY:
				routeInfo.setRouteChangesHistory(value);
				break;
			case RouteInfo.ROUTE_NAME:
				routeInfo.setRouteName(value);
				break;
			case RouteInfo.ROUTE_STATUS:
				routeInfo.setRouteStatus(value);
				break;
			case RouteInfo.VALIDITY_PERIOD_START:
				Date validityPeriodStart = parseDateTime(value);
				if (validityPeriodStart != null)
					routeInfo.setValidityPeriodStart(validityPeriodStart);
				break;
			case RouteInfo.VALIDITY_PERIOD_STOP:
				Date validityPeriodStop = parseDateTime(value);
				if (validityPeriodStop != null)
					routeInfo.setValidityPeriodStop(validityPeriodStop);
				break;
			case RouteInfo.VESSEL_CARGO:
				routeInfo.setVesselCargo(Integer.parseInt(value));
				break;
			case RouteInfo.VESSEL_DISPLACEMENT:
				routeInfo.setVesselDisplacement(Integer.parseInt(value));
				break;
			case RouteInfo.VESSEL_GM:
				routeInfo.setVesselGM(Double.parseDouble(value));
				break;
			case RouteInfo.VESSEL_IMO:
				routeInfo.setVesselIMO(Integer.parseInt(value));
				break;
			case RouteInfo.VESSEL_MAX_ROLL:
				routeInfo.setVesselMaxRoll(Integer.parseInt(value));
				break;
			case RouteInfo.VESSEL_MAX_WAVE:
				routeInfo.setVesselMaxWave(Double.parseDouble(value));
				break;
			case RouteInfo.VESSEL_MAX_WIND:
				routeInfo.setVesselMaxWind(Double.parseDouble(value));
				break;
			case RouteInfo.VESSEL_MMSI:
				routeInfo.setVesselMMSI(Integer.parseInt(value));
				break;
			case RouteInfo.VESSEL_NAME:
				routeInfo.setVesselName(value);
				break;
			case RouteInfo.VESSEL_SERVICE_MAX:
				routeInfo.setVesselServiceMax(Double.parseDouble(value));
				break;
			case RouteInfo.VESSEL_SERVICE_MIN:
				routeInfo.setVesselServiceMin(Double.parseDouble(value));
				break;
			case RouteInfo.VESSEL_SPEED_MAX:
				routeInfo.setVesselSpeedMax(Double.parseDouble(value));
				break;
			case RouteInfo.VESSEL_VOYAGE:
				routeInfo.setVesselVoyage(Integer.parseInt(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element RouteInfo");
				break;
			}
		}
	}

	public static void setScheduleAttributes(Attributes atts, Schedule schedule) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case Schedule.ID:
				schedule.setId(Integer.parseInt(value));
				break;
			case Schedule.NAME:
				schedule.setName(value);
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element Schedule");
				break;
			}
		}
	}

	public static void setScheduleElementAttributes(Attributes atts,
			ScheduleElement scheduleElement) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case ScheduleElement.ABS_FUEL_SAVE:
				scheduleElement.setAbsFuelSave(Double.parseDouble(value));
				break;
			case ScheduleElement.CURRENT_DIRECTION:
				scheduleElement.setCurrentDirection(Double.parseDouble(value));
				break;
			case ScheduleElement.CURRENT_SPEED:
				scheduleElement.setCurrentSpeed(Double.parseDouble(value));
				break;
			case ScheduleElement.ETA:
				Date eta = parseDateTime(value);
				if (eta != null) scheduleElement.setEta(eta);
				break;
			case ScheduleElement.ETA_WINDOW_AFTER:
				scheduleElement.setEtaWindowAfter(Double.parseDouble(value));
				break;
			case ScheduleElement.ETA_WINDOW_BEFORE:
				scheduleElement.setEtaWindowBefore(Double.parseDouble(value));
				break;
			case ScheduleElement.ETD:
				Date etd = parseDateTime(value);
				if (etd != null) scheduleElement.setEtd(etd);
				break;
			case ScheduleElement.ETD_WINDOW_AFTER:
				scheduleElement.setEtdWindowAfter(Double.parseDouble(value));
				break;
			case ScheduleElement.ETD_WINDOW_BEFORE:
				scheduleElement.setEtdWindowBefore(Double.parseDouble(value));
				break;
			case ScheduleElement.FUEL:
				scheduleElement.setFuel(Double.parseDouble(value));
				break;
			case ScheduleElement.NOTE:
				scheduleElement.setNote(value);
				break;
			case ScheduleElement.PITCH:
				scheduleElement.setPitch(Integer.parseInt(value));
				break;
			case ScheduleElement.REL_FUEL_SAVE:
				scheduleElement.setRelFuelSave(Double.parseDouble(value));
				break;
			case ScheduleElement.RPM:
				scheduleElement.setRpm(Double.parseDouble(value));
				break;
			case ScheduleElement.SPEED:
				scheduleElement.setSpeed(Double.parseDouble(value));
				break;
			case ScheduleElement.SPEED_WINDOW:
				scheduleElement.setSpeedWindow(Double.parseDouble(value));
				break;
			case ScheduleElement.STAY:
				scheduleElement.setStay(Double.parseDouble(value));
				break;
			case ScheduleElement.TOTAL_LOSS:
				scheduleElement.setTotalLoss(Double.parseDouble(value));
				break;
			case ScheduleElement.WAVE_LOSS:
				scheduleElement.setWaveLoss(Double.parseDouble(value));
				break;
			case ScheduleElement.WAYPOINT_ID:
				scheduleElement.setWaypointID(Integer.parseInt(value));
				break;
			case ScheduleElement.WIND_DIRECTION:
				scheduleElement.setWindDirection(Double.parseDouble(value));
				break;
			case ScheduleElement.WIND_LOSS:
				scheduleElement.setWindLoss(Double.parseDouble(value));
				break;
			case ScheduleElement.WIND_SPEED:
				scheduleElement.setWindSpeed(Double.parseDouble(value));
				break;
			default:
				LOG.debug("could not set attribute "+key+" for element ScheduleElement");
				break;
			}
		}
	}

	public static void setWaypointAttributes(Attributes atts, Waypoint waypoint) {
		for (int i = 0; i < atts.getLength(); i++) {
			String key = atts.getLocalName(i);
			String value = atts.getValue(i);

			switch (key) {
			case Waypoint.ID:
				waypoint.setId(Integer.parseInt(value));
				break;
			case Waypoint.NAME:
				waypoint.setName(value);
				break;
			case Waypoint.RADIUS:
				waypoint.setRadius(Double.parseDouble(value));
				break;
			case Waypoint.REVISION:
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
