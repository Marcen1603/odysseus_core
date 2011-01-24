package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTCircleSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private final Color color;
	
	public SWTCircleSymbolElement( Color color ) {
		this.color = color;
	}
	
	@Override
	public void draw( Vector pos, int width, int height, float zoomFactor ) {
		
		// Kreis zeichnen
		GC gc = getActualGC();
		
		
		if( gc == null )
			return;

		gc.setForeground( color );
			
		gc.drawOval( (int)pos.getX(), (int)pos.getY(), width, height );
	}

}
