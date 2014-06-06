package de.uniol.inf.is.odysseus.datarate;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

public class LatencyDatarate extends Latency implements ILatencyDatarate, ILatency {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[]{ 
		ILatency.class, IDatarate.class
	};

	private Datarate datarate;
	
	public LatencyDatarate() {
		datarate = new Datarate();
	}
	
	public LatencyDatarate( LatencyDatarate other ) {
		super(other);
		datarate = new Datarate(other.datarate);
	}
	
	@Override
	public void setDatarate(double datarate) {
		this.datarate.setDatarate(datarate);
	}

	@Override
	public double getDatarate() {
		return datarate.getDatarate();
	}
	
	@Override
	public LatencyDatarate clone() {
		return new LatencyDatarate(this);
	}
	
	@Override
	public String toString() {
		return "( l = " + super.toString() + " | " + datarate.toString() + " )";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + datarate.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		return super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter + datarate.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	@Override
	public String getName() {
		return "LatencyDatarate";
	}
}
