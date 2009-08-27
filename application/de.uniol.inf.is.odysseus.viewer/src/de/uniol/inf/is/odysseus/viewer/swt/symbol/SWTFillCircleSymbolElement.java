package de.uniol.inf.is.odysseus.viewer.swt.symbol;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;

public class SWTFillCircleSymbolElement<C> extends AbstractUnfreezableSWTSymbolElement<C> {

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
			
		gc.fillOval( pos.getX(), pos.getY(), width, height );
		
	}

}
