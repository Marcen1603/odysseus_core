package de.uniol.inf.is.odysseus.systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

final public class LatencySystemLoad extends AbstractCombinedMetaAttribute implements
	  ILatency, ISystemLoad {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			ILatency.class, ISystemLoad.class };

	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(CLASSES.length);
	static{
		schema.addAll(Latency.schema);
		schema.addAll(SystemLoad.schema);
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}
	
	final private ILatency latency;
	final private ISystemLoad systemload;

	public LatencySystemLoad() {
		latency = new Latency();
		systemload = new SystemLoad();
	}

	public LatencySystemLoad(LatencySystemLoad other) {
		latency = other.latency.clone();
		systemload = other.systemload.clone();
	}

	@Override
	public LatencySystemLoad clone() {
		return new LatencySystemLoad(this);
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		latency.retrieveValues(values);
		systemload.retrieveValues(values);
	}
	
	@Override
	public void writeValues(List<Tuple<?>> values) {
		latency.writeValue(values.get(0));
		systemload.writeValue(values.get(1));		
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return latency.getValue(0, index);
			case 1:
				return systemload.getValue(0, index);
		}
		return null;
	}

	
	@Override
	public String getCSVHeader(char delimiter) {
		return  latency.getCSVHeader(delimiter)
				+ systemload.getCSVHeader(delimiter);
	}

	@Override
	public String csvToString(WriteOptions options) {
		return  latency.csvToString(options) + options.getDelimiter()
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

}
