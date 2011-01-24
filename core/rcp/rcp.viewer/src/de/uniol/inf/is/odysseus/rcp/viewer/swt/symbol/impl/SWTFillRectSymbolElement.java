package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTFillRectSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private final Color borderColor;
	
	public SWTFillRectSymbolElement( Color borderColor ) {
		this.borderColor = borderColor;
	}
	
	@Override
	public void draw( Vector pos, int width, int height, float zoomFactor  ) {
		
		// Kreis zeichnen
		GC gc = getActualGC();
		
		if( gc == null )
			return;
		
		gc.setBackground( borderColor );
		gc.fillRectangle( (int)pos.getX(), (int)pos.getY(), width, height );
	}

}
