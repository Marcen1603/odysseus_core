package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator;

import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRouteInfo;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IIecElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECExtensionTypes.ExtensionTypes;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECExtensionTypes.PitchExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECExtensionTypes.RouteInfoExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.IECExtensionTypes.RpmExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.Pitch;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.Rpm;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionPlan;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteState;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.enums.DataItemTypes;

public class ToShipRouteConverter {

	public static IShipRouteRootElement convertIECToShipRoute(IECRoute iecRoute) {
		if (!iecRoute.isValid()) {
			throw new IllegalArgumentException("IEC Element is not valid");
		}

		RouteDataItem routeDataItem = new RouteDataItem();
		routeDataItem.setData_item_id(DataItemTypes.Route.toString());
		Route route = new Route();

		IECRouteInfo iecRouteInfo = iecRoute.getRouteInfo();
		if (iecRouteInfo.getRouteName() != null)
			route.setRoute_label(iecRouteInfo.getRouteName());
		if (iecRouteInfo.getRouteAuthor() != null)
			route.setBaseSignature(iecRouteInfo.getRouteAuthor());
		addExtensions(iecRouteInfo, route);
		
		// TODO set values

		routeDataItem.setRoute(route);
		return routeDataItem;
	}

	

	public static IShipRouteRootElement convertIECToPrediction(IECRoute iecRoute) {
		if (!iecRoute.isValid()) {
			throw new IllegalArgumentException("IEC Element is not valid");
		}

		PredictionDataItem predictionDataItem = new PredictionDataItem();
		predictionDataItem.setData_item_id(DataItemTypes.Prediction.toString());

		PredictionPlan predictionPlan = new PredictionPlan();
		// TODO set values

		predictionDataItem.setMplan(predictionPlan);
		return predictionDataItem;
	}

	public static IShipRouteRootElement convertIECToManoeuvre(IECRoute iecRoute) {
		if (!iecRoute.isValid()) {
			throw new IllegalArgumentException("IEC Element is not valid");
		}

		ManoeuvrePlanDataItem manoeuvrePlanDataItem = new ManoeuvrePlanDataItem();
		manoeuvrePlanDataItem.setData_item_id(DataItemTypes.MPlan.toString());

		ManoeuvrePlan manoeuvrePlan = new ManoeuvrePlan();
		// TODO set values

		manoeuvrePlanDataItem.setMplan(manoeuvrePlan);
		return manoeuvrePlanDataItem;
	}

	
	private static void addExtensions(IIecElement iecElement,
			IShipRouteElement shipRouteElement) {
		if (iecElement.getExtensions() != null
				&& !iecElement.getExtensions().isEmpty()) {
			for (IECExtension iecExtension : iecElement.getExtensions()) {
				ExtensionTypes type = ExtensionTypes.parse(iecExtension
						.getName());
				if (type != null) {
					switch (type) {
					case RouteInfoExtension:
						if (shipRouteElement instanceof Route) {
							Route route = (Route) shipRouteElement;
							RouteState routeState = new RouteState();
							Boolean hasAlarms = (Boolean) iecExtension
									.getExtensionValues().get(
											RouteInfoExtension.has_alarms);
							if (hasAlarms != null)
								routeState.setHas_alarms(hasAlarms);
							Boolean hasCautions = (Boolean) iecExtension
									.getExtensionValues().get(
											RouteInfoExtension.has_cautions);
							if (hasCautions != null)
								routeState.setHas_cautions(hasCautions);
							Boolean hasGeometricProblems = (Boolean) iecExtension
									.getExtensionValues()
									.get(RouteInfoExtension.has_geometric_problems);
							if (hasGeometricProblems != null)
								routeState
										.setHas_geometric_problems(hasGeometricProblems);
							Boolean hasWarnings = (Boolean) iecExtension
									.getExtensionValues().get(
											RouteInfoExtension.has_warnings);
							if (hasWarnings != null)
								routeState.setHas_warnings(hasWarnings);

							route.setRoute_state(routeState);
						}
						break;
					case PitchExtension:
						if (shipRouteElement instanceof ManoeuvrePoint) {
							ManoeuvrePoint mPoint = (ManoeuvrePoint) shipRouteElement;
							Pitch pitch = new Pitch();

							Double pitch2cmd = (Double) iecExtension
									.getExtensionValues().get(
											PitchExtension.pitch2_cmd);
							if (pitch2cmd != null)
								pitch.setPitch2_cmd_perc(pitch2cmd);
							Double pitch3cmd = (Double) iecExtension
									.getExtensionValues().get(
											PitchExtension.pitch3_cmd);
							if (pitch3cmd != null)
								pitch.setPitch3_cmd_perc(pitch3cmd);
							Double pitch4cmd = (Double) iecExtension
									.getExtensionValues().get(
											PitchExtension.pitch4_cmd);
							if (pitch4cmd != null)
								pitch.setPitch2_cmd_perc(pitch4cmd);

							mPoint.setPitch_command(pitch);
						}
						break;
					case RpmExtension:
						if (shipRouteElement instanceof ManoeuvrePoint) {
							ManoeuvrePoint mPoint = (ManoeuvrePoint) shipRouteElement;
							Rpm rpm = new Rpm();

							Double rpm2cmd = (Double) iecExtension
									.getExtensionValues().get(
											RpmExtension.rpm2_cmd);
							if (rpm2cmd != null)
								rpm.setRpm2_cmd_rpm(rpm2cmd);
							Double rpm3cmd = (Double) iecExtension
									.getExtensionValues().get(
											RpmExtension.rpm3_cmd);
							if (rpm3cmd != null)
								rpm.setRpm3_cmd_rpm(rpm3cmd);
							Double rpm4cmd = (Double) iecExtension
									.getExtensionValues().get(
											RpmExtension.rpm4_cmd);
							if (rpm4cmd != null)
								rpm.setRpm2_cmd_rpm(rpm4cmd);

							mPoint.setRpm_command(rpm);
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}
}
