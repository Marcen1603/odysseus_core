package de.uniol.inf.is.odysseus.datarate_systemload;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.TimeIntervalSystemLoad;

public class TimeIntervalDatarateSystemLoad extends TimeIntervalSystemLoad implements ITimeIntervalDatarateSystemLoad, IDatarate {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class, IDatarate.class, ITimeInterval.class };

	private Datarate datarate;
	
	public TimeIntervalDatarateSystemLoad() {
		datarate = new Datarate();
	}
	
	public TimeIntervalDatarateSystemLoad( TimeIntervalDatarateSystemLoad other ) {
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
	public TimeIntervalDatarateSystemLoad clone() {
		return new TimeIntervalDatarateSystemLoad(this);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	@Override
	public String toString() {
		return "( (i, sysload) = " + super.toString() + " | datarate = " + datarate.toString() + " )";
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
	public String getName() {
		return "TimeIntervalDatarateSystemLoad";
	}
}
