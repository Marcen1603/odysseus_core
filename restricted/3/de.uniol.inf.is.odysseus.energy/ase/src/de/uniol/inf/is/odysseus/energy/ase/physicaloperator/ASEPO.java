package de.uniol.inf.is.odysseus.energy.ase.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * 
 * @author Jan Sören Schwarz
 *
 */
public class ASEPO<M extends IMetaAttribute> extends AbstractPipe<KeyValueObject<M>, Tuple<M>> {

	@Override
	public OutputMode getOutputMode() {
		return null;
	}

	@Override
	protected void process_next(KeyValueObject<M> object, int port) {
	}

}
