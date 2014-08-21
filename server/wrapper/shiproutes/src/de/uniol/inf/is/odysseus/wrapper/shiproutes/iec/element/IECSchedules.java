package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class IECSchedules implements IIecElement {
	// map constants
	private static final String ELEMENT_PREFIX = "_scheds";

	private static final String OPEN_TAG = "<schedules>";
	private static final String CLOSE_TAG = "</schedules>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	private List<IECSchedule> schedules;
	private List<IECExtension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		
		if (schedules != null) {
			for (IECSchedule schedule : schedules) {
				schedule.fillMap(map, prefix);
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

		if (schedules != null) {
			for (IECSchedule schedule : schedules) {
				builder.append(schedule.toXML());
			}
		}

		if (extensions != null) {
			builder.append(OPEN_EXTENSIONS_TAG);
			for (IECExtension extension : extensions) {
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

	public void addSchedule(IECSchedule schedule) {
		if (schedules == null)
			schedules = new ArrayList<IECSchedule>();
		schedules.add(schedule);
	}

	@Override
	public void addExtension(IECExtension extension) {
		if (extensions == null)
			extensions = new ArrayList<IECExtension>();
		extensions.add(extension);
	}

	public List<IECExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<IECExtension> extensions) {
		this.extensions = extensions;
	}

	public List<IECSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<IECSchedule> schedules) {
		this.schedules = schedules;
	}

}
