package de.uniol.inf.is.odysseus.viewer.swt.diagram;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.view.stream.Parameters;


/**
 * Liniendiagramm. Bildet aus den letzten X Werten den Durchschnitt
 * 
 * @author boomer
 */
public class SWTLineDiagram extends AbstractSWTDiagram<Float> implements PaintListener {

	private final Canvas canvas;
	private int width;
	private int height;
	
	// Werte zur Darstellung
	private static final int AXIS_SPACE_TOP = 20; // Abstände zum Rand
	private static final int AXIS_SPACE_LEFT = 20;
	private static final int AXIS_SPACE_BOTTOM = 20;
	private static final int AXIS_SPACE_RIGHT = 20;
	
	private static final int LABEL_SPACE_SIDE = 30; // Textgröße der Achsenbeschriftungen
	private static final int LABEL_SPACE_TOPDOWN = 20; 
	
	private float maxValue = 0; // Höchster bisheriger Wert
	private final int maxItemCount;
	
	private ArrayList<Float> values = new ArrayList<Float>();
	private ArrayList<Float> recievedElements = new ArrayList<Float>();
	
	public SWTLineDiagram( INodeModel<?> node, final Composite composite, Parameters params ) {
		super( node, composite, params );
		
		composite.setLayout( new GridLayout() );
		width = composite.getClientArea().width;
		height = composite.getClientArea().height;
		
		composite.addListener( SWT.Resize, new Listener() {
			@Override
			public void handleEvent( Event arg0 ) {
				width = composite.getClientArea().width;
				height = composite.getClientArea().height;
				canvas.redraw();
			}
		});
		
		canvas = new Canvas( composite, SWT.BORDER );
		canvas.setLayoutData( new GridData(GridData.FILL_BOTH) );
		canvas.setBackground( Display.getDefault().getSystemColor( SWT.COLOR_WHITE ) );
		canvas.addPaintListener( this );
		
		// Parameter umsetzen
		maxItemCount = params.getInteger( "maxItems", 100 );
	}

	@Override
	public void dataElementRecievedSynchronized( Float element, int port ) {
		recievedElements.add( element );
		
		// Durchschnitt berechnen
		final int itemsToCount = Math.min( maxItemCount, recievedElements.size() );
		float sum = 0;
		for( int i = 0; i < itemsToCount; i++ )
			sum += recievedElements.get( recievedElements.size() - 1 - i );
		
		float value = sum / itemsToCount;
		if( value > maxValue )
			maxValue = value;
		
		values.add( value );
		if( values.size() > maxItemCount )
			values.remove( 0 );
		
		canvas.redraw();
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void paintControl( PaintEvent arg0 ) {
		GC gc = arg0.gc;
		
		// Achsen zeichnen
		gc.setForeground( Display.getDefault().getSystemColor( SWT.COLOR_BLACK ) );
		gc.drawLine( LABEL_SPACE_SIDE + AXIS_SPACE_LEFT, AXIS_SPACE_TOP, LABEL_SPACE_SIDE + AXIS_SPACE_LEFT, height - AXIS_SPACE_BOTTOM - LABEL_SPACE_TOPDOWN);
		gc.drawLine( LABEL_SPACE_SIDE + AXIS_SPACE_LEFT, height - AXIS_SPACE_BOTTOM - LABEL_SPACE_TOPDOWN, width - AXIS_SPACE_RIGHT, height - AXIS_SPACE_BOTTOM - LABEL_SPACE_TOPDOWN);
	
		// Achsenbeschriftung
		gc.drawString( String.valueOf( maxValue ), AXIS_SPACE_LEFT, AXIS_SPACE_TOP, true );
		gc.drawString( "0", AXIS_SPACE_LEFT, height - AXIS_SPACE_BOTTOM - LABEL_SPACE_TOPDOWN, true );
		final int minItemCount = Math.max( recievedElements.size() - maxItemCount, 0 );
		gc.drawString( String.valueOf( minItemCount ), AXIS_SPACE_LEFT + LABEL_SPACE_SIDE, height - AXIS_SPACE_BOTTOM - LABEL_SPACE_TOPDOWN, true );
		gc.drawString( String.valueOf( recievedElements.size() ), width - AXIS_SPACE_RIGHT - LABEL_SPACE_SIDE, height - AXIS_SPACE_BOTTOM - LABEL_SPACE_TOPDOWN, true );
		
		// Werte als Graphen zeichnen
		if( values.isEmpty() )
			return;
		
		final float step = (float)( width - AXIS_SPACE_LEFT - AXIS_SPACE_RIGHT - LABEL_SPACE_SIDE ) / maxItemCount;
		final int maxHeight = ( height - AXIS_SPACE_TOP - AXIS_SPACE_BOTTOM - LABEL_SPACE_TOPDOWN );
		
		int oldX = -1;
		int oldY = -1;
		for( int i = 0; i < values.size(); i++ ) {
			
			final float actValue = values.get( i );
			
			int x = (int)(AXIS_SPACE_LEFT + LABEL_SPACE_SIDE + ( i * step ));
			int y = AXIS_SPACE_TOP + maxHeight - (int)( maxHeight * ( actValue / maxValue ) );
						
			
			if( oldX != -1 && oldY != -1 ) {
				gc.drawLine( oldX, oldY, x, y );
				if( i == values.size() - 1) {
					String text = String.valueOf( actValue );
					int w = 0;
					for( int p = 0; p < text.length(); p++ )
						w += gc.getCharWidth( text.charAt( p ) );
					gc.drawString( text, x - w, y, true );
				}
			}
			
			oldX = x;
			oldY = y;
		}
	}

}
