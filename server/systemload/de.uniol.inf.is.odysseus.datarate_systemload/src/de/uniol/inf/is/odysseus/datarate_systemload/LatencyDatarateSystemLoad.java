package de.uniol.inf.is.odysseus.datarate_systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoadEntry;

final public class LatencyDatarateSystemLoad extends AbstractCombinedMetaAttribute implements
		IDatarate, ISystemLoad, ILatency {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			IDatarate.class, ILatency.class, ISystemLoad.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(CLASSES.length);
	static{
		schema.addAll(Datarate.schema);
		schema.addAll(Latency.schema);
		schema.addAll(SystemLoad.schema);
	}
	
	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}
	
	final private IDatarate datarate;
	final private ILatency latency;
	final private ISystemLoad systemload;

	public LatencyDatarateSystemLoad() {
		datarate = new Datarate();
		latency = new Latency();
		systemload = new SystemLoad();
	}

	public LatencyDatarateSystemLoad(LatencyDatarateSystemLoad other) {
		datarate = other.datarate.clone();
		latency = other.latency.clone();
		systemload = other.systemload.clone();
	}

	@Override
	public LatencyDatarateSystemLoad clone() {
		return new LatencyDatarateSystemLoad(this);
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		datarate.retrieveValues(values);
		latency.retrieveValues(values);
		systemload.retrieveValues(values);
	}
	
	@Override
	public void writeValues(List<Tuple<?>> values) {
		datarate.writeValue(values.get(0));
		latency.writeValue(values.get(1));
		systemload.writeValue(values.get(2));		
	}
	
	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(datarate.getInlineMergeFunctions());
		list.addAll(latency.getInlineMergeFunctions());
		list.addAll(systemload.getInlineMergeFunctions());
		return list;
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return datarate.getValue(0, index);
			case 1:
				return latency.getValue(0, index);
			case 2:
				return systemload.getValue(0, index);
		}
		return null;
	}

	
	@Override
	public String getCSVHeader(char delimiter) {
		return datarate.getCSVHeader(delimiter) + delimiter
				+ latency.getCSVHeader(delimiter)
				+ systemload.getCSVHeader(delimiter);
	}

	@Override
	public String csvToString(WriteOptions options) {
		return datarate.csvToString(options) + options.getDelimiter()
				+ latency.csvToString(options) + options.getDelimiter()
				+ systemload.csvToString(options);
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	@Override
	public String getName() {
		return "LatencyDatarateSystemLoad";
	}

	// ------------------------------------------------------------------------------
	// Delegates for Datarate
	// ------------------------------------------------------------------------------

	@Override
	public void setDatarate(double datarate) {
		this.datarate.setDatarate(datarate);
	}

	@Override
	public double getDatarate() {
		return datarate.getDatarate();
	}

	// ------------------------------------------------------------------------------
	// Delegates for latency
	// ------------------------------------------------------------------------------

	@Override
	public final long getLatency() {
		return latency.getLatency();
	}

	@Override
	public long getMaxLatency() {
		return latency.getMaxLatency();
	}

	@Override
	public final long getLatencyEnd() {
		return latency.getLatencyEnd();
	}

	@Override
	public final long getLatencyStart() {
		return latency.getLatencyStart();
	}

	@Override
	public long getMaxLatencyStart() {
		return latency.getMaxLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setMinLatencyStart(long timestamp) {
		latency.setMinLatencyStart(timestamp);
	}

	@Override
	public void setMaxLatencyStart(long timestamp) {
		latency.setMaxLatencyStart(timestamp);
	}

	// ------------------------------------------------------------------------------
	// Delegates for Systemload
	// ------------------------------------------------------------------------------

	public void addSystemLoad(String name) {
		systemload.addSystemLoad(name);
	}

	public void removeSystemLoad(String name) {
		systemload.removeSystemLoad(name);
	}

	public Collection<String> getSystemLoadNames() {
		return systemload.getSystemLoadNames();
	}

	public int getCpuLoad(String name) {
		return systemload.getCpuLoad(name);
	}

	public int getMemLoad(String name) {
		return systemload.getMemLoad(name);
	}

	public int getNetLoad(String name) {
		return systemload.getNetLoad(name);
	}

	public void insert(ISystemLoad other) {
		systemload.insert(other);
	}

	public SystemLoadEntry getSystemLoad(String name) {
		return systemload.getSystemLoad(name);
	}	
	
}
