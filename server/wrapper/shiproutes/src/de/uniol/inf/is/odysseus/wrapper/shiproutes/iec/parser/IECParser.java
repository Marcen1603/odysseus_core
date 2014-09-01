package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.parser;

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

import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECCalculated;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECDefaultWaypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IIecElement;
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
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECElementType;

public class IECParser extends DefaultHandler {

	private final Logger LOG = LoggerFactory.getLogger(IECParser.class);

	private SAXParser parser;
	private IECRoute iec;
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
			IECCalculated calculated = new IECCalculated();
			// no attributes need to be set
			newElement = calculated;
			break;
		case defaultWaypoint:
			IECDefaultWaypoint defaultWaypoint = new IECDefaultWaypoint();
			IECParserHelper.setDefaultWaypointAttributes(atts, defaultWaypoint);
			newElement = defaultWaypoint;
			break;
		case extension:
			IECExtension extension = new IECExtension();
			IECParserHelper.setExtensionAttributes(atts, extension);
			newElement = extension;
			break;
		case leg:
			IECLeg leg = new IECLeg();
			IECParserHelper.setLegAttributes(atts, leg);
			newElement = leg;
			break;
		case manual:
			IECManual manual = new IECManual();
			// no attributes need to be set
			newElement = manual;
			break;
		case position:
			IECPosition position = new IECPosition();
			IECParserHelper.setPositionAttributes(atts, position);
			newElement = position;
			break;
		case route:
			IECRoute route = new IECRoute();
			IECParserHelper.setRouteAttributes(atts, route);
			newElement = route;
			break;
		case routeInfo:
			IECRouteInfo routeInfo = new IECRouteInfo();
			IECParserHelper.setRouteInfoAttributes(atts, routeInfo);
			newElement = routeInfo;
			break;
		case schedule:
			IECSchedule schedule = new IECSchedule();
			IECParserHelper.setScheduleAttributes(atts, schedule);
			newElement = schedule;
			break;
		case scheduleElement:
			IECScheduleElement scheduleElement = new IECScheduleElement();
			IECParserHelper.setScheduleElementAttributes(atts, scheduleElement);
			newElement = scheduleElement;
			break;
		case schedules:
			IECSchedules schedules = new IECSchedules();
			// no attributes need to be set
			newElement = schedules;
			break;
		case waypoint:
			IECWaypoint waypoint = new IECWaypoint();
			IECParserHelper.setWaypointAttributes(atts, waypoint);
			newElement = waypoint;
			break;
		case waypoints:
			IECWaypoints waypoints = new IECWaypoints();
			// no attributes need to be set
			newElement = waypoints;
			break;
		default:
			break;
		}

		if (newElement != null) {
			elementStack.push(newElement);
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
			IECCalculated calculated = (IECCalculated) (elementStack.pop());
			if (elementStack.peek() instanceof IECSchedule) {
				((IECSchedule) elementStack.peek()).setCalculated(calculated);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.calculated
						+ " elements");
			}
			break;
		case defaultWaypoint:
			IECDefaultWaypoint defaultWaypoint = (IECDefaultWaypoint) (elementStack
					.pop());
			if (elementStack.peek() instanceof IECWaypoints) {
				((IECWaypoints) elementStack.peek())
						.setDefaultWaypoint(defaultWaypoint);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.defaultWaypoint
						+ " elements");
			}
			break;
		case extension:
			IECExtension extension = (IECExtension) (elementStack.pop());
			// if (elementStack.peek() instanceof IIecElement) {
			elementStack.peek().addExtension(extension);
			// } else {
			// // Stack only contains IIecElement's, so elementStack.peek()
			// // needs to be an instance of it
			// }
			break;
		case leg:
			IECLeg leg = (IECLeg) (elementStack.pop());
			if (elementStack.peek() instanceof IECDefaultWaypoint) {
				((IECDefaultWaypoint) elementStack.peek()).setLeg(leg);
			} else if (elementStack.peek() instanceof IECWaypoint) {
				((IECWaypoint) elementStack.peek()).setLeg(leg);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.leg
						+ " elements");
			}
			break;
		case manual:
			IECManual manual = (IECManual) (elementStack.pop());
			if (elementStack.peek() instanceof IECSchedule) {
				((IECSchedule) elementStack.peek()).setManual(manual);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.manual
						+ " elements");
			}
			break;
		case position:
			IECPosition position = (IECPosition) (elementStack.pop());
			if (elementStack.peek() instanceof IECWaypoint) {
				((IECWaypoint) elementStack.peek()).setPosition(position);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.position
						+ " elements");
			}
			break;
		case route:
			// Route is top-level element
			iec = (IECRoute) (elementStack.pop());
			break;
		case routeInfo:
			IECRouteInfo routeInfo = (IECRouteInfo) (elementStack.pop());
			if (elementStack.peek() instanceof IECRoute) {
				((IECRoute) elementStack.peek()).setRouteInfo(routeInfo);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.routeInfo
						+ " elements");
			}
			break;
		case schedule:
			IECSchedule schedule = (IECSchedule) (elementStack.pop());
			if (elementStack.peek() instanceof IECSchedules) {
				((IECSchedules) elementStack.peek()).addSchedule(schedule);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.schedule
						+ " elements");
			}
			break;
		case scheduleElement:
			IECScheduleElement scheduleElement = (IECScheduleElement) (elementStack
					.pop());
			if (elementStack.peek() instanceof IECManual) {
				((IECManual) elementStack.peek())
						.addScheduleElement(scheduleElement);
			} else if (elementStack.peek() instanceof IECCalculated) {
				((IECCalculated) elementStack.peek())
						.addScheduleElement(scheduleElement);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.scheduleElement
						+ " elements");
			}
			break;
		case schedules:
			IECSchedules schedules = (IECSchedules) (elementStack.pop());
			if (elementStack.peek() instanceof IECRoute) {
				((IECRoute) elementStack.peek()).setSchedules(schedules);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.schedules
						+ " elements");
			}
			break;
		case waypoint:
			IECWaypoint waypoint = (IECWaypoint) (elementStack.pop());
			if (elementStack.peek() instanceof IECWaypoints) {
				((IECWaypoints) elementStack.peek()).addwaypoint(waypoint);
			} else {
				LOG.debug("Parent element " + elementStack.peek().getClass()
						+ " is not valid for " + IECElementType.waypoint
						+ " elements");
			}
			break;
		case waypoints:
			IECWaypoints waypoints = (IECWaypoints) (elementStack.pop());
			if (elementStack.peek() instanceof IECRoute) {
				((IECRoute) elementStack.peek()).setWaypoints(waypoints);
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

	public IECRoute getIEC() {
		if (iec.isValid()) {
			return iec;
		} else {
			LOG.debug("IEC Element is invalid => not processed");
			return null;
		}		
	}

	public void resetParser() {
		iec = null;
		elementStack.clear();
	}

}
