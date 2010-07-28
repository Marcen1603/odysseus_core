package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.viewer.model.meta.IMetadataModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTSymbolElement;


public class SWTSelectivitySymbolElement<C> extends SWTSymbolElement<C> {

	private double value = 0.0;
	
	@Override
	public void draw( Vector position, int width, int height, float zoomFactor ) {
		
		GC gc = getActualGC();
		if( gc == null ) 
			return;
		
		// Rechteck zeichnen
		if( value > 0.0 ) { 
			int realHeight = (int)(height * 0.3f);
			// Balken reinzeichnen
			gc.setBackground( Display.getDefault().getSystemColor( SWT.COLOR_RED ) );
			gc.fillRectangle( position.getX(), position.getY(), (int)(width * value), realHeight );
			
			gc.setForeground( Display.getDefault().getSystemColor( SWT.COLOR_BLACK ) );
			gc.drawRectangle( position.getX(), position.getY(), width,  realHeight );
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		IMetadataModel<C> node = (IMetadataModel<C>)(getNodeView().getModelNode());
		if( node == null )
			return;
		
		// Selektivitätswert ermitteln
		IMonitoringData<Double> selectivity = (IMonitoringData<Double>)node.getMetadataItem( "selectivity" );
		if( selectivity != null )
			value = selectivity.getValue();
		else
			value = 0.0;
	}

}
