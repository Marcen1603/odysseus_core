package de.uniol.inf.is.odysseus.viewer.swt.symbol;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;

public class SWTRectSymbolElement<C> extends AbstractUnfreezableSWTSymbolElement<C> {

	private final Color borderColor;
	
	public SWTRectSymbolElement( Color borderColor ) {
		this.borderColor = borderColor;
	}
	
	@Override
	public void draw( Vector pos, int width, int height, float zoomFactor  ) {
		
		// Kreis zeichnen
		GC gc = getActualGC();
		
		if( gc == null )
			return;
		
		gc.setForeground( borderColor );
		gc.drawRectangle( pos.getX(), pos.getY(), width, height );
	}

}
