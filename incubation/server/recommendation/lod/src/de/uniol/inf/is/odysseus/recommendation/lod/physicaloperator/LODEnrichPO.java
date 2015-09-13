package de.uniol.inf.is.odysseus.recommendation.lod.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Christopher Schwarz
 */
public class LODEnrichPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {
	
	public LODEnrichPO() {
		super();
	}
	
	@Override
	public OutputMode getOutputMode() {
	    return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	protected void process_next(Tuple<T> tuple, int port) {

		
		tuple = tuple.append("test", true);
		
//		System.out.println(tuple.toString());
		
		transfer(tuple, port);
	}
}
