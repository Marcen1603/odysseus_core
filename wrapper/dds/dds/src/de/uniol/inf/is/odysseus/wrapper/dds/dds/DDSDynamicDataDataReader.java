package de.uniol.inf.is.odysseus.wrapper.dds.dds;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.typecode.TypeCode;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class DDSDynamicDataDataReader extends
		AbstractDDSDataReader<Tuple<IMetaAttribute>> {

	final List<String> attributes;
	final Map<String, IDDSDataReader<?>> readers = new HashMap<String, IDDSDataReader<?>>();

	@Override
	public TypeCode getTypeCode() {
		return null;
	}
	
	@Override
	public TypeCode getSubTypeCode() {
		return null;
	}
	
	public DDSDynamicDataDataReader(List<String> attributes, List<TypeCode> types) {	
		this.attributes = new ArrayList<>(attributes);
		for (int pos = 0;pos<types.size();pos++) {
			readers.put(attributes.get(pos),TypeCodeMapper.getDataReader(types.get(pos)));
		}
	}

	@Override
	public Tuple<IMetaAttribute> getValue(DynamicData data, String name, int pos) {
		DynamicData dynData = new DynamicData();
		data.get_complex_member(dynData, name, pos);
		return getValue(dynData);
	}

	public Tuple<IMetaAttribute> getValue(DynamicData dynData) {
		Tuple<IMetaAttribute> ret = new Tuple<IMetaAttribute>(attributes.size(), false);
		int pos = 0;
		for (String attr: attributes){
			IDDSDataReader<?> reader = readers.get(attr);
			Object value = null;
			if (reader == null){
				System.err.println("No reader for "+attr);
			}else{
				value = reader.getValue(dynData, attr, 0);
			}
			
			ret.setAttribute(pos++, value);
		}		
		return ret;
	}

}
