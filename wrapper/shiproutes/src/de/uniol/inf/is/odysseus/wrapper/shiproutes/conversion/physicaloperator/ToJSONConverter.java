package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.MSG_VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.MSG_IVEF;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.ObjectData;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECExtension;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECLeg;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECManual;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRoute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECRouteInfo;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECSchedule;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECScheduleElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element.IECWaypoint;
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
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionPoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Route;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteState;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.Waypoint;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.enums.DataItemTypes;

public class ToJSONConverter {

	private static final double MS_TO_KTS = 1.9438444924574;

	private static final Logger LOG = LoggerFactory
			.getLogger(ToJSONConverter.class);

	public static IShipRouteRootElement convertIECToJSON(IECRoute iecRoute) {
		if (!iecRoute.isValid()) {
			LOG.debug("IEC Element is invalid");
			return null;
		}

		RouteDataItem routeDataItem = new RouteDataItem();
		routeDataItem.setData_item_id(DataItemTypes.Route.toString());
		Route route = new Route();

		// Route Info
		IECRouteInfo iecRouteInfo = iecRoute.getRouteInfo();
		if (iecRouteInfo.getRouteName() != null)
			route.setRoute_label(iecRouteInfo.getRouteName());
		if (iecRouteInfo.getRouteAuthor() != null)
			route.setBaseSignature(iecRouteInfo.getRouteAuthor());
		if (iecRouteInfo.getVesselVoyage() != null)
			route.setRoute_ID(iecRouteInfo.getVesselVoyage());
		addExtensions(iecRouteInfo, route);

		List<Waypoint> waypoints = new ArrayList<Waypoint>();

		for (IECWaypoint iecWaypoint : iecRoute.getWaypoints().getWaypoints()) {
			Waypoint waypoint = new Waypoint();
			waypoint.setID(iecWaypoint.getId());
			if (iecWaypoint.getName() != null)
				waypoint.setLabel(iecWaypoint.getName());
			if (iecWaypoint.getRadius() != null)
				waypoint.setTurnradius_nm(iecWaypoint.getRadius());
			if (iecWaypoint.getPosition() != null) {
				waypoint.setLat_rad(Math.toRadians(iecWaypoint.getPosition()
						.getLatitude()));
				waypoint.setLon_rad(Math.toRadians(iecWaypoint.getPosition()
						.getLongitude()));
			}
			if (iecWaypoint.getLeg() != null) {
				IECLeg iecLeg = iecWaypoint.getLeg();
				if (iecLeg.getStarboardXTD() != null)
					waypoint.setCrosstracklimit_stbd_m(iecLeg.getStarboardXTD()
							.intValue());
				if (iecLeg.getPortsideXTD() != null)
					waypoint.setCrosstracklimit_port_m(iecLeg.getPortsideXTD()
							.intValue());
				if (iecLeg.getSpeedMin() != null)
					waypoint.setSpeedlimit_lower_kts(iecLeg.getSpeedMin());
				if (iecLeg.getSpeedMax() != null)
					waypoint.setSpeedlimit_upper_kts(iecLeg.getSpeedMax());
			}
			waypoints.add(waypoint);
		}

		for (IECSchedule schedule : iecRoute.getSchedules().getSchedules()) {
			// if (schedule.getCalculated() != null) {
			// IECCalculated calculated = schedule.getCalculated();
			// for (IECScheduleElement iecScheduleElement : calculated
			// .getScheduleElements()) {
			//
			// }
			// }
			if (schedule.getManual() != null) {
				IECManual manual = schedule.getManual();
				for (IECScheduleElement iecScheduleElement : manual
						.getScheduleElements()) {
					Waypoint waypoint = findOrCreateWaypoint(waypoints,
							iecScheduleElement.getWaypointID());
					if (iecScheduleElement.getEta() != null)
						waypoint.setETA(iecScheduleElement.getEta().getTime());
					if (iecScheduleElement.getSpeed() != null)
						waypoint.setSpeed_kts(iecScheduleElement.getSpeed());
				}
			}
		}

		route.setWaypoints(waypoints);
		route.setNumber_of_wp(waypoints.size());
		routeDataItem.setRoute(route);
		return routeDataItem;
	}

