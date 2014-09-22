package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class IECWaypoints implements IIecElement {
	// map constants
	private static final String ELEMENT_PREFIX = "_wayPs";

	// XML Constants
	private static final String OPEN_TAG = "<waypoints";
	private static final String CLOSE_TAG = "</waypoints>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private IECDefaultWaypoint defaultWaypoint;
	private List<IECWaypoint> waypoints;
	private List<IECExtension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;

		if (defaultWaypoint != null)
			defaultWaypoint.fillMap(map, prefix);

		if (waypoints != null) {
			for (IECWaypoint waypoint : waypoints) {
				waypoint.fillMap(map, prefix);
			}
		}

		if (extensions != null) {
			for (IECExtension extension : extensions) {
				extension.fillMap(map, prefix);
			}
		}
	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");

			if (defaultWaypoint != null)
				builder.append(defaultWaypoint.toXML());

			if (waypoints != null) {
				for (IECWaypoint waypoint : waypoints) {
					builder.append(waypoint.toXML());
				}
			}

			if (extensions != null && !extensions.isEmpty()) {
				builder.append(OPEN_EXTENSIONS_TAG);
				for (IECExtension extension : extensions) {
					builder.append(extension.toXML());
				}
				builder.append(CLOSE_EXTENSIONS_TAG);
			}
			builder.append(CLOSE_TAG);
		}

		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		if (defaultWaypoint == null
				&& (waypoints == null || waypoints.isEmpty())
				&& (extensions == null || extensions.isEmpty())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		if (defaultWaypoint != null) {
			if (!defaultWaypoint.isValid())
				return false;
		}
		if (waypoints != null) {
			if (waypoints.size() < 2) {
				return false;
			} else {
				for (IECWaypoint waypoint : waypoints) {
					if (!waypoint.isValid())
						return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public void addwaypoint(IECWaypoint waypoint) {
		if (waypoints == null)
			waypoints = new ArrayList<IECWaypoint>();
		waypoints.add(waypoint);
	}

	@Override
	public void addExtension(IECExtension extension) {
		if (extensions == null)
			extensions = new ArrayList<IECExtension>();
		extensions.add(extension);
	}
	
	@Override
	public List<IECExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<IECExtension> extensions) {
		this.extensions = extensions;
	}

	public IECDefaultWaypoint getDefaultWaypoint() {
		return defaultWaypoint;
	}

	public void setDefaultWaypoint(IECDefaultWaypoint defaultWaypoint) {
		this.defaultWaypoint = defaultWaypoint;
	}

	public List<IECWaypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<IECWaypoint> waypoints) {
		this.waypoints = waypoints;
	}

}
