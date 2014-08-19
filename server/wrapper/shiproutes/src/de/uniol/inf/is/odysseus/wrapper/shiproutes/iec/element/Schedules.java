package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class Schedules implements IIecElement {
	// map constants
	private static final String ELEMENT_PREFIX = "_scheds";

	private static final String OPEN_TAG = "<schedules>";
	private static final String CLOSE_TAG = "</schedules>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private List<Schedule> schedules;
	private List<Extension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		
		if (schedules != null) {
			for (Schedule schedule : schedules) {
				schedule.fillMap(map, prefix);
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

		if (schedules != null) {
			for (Schedule schedule : schedules) {
				builder.append(schedule.toXML());
			}
		}

		if (extensions != null) {
			builder.append(OPEN_EXTENSIONS_TAG);
			for (Extension extension : extensions) {
				builder.append(extension.toXML());
			}
			builder.append(CLOSE_EXTENSIONS_TAG);
		}

		builder.append(CLOSE_TAG);
		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		if ((schedules != null && !schedules.isEmpty())
				|| (extensions != null && !extensions.isEmpty())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public void addSchedule(Schedule schedule) {
		if (schedules == null)
			schedules = new ArrayList<Schedule>();
		schedules.add(schedule);
	}

	@Override
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

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

}
