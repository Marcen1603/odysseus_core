package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public abstract class AbstractScheduleElementIdentifier implements IIecElement {

	// map constants
	private static final String ELEMENT_PREFIX_CALC = "_calc";
	private static final String ELEMENT_PREFIX_MAN = "_man";

	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private List<IECScheduleElement> scheduleElements;
	private List<IECExtension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		if (this instanceof IECCalculated) {
			prefix += ELEMENT_PREFIX_CALC;
		} else if (this instanceof IECManual) {
			prefix += ELEMENT_PREFIX_MAN;
		} else {
			return;
		}

		if (scheduleElements != null) {
			for (IECScheduleElement scheduleElement : scheduleElements) {
				scheduleElement.fillMap(map, prefix);
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
		String startTag;
		String endTag;

		if (this instanceof IECCalculated) {
			startTag = "<calculated";
			endTag = "</calculated>";
		} else if (this instanceof IECManual) {
			startTag = "<manual";
			endTag = "</manual>";
		} else {
			return null;
		}

		StringBuilder builder = new StringBuilder(startTag);
		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");

			if (scheduleElements != null) {
				for (IECScheduleElement scheduleElement : scheduleElements) {
					builder.append(scheduleElement.toXML());
				}
			}

			if (extensions != null) {
				builder.append(OPEN_EXTENSIONS_TAG);
				for (IECExtension extension : extensions) {
					builder.append(extension.toXML());
				}
				builder.append(CLOSE_EXTENSIONS_TAG);
			}

			builder.append(endTag);
		}
		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		if ((scheduleElements == null || scheduleElements.isEmpty())
				&& (extensions == null || extensions.isEmpty())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		for (IECScheduleElement element : scheduleElements) {
			if (!element.isValid())
				return false;
		}
		return true;
	}

	public void addScheduleElement(IECScheduleElement scheduleElement) {
		if (scheduleElements == null)
			scheduleElements = new ArrayList<IECScheduleElement>();
		scheduleElements.add(scheduleElement);
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

	public List<IECScheduleElement> getScheduleElements() {
		return scheduleElements;
	}

	public void setScheduleElements(List<IECScheduleElement> scheduleElements) {
		this.scheduleElements = scheduleElements;
	}
}
