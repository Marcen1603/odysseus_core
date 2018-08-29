package de.uniol.inf.is.odysseus.wrapper.dds.dds.simple;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;


public class DDSShortDataReader extends AbstractDDSSimpleTypeDataReader<Short> {

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.TC_SHORT;
	}
	
	@Override
	public Short getValue(DynamicData data, String name, int pos) {
		return data.get_short(name, pos);
	}

}
