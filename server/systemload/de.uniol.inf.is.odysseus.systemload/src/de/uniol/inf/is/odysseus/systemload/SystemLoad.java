package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

final public class SystemLoad extends AbstractBaseMetaAttribute implements ISystemLoad, Cloneable, Serializable {

	public static final String SYSTEMLOAD = "Systemload";
	private static final String LOCAL_NAME = "local";
	private transient static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(CLASSES.length);

	static {

		List<SDFAttribute> sysLoadAttributes = new ArrayList<SDFAttribute>();
		sysLoadAttributes.add(new SDFAttribute("SystemloadEntry", "Name", SDFDatatype.STRING));
		sysLoadAttributes.add(new SDFAttribute("SystemloadEntry", "CpuLoad", SDFDatatype.DOUBLE));
		sysLoadAttributes.add(new SDFAttribute("SystemloadEntry", "MemLoad", SDFDatatype.DOUBLE));
		sysLoadAttributes.add(new SDFAttribute("SystemloadEntry", "NetLoad", SDFDatatype.DOUBLE));

		SDFSchema sysLoadEntry = SDFSchemaFactory.createNewSchema("SystemloadEntry", Tuple.class, sysLoadAttributes);

		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute(SYSTEMLOAD, "EntryList", SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST_TUPLE, sysLoadEntry)));
		schema.add(SDFSchemaFactory.createNewMetaSchema(SYSTEMLOAD, Tuple.class, attributes, ISystemLoad.class));
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

	@SuppressWarnings("rawtypes")
	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		Tuple t = createTupleFromState();
		values.add(t);
	}

	@SuppressWarnings("rawtypes")
	private Tuple createTupleFromState() {
		Tuple t = new Tuple(1, true);
		List<Tuple<?>> l = createEntryListFromState();
		t.setAttribute(0, l);
		return t;
	}

	@SuppressWarnings("rawtypes")
	private List<Tuple<?>> createEntryListFromState() {
		List<Tuple<?>> l = new ArrayList<>();
		for (SystemLoadEntry v : systemLoads.values()) {
			Tuple sysLoadEntry = new Tuple(4, false);
			sysLoadEntry.setAttribute(0, v.getName());
			sysLoadEntry.setAttribute(1, v.getCpuLoad());
			sysLoadEntry.setAttribute(2, v.getMemLoad());
			sysLoadEntry.setAttribute(3, v.getNetLoad());
			l.add(sysLoadEntry);
		}
		return l;
	}

	@Override
	public void writeValue(Tuple<?> value) {
		List<Tuple<?>> l = value.getAttribute(0);
		for (Tuple<?> e : l) {
			String name = e.getAttribute(0);
			double cpuLoad = e.getAttribute(1);
			double memLoad = e.getAttribute(2);
			double netLoad = e.getAttribute(3);
			SystemLoadEntry sysEntry = new SystemLoadEntry(name,cpuLoad, memLoad, netLoad);
			systemLoads.put(name, sysEntry);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		return (K) createEntryListFromState();
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new SystemLoadMergeFunction();
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
	public void removeSystemLoad(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for system load must not be null or empty!");

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

	@Override
	public void insert(ISystemLoad other) {
		for (String name : other.getSystemLoadNames()) {
			systemLoads.put(name, other.getSystemLoad(name));
		}
	}

	@Override
	public Collection<String> getSystemLoadNames() {
		return systemLoads.keySet();
	}

	@Override
	public SystemLoadEntry getSystemLoad(String name) {
		return systemLoads.get(name);
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	@Override
	public String getName() {
		return SYSTEMLOAD;
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
