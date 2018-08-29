package de.uniol.inf.is.odysseus.wrapper.dds.dds.seq;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.infrastructure.BooleanSeq;
import com.rti.dds.typecode.TypeCode;


public class DDSBooleanSeqDataReader extends AbstractDDSSequenceDataReader<BooleanSeq> {
	
	
	@Override
	public TypeCode getSubTypeCode() {
		return TypeCode.TC_BOOLEAN;
	}
	
	@Override
	public BooleanSeq getValue(DynamicData data, String name, int pos) {
		BooleanSeq ret = new BooleanSeq();
		data.get_boolean_seq(ret,name, pos);
		return ret;
	}

}
