package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Waypoint;

public class ToIVEFConverterHelper {

	@SuppressWarnings("unused")
	public static MSG_VesselData convertShipRouteToIVEF(RouteDataItem routeDataItem) {
		Route receivedRoute = routeDataItem.getRoute();
		List<Waypoint> receivedWaypoints = routeDataItem.getRoute()
				.getWaypoints();
		
		// Create IVEF Elements
		MSG_VesselData vesselData = new MSG_VesselData();
		
		Header header = new Header();
		vesselData.setHeader(header);

		Body body = new Body();
		
		for (Waypoint waypoint : receivedWaypoints) {
			
		}
		
		vesselData.setBody(body);
		return vesselData;
	}

	@SuppressWarnings("unused")
	public static MSG_VesselData convertManoeuvreToIVEF(
			ManoeuvrePlanDataItem manoeuvrePlanDataItem) {
		ManoeuvrePlan receivedMPlan = manoeuvrePlanDataItem.getMplan();
		List<ManoeuvrePoint> receivedManoeuvrePoints = receivedMPlan
				.getMpoints();
		
		// Create IVEF Elements
		MSG_VesselData vesselData = new MSG_VesselData();
		
		Header header = new Header();
		vesselData.setHeader(header);

		Body body = new Body();
		
		for (ManoeuvrePoint manoeuvrePoint : receivedManoeuvrePoints) {
			
		}
		
		vesselData.setBody(body);
		return vesselData;	}

}
