package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;

public class DummySenderPO<T extends IStreamObject<?>> extends AbstractSink<T> {

	public DummySenderPO() {
		super();
	}
	
	public DummySenderPO(DummySenderPO<T> po) {
		super(po);
	}
	
	public DummySenderPO(DummyAO dummyAO) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process_next(T object, int port) {
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AbstractSink<T> clone() {
		return new DummySenderPO(this);
	}

}
