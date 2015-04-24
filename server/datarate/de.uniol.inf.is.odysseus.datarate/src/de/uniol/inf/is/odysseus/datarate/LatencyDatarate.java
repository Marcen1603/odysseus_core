package de.uniol.inf.is.odysseus.datarate;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

final public class LatencyDatarate extends AbstractMetaAttribute implements IDatarate, ILatency {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[]{ 
		ILatency.class, IDatarate.class
	};

	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(CLASSES.length);
	static{
		schema.addAll(Latency.schema);
		schema.addAll(Datarate.schema);
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}
	
	final private ILatency latency;
	final private IDatarate datarate;

	
	public LatencyDatarate() {
		latency = new Latency();
		datarate = new Datarate();
	}
	
	public LatencyDatarate( LatencyDatarate other ) {
		latency = other.latency.clone();
		datarate = other.datarate.clone();
	}
	
	@Override
	public LatencyDatarate clone() {
		return new LatencyDatarate(this);
	}
	
	@Override
	public String getName() {
		return "LatencyDatarate";
	}
	
	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void fillValueList(List<Tuple<?>> values) {
		latency.fillValueList(values);
		datarate.fillValueList(values);
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return latency.getValue(0, index);
			case 1:
				return datarate.getValue(0, index);

		}
		return null;
	}

	@Override
	public String toString() {
		return "( l = " + super.toString() + " | " + datarate.toString() + " )";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return latency.getCSVHeader(delimiter) + delimiter + datarate.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		return latency.csvToString(options) + options.getDelimiter() + datarate.csvToString(options);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	// ------------------------------------------------------------------------------
	// Delegates for datarate
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
	
}
