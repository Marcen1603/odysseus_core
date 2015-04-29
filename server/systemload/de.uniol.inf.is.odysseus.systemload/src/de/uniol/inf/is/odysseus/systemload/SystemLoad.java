package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

final public class SystemLoad extends AbstractBaseMetaAttribute implements
		ISystemLoad, Cloneable, Serializable {

	private static final String LOCAL_NAME = "local";
	private transient static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(
			CLASSES.length);

	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("Systemload", "todo",
				SDFDatatype.DOUBLE, null));
		schema.add(SDFSchemaFactory.createNewMetaSchema("Systemload", Tuple.class,
				attributes, ISystemLoad.class));
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	private final Map<String, SystemLoadEntry> systemLoads = Maps.newHashMap();

	public SystemLoad() {
		addSystemLoad(LOCAL_NAME);
	}

	public SystemLoad(SystemLoad copy) {
		for (String name : copy.systemLoads.keySet()) {
			systemLoads.put(name, copy.systemLoads.get(name).clone());
		}
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(1, false);
		t.setAttribute(0, "toto");
		values.add(t);			
	}

	@Override
	public void writeValue(Tuple<?> value) {
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		return null;
	}


	@Override
	public ISystemLoad clone() {
		return new SystemLoad(this);
	}

	@Override
	public void addSystemLoad(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name),
				"Name for system load must not be null or empty!");

		// replaces older ones if necessary
		systemLoads.put(name, new SystemLoadEntry(name));
	}

	@Override
	public void removeSystemLoad(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name),
				"Name for system load must not be null or empty!");

		systemLoads.remove(name);
	}

	@Override
	public int getCpuLoad(String name) {
		return (int) (systemLoads.get(checkName(name)).getCpuLoad() * 100);
	}

	@Override
	public int getMemLoad(String name) {
		return (int) (systemLoads.get(checkName(name)).getMemLoad() * 100);
	}

	@Override
	public int getNetLoad(String name) {
		return (int) (systemLoads.get(checkName(name)).getNetLoad() * 100);
	}

	private static String checkName(String name) {
		return Strings.isNullOrEmpty(name) ? LOCAL_NAME : name;
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
	public String csvToString(WriteOptions options) {
		char delimiter = options.getDelimiter();
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
			sb.append(LOCAL_NAME).append(delimiter);

			SystemLoadEntry systemLoadEntry = new SystemLoadEntry(LOCAL_NAME);

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
