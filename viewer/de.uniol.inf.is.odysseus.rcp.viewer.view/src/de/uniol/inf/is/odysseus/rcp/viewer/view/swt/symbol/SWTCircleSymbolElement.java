package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;

public class SWTCircleSymbolElement<C> extends AbstractUnfreezableSWTSymbolElement<C> {

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
			
		gc.drawOval( pos.getX(), pos.getY(), width, height );
	}

}
