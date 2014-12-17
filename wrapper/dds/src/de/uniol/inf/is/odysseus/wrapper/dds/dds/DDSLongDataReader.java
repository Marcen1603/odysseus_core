package de.uniol.inf.is.odysseus.wrapper.dds.dds;
import com.rti.dds.dynamicdata.DynamicData;


public class DDSLongDataReader extends AbstractDDSDataReader<Long> {

	@Override
	public Long getValue(DynamicData data, String name, int pos) {
		return data.get_long(name, pos);
	}

}
