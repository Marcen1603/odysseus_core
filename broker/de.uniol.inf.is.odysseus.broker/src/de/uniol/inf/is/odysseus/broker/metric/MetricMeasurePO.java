package de.uniol.inf.is.odysseus.broker.metric;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class MetricMeasurePO<T extends IMetaAttribute> extends AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	private int attributePosition = -1;

	public MetricMeasurePO(int attributePosition) {
		this.attributePosition = attributePosition;
	}

	public MetricMeasurePO(MetricMeasurePO<T> original) {
		super(original);
		this.attributePosition = original.attributePosition;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public MetricMeasurePO<T> clone() throws CloneNotSupportedException {
		return new MetricMeasurePO<T>(this);
	}

	@Override
	protected void process_next(RelationalTuple<T> tuple, int port) {
		measure(tuple);
		transfer(tuple);
	}

	private void measure(RelationalTuple<T> tuple) {
		if (attributePosition >= 0) {
			try {
				Long attribute = (Long) tuple.getAttribute(attributePosition);
				long currentTime = System.currentTimeMillis();
				long offset = currentTime - attribute.longValue();
				System.out.println("Tuple needed " + offset + " ms");
			} catch (ClassCastException e) {
				System.err.println("Only Long is supported for measuring!");
			}
		}

	}

}
