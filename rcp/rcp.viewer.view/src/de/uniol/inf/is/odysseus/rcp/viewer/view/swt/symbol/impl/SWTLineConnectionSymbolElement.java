package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.impl;

import org.eclipse.swt.graphics.Color;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTConnectionSymbolElement;

public class SWTLineConnectionSymbolElement<C> extends SWTConnectionSymbolElement<C> {

	private Color color;
	
	public SWTLineConnectionSymbolElement( Color lineColor ) {
		color = lineColor;
	}

	@Override
	public void draw(Vector start, Vector end, float zoomFactor ) {
		
		if( getActualGC() == null ) {
			return;
		}
	
		getActualGC().setForeground( color );
		getActualGC().drawLine( (int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY() );
	}
	
	@Override
	public void update(  ) {}
}
