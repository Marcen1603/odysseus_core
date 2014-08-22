package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class IECExtension implements IIecElement {
	// map constants
	private static final String ELEMENT_PREFIX = "_ext";

	private static final String OPEN_TAG = "<extension";
	private static final String CLOSE_TAG = "</extension>";

	public static final String MANUFACTURER = "manufacturer";
	public static final String NAME = "name";
	public static final String VERSION = "version";

	private String manufacturer;
	private String name;
	private String version;
	private Map<String, Object> extensionValues;
	
	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		if (name != null && !name.isEmpty()){
			if (!name.startsWith("_")){
				prefix += "_"+name;
			} else{
				prefix += name;				
			}
		} else {
			prefix += ELEMENT_PREFIX;			
		}
		String elementPrefix = prefix + "_";
		
		if (manufacturer != null)
			map.addAttributeValue(elementPrefix+MANUFACTURER, manufacturer);
		if (name != null)
			map.addAttributeValue(elementPrefix+NAME, name);
		if (version != null)
			map.addAttributeValue(elementPrefix+VERSION, version);
		
		if (extensionValues != null && !extensionValues.isEmpty()){
			for (String key : extensionValues.keySet()) {
				map.addAttributeValue(key, extensionValues.get(key).toString());
			}
		}
	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (manufacturer != null)
			IECStringHelper.appendStringElement(builder, MANUFACTURER,
					manufacturer);
		if (name != null)
			IECStringHelper.appendStringElement(builder, NAME, name);
		if (version != null)
			IECStringHelper.appendStringElement(builder, VERSION, version);
		builder.append(">");

		if (extensionValues != null && !extensionValues.isEmpty()){
			for (String key : extensionValues.keySet()) {
				builder.append("<"+key+" value=\""+extensionValues.get(key)+"\"/>");
			}
		}

		builder.append(CLOSE_TAG);
		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		return !extensionValues.isEmpty();
	}

	@Override
	public boolean isValid() {
		if (manufacturer == null || manufacturer.isEmpty()) {
			return false;
		}
		return true;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public void addExtension(IECExtension extension) {
		// noOp because extension can not have extensions
	}

	public Map<String, Object> getExtensionValues() {
		return extensionValues;
	}

	public void setExtensionValues(Map<String, Object> extensionValues) {
		this.extensionValues = extensionValues;
	}
	
	public void addExtensionValue(String key, Object value){
		if (extensionValues == null){
			extensionValues = new HashMap<String, Object>();
		}
		extensionValues.put(key, value);
	}

}
