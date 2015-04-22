package de.uniol.inf.is.odysseus.datarate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class Datarate implements IDatarate, Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IDatarate.class };

	static final SDFSchema schema;
	static{
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("Datarate", "datarate", SDFDatatype.DOUBLE, null));
		schema = SDFSchemaFactory.createNewSchema("Datarate", Tuple.class, attributes);
	}
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
	
	private double datarate;
	
	public Datarate() {
		
	}
	
	public Datarate( Datarate other ) {
		datarate = other.datarate;
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	@Override
	public String getName() {
		return "Datarate";
	}

	@Override
	public String csvToString(WriteOptions options) {
		if( options.hasFloatingFormatter() ) {
			return options.getFloatingFormatter().format(datarate);
		} else if( options.hasNumberFormatter()) {
			return options.getNumberFormatter().format(datarate);
		}
		
		return String.valueOf(datarate);
	}
	

	@Override
	public String getCSVHeader(char delimiter) {
		return "Datarate";
	}

	@Override
	public void setDatarate(double datarate) {
		this.datarate = datarate;
	}

	@Override
	public double getDatarate() {
		return datarate;
	}
	
	@Override
	public IDatarate clone() {
		return new Datarate(this);
	}

	@Override
	public String toString() {
		return "rate = " + datarate;
	}
}
