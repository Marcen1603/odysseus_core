package de.uniol.inf.is.odysseus.wrapper.dds.dds;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;


public interface IDDSDataReader<T> {

	TypeCode getTypeCode();
	TypeCode getSubTypeCode();
	
	T getValue(DynamicData data, String name, int pos);
}
