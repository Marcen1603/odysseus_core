package de.uniol.inf.is.odysseus.wrapper.protobuf;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractTransformer;

public class GeneratedMessageToTuple extends
		AbstractTransformer<GeneratedMessage, Tuple<? extends IMetaAttribute>> {

	static Map<SDFSchema,GeneratedMessageToTuple> instanceMap = new HashMap<SDFSchema, GeneratedMessageToTuple>();
	private SDFSchema schema;

	public GeneratedMessageToTuple() {
	}
	
	private GeneratedMessageToTuple(SDFSchema schema) {
		if (schema == null){
			throw new IllegalArgumentException("Schema cannot be null!");
		}
		this.schema = schema;
	}

	public GeneratedMessageToTuple getInstance(Map<String, String> options, SDFSchema schema) {
		GeneratedMessageToTuple ret = instanceMap.get(schema);
		if (ret == null){
			ret = new GeneratedMessageToTuple(schema);
			instanceMap.put(schema, ret);
		}
		return ret;
	}

	@Override
	public Tuple<?> transform(GeneratedMessage input) {

		Map<FieldDescriptor, Object> test = input.getAllFields();
		@SuppressWarnings("rawtypes")
		Tuple<?> ret = new Tuple(schema.size());

		for (Entry<FieldDescriptor, Object> ent : test.entrySet()) {
			if (ent.getValue() != null && ent.getKey() != null) {
				// Numbers starting with 1
				ret.setAttribute(ent.getKey().getNumber()-1, ent.getValue());
			}
		}

		return ret;
	}
	
	@Override
	public String getName() {
		return "GeneratedMessageToTuple";
	}

}
