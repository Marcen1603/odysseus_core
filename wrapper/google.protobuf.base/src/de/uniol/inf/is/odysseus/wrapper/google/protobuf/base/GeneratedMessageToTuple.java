package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base;

import java.util.Map;
import java.util.Map.Entry;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractTransformer;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class GeneratedMessageToTuple extends
		AbstractTransformer<GeneratedMessage, Tuple<? extends IMetaAttribute>> {

	static GeneratedMessageToTuple instance = new GeneratedMessageToTuple();

	public static GeneratedMessageToTuple getInstance() {
		return instance;
	}

	@Override
	public Tuple<?> transform(GeneratedMessage input) {

		Map<FieldDescriptor, Object> test = input.getAllFields();
		@SuppressWarnings("rawtypes")
		Tuple<?> ret = new Tuple(test.size());

		for (Entry<FieldDescriptor, Object> ent : test.entrySet()) {
			if (ent.getValue() != null && ent.getKey() != null) {
				ret.setAttribute(ent.getKey().getIndex(), ent.getValue());
			}
		}

		return ret;
	}

}
