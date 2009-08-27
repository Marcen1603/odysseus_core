package de.uniol.inf.is.odysseus.viewer.swt.symbol;

import org.eclipse.swt.graphics.GC;
import de.uniol.inf.is.odysseus.viewer.view.symbol.AbstractConnectionSymbolElement;

public abstract class AbstractSWTConnectionSymbolElement<C> extends AbstractConnectionSymbolElement<C> {

	private GC gc;
	
	public void setActualGC( GC gc ) {
		this.gc = gc;
	}
	
	public GC getActualGC() {
		return gc;
	}

}
