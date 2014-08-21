package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator;

import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionPlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.enums.DataItemTypes;

public class ToShipRouteConverter {

	public static IShipRouteRootElement convertIECToShipRoute(
			IECRoute iecRoute) {
		RouteDataItem routeDataItem = new RouteDataItem();
		routeDataItem.setData_item_id(DataItemTypes.Route.toString());
		
		Route route = new Route();
		// TODO set values
		
		routeDataItem.setRoute(route);
		return routeDataItem;
	}
	

	public static IShipRouteRootElement convertIECToPrediction(
			IECRoute iecRoute) {
		PredictionDataItem predictionDataItem = new PredictionDataItem();
		predictionDataItem.setData_item_id(DataItemTypes.Prediction.toString());
		
		PredictionPlan predictionPlan = new PredictionPlan();
		// TODO set values
		
		predictionDataItem.setMplan(predictionPlan);
		return predictionDataItem;
	}

	public static IShipRouteRootElement convertIECToManoeuvre(
			IECRoute iecRoute) {
		ManoeuvrePlanDataItem manoeuvrePlanDataItem = new ManoeuvrePlanDataItem();
		manoeuvrePlanDataItem.setData_item_id(DataItemTypes.MPlan.toString());
		
		ManoeuvrePlan manoeuvrePlan = new ManoeuvrePlan();
		// TODO set values

		manoeuvrePlanDataItem.setMplan(manoeuvrePlan);
		return manoeuvrePlanDataItem;
	}

}
