package de.uniol.inf.is.odysseus.datarate_systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;

final public class DatarateSystemLoad extends AbstractMetaAttribute implements ISystemLoad, IDatarate {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class, IDatarate.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(CLASSES.length);
	static{
		schema.addAll(Datarate.schema);
		schema.addAll(SystemLoad.schema);
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}

	final private IDatarate datarate;
	final private ISystemLoad systemload;

	public DatarateSystemLoad() {
		datarate = new Datarate();
		systemload = new SystemLoad();
	}

	public DatarateSystemLoad(DatarateSystemLoad other) {
		systemload = other.systemload.clone();
		datarate = other.datarate.clone();
	}

	@Override
	public DatarateSystemLoad clone() {
		return new DatarateSystemLoad(this);
	}

	@Override
	public String getName() {
		return "DatarateSystemLoad";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------
	
	@Override
	public void fillValueList(List<Tuple<?>> values) {
		systemload.fillValueList(values);
		datarate.fillValueList(values);
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return systemload.getValue(0, index);
			case 1:
				return datarate.getValue(0, index);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "( sysload = " + systemload.toString() + " | " + datarate.toString() + " )";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return systemload.getCSVHeader(delimiter) + delimiter + datarate.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		return systemload.csvToString(options) + options.getDelimiter()+ datarate.csvToString(options);
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
