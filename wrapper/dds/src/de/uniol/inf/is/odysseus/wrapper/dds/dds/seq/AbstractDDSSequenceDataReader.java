package de.uniol.inf.is.odysseus.wrapper.dds.dds.seq;

import com.rti.dds.typecode.TypeCode;

import de.uniol.inf.is.odysseus.wrapper.dds.dds.AbstractDDSDataReader;

abstract public class AbstractDDSSequenceDataReader<T> extends AbstractDDSDataReader<T> {

	
	@Override
	public TypeCode getTypeCode() {
		return null;
	}
	
}
