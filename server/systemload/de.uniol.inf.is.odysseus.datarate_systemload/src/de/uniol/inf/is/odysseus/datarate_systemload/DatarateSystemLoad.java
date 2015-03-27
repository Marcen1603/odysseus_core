package de.uniol.inf.is.odysseus.datarate_systemload;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;

public class DatarateSystemLoad extends SystemLoad implements IDatarateSystemLoad, IDatarate {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class, IDatarate.class };

	private Datarate datarate;

	public DatarateSystemLoad() {
		datarate = new Datarate();
	}

	public DatarateSystemLoad(DatarateSystemLoad other) {
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
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	@Override
	public DatarateSystemLoad clone() {
		return new DatarateSystemLoad(this);
	}
	 
	@Override
	public String toString() {
		return "( sysload = " + super.toString() + " | " + datarate.toString() + " )";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + datarate.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		return super.csvToString(options) + options.getDelimiter()+ datarate.csvToString(options);
	}
	
	@Override
	public String getName() {
		return "DatarateSystemLoad";
	}
}
