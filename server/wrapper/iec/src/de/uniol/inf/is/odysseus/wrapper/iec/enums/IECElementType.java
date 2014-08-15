package de.uniol.inf.is.odysseus.wrapper.iec.enums;

public enum IECElementType {
	calculated,
	defaultWaypoint,
	extension,
	leg,
	manual,
	position,
	route,
	routeInfo,
	schedule,
	scheduleElement,
	schedules,
	waypoint,
	waypoints;
	
	public static IECElementType parse(String value) {
		for (IECElementType iecElement : IECElementType.class.getEnumConstants()) {
			if (iecElement.toString().equalsIgnoreCase(value)) {
				return iecElement;
			}
		}
		return null;
	}
}
