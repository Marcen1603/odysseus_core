package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PlaceHolderAO;

/**
 * This is just an help operator that is used for rekursive plans and does
 * provide no execution function
 * 
 * @author MarcoGrawunder
 *
 */
public class PlaceHolderPO<R extends IStreamObject<?>, W extends IStreamObject<?>> extends AbstractPipe<R, W> {

	final PlaceHolderAO placeHolder;
	
	public PlaceHolderPO(PlaceHolderAO placeHolder) {
		this.placeHolder = placeHolder;
	}
	
	public PlaceHolderAO getPlaceHolder() {
		return placeHolder;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		throw new IllegalArgumentException("This operator cannot be used!");
	}

	@Override
	public OutputMode getOutputMode() {
		// I am not sure, if this matters ...
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		throw new IllegalArgumentException("This operator cannot be used!");
	}

}
