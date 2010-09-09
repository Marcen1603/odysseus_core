package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractParameter;

public class BatchParameter extends AbstractParameter<BatchItem> {
	public BatchParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	protected void internalAssignment() {
		List<?> list = (List<?>) this.inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException(
					"wrong number of inputs for batch: " + list.size());
		}
		int size = ((Number) list.get(0)).intValue();
		long wait = (Long) list.get(1);
		BatchItem batchItem = new BatchItem(size, wait);
		setValue(batchItem);
	}

}
