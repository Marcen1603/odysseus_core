package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class KeyValueRenamePO<T extends KeyValueObject<?>> extends AbstractPipe<T, T> {
	private Map<String, String> renameMap;
	boolean keepInputObject;

	public KeyValueRenamePO() {
		
	}

	public KeyValueRenamePO(RenameAO renameAO) {
		this.renameMap = renameAO.getAliasesAsMap();
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T input, int port) {
		Map<String, Object> attributes = input.getAttributes();
		for(Entry<String, String> renamePair : this.renameMap.entrySet()) {
			attributes.put(renamePair.getValue(), attributes.remove(renamePair.getKey()));
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		T output = (T) new KeyValueObject(attributes);
		transfer(output);	
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

}