	private static Waypoint findOrCreateWaypoint(List<Waypoint> waypoints,
			Integer id) {
		for (Waypoint waypoint : waypoints) {
			if (waypoint.getID().equals(id)) {
				return waypoint;
			}
		}
		Waypoint waypoint = new Waypoint();
		waypoint.setID(id);
		return waypoint;
	}

	public static IShipRouteRootElement convertIECToPrediction(IECRoute iecRoute) {
		if (!iecRoute.isValid()) {
			LOG.debug("IEC Element is invalid");
			return null;
		}

		PredictionDataItem predictionDataItem = new PredictionDataItem();
		predictionDataItem.setData_item_id(DataItemTypes.Prediction.toString());

		PredictionPlan predictionPlan = new PredictionPlan();

		List<PredictionPoint> predictionPoints = new ArrayList<PredictionPoint>();
		for (IECWaypoint iecWaypoint : iecRoute.getWaypoints().getWaypoints()) {
			PredictionPoint predictionPoint = new PredictionPoint();
			predictionPoint.setID(iecWaypoint.getId());

			if (iecWaypoint.getRadius() != null)
				predictionPoint.setHeading_rad(iecWaypoint.getRadius());
			if (iecWaypoint.getPosition() != null) {
				predictionPoint.setLat_rad(Math.toRadians(iecWaypoint
						.getPosition().getLatitude()));
				predictionPoint.setLon_rad(Math.toRadians(iecWaypoint
						.getPosition().getLongitude()));
			}
			predictionPoints.add(predictionPoint);
		}
		predictionPlan.setNumber_of_Prediction_points(predictionPoints.size());
		predictionPlan.setPred_points(predictionPoints);
		predictionDataItem.setMplan(predictionPlan);
		return predictionDataItem;
	}

	public static IShipRouteRootElement convertIECToManoeuvre(IECRoute iecRoute) {
		if (!iecRoute.isValid()) {
			LOG.debug("IEC Element is invalid");
			return null;
		}

		ManoeuvrePlanDataItem manoeuvrePlanDataItem = new ManoeuvrePlanDataItem();
		manoeuvrePlanDataItem.setData_item_id(DataItemTypes.MPlan.toString());

		ManoeuvrePlan manoeuvrePlan = new ManoeuvrePlan();

		// Route Info
		IECRouteInfo iecRouteInfo = iecRoute.getRouteInfo();
		if (iecRouteInfo.getRouteName() != null)
			manoeuvrePlan.setMplan_label(iecRouteInfo.getRouteName());
		if (iecRouteInfo.getVesselVoyage() != null)
			manoeuvrePlan.setMplan_ID(iecRouteInfo.getVesselVoyage());

		List<ManoeuvrePoint> manoeuvrePoints = new ArrayList<ManoeuvrePoint>();
		for (IECWaypoint iecWaypoint : iecRoute.getWaypoints().getWaypoints()) {
			ManoeuvrePoint manoeuvrePoint = new ManoeuvrePoint();
			manoeuvrePoint.setID(iecWaypoint.getId());
			if (iecWaypoint.getName() != null)
				manoeuvrePoint.setLabel(iecWaypoint.getName());
			if (iecWaypoint.getRadius() != null)
				manoeuvrePoint.setHeading_rad(iecWaypoint.getRadius());
			if (iecWaypoint.getPosition() != null) {
				manoeuvrePoint.setLat_rad(Math.toRadians(iecWaypoint
						.getPosition().getLatitude()));
				manoeuvrePoint.setLon_rad(Math.toRadians(iecWaypoint
						.getPosition().getLongitude()));
			}
			manoeuvrePoints.add(manoeuvrePoint);
		}

		for (IECSchedule schedule : iecRoute.getSchedules().getSchedules()) {
			// if (schedule.getCalculated() != null) {
			// IECCalculated calculated = schedule.getCalculated();
			// for (IECScheduleElement iecScheduleElement : calculated
			// .getScheduleElements()) {
			//
			// }
			// }
			if (schedule.getManual() != null) {
				IECManual manual = schedule.getManual();
				for (IECScheduleElement iecScheduleElement : manual
						.getScheduleElements()) {
					ManoeuvrePoint manoeuvrePoint = findOrCreateManoeuvrePoint(
							manoeuvrePoints, iecScheduleElement.getWaypointID());

					if (iecScheduleElement.getWindSpeed() != null)
						manoeuvrePoint.setWind_speed_kts(iecScheduleElement
								.getWindSpeed());
					if (iecScheduleElement.getWindDirection() != null)
						manoeuvrePoint.setWind_dir_rad(Math
								.toRadians(iecScheduleElement
										.getWindDirection()));
					if (iecScheduleElement.getCurrentSpeed() != null)
						manoeuvrePoint.setCurrent_speed_kts(iecScheduleElement
								.getCurrentSpeed());
					if (iecScheduleElement.getCurrentDirection() != null)
						manoeuvrePoint.setCurrent_dir_rad(Math
								.toRadians(iecScheduleElement
										.getCurrentDirection()));

					// set RPM and Pitch
					addExtensions(iecScheduleElement, manoeuvrePoint);
					if (iecScheduleElement.getRpm() != null) {
						if (manoeuvrePoint.getRpm_command() == null) {
							manoeuvrePoint.setRpm_command(new Rpm());
						}
						manoeuvrePoint.getRpm_command().setRpm1_cmd_rpm(
								iecScheduleElement.getRpm());
					}
					if (iecScheduleElement.getPitch() != null) {
						if (manoeuvrePoint.getPitch_command() != null) {
							manoeuvrePoint.setPitch_command(new Pitch());
						}
						manoeuvrePoint.getPitch_command().setPitch1_cmd_perc(
								iecScheduleElement.getPitch().doubleValue());
					}

				}
			}
		}

		manoeuvrePlan.setNumber_of_mp(manoeuvrePoints.size());
		manoeuvrePlan.setMpoints(manoeuvrePoints);
		manoeuvrePlanDataItem.setMplan(manoeuvrePlan);
		return manoeuvrePlanDataItem;
	}

