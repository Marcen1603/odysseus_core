package de.uniol.inf.is.odysseus.datarate_systemload;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;

public class LatencyDatarateSystemLoad extends DatarateSystemLoad implements ILatencyDatarateSystemLoad, ILatency {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class, IDatarate.class, ILatency.class };

	private ILatency latency;
	
	public LatencyDatarateSystemLoad() {
		latency = new Latency();
	}
	
	public LatencyDatarateSystemLoad( LatencyDatarateSystemLoad other ) {
		super(other);
		
		latency = other.latency.clone();
	}
	
	@Override
	public void setMinLatencyStart(long timestamp) {
		latency.setMinLatencyStart(timestamp);
	}

	@Override
	public void setMaxLatencyStart(long timestamp) {
		latency.setMaxLatencyStart(timestamp);
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		latency.setLatencyEnd(timestamp);
	}

	@Override
	public long getLatencyStart() {
		return latency.getLatencyStart();
	}

	@Override
	public long getMaxLatency() {
		return latency.getMaxLatency();
	}

	@Override
	public long getLatencyEnd() {
		return latency.getLatencyEnd();
	}

	@Override
	public long getLatency() {
		return latency.getLatency();
	}

	@Override
	public long getMaxLatencyStart() {
		return latency.getMaxLatencyStart();
	}

	@Override
	public LatencyDatarateSystemLoad clone() {
		return new LatencyDatarateSystemLoad(this);
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + latency.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		return super.csvToString(options) + options.getDelimiter() + latency.csvToString(options);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	@Override
	public String getName() {
		return "LatencyDatarateSystemLoad";
	}
}
