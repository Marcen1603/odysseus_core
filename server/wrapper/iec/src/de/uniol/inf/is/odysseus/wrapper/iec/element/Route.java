package de.uniol.inf.is.odysseus.wrapper.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.iec.helper.IECStringHelper;

public class Route implements IIecElement {
	public static final String VERSION = "version";

	// map constants
	private static final String ELEMENT_PREFIX = "route";

	private static final String OPEN_TAG = "<route";
	private static final String CLOSE_TAG = "</route>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private String version;
	private RouteInfo routeInfo;
	private Schedules schedules;
	private Waypoints waypoints;
	private List<Extension> extensions;

	public KeyValueObject<? extends IMetaAttribute> toMap() {
		KeyValueObject<? extends IMetaAttribute> map = new KeyValueObject<>();
		fillMap(map, "");
		return map;
	}

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";
		
		if (version != null)
			map.addAttributeValue(elementPrefix+VERSION, version);

		// fill map with values from composites
		if (routeInfo != null)
			routeInfo.fillMap(map, prefix);
		if (schedules != null)
			schedules.fillMap(map, prefix);
		if (waypoints != null)
			waypoints.fillMap(map, prefix);
		if (extensions != null) {
			for (Extension extension : extensions) {
				extension.fillMap(map, prefix);
			}
		}
	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (version != null) {
			IECStringHelper.appendStringElement(builder, VERSION, version);
		} else {
			return null; // value is mandatory
		}

		builder.append(">");

		if (routeInfo != null) {
			builder.append(routeInfo.toXML());
		} else {
			return null; // value is mandatory
		}

		if (schedules != null) {
			builder.append(schedules.toXML());
		} else {
			return null; // value is mandatory
		}

		if (waypoints != null) {
			builder.append(waypoints.toXML());
		} else {
			return null; // value is mandatory
		}

		if (extensions != null && !extensions.isEmpty()) {
			builder.append(OPEN_EXTENSIONS_TAG);
			for (Extension extension : extensions) {
				builder.append(extension.toXML());
			}
			builder.append(CLOSE_EXTENSIONS_TAG);
		}

		builder.append(CLOSE_TAG);
		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		return true;
	}

	@Override
	public boolean isValid() {
		if (version == null || version.isEmpty()) {
			return false;
		}
		return true;
	}

	public void addExtension(Extension extension) {
		if (extensions == null)
			extensions = new ArrayList<Extension>();
		extensions.add(extension);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public RouteInfo getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(RouteInfo routeInfo) {
		this.routeInfo = routeInfo;
	}

	public List<Extension> getExtension() {
		return extensions;
	}

	public void setExtension(List<Extension> extensions) {
		this.extensions = extensions;
	}

	public Schedules getSchedules() {
		return schedules;
	}

	public void setSchedules(Schedules schedules) {
		this.schedules = schedules;
	}

	public Waypoints getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(Waypoints waypoints) {
		this.waypoints = waypoints;
	}

}
