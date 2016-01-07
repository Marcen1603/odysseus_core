package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.SharedQuerySenderAO;

@SuppressWarnings("rawtypes")
public class SharedQuerySenderPO<T extends IStreamObject> extends AbstractSink<T> {

	public SharedQuerySenderPO(SharedQuerySenderAO operatorAO) {
		super();
	}

	public SharedQuerySenderPO(SharedQuerySenderPO<T> other) {
		super(other);
	}

	@Override
	protected AbstractSink clone() throws CloneNotSupportedException {
		return new SharedQuerySenderPO<T>(this);
	}

	@Override
	protected void process_open() throws OpenFailedException {

	}

	@Override
	protected void process_next(T object, int port) {

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

	@Override
	protected void process_close() {
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}

		return false;
	}

}