	private static ManoeuvrePoint findOrCreateManoeuvrePoint(
			List<ManoeuvrePoint> manoeuvrePoints, Integer waypointID) {
		for (ManoeuvrePoint manoeuvrePoint : manoeuvrePoints) {
			if (manoeuvrePoint.getID().equals(waypointID)) {
				return manoeuvrePoint;
			}
		}
		ManoeuvrePoint manoeuvrePoint = new ManoeuvrePoint();
		manoeuvrePoint.setID(waypointID);
		return manoeuvrePoint;
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

	public static List<IShipRouteRootElement> convertIVEFToRoute(
			MSG_VesselData msg_VesselData) {
		Map<Integer, RouteDataItem> routeDatas = new HashMap<Integer, RouteDataItem>();
		int countOfVesselDatas = msg_VesselData.getBody().countOfVesselDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfVesselDatas; i++) {
			VesselData vesselData = msg_VesselData.getBody().getVesselDataAt(i);
			if (vesselData.getPosReport() != null) {
				Route route = null;
				if (routeDatas.containsKey(vesselData.getPosReport().getId())) {
					route = routeDatas.get(vesselData.getPosReport().getId())
							.getRoute();
				} else {
					RouteDataItem routeDataItem = new RouteDataItem();
					routeDataItem.setData_item_id("Route");
					route = new Route();
					routeDataItem.setRoute(route);

					if (vesselData.countOfStaticDatas() > 0) {
						StaticData staticData = vesselData.getStaticDataAt(0);
						route.setRoute_ID(staticData.getSource());
						route.setRoute_label(staticData.getSourceName());
					}
					RouteState routeState = new RouteState();
					routeState.setHas_alarms(false);
					routeState.setHas_cautions(false);
					routeState.setHas_geometric_problems(false);
					routeState.setHas_warnings(false);
					route.setRoute_state(routeState);

					routeDatas.put(vesselData.getPosReport().getId(),
							routeDataItem);
				}

				Waypoint waypoint = new Waypoint();
				waypoint.setID(idCounter);
				if (vesselData.getPosReport().getPos() != null) {
					waypoint.setLat_rad(Math.toRadians(vesselData
							.getPosReport().getPos().getLat()));
					waypoint.setLon_rad(Math.toRadians(vesselData
							.getPosReport().getPos().getLong()));
				}
				waypoint.setTurnradius_nm(vesselData.getPosReport()
						.getRateOfTurn());
				waypoint.setSpeed_kts(vesselData.getPosReport().getSOG()
						* MS_TO_KTS);

				if (route.getWaypoints() == null) {
					route.setWaypoints(new ArrayList<Waypoint>());
				}
				route.getWaypoints().add(waypoint);
				route.setNumber_of_wp(route.getWaypoints().size());
				idCounter++;
			}
		}
		return new ArrayList<IShipRouteRootElement>(routeDatas.values());
	}

