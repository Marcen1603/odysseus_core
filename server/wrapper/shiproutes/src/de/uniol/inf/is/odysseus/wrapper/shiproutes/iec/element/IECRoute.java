package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class IECRoute implements IIecElement {
	public static final String VERSION = "version";

	// map constants
	private static final String ELEMENT_PREFIX = "route";

	private static final String OPEN_TAG = "<route";
	private static final String CLOSE_TAG = "</route>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private String version;
	private IECRouteInfo routeInfo;
	private IECSchedules schedules;
	private IECWaypoints waypoints;
	private List<IECExtension> extensions;

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
			for (IECExtension extension : extensions) {
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
			for (IECExtension extension : extensions) {
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

	@Override
	public void addExtension(IECExtension extension) {
		if (extensions == null)
			extensions = new ArrayList<IECExtension>();
		extensions.add(extension);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public IECRouteInfo getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(IECRouteInfo routeInfo) {
		this.routeInfo = routeInfo;
	}

	public List<IECExtension> getExtension() {
		return extensions;
	}

	public void setExtension(List<IECExtension> extensions) {
		this.extensions = extensions;
	}

	public IECSchedules getSchedules() {
		return schedules;
	}

	public void setSchedules(IECSchedules schedules) {
		this.schedules = schedules;
	}

	public IECWaypoints getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(IECWaypoints waypoints) {
		this.waypoints = waypoints;
	}

}
