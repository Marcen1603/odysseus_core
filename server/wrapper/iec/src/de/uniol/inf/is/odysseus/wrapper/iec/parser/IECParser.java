package de.uniol.inf.is.odysseus.wrapper.iec.parser;

import java.io.StringReader;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.uniol.inf.is.odysseus.wrapper.iec.element.Calculated;
import de.uniol.inf.is.odysseus.wrapper.iec.element.DefaultWaypoint;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Extension;
import de.uniol.inf.is.odysseus.wrapper.iec.element.IIecElement;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Leg;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Manual;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Position;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Route;
import de.uniol.inf.is.odysseus.wrapper.iec.element.RouteInfo;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Schedule;
import de.uniol.inf.is.odysseus.wrapper.iec.element.ScheduleElement;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Schedules;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Waypoint;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Waypoints;
import de.uniol.inf.is.odysseus.wrapper.iec.enums.IECElementType;

public class IECParser extends DefaultHandler {

	private final Logger LOG = LoggerFactory.getLogger(IECParser.class);

	private SAXParser parser;
	private Route iec;
	private Stack<IIecElement> elementStack = new Stack<IIecElement>();

	public IECParser() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			parser = factory.newSAXParser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String namespaceUri, String localName,
			String qName, Attributes atts) throws SAXException {

		IIecElement newElement = null;

		IECElementType elementType = IECElementType.parse(qName);
		if (elementType == null) {
			LOG.debug("TagName " + qName + " could not been processed");
			return;
		}

		switch (elementType) {
		case calculated:
			Calculated calculated = new Calculated();
			// no attributes need to be set
			newElement = calculated;
			break;
		case defaultWaypoint:
			DefaultWaypoint defaultWaypoint = new DefaultWaypoint();
			IECParserHelper.setDefaultWaypointAttributes(atts, defaultWaypoint);
			newElement = defaultWaypoint;
			break;
		case extension:
			Extension extension = new Extension();
			IECParserHelper.setExtensionAttributes(atts, extension);
			newElement = extension;
			break;
		case leg:
			Leg leg = new Leg();
			IECParserHelper.setLegAttributes(atts, leg);
			newElement = leg;
			break;
		case manual:
			Manual manual = new Manual();
			// no attributes need to be set
			newElement = manual;
			break;
		case position:
			Position position = new Position();
			IECParserHelper.setPositionAttributes(atts, position);
			newElement = position;
			break;
		case route:
			Route route = new Route();
			IECParserHelper.setRouteAttributes(atts, route);
			newElement = route;
			break;
		case routeInfo:
			RouteInfo routeInfo = new RouteInfo();
			IECParserHelper.setRouteInfoAttributes(atts, routeInfo);
			newElement = routeInfo;
			break;
		case schedule:
			Schedule schedule = new Schedule();
			IECParserHelper.setScheduleAttributes(atts, schedule);
			newElement = schedule;
			break;
		case scheduleElement:
			ScheduleElement scheduleElement = new ScheduleElement();
			IECParserHelper.setScheduleElementAttributes(atts, scheduleElement);
			newElement = scheduleElement;
			break;
		case schedules:
			Schedules schedules = new Schedules();
			// no attributes need to be set
			newElement = schedules;
			break;
		case waypoint:
			Waypoint waypoint = new Waypoint();
			IECParserHelper.setWaypointAttributes(atts, waypoint);
			newElement = waypoint;
			break;
		case waypoints:
			Waypoints waypoints = new Waypoints();
			// no attributes need to be set
			newElement = waypoints;
			break;
		default:
			break;
		}

		if (newElement != null) {
			if (newElement.isValid()) {
				elementStack.push(newElement);
			} else {
				throw new SAXException("Validation Error");
			}
		}
	}

	@Override
	public void endElement(String namespaceUri, String localName, String qName)
			throws SAXException {
		IECElementType element = IECElementType.parse(qName);
		if (element == null) {
			LOG.debug("TagName " + qName + " could not been processed");
			return;
		}

		switch (element) {
		case calculated:
			Calculated calculated = (Calculated) (elementStack.pop());
			if (elementStack.peek() instanceof Schedule) {
				((Schedule) elementStack.peek()).setCalculated(calculated);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.calculated
						+ " elements");
			}
			break;
		case defaultWaypoint:
			DefaultWaypoint defaultWaypoint = (DefaultWaypoint) (elementStack
					.pop());
			if (elementStack.peek() instanceof Waypoints) {
				((Waypoints) elementStack.peek())
						.setDefaultWaypoint(defaultWaypoint);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.defaultWaypoint
						+ " elements");
			}
			break;
		case extension:
			Extension extension = (Extension) (elementStack.pop());
			if (elementStack.peek() instanceof IIecElement) {
				((IIecElement) elementStack.peek()).addExtension(extension);
			} else {
				// Stack only contains IIecElement's, so elementStack.peek()
				// needs to be an instance of it
			}
			break;
		case leg:
			Leg leg = (Leg) (elementStack.pop());
			if (elementStack.peek() instanceof DefaultWaypoint) {
				((DefaultWaypoint) elementStack.peek()).setLeg(leg);
			} else if (elementStack.peek() instanceof Waypoint) {
				((Waypoint) elementStack.peek()).setLeg(leg);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.leg
						+ " elements");
			}
			break;
		case manual:
			Manual manual = (Manual) (elementStack.pop());
			if (elementStack.peek() instanceof Schedule) {
				((Schedule) elementStack.peek()).setManual(manual);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.manual
						+ " elements");
			}
			break;
		case position:
			Position position = (Position) (elementStack.pop());
			if (elementStack.peek() instanceof Waypoint) {
				((Waypoint) elementStack.peek()).setPosition(position);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.position
						+ " elements");
			}
			break;
		case route:
			// Route is top-level element
			iec = (Route) (elementStack.pop());
			break;
		case routeInfo:
			RouteInfo routeInfo = (RouteInfo) (elementStack.pop());
			if (elementStack.peek() instanceof Route) {
				((Route) elementStack.peek()).setRouteInfo(routeInfo);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.routeInfo
						+ " elements");
			}
			break;
		case schedule:
			Schedule schedule = (Schedule) (elementStack.pop());
			if (elementStack.peek() instanceof Schedules) {
				((Schedules) elementStack.peek()).addSchedule(schedule);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.schedule
						+ " elements");
			}
			break;
		case scheduleElement:
			ScheduleElement scheduleElement = (ScheduleElement) (elementStack
					.pop());
			if (elementStack.peek() instanceof Manual) {
				((Manual) elementStack.peek())
						.addScheduleElement(scheduleElement);
			} else if (elementStack.peek() instanceof Calculated) {
				((Calculated) elementStack.peek())
						.addScheduleElement(scheduleElement);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.scheduleElement
						+ " elements");
			}
			break;
		case schedules:
			Schedules schedules = (Schedules) (elementStack.pop());
			if (elementStack.peek() instanceof Route) {
				((Route) elementStack.peek()).setSchedules(schedules);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.schedules
						+ " elements");
			}
			break;
		case waypoint:
			Waypoint waypoint = (Waypoint) (elementStack.pop());
			if (elementStack.peek() instanceof Waypoints) {
				((Waypoints) elementStack.peek()).addwaypoint(waypoint);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.waypoint
						+ " elements");
			}
			break;
		case waypoints:
			Waypoints waypoints = (Waypoints) (elementStack.pop());
			if (elementStack.peek() instanceof Route) {
				((Route) elementStack.peek()).setWaypoints(waypoints);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.waypoints
						+ " elements");
			}
			break;
		default:
			break;
		}
	}

	public boolean parse(String iecMessage) {
		try {
			parser.parse(new InputSource(new StringReader(iecMessage)), this);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
		return false;
	}

	public Route getIEC() {
		return iec;
	}

	public void resetParser() {
		iec = null;
		elementStack.clear();
	}

}
