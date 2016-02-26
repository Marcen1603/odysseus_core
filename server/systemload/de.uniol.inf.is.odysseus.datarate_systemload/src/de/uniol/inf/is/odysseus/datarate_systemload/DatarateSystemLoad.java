package de.uniol.inf.is.odysseus.datarate_systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoadEntry;

final public class DatarateSystemLoad extends AbstractCombinedMetaAttribute implements ISystemLoad, IDatarate {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ISystemLoad.class, IDatarate.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(CLASSES.length);
	static{
		schema.addAll(Datarate.schema);
		schema.addAll(SystemLoad.schema);
	}
	
	@Override
	public List<SDFMetaSchema> getSchema() {
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
	public void retrieveValues(List<Tuple<?>> values) {
		systemload.retrieveValues(values);
		datarate.retrieveValues(values);
	}
	
	@Override
	public void writeValues(List<Tuple<?>> values) {
		systemload.writeValue(values.get(0));
		datarate.writeValue(values.get(1));
	}
	
	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(systemload.getInlineMergeFunctions());
		list.addAll(datarate.getInlineMergeFunctions());
		return list;
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

	public void insert(ISystemLoad other) {
		systemload.insert(other);
	}

	public SystemLoadEntry getSystemLoad(String name) {
		return systemload.getSystemLoad(name);
	}	
	
	
	 

}
