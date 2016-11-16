package de.uniol.inf.is.odysseus.datarate_systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoadEntry;

final public class TimeIntervalDatarateSystemLoad extends AbstractCombinedMetaAttribute
		implements ITimeInterval, IDatarate, ISystemLoad  {

	private static final long serialVersionUID = -3865342510097346722L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			ITimeInterval.class, IDatarate.class, ISystemLoad.class};

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(CLASSES.length);
	static{
		schema.addAll(TimeInterval.schema);
		schema.addAll(Datarate.schema);
		schema.addAll(SystemLoad.schema);
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	final private ITimeInterval timeInterval;
	final private IDatarate datarate;
	final private ISystemLoad systemload;

	public TimeIntervalDatarateSystemLoad() {
		timeInterval = new TimeInterval();
		datarate = new Datarate();
		systemload = new SystemLoad();
	}

	public TimeIntervalDatarateSystemLoad(TimeIntervalDatarateSystemLoad other) {
		timeInterval = (ITimeInterval) other.timeInterval.clone();
		datarate = (IDatarate) other.datarate.clone();
		systemload = (ISystemLoad) other.systemload.clone();
	}

	@Override
	public TimeIntervalDatarateSystemLoad clone() {
		return new TimeIntervalDatarateSystemLoad(this);
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "( i = "+ timeInterval.toString() +"| datarate = "
				+ datarate.toString() + "| sysload = " + systemload.toString() + " )";
	}

	@Override
	public String getName() {
		return "TimeIntervalDatarateSystemLoad";
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		datarate.retrieveValues(values);
		systemload.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		timeInterval.writeValue(values.get(0));
		datarate.writeValue(values.get(1));
		systemload.writeValue(values.get(2));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(timeInterval.getInlineMergeFunctions());
		list.addAll(datarate.getInlineMergeFunctions());
		list.addAll(systemload.getInlineMergeFunctions());
		return list;
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch (subtype) {
		case 0:
			return timeInterval.getValue(0, index);
		case 1:
			return datarate.getValue(0, index);
		case 2:
			return systemload.getValue(0, index);
		}
		return null;
	}

	// ------------------------------------------------------------------------------
	// Delegates for timeInterval
	// ------------------------------------------------------------------------------

	@Override
	public PointInTime getStart() {
		return timeInterval.getStart();
	}

	@Override
	public PointInTime getEnd() {
		return timeInterval.getEnd();
	}

	@Override
	public void setStart(PointInTime point) {
		timeInterval.setStart(point);
	}

	@Override
	public void setEnd(PointInTime point) {
		timeInterval.setEnd(point);
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		timeInterval.setStartAndEnd(start, end);
	}

	@Override
	public int compareTo(ITimeInterval o) {
		return timeInterval.compareTo(o);
	}

	// ------------------------------------------------------------------------------
	// Delegates for Datarate
	// ------------------------------------------------------------------------------

	@Override
	public void setDatarate(String key, double datarate) {
		this.datarate.setDatarate(key, datarate);
	}

	@Override
	public double getDatarate(String key) {
		return datarate.getDatarate(key);
	}

	@Override
	public Map<String, Double> getDatarates() {
		return datarate.getDatarates();
	}

	@Override
	public void setDatarates(Map<String, Double> datarates) {
		datarate.setDatarates(datarates);
	}

	// ------------------------------------------------------------------------------
	// Delegates for Systemload
	// ------------------------------------------------------------------------------

	@Override
	public void addSystemLoad(String name) {
		systemload.addSystemLoad(name);
	}

	@Override
	public void removeSystemLoad(String name) {
		systemload.removeSystemLoad(name);
	}

	@Override
	public Collection<String> getSystemLoadNames() {
		return systemload.getSystemLoadNames();
	}

	@Override
	public int getCpuLoad(String name) {
		return systemload.getCpuLoad(name);
	}

	@Override
	public int getMemLoad(String name) {
		return systemload.getMemLoad(name);
	}

	@Override
	public int getNetLoad(String name) {
		return systemload.getNetLoad(name);
	}

	@Override
	public void insert(ISystemLoad other) {
		systemload.insert(other);
	}

	@Override
	public SystemLoadEntry getSystemLoad(String name) {
		return systemload.getSystemLoad(name);
	}
}
