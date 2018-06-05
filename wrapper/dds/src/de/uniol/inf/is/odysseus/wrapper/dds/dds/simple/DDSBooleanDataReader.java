package de.uniol.inf.is.odysseus.wrapper.dds.dds.simple;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;


public class DDSBooleanDataReader extends AbstractDDSSimpleTypeDataReader<Boolean> {

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.TC_BOOLEAN;
	}
	
	@Override
	public Boolean getValue(DynamicData data, String name, int pos) {
		return data.get_boolean(name, pos);
	}

}
