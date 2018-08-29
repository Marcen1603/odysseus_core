package de.uniol.inf.is.odysseus.wrapper.dds.dds.kind;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TCKind;


public class DDSStringDataReader extends AbstractDDSKindDataReader<String> {

	@Override
	public TCKind getKind() {
		return TCKind.TK_STRING;
	}
	
	@Override
	public String getValue(DynamicData data, String name, int pos) {
		return data.get_string(name, pos);
	}

}
