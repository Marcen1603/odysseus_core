package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public class MetadataPO<R extends IStreamObject<IMetaAttribute>> extends
		AbstractPipe<R, R> {

	IMetaAttribute outMeta;
	Class<? extends IMetaAttribute> metaClass;
	private Map<Integer, Integer> mappings;
	final List<Tuple<?>> inputMeta = new ArrayList<>();
	final List<Tuple<?>> currentValues = new ArrayList<>();

	public MetadataPO(IMetaAttribute outMeta, Map<Integer, Integer> mappings) {
		this.outMeta = outMeta;
		metaClass = outMeta.getClass();
		this.mappings = mappings;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(R object, int port) {

		synchronized (inputMeta) {
			// 1. Get input metadata values
			inputMeta.clear();
			currentValues.clear();
			object.getMetadata().retrieveValues(inputMeta);

			// 2. Create new metadata field
			try {
				object.setMetadata(metaClass.newInstance());
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			// 3. Restore "old" values
			object.getMetadata().retrieveValues(currentValues);
			for (Entry<Integer, Integer> mapping: mappings.entrySet()){
				currentValues.set(mapping.getValue(), inputMeta.get(mapping.getKey()));
			}
			object.getMetadata().writeValues(currentValues);
		}
		transfer(object);
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof MetadataPO)){
			return false;
		}
		@SuppressWarnings("unchecked")
		MetadataPO<IStreamObject<IMetaAttribute>> other = (MetadataPO<IStreamObject<IMetaAttribute>>)ipo;
		return this.metaClass.equals(other.metaClass);
		
	}

}
