package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol;

import org.eclipse.swt.graphics.Color;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;

public class SWTConnectionSymbolElement<C> extends AbstractSWTConnectionSymbolElement<C> {

	private Color color;
	
	public SWTConnectionSymbolElement( Color lineColor ) {
		color = lineColor;
	}

	@Override
	public void draw(Vector start, Vector end, float zoomFactor ) {
		
		if( getActualGC() == null ) {
			return;
		}
	
		getActualGC().setForeground( color );
		getActualGC().drawLine( start.getX(), start.getY(), end.getX(), end.getY() );
	}
	
	@Override
	public void update(  ) {}
}
