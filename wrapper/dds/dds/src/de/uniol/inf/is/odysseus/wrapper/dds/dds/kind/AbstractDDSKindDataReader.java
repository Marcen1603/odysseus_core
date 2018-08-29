package de.uniol.inf.is.odysseus.wrapper.dds.dds.kind;

import com.rti.dds.typecode.TCKind;
import com.rti.dds.typecode.TypeCode;

import de.uniol.inf.is.odysseus.wrapper.dds.dds.AbstractDDSDataReader;

public abstract class AbstractDDSKindDataReader<T> extends
		AbstractDDSDataReader<T> {

	@Override
	public TypeCode getSubTypeCode() {
		return null;
	}
	
	@Override
	public TypeCode getTypeCode() {
		return null;
	}

	public abstract TCKind getKind();
}
