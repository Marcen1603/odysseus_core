package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class SystemLoad implements ISystemLoad, Cloneable, Serializable {

	private transient static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class };

	private final Map<String, SystemLoadEntry> systemLoads = Maps.newHashMap();

	public SystemLoad() {

	}

	public SystemLoad(SystemLoad copy) {
		for (String name : copy.systemLoads.keySet()) {
			systemLoads.put(name, copy.systemLoads.get(name).clone());
		}
	}

	@Override
	public ISystemLoad clone() {
		return new SystemLoad(this);
	}

	@Override
	public void addSystemLoad(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for system load must not be null or empty!");

		// replaces older ones if necessary
		systemLoads.put(name, new SystemLoadEntry(name));
	}

	@Override
	public int getCpuLoad(String name) {
		return (int) (systemLoads.get(name).getCpuLoad() * 100);
	}

	@Override
	public int getMemLoad(String name) {
		return (int) (systemLoads.get(name).getMemLoad() * 100);
	}

	@Override
	public int getNetLoad(String name) {
		return (int) (systemLoads.get(name).getNetLoad() * 100);
	}

	void insert(SystemLoad other) {
		for (String name : other.systemLoads.keySet()) {
			systemLoads.put(name, other.systemLoads.get(name));
		}
	}

	@Override
	public Collection<String> getSystemLoadNames() {
		return systemLoads.keySet();
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	@Override
	public String getName() {
		return "SystemLoad";
	}

	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		StringBuilder sb = new StringBuilder();

		if (!systemLoads.isEmpty()) {
			for (String name : systemLoads.keySet()) {
				if (sb.length() > 0) {
					sb.append(delimiter);
				}
				sb.append(name).append(delimiter);

				SystemLoadEntry systemLoadEntry = systemLoads.get(name);
				int cpuValue = (int) (systemLoadEntry.getCpuLoad() * 100);
				int memValue = (int) (systemLoadEntry.getMemLoad() * 100);
				int netValue = (int) (systemLoadEntry.getNetLoad() * 100);

				sb.append(cpuValue).append(delimiter);
				sb.append(memValue).append(delimiter);
				sb.append(netValue);
			}
		} else {
			sb.append("local").append(delimiter);

			SystemLoadEntry systemLoadEntry = new SystemLoadEntry("local");
			
			int cpuValue = (int) (systemLoadEntry.getCpuLoad() * 100);
			int memValue = (int) (systemLoadEntry.getMemLoad() * 100);
			int netValue = (int) (systemLoadEntry.getNetLoad() * 100);

			sb.append(cpuValue).append(delimiter);
			sb.append(memValue).append(delimiter);
			sb.append(netValue);
			
		}
		
		return sb.toString();
	}

	@Override
	public String getCSVHeader(char delimiter) {
		StringBuilder sb = new StringBuilder();

		for (String name : systemLoads.keySet()) {
			if (sb.length() > 0) {
				sb.append(delimiter);
			}
			sb.append("name").append(delimiter);

			sb.append("cpu_").append(name).append(delimiter);
			sb.append("mem_").append(name).append(delimiter);
			sb.append("net_").append(name);
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{sysload:");
		for (String name : systemLoads.keySet()) {

			SystemLoadEntry systemLoadEntry = systemLoads.get(name);
			sb.append(name).append("-->{");
			String delimiter = ",";

			int cpuValue = (int) (systemLoadEntry.getCpuLoad() * 100);
			int memValue = (int) (systemLoadEntry.getMemLoad() * 100);
			int netValue = (int) (systemLoadEntry.getNetLoad() * 100);

			sb.append("cpu=").append(cpuValue).append(delimiter);
			sb.append("mem=").append(memValue).append(delimiter);
			sb.append("net=").append(netValue);

			sb.append("}");
		}
		sb.append("}");

		return sb.toString();
	}
}
