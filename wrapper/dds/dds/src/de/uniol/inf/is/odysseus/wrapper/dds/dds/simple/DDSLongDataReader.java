package de.uniol.inf.is.odysseus.wrapper.dds.dds.simple;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;


public class DDSLongDataReader extends AbstractDDSSimpleTypeDataReader<Long> {

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.TC_LONG;
	}
	
	@Override
	public Long getValue(DynamicData data, String name, int pos) {
		return data.get_long(name, pos);
	}

}
