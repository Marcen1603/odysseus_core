package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class DefaultWaypoint implements IIecElement {
	public static final String RADIUS = "radius";

	// map constants
	private static final String ELEMENT_PREFIX = "_defWayP";

	private static final String OPEN_TAG = "<defaultWaypoint";
	private static final String CLOSE_TAG = "</defaultWaypoint>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private Double radius;
	private Leg leg;
	private List<Extension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";
		
		if (radius != null)
			map.addAttributeValue(elementPrefix+RADIUS, radius);

		if (leg != null)
			leg.fillMap(map, prefix);

		if (extensions != null) {
			for (Extension extension : extensions) {
				extension.fillMap(map, prefix);
			}
		}

	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (radius != null)
			IECStringHelper.appendDoubleElement(builder, RADIUS, radius);
		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");

			if (leg != null)
				builder.append(leg.toXML());

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
		if (leg == null && (extensions == null || extensions.isEmpty())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void addExtension(Extension extension) {
		if (extensions == null)
			extensions = new ArrayList<Extension>();
		extensions.add(extension);
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Leg getLeg() {
		return leg;
	}

	public void setLeg(Leg leg) {
		this.leg = leg;
	}

	public List<Extension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<Extension> extensions) {
		this.extensions = extensions;
	}

}
