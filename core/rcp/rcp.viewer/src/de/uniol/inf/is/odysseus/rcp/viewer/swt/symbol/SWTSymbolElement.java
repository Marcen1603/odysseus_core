package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol;

import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.symbol.impl.AbstractSymbolElement;

public abstract class SWTSymbolElement<C> extends AbstractSymbolElement<C>  {
	
	private GC actualGC;
	
	public void setActualGC( GC gc ) {
		actualGC = gc;
	}
	
	public GC getActualGC() {
		return actualGC;
	}

}
