package de.uniol.inf.is.odysseus.viewer.swt.diagram;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import de.uniol.inf.is.odysseus.viewer.view.stream.Parameters;

public class SWTHistogram extends AbstractSWTDiagram<Integer> implements PaintListener {

	private static final int AXIS_SPACE_TOP = 20;
	private static final int AXIS_SPACE_LEFT = 20;
	private static final int AXIS_SPACE_BOTTOM = 20;
	private static final int AXIS_SPACE_RIGHT = 20;
	private static final int DIVIDE = 20;
	
	private Composite parent;
	private Canvas canvas;
	private int width;
	private int height;
	
	private Color blackColor;
	
	// Daten
	private int maxCount;
	private int[] counts = new int[DIVIDE];
	
	public SWTHistogram( final Composite composite, Parameters params ) {
		super( composite, params );
		
		composite.setLayout( new GridLayout() );
		
		this.width = composite.getClientArea().width;
		this.height = composite.getClientArea().height;
		this.parent = composite; 
		
		blackColor = composite.getDisplay().getSystemColor( SWT.COLOR_BLACK );
		
		composite.addListener( SWT.Resize, new Listener() {
			@Override
			public void handleEvent( Event arg0 ) {
				width = composite.getClientArea().width;
				height = composite.getClientArea().height;
				canvas.redraw();
			}
		});
		
		canvas = new Canvas(composite, SWT.BORDER);
		GridData data = new GridData( GridData.FILL_BOTH);
		canvas.setLayoutData( data );
		canvas.setBackground( composite.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
		canvas.addPaintListener( this );
	}
	
	@Override
	public void paintControl( PaintEvent event ) {
		
		final int yAxisStartX = AXIS_SPACE_LEFT;
		final int yAxisStartY = AXIS_SPACE_TOP;
		final int yAxisEndX = AXIS_SPACE_LEFT;
		final int yAxisEndY = height - AXIS_SPACE_BOTTOM; 
		
		final int xAxisStartX = AXIS_SPACE_LEFT;
		final int xAxisStartY = height - AXIS_SPACE_BOTTOM;
		final int xAxisEndX = width - AXIS_SPACE_RIGHT;
		final int xAxisEndY = height - AXIS_SPACE_BOTTOM;
		
		GC gc = event.gc;
		gc.setForeground( blackColor );
		gc.drawLine( xAxisStartX, xAxisStartY, xAxisEndX, xAxisEndY );
		gc.drawLine( yAxisStartX, yAxisStartY, yAxisEndX, yAxisEndY );
		
		// Beschriftungen
		final int yX = AXIS_SPACE_LEFT - 10;
		final int yY = AXIS_SPACE_TOP;
		final int xX = width - AXIS_SPACE_RIGHT + 10;
		final int xY = height - AXIS_SPACE_BOTTOM;
		gc.drawString( "y", yX, yY, true );
		gc.drawString( "x", xX, xY, true );
		
		// Balken
		final int balkenWidth = ( width - AXIS_SPACE_LEFT - AXIS_SPACE_RIGHT ) / DIVIDE;
		
		for( int i = 0; i < DIVIDE; i++ ) {
			final int count = counts[i];
			final int balkenHeight = (int)(( height - AXIS_SPACE_TOP - AXIS_SPACE_BOTTOM ) * ( count / (float)maxCount ));
			final int balkenX = AXIS_SPACE_LEFT + (i * balkenWidth );
			final int balkenY = height - AXIS_SPACE_BOTTOM - balkenHeight;
			
			final int col = (int)(255 * ( count / (float)maxCount ));
			final Color color = new Color(parent.getDisplay(), 0,col,0);
			
			gc.setBackground( color );
			gc.fillRectangle( balkenX, balkenY, balkenWidth, balkenHeight );
			gc.drawRectangle( balkenX, balkenY, balkenWidth, balkenHeight );
			color.dispose();
		}
	}

	@Override
	public void reset() {
		maxCount = 0;
		counts = new int[DIVIDE];
		canvas.redraw();
	}

	@Override
	public void dataElementRecievedSynchronized( Integer element, int port ) {
		if( canvas.isDisposed() )
			return;
		
		int value = element;
		
		if( value == 1000 )
			value--;
		
		final int pos = value / (1000 / DIVIDE);
		counts[pos]++;
		if( counts[pos] > maxCount )
			maxCount = counts[pos];
		
		canvas.redraw();
	}

}
