package de.uniol.inf.is.odysseus.wrapper.dds.dds.simple;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;


public class DDSCharDataReader extends AbstractDDSSimpleTypeDataReader<Character> {

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.TC_CHAR;
	}
	
	@Override
	public Character getValue(DynamicData data, String name, int pos) {
		return data.get_char(name, pos);
	}

}
