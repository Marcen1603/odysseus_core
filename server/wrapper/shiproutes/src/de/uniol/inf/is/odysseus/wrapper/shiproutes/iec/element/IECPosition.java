package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class IECPosition implements IIecElement {
	public static final String LAT = "lat";
	public static final String LON = "lon";

	// map constants
	private static final String ELEMENT_PREFIX = "_pos";

	private static final String OPEN_TAG = "<position";

	private Double latitude;
	private Double longitude;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";
		
		if (latitude != null)
			map.addAttributeValue(elementPrefix+LAT, latitude);
		if (longitude != null)
			map.addAttributeValue(elementPrefix+LON, longitude);
	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (latitude != null)
			IECStringHelper.appendDoubleElement(builder, LAT, latitude);
		if (longitude != null)
			IECStringHelper.appendDoubleElement(builder, LON, longitude);
		builder.append("/>");
		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		return false;
	}

	@Override
	public boolean isValid() {
		if (latitude != null && longitude != null) {
			return true;
		}
		return false;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public void addExtension(IECExtension extension) {
		// noOp because position has no extension
	}

	@Override
	public List<IECExtension> getExtensions() {
		return null;
	}

}
