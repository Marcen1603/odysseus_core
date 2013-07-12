package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <K>
 * @param <T>
 */
public abstract class ExistenceToPayloadPO<K extends IProbabilistic, T extends IStreamObject<K>> extends AbstractPipe<T, T> {

	public ExistenceToPayloadPO() {
	}

	public ExistenceToPayloadPO(ExistenceToPayloadPO<K, T> existenceToPayloadPO) {
		super(existenceToPayloadPO);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
