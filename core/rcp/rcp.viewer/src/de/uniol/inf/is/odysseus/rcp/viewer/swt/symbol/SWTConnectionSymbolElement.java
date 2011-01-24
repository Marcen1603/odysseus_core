package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol;

import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.symbol.impl.AbstractConnectionSymbolElement;

public abstract class SWTConnectionSymbolElement<C> extends AbstractConnectionSymbolElement<C> {

	private GC gc;
	
	public void setActualGC( GC gc ) {
		this.gc = gc;
	}
	
	public GC getActualGC() {
		return gc;
	}

}
