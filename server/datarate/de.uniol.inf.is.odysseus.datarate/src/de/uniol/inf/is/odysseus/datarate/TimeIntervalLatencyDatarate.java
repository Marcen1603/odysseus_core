package de.uniol.inf.is.odysseus.datarate;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

public class TimeIntervalLatencyDatarate extends TimeIntervalDatarate implements ILatency, ITimeIntervalLatencyDatarate {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[]{ 
		ITimeInterval.class, ILatency.class, IDatarate.class
	};
	
	private ILatency latency;

	public TimeIntervalLatencyDatarate() {
		latency = new Latency();
	}
	
	public TimeIntervalLatencyDatarate( TimeIntervalLatencyDatarate other ) {
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
	public TimeIntervalLatencyDatarate clone() {
		return new TimeIntervalLatencyDatarate(this);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	@Override
	public String toString() {
		return "( (i,datarate) = " + super.toString() + " | l = " + latency.toString() + " )";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + latency.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		return super.csvToString(options)+ options.getDelimiter() + latency.csvToString(options);
	}
	
	@Override
	public String getName() {
		return "TimeIntervalLatencyDatarate";
	}
}
