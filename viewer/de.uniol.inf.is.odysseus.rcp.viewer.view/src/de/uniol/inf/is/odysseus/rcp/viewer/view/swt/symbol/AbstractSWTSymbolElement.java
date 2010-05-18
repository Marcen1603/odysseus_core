package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol;

import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.AbstractSymbolElement;

public abstract class AbstractSWTSymbolElement<C> extends AbstractSymbolElement<C>  {
	
	private GC actualGC;
	
	public void setActualGC( GC gc ) {
		actualGC = gc;
	}
	
	public GC getActualGC() {
		return actualGC;
	}

}
