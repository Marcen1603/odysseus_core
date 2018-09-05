package de.uniol.inf.is.odysseus.wrapper.dds.dds.simple;

import com.rti.dds.typecode.TypeCode;

import de.uniol.inf.is.odysseus.wrapper.dds.dds.AbstractDDSDataReader;

abstract public class AbstractDDSSimpleTypeDataReader<T> extends AbstractDDSDataReader<T> {

	@Override
	public TypeCode getSubTypeCode() {
		return null;
	}
	
}
