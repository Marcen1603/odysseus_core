package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class Waypoint implements IIecElement {
	// map constants
	private static final String ELEMENT_PREFIX = "_wayP";

	public static final String ID = "id";
	public static final String REVISION = "revision";
	public static final String NAME = "name";
	public static final String RADIUS = "radius";

	private static final String OPEN_TAG = "<waypoint";
	private static final String CLOSE_TAG = "</waypoint>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private Integer id;
	private Integer revision;
	private String name;
	private Double radius;
	private Position position;
	private Leg leg;
	private List<Extension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map, String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";
		
		if (id != null)
			map.addAttributeValue(elementPrefix+ID, id);
		if (revision != null)
			map.addAttributeValue(elementPrefix+REVISION, revision);
		if (name != null)
			map.addAttributeValue(elementPrefix+NAME, name);
		if (radius != null)
			map.addAttributeValue(elementPrefix+RADIUS, radius);
		if (position != null)
			position.fillMap(map, prefix);
		if (leg != null)
			leg.fillMap(map, prefix);

		if (extensions != null && !extensions.isEmpty()) {
			for (Extension extension : extensions) {
				extension.fillMap(map, prefix);
			}
		}
	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (id != null)
			IECStringHelper.appendIntElement(builder, ID, id);
		if (revision != null)
			IECStringHelper.appendIntElement(builder, REVISION, revision);
		if (name != null)
			IECStringHelper.appendStringElement(builder, NAME, name);
		if (radius != null)
			IECStringHelper.appendDoubleElement(builder, RADIUS, radius);

		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");
			if (position != null)
				builder.append(position.toXML());
			if (leg != null)
				builder.append(leg.toXML());

			if (extensions != null) {
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
		if (position == null && leg == null
				&& (extensions == null || extensions.isEmpty())) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid(){
		if (id != null){
			return true;			
		}
		return false;
	}
	
	public void addExtension(Extension extension) {
		if (extensions == null)
			extensions = new ArrayList<Extension>();
		extensions.add(extension);
	}

	public Leg getLeg() {
		return leg;
	}

	public void setLeg(Leg leg) {
		this.leg = leg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public List<Extension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<Extension> extensions) {
		this.extensions = extensions;
	}

}
