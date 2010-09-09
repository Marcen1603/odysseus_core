package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.impl;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;

public class SWTFillCircleSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private final Color color;
	
	public SWTFillCircleSymbolElement(  Color color ) {
		this.color = color;
	}
	
	@Override
	public void draw( Vector pos, int width, int height, float zoomFactor ) {
		
		// Kreis zeichnen
		GC gc = getActualGC();
		
		if( gc == null )
			return;

		gc.setBackground( color );
			
		gc.fillOval( (int)pos.getX(), (int)pos.getY(), width, height );
		
	}

}
