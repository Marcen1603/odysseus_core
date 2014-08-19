package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class Waypoints implements IIecElement {
	// map constants
	private static final String ELEMENT_PREFIX = "_wayPs";
	
	// XML Constants
	private static final String OPEN_TAG = "<waypoints";
	private static final String CLOSE_TAG = "</waypoints>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private DefaultWaypoint defaultWaypoint;
	private List<Waypoint> waypoints;
	private List<Extension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map, String prefix) {
		prefix += ELEMENT_PREFIX;
		
		if (defaultWaypoint != null)
			defaultWaypoint.fillMap(map, prefix);

		if (waypoints != null) {
			for (Waypoint waypoint : waypoints) {
				waypoint.fillMap(map, prefix);
			}
		}

		if (extensions != null) {
			for (Extension extension : extensions) {
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
				for (Waypoint waypoint : waypoints) {
					builder.append(waypoint.toXML());
				}
			}

			if (extensions != null && !extensions.isEmpty()) {
				builder.append(OPEN_EXTENSIONS_TAG);
				for (Extension extension : extensions) {
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
	public boolean isValid(){
		return true;
	}
	
	public void addwaypoint(Waypoint waypoint) {
		if (waypoints == null)
			waypoints = new ArrayList<Waypoint>();
		waypoints.add(waypoint);
	}
	
	public void addExtension(Extension extension) {
		if (extensions == null)
			extensions = new ArrayList<Extension>();
		extensions.add(extension);
	}

	public List<Extension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<Extension> extensions) {
		this.extensions = extensions;
	}

	public DefaultWaypoint getDefaultWaypoint() {
		return defaultWaypoint;
	}

	public void setDefaultWaypoint(DefaultWaypoint defaultWaypoint) {
		this.defaultWaypoint = defaultWaypoint;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

}
