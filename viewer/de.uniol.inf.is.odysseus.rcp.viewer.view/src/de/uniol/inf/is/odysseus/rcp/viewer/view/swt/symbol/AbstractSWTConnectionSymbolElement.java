package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol;

import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.AbstractConnectionSymbolElement;

public abstract class AbstractSWTConnectionSymbolElement<C> extends AbstractConnectionSymbolElement<C> {

	private GC gc;
	
	public void setActualGC( GC gc ) {
		this.gc = gc;
	}
	
	public GC getActualGC() {
		return gc;
	}

}
