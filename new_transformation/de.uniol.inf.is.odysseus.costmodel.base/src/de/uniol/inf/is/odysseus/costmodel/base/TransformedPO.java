package de.uniol.inf.is.odysseus.costmodel.base;

import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

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