	public static List<IShipRouteRootElement> convertIVEFToPrediction(
			MSG_VesselData msg_VesselData) {
		Map<Integer, PredictionDataItem> predictionDatas = new HashMap<Integer, PredictionDataItem>();
		int countOfVesselDatas = msg_VesselData.getBody().countOfVesselDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfVesselDatas; i++) {
			VesselData vesselData = msg_VesselData.getBody().getVesselDataAt(i);
			if (vesselData.getPosReport() != null) {
				PredictionPlan predictionPlan = null;
				if (predictionDatas.containsKey(vesselData.getPosReport()
						.getId())) {
					predictionPlan = predictionDatas.get(
							vesselData.getPosReport().getId()).getMplan();
				} else {
					PredictionDataItem predictionDataItem = new PredictionDataItem();
					predictionDataItem.setData_item_id("Prediction");
					predictionPlan = new PredictionPlan();
					predictionDataItem.setMplan(predictionPlan);
					predictionDatas.put(vesselData.getPosReport().getId(),
							predictionDataItem);
				}

				PredictionPoint predictionPoint = new PredictionPoint();
				predictionPoint.setID(idCounter);
				if (vesselData.getPosReport().getPos() != null) {
					predictionPoint.setLat_rad(Math.toRadians(vesselData
							.getPosReport().getPos().getLat()));
					predictionPoint.setLon_rad(Math.toRadians(vesselData
							.getPosReport().getPos().getLong()));
				}
				if (vesselData.getPosReport().getCOG() != null)
					predictionPoint.setCourse_over_ground_rad(vesselData
							.getPosReport().getCOG());
				predictionPoint.setRate_of_turn(vesselData.getPosReport()
						.getRateOfTurn());
				if (predictionPlan.getPred_points() == null) {
					predictionPlan
							.setPred_points(new ArrayList<PredictionPoint>());
				}
				predictionPlan.getPred_points().add(predictionPoint);
				predictionPlan.setNumber_of_Prediction_points(predictionPlan
						.getPred_points().size());
				idCounter++;
			}
		}
		return new ArrayList<IShipRouteRootElement>(predictionDatas.values());
	}

	public static List<IShipRouteRootElement> convertIVEFToManoeuvre(
			MSG_VesselData msg_VesselData) {
		Map<Integer, ManoeuvrePlanDataItem> manoeuvreDatas = new HashMap<Integer, ManoeuvrePlanDataItem>();
		int countOfVesselDatas = msg_VesselData.getBody().countOfVesselDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfVesselDatas; i++) {
			VesselData vesselData = msg_VesselData.getBody().getVesselDataAt(i);
			if (vesselData.getPosReport() != null) {
				ManoeuvrePlan manoeuvrePlan = null;
				if (manoeuvreDatas.containsKey(vesselData.getPosReport()
						.getId())) {
					manoeuvrePlan = manoeuvreDatas.get(
							vesselData.getPosReport().getId()).getMplan();
				} else {
					ManoeuvrePlanDataItem manoeuvreDataItem = new ManoeuvrePlanDataItem();
					manoeuvreDataItem.setData_item_id("MPlan");
					manoeuvrePlan = new ManoeuvrePlan();
					if (vesselData.countOfStaticDatas() > 0) {
						StaticData staticData = vesselData.getStaticDataAt(0);
						manoeuvrePlan.setMplan_ID(staticData.getSource());
						manoeuvrePlan
								.setMplan_label(staticData.getSourceName());
					}
					manoeuvreDataItem.setMplan(manoeuvrePlan);
					manoeuvreDatas.put(vesselData.getPosReport().getId(),
							manoeuvreDataItem);
				}

				ManoeuvrePoint manoeuvrePoint = new ManoeuvrePoint();
				manoeuvrePoint.setID(idCounter);
				if (vesselData.getPosReport().getPos() != null) {
					manoeuvrePoint.setLat_rad(Math.toRadians(vesselData
							.getPosReport().getPos().getLat()));
					manoeuvrePoint.setLon_rad(Math.toRadians(vesselData
							.getPosReport().getPos().getLong()));
				}
				manoeuvrePoint.setSog_long_kts(vesselData.getPosReport()
						.getSOG());
				manoeuvrePoint.setCourse_over_ground_rad(vesselData
						.getPosReport().getCOG());
				manoeuvrePoint.setRate_of_turn(vesselData.getPosReport()
						.getRateOfTurn());

				if (manoeuvrePlan.getMpoints() == null) {
					manoeuvrePlan.setMpoints(new ArrayList<ManoeuvrePoint>());
				}
				manoeuvrePlan.getMpoints().add(manoeuvrePoint);
				idCounter++;
			}
		}
		return new ArrayList<IShipRouteRootElement>(manoeuvreDatas.values());
	}

	public static List<IShipRouteRootElement> convertIVEF025ToRoute(
			MSG_IVEF msg_ivef) {
		Map<Integer, RouteDataItem> routeDatas = new HashMap<Integer, RouteDataItem>();
		int countOfObjectDatas = msg_ivef.getBody().getObjectDatas()
				.countOfObjectDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfObjectDatas; i++) {
			ObjectData objectData = msg_ivef.getBody().getObjectDatas()
					.getObjectDataAt(i);
			if (objectData.getVesselDataAt(0) != null) {
				Route route = null;
				if (routeDatas.containsKey(objectData.getVesselDataAt(0)
						.getId())) {
					route = routeDatas.get(
							objectData.getVesselDataAt(0).getId()).getRoute();
				} else {
					RouteDataItem routeDataItem = new RouteDataItem();
					routeDataItem.setData_item_id("Route");
					route = new Route();
					routeDataItem.setRoute(route);

					if (objectData.countOfVesselDatas() > 0) {
						de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VesselData vesselData = objectData
								.getVesselDataAt(0);
						route.setRoute_ID(Integer.parseInt(vesselData
								.getSourceId()));
						route.setRoute_label(vesselData.getSourceName());
					}
					RouteState routeState = new RouteState();
					routeState.setHas_alarms(false);
					routeState.setHas_cautions(false);
					routeState.setHas_geometric_problems(false);
					routeState.setHas_warnings(false);
					route.setRoute_state(routeState);

					routeDatas.put(objectData.getVesselDataAt(0).getId(),
							routeDataItem);
				}

				for (int j = 0; j < objectData.getVoyageDataAt(0)
						.countOfWaypoints(); j++) {
					de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint voyageWaypoint = objectData
							.getVoyageDataAt(0).getWaypointAt(j);

					Waypoint waypoint = new Waypoint();
					waypoint.setID(idCounter);
					if (voyageWaypoint.getPos() != null) {
						waypoint.setLat_rad(Math.toRadians(voyageWaypoint
								.getPos().getLat()));
						waypoint.setLon_rad(Math.toRadians(voyageWaypoint
								.getPos().getLong()));
					}

					if (route.getWaypoints() == null) {
						route.setWaypoints(new ArrayList<Waypoint>());
					}
					route.getWaypoints().add(waypoint);
					idCounter++;
				}

				route.setNumber_of_wp(route.getWaypoints().size());
			}
		}
		return new ArrayList<IShipRouteRootElement>(routeDatas.values());
	}

	public static List<IShipRouteRootElement> convertIVEF025ToPrediction(
			MSG_IVEF msg_ivef) {
		Map<Integer, PredictionDataItem> predictionDatas = new HashMap<Integer, PredictionDataItem>();
		int countOfObjectDatas = msg_ivef.getBody().getObjectDatas()
				.countOfObjectDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfObjectDatas; i++) {
			ObjectData objectData = msg_ivef.getBody().getObjectDatas()
					.getObjectDataAt(i);
			if (objectData.getVesselDataAt(0) != null) {
				PredictionPlan predictionPlan = null;
				if (predictionDatas.containsKey(objectData.getVesselDataAt(0)
						.getId())) {
					predictionPlan = predictionDatas.get(
							objectData.getVesselDataAt(0).getId()).getMplan();
				} else {
					PredictionDataItem predictionDataItem = new PredictionDataItem();
					predictionDataItem.setData_item_id("Prediction");
					predictionPlan = new PredictionPlan();
					predictionDataItem.setMplan(predictionPlan);
					predictionDatas.put(objectData.getVesselDataAt(0).getId(),
							predictionDataItem);
				}

				for (int j = 0; j < objectData.getVoyageDataAt(0)
						.countOfWaypoints(); j++) {
					de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint voyageWaypoint = objectData
							.getVoyageDataAt(0).getWaypointAt(j);

					PredictionPoint predictionPoint = new PredictionPoint();
					predictionPoint.setID(idCounter);
					if (voyageWaypoint.getPos() != null) {
						predictionPoint.setLat_rad(Math
								.toRadians(voyageWaypoint.getPos().getLat()));
						predictionPoint.setLon_rad(Math
								.toRadians(voyageWaypoint.getPos().getLong()));
					}

					if (predictionPlan.getPred_points() == null) {
						predictionPlan
								.setPred_points(new ArrayList<PredictionPoint>());
					}
					predictionPlan.getPred_points().add(predictionPoint);
					idCounter++;
				}

				predictionPlan.setNumber_of_Prediction_points(predictionPlan
						.getPred_points().size());
			}
		}
		return new ArrayList<IShipRouteRootElement>(predictionDatas.values());
	}

	public static List<IShipRouteRootElement> convertIVEF025ToManoeuvre(
			MSG_IVEF msg_ivef) {
		Map<Integer, ManoeuvrePlanDataItem> manoeuvreDatas = new HashMap<Integer, ManoeuvrePlanDataItem>();
		int countOfObjectDatas = msg_ivef.getBody().getObjectDatas()
				.countOfObjectDatas();

		int idCounter = 0;

		for (int i = 0; i < countOfObjectDatas; i++) {
			ObjectData objectData = msg_ivef.getBody().getObjectDatas()
					.getObjectDataAt(i);
			if (objectData.getVesselDataAt(0) != null) {
				ManoeuvrePlan manoeuvrePlan = null;
				if (manoeuvreDatas.containsKey(objectData.getVesselDataAt(0)
						.getId())) {
					manoeuvrePlan = manoeuvreDatas.get(
							objectData.getVesselDataAt(0).getId()).getMplan();
				} else {
					ManoeuvrePlanDataItem manoeuvreDataItem = new ManoeuvrePlanDataItem();
					manoeuvreDataItem.setData_item_id("MPlan");
					manoeuvrePlan = new ManoeuvrePlan();
					if (objectData.countOfVesselDatas() > 0) {
						de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VesselData vesselData = objectData
								.getVesselDataAt(0);
						manoeuvrePlan.setMplan_ID(Integer.parseInt(vesselData
								.getSourceId()));
						manoeuvrePlan
								.setMplan_label(vesselData.getSourceName());
					}
					manoeuvreDataItem.setMplan(manoeuvrePlan);
					manoeuvreDatas.put(objectData.getVesselDataAt(0).getId(),
							manoeuvreDataItem);
				}

				for (int j = 0; j < objectData.getVoyageDataAt(0)
						.countOfWaypoints(); j++) {
					de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Waypoint voyageWaypoint = objectData
							.getVoyageDataAt(0).getWaypointAt(j);

					ManoeuvrePoint manoeuvrePoint = new ManoeuvrePoint();
					manoeuvrePoint.setID(idCounter);
					if (voyageWaypoint.getPos() != null) {
						manoeuvrePoint.setLat_rad(Math.toRadians(voyageWaypoint
								.getPos().getLat()));
						manoeuvrePoint.setLon_rad(Math.toRadians(voyageWaypoint
								.getPos().getLong()));
					}

					if (manoeuvrePlan.getMpoints() == null) {
						manoeuvrePlan
								.setMpoints(new ArrayList<ManoeuvrePoint>());
					}
					manoeuvrePlan.getMpoints().add(manoeuvrePoint);
					idCounter++;
				}
				manoeuvrePlan
						.setNumber_of_mp(manoeuvrePlan.getMpoints().size());
			}
		}
		return new ArrayList<IShipRouteRootElement>(manoeuvreDatas.values());
	}

}
