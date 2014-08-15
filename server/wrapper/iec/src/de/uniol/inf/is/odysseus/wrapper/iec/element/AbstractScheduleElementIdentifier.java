package de.uniol.inf.is.odysseus.wrapper.iec.element;

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

	private List<ScheduleElement> scheduleElements;
	private List<Extension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map, String prefix) {
		if (this instanceof Calculated) {
			prefix += ELEMENT_PREFIX_CALC;			
		} else if (this instanceof Manual) {
			prefix += ELEMENT_PREFIX_MAN;
		} else {
			return;
		}
		
		if (scheduleElements != null) {
			for (ScheduleElement scheduleElement : scheduleElements) {
				scheduleElement.fillMap(map, prefix);
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
		String startTag;
		String endTag;

		if (this instanceof Calculated) {
			startTag = "<calculated";
			endTag = "</calculated>";
		} else if (this instanceof Manual) {
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
				for (ScheduleElement scheduleElement : scheduleElements) {
					builder.append(scheduleElement.toXML());
				}
			}

			if (extensions != null) {
				builder.append(OPEN_EXTENSIONS_TAG);
				for (Extension extension : extensions) {
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
		return true;
	}
	
	public void addScheduleElement(ScheduleElement scheduleElement) {
		if (scheduleElements == null)
			scheduleElements = new ArrayList<ScheduleElement>();
		scheduleElements.add(scheduleElement);
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

	public List<ScheduleElement> getScheduleElements() {
		return scheduleElements;
	}

	public void setScheduleElements(List<ScheduleElement> scheduleElements) {
		this.scheduleElements = scheduleElements;
	}
}
