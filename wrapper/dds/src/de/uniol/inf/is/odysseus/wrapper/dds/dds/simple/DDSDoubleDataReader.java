package de.uniol.inf.is.odysseus.wrapper.dds.dds.simple;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;


public class DDSDoubleDataReader extends AbstractDDSSimpleTypeDataReader<Double> {

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.TC_DOUBLE;
	}
	
	@Override
	public Double getValue(DynamicData data, String name, int pos) {
		return data.get_double(name, pos);
	}

}
