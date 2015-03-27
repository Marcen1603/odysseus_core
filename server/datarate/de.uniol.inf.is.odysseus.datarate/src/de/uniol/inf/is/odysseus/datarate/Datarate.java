package de.uniol.inf.is.odysseus.datarate;

import java.io.Serializable;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class Datarate implements IDatarate, Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IDatarate.class };

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
