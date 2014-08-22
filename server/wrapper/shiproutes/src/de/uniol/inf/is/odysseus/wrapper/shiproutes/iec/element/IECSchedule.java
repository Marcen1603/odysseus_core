package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class IECSchedule implements IIecElement {

	public static final String ID = "id";
	public static final String NAME = "name";

	// map constants
	private static final String ELEMENT_PREFIX = "_sched";

	private static final String OPEN_TAG = "<schedule";
	private static final String CLOSE_TAG = "</schedule>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private Integer id;
	private String name;

	private IECManual manual;
	private IECCalculated calculated;
	private List<IECExtension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (id != null)
			map.addAttributeValue(elementPrefix + ID, id);
		if (name != null)
			map.addAttributeValue(elementPrefix + NAME, name);
		if (calculated != null)
			calculated.fillMap(map, prefix);
		if (getManual() != null)
			getManual().fillMap(map, prefix);
		if (extensions != null) {
			for (IECExtension extension : extensions) {
				extension.fillMap(map, prefix);
			}
		}

	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (id != null) {
			IECStringHelper.appendIntElement(builder, ID, id);
		} else {
			return null;
		}
		if (name != null)
			IECStringHelper.appendStringElement(builder, NAME, name);

		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");

			if (getManual() != null)
				builder.append(getManual().toXML());
			if (calculated != null)
				builder.append(calculated.toXML());
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
		if (calculated == null && getManual() == null
				&& (extensions == null || extensions.isEmpty())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		boolean allChildsValid = true;
		if (id == null) {
			return false;
		} else {
			if (calculated != null) {
				for (IECScheduleElement scheduleElement : calculated
						.getScheduleElements()) {
					if (!scheduleElement.isValid())
						allChildsValid = false;
				}
			}
			if (manual != null) {
				for (IECScheduleElement scheduleElement : manual
						.getScheduleElements()) {
					if (!scheduleElement.isValid())
						allChildsValid = false;
				}
			}
		}
		return allChildsValid;
	}

	@Override
	public void addExtension(IECExtension extension) {
		if (extensions == null)
			extensions = new ArrayList<IECExtension>();
		extensions.add(extension);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IECCalculated getCalculated() {
		return calculated;
	}

	public void setCalculated(IECCalculated calculated) {
		this.calculated = calculated;
	}

	@Override
	public List<IECExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<IECExtension> extensions) {
		this.extensions = extensions;
	}

	public IECManual getManual() {
		return manual;
	}

	public void setManual(IECManual manual) {
		this.manual = manual;
	}

}
