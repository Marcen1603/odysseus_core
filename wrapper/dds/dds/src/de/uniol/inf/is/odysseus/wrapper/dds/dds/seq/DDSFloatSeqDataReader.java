package de.uniol.inf.is.odysseus.wrapper.dds.dds.seq;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.infrastructure.FloatSeq;
import com.rti.dds.typecode.TypeCode;


public class DDSFloatSeqDataReader extends AbstractDDSSequenceDataReader<FloatSeq> {
	

	@Override
	public TypeCode getSubTypeCode() {
		return TypeCode.TC_FLOAT;
	}
	
	@Override
	public FloatSeq getValue(DynamicData data, String name, int pos) {
		FloatSeq ret = new FloatSeq();
		data.get_float_seq(ret,name, pos);
		return ret;
	}

}
