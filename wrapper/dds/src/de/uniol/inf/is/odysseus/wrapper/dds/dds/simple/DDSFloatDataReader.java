package de.uniol.inf.is.odysseus.wrapper.dds.dds.simple;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;


public class DDSFloatDataReader extends AbstractDDSSimpleTypeDataReader<Float> {

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.TC_FLOAT;
	}
	
	@Override
	public Float getValue(DynamicData data, String name, int pos) {
		return data.get_float(name, pos);
	}

}
