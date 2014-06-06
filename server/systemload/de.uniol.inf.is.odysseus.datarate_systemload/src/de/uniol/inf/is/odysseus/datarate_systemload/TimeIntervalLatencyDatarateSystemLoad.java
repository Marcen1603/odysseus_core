package de.uniol.inf.is.odysseus.datarate_systemload;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;

public class TimeIntervalLatencyDatarateSystemLoad extends TimeIntervalDatarateSystemLoad implements ITimeIntervalLatencyDatarateSystemLoad, ILatency {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class, IDatarate.class, ILatency.class, ITimeInterval.class };

	private ILatency latency;
	
	public TimeIntervalLatencyDatarateSystemLoad() {
		latency = new Latency();
	}
	
	public TimeIntervalLatencyDatarateSystemLoad( TimeIntervalLatencyDatarateSystemLoad other ) {
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
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	@Override
	public TimeIntervalLatencyDatarateSystemLoad clone() {
		return new TimeIntervalLatencyDatarateSystemLoad(this);
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + latency.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		return super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter + latency.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata);
	}

	@Override
	public String getName() {
		return "TimeIntervalLatencyDatarateSystemLoad";
	}
	
	@Override
	public String toString() {
		return "( (sysload,i,datarate) = " + super.toString() + " | l = " + latency.toString() + " )";
	}
}
