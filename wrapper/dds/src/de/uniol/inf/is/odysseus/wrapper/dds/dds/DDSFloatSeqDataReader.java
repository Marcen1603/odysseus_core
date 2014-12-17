package de.uniol.inf.is.odysseus.wrapper.dds.dds;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.infrastructure.FloatSeq;


public class DDSFloatSeqDataReader extends AbstractDDSDataReader<FloatSeq> {

	@Override
	public FloatSeq getValue(DynamicData data, String name, int pos) {
		FloatSeq ret = new FloatSeq();
		data.get_float_seq(ret,name, pos);
		return ret;
	}

}
