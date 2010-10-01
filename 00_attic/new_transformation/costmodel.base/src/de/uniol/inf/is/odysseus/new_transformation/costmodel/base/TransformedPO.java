package de.uniol.inf.is.odysseus.new_transformation.costmodel.base;

import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;

public class TransformedPO {
	private final ISource<?> source;
	private final ISink<?> sink;

	public TransformedPO(ISource<?> source, ISink<?> sink) {
		this.source = source;
		this.sink = sink;
	}

	public TransformedPO(IPipe<?, ?> pipe) {
		this.source = pipe;
		this.sink = pipe;
	}
	
	public TransformedPO(ISource<?> source) {
		this.source = source;
		this.sink = null;
	}

	public ISource<?> getSource() {
		return source;
	}

	public ISink<?> getSink() {
		return sink;
	}
}
