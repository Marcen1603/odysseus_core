package de.uniol.inf.is.odysseus.datarate;

import java.io.Serializable;
import java.text.NumberFormat;

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
		return "datarate";
	}

	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		if( floatingFormatter != null ) {
			return floatingFormatter.format(datarate);
		} else if( numberFormatter != null ) {
			return numberFormatter.format(datarate);
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
