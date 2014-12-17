package de.uniol.inf.is.odysseus.wrapper.dds.dds;
import com.rti.dds.dynamicdata.DynamicData;


public class DDSStringDataReader extends AbstractDDSDataReader<String> {

	@Override
	public String getValue(DynamicData data, String name, int pos) {
		return data.get_string(name, pos);
	}

}
