package de.uniol.inf.is.odysseus.viewer.swt.render;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.viewer.view.render.IRenderManager;

public class SWTMiniMapWindow<C> {

	private static final float SPACE_PERC = 0.1f;

	private final Canvas canvas;
	private final Composite composite;
	private final SWTSimpleRenderer<C> renderer;
	private float zoomFactor;
	private Vector shift = new Vector(0,0);
	private boolean leftButtonPressed = false;
	
	public SWTMiniMapWindow( Composite comp, final IRenderManager<C> mainRenderManager ) {
		
		if( comp == null )
			throw new IllegalArgumentException("shell is null!");
		if( mainRenderManager == null )
			throw new IllegalArgumentException("mainRenderManager is null!");
				
		renderer = new SWTSimpleRenderer< C >();
		canvas = new Canvas( comp, SWT.BORDER);
		canvas.setBounds( comp.getClientArea() );
		canvas.setBackground( comp.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
		canvas.addPaintListener( new PaintListener() {
			@Override
			public void paintControl( PaintEvent arg0 ) {
				// GC speichern
				GC canvasGC = arg0.gc;
				
				// Zoomfaktor ermitteln
				calcZoomFactor( mainRenderManager.getDisplayedGraph() );
				
				// Direkt rendern, kein RenderManager nötig
				if( mainRenderManager.getDisplayedGraph() != null ) {
					renderer.setGC( canvasGC );
					renderer.render( mainRenderManager.getDisplayedGraph(), zoomFactor, null, shift );
				}
				
				// Rahmen zeichnen, wo unser Hauptfenster gerade ist
				final Vector pos = mainRenderManager.getGraphOffset()
								.mul( -1 )
								.add( shift )
								.mul( zoomFactor );
				final int width = (int)(mainRenderManager.getRenderWidth() / mainRenderManager.getZoomFactor() * zoomFactor);
				final int height = (int)(mainRenderManager.getRenderHeight() / mainRenderManager.getZoomFactor() * zoomFactor);
				
				canvasGC.drawRectangle( pos.getX(), pos.getY(), width, height );
				
			}
		});
		
		canvas.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseDown( MouseEvent e ) {
				
				if( e.button == 1 ) {
					final Vector pos = new Vector(e.x, e.y)
									.mul( -1 )
									.div( zoomFactor )
									.add( shift )
									.add( (int)(mainRenderManager.getRenderWidth() / mainRenderManager.getZoomFactor() / 2), (int)(mainRenderManager.getRenderHeight() / mainRenderManager.getZoomFactor() / 2));
					
					mainRenderManager.setGraphOffset( pos );
					leftButtonPressed = true;
				}
			}
			
			@Override
			public void mouseUp( MouseEvent e ) {
				if( e.button == 1) {
					leftButtonPressed = false;
				}
			}
		});
		
		canvas.addMouseMoveListener( new MouseMoveListener() {
			@Override
			public void mouseMove( MouseEvent e ) {
				if( leftButtonPressed ) {
					final Vector pos = new Vector(e.x, e.y)	
									.mul( -1 )
									.div( zoomFactor )
									.add( shift )
									.add( (int)(mainRenderManager.getRenderWidth() / mainRenderManager.getZoomFactor() / 2), (int)(mainRenderManager.getRenderHeight() / mainRenderManager.getZoomFactor() / 2) );
					
					mainRenderManager.setGraphOffset( pos );
					leftButtonPressed = true;
				}
			}
		});
		
		this.composite = comp;
		this.composite.addListener( SWT.Resize, new Listener() {
			@Override
			public void handleEvent( Event arg0 ) {
				canvas.setBounds( composite.getClientArea() );
			}
		});
	}
	
	public void refreshView() {
		if( canvas.isDisposed())
			return;
		
		canvas.redraw();
	}
	
	public Composite getComposite() {
		return composite;
	}
	
	private void calcZoomFactor( IGraphView<C> graphView ) {
		if( graphView == null ) {
			zoomFactor = 1.0f;
			shift = new Vector(0,0);
			return;
		}
			
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for( INodeView<C> node : graphView.getViewedNodes() ) {

			if( node.getPosition().getX() < minX ) 
				minX = node.getPosition().getX();
			
			if( node.getPosition().getY() < minY ) 
				minY = node.getPosition().getY();
			
			if( node.getPosition().getX() > maxX )
				maxX = node.getPosition().getX();
			
			if( node.getPosition().getY() > maxY ) 
				maxY = node.getPosition().getY();
			
		}
	
		final int graphWidth = maxX - minX;
		final int graphHeight = maxY - minY;
		final float zoomFactorWidth = canvas.getClientArea().width / (graphWidth + (SPACE_PERC * graphWidth));
		final float zoomFactorHeight = canvas.getClientArea().height / (graphHeight + (SPACE_PERC * graphHeight));
		
		zoomFactor = Math.min( zoomFactorWidth, zoomFactorHeight );	
		shift = new Vector(-minX, -minY);
	}
}
