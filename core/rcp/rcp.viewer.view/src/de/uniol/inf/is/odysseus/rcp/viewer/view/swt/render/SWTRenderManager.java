package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.render;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.position.INodePositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.view.render.IRenderManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.render.impl.RenderRange;
import de.uniol.inf.is.odysseus.rcp.viewer.view.select.impl.GraphSelector;

public final class SWTRenderManager<C> implements PaintListener, MouseListener, MouseMoveListener, MouseWheelListener, IRenderManager<C> {
		
	private static final Logger logger = LoggerFactory.getLogger( SWTRenderManager.class );

	private static final int MIN_SELECTRECT = 5;
	private static final int SCROLL_SPEED = 20;
	
	// 1 = linke Maustaste
	// 2 = Mittlere Maustaste
	// 3 = rechte Maustaste
	private static final int MOUSE_SELECT_BUTTON1 = 1;
	private static final int MOUSE_SELECT_BUTTON2 = 3;
	private static final int MOUSE_DRAG_BUTTON = 2;
	private static final int MOUSE_BORDER_BUTTON = 1;
	
	private RenderRange renderRange = new RenderRange();
	private final Canvas canvas;

	private final GraphSelector<C> nodeSelector = new GraphSelector<C>();
	private Vector dragStart = new Vector(0,0);

	private INodePositioner<C> nodePositioner;
	
	private ArrayList<INodeView<C>> draggedNode = new ArrayList<INodeView<C>>();
	private ArrayList<Vector> dragObject = new ArrayList<Vector>();
	private Vector dragGraph = new Vector(0,0);
	
	private boolean mouseDragButtonPressed = false;
	
	private int width;
	private int height;
	
	private Rectangle selectRect;
	
//	private int updateInterval = 1000; // in ms
//	private Thread updaterThread;
	
	private SWTSymbolRenderer<C> renderer;
	private IGraphView<C> graphView;
	private Vector shift = new Vector(0,0);
	private float zoomFactor = 1.0f;
		
	public SWTRenderManager( Composite comp, INodePositioner<C> nodePositioner ) {
		
		if( nodePositioner == null )
			throw new IllegalArgumentException("device is null");
		
		this.renderer = new SWTSymbolRenderer< C >();
		this.nodePositioner = nodePositioner;
		comp.setLayout(new FillLayout());
		this.canvas = new Canvas(comp, SWT.BORDER );
		this.canvas.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		this.canvas.setSize(1000, 1000);
		
		
		canvas.addListener( SWT.Resize, new Listener() {

			@Override
			public void handleEvent( Event event ) {
				
				final Rectangle rect = canvas.getClientArea();
				
				if( getDisplayedGraph() != null ) {
					// Graphposition neu ausrichten
					final Vector newPos = shift.add( ( rect.width - width ) / 2, ( rect.height - height ) / 2 );
					setGraphOffset( newPos );
				}
				
				renderRange.x = rect.x;
				renderRange.y = rect.y;
				renderRange.width = width = rect.width;
				renderRange.height = height = rect.height;
				
				//if( logger.isDebugEnabled() )
					//logger.debug( "SWTRenderCanvas resize:" + rect.width + " x " + rect.height );
				
				refreshView();
			}
			
		});
		
		canvas.addKeyListener( new KeyListener() {

			@Override
			public void keyPressed( KeyEvent arg0 ) {
				if( arg0.keyCode == SWT.ARROW_DOWN ) 
					setGraphOffset( getGraphOffset().sub( 0, SCROLL_SPEED ));
				if( arg0.keyCode == SWT.ARROW_UP) 
					setGraphOffset( getGraphOffset().add( 0, SCROLL_SPEED ));
				if( arg0.keyCode == SWT.ARROW_LEFT ) 
					setGraphOffset( getGraphOffset().add( SCROLL_SPEED, 0));
				if( arg0.keyCode == SWT.ARROW_RIGHT ) 
					setGraphOffset( getGraphOffset().sub( SCROLL_SPEED, 0 ));
			}

			@Override
			public void keyReleased( KeyEvent arg0 ) {}
			
		});
		
		canvas.addPaintListener( this );
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
		canvas.addMouseWheelListener(this);
		
//		updaterThread = new Thread( new Runnable() {
//			@Override
//			public void run() {
//				
//				while( true ) {
//					if( Display.getDefault().isDisposed())
//						break;
//					
//					Display.getDefault().asyncExec( new Runnable() {
//
//						@Override
//						public void run() {
//						renderer.update(graphView);
//							refreshView();
//						}
//						
//					});
//					
//					try {
//						Thread.sleep( updateInterval );
//					} catch( InterruptedException ex ) {}
//				}
//				
//			}			
//		});
//		updaterThread.start();
	}
	
	public void setUpdateInterval( int interval ) {
		if( interval < 10 ) 
			interval = 10;
//		updateInterval = interval;
	}
	
	public int getUpdateInterval() {
//		return updateInterval;
		return 0;
	}

	
	
	
	@Override
	public GraphSelector<C> getSelector() {
		return nodeSelector;
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#resetPositions()
	 */
	@Override
	public void resetPositions() {
		if( getDisplayedGraph() != null ) {
			final int width = canvas.getClientArea().width;
			final int height = canvas.getClientArea().height;
			
			nodePositioner.positionize( graphView, width, height );
			resetGraphOffset();
			refreshView();
		}
	}
	
	private INodeView<C> getNodeFromPosition( int x, int y ) {
		if( graphView != null ) {
			for( INodeView<C> node : graphView.getViewedNodes() ) {
				if( !node.isVisible() )
					continue;
				
				final Vector realPos = node.getPosition().add( shift ).mul( zoomFactor );
				final int realWidth = (int)(node.getWidth() * zoomFactor);
				final int realHeight = (int)(node.getHeight() * zoomFactor);
				
				if( x >= realPos.getX() && y >= realPos.getY()  && x <= realPos.getX() + realWidth && y <= realPos.getY() + realHeight ) {
					return node;
				}
			}
		}
		return null;
	}

	
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#getZoomFactor()
	 */
	@Override
	public float getZoomFactor() {
		return zoomFactor;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#setZoomFactor(float)
	 */
	@Override
	public void setZoomFactor( float zoomFactor ) {
		if( zoomFactor < 0.2f )
			zoomFactor = 0.2f;
		if( zoomFactor > 4.0f )
			zoomFactor = 4.0f;
		
		if( zoomFactor != this.zoomFactor ) {
			this.zoomFactor = zoomFactor;
			refreshView();
			logger.debug( "Zoomfactor set:" + zoomFactor );
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#resetZoom()
	 */
	@Override
	public void resetZoom() {
		setZoomFactor(1.0f);
		refreshView();
	}
	
	@Override
	public void zoom( float offsetZoom ) {
		zoom( (int)(canvas.getClientArea().width / zoomFactor / 2), 
			(int)(canvas.getClientArea().height / zoomFactor / 2), offsetZoom );
	}
	
	@Override
	public void zoom( int centerX, int centerY, float offsetZoom ) {
		
		final int width = canvas.getClientArea().width;
		final int height = canvas.getClientArea().height;
		
		final float oldWidth = ( width / zoomFactor  );
		final float oldHeight = ( height / zoomFactor );
		
		setZoomFactor( zoomFactor + offsetZoom );
		
		final float widthPerc = (float)centerX / width;
		final float heightPerc = (float)centerY / height; 
		
		final float newWidth = ( width / zoomFactor );
		final float newHeight = ( height / zoomFactor );
		
		final float xMove = ( oldWidth - newWidth ) * widthPerc;
		final float yMove = ( oldHeight - newHeight)  * heightPerc;
		setGraphOffset( shift.sub( (int)xMove, (int)yMove ));
	}

	
	
		
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#setDisplayedGraph(de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView)
	 */
	@Override
	public void setDisplayedGraph( IGraphView<C> graph ) {
		nodeSelector.unselectAll();
		if( this.graphView != graph ) {
			this.graphView = graph;
			refreshView();
			logger.debug( "Displayed graph changed: " + graphView.toString() );
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#getDisplayedGraph()
	 */
	@Override
	public IGraphView<C> getDisplayedGraph() {
		return graphView;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#resetGraphOffset()
	 */
	@Override
	public void resetGraphOffset() {
		setGraphOffset( new Vector(0,0));
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#getGraphOffset()
	 */
	@Override
	public Vector getGraphOffset() {
		return shift;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#setGraphOffset(de.uniol.inf.is.odysseus.viewer.view.graph.Vector)
	 */
	@Override
	public void setGraphOffset( Vector offset ) {
		shift = offset;
		refreshView();
		//logger.debug( "Displayed graph changed in position: " + offset.getX() + " " + offset.getY() );
	}
	


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.viewer.swt.render.IRenderManager#refreshView()
	 */
	@Override
	public void refreshView() {
		if( canvas.isDisposed() )
			return;
				
		canvas.redraw();
	}
	
	@Override
	public void paintControl( PaintEvent e ) {
		renderer.setGC( e.gc );
		renderer.render( graphView, nodeSelector.getSelected(), zoomFactor, renderRange, shift );
		
		// Auswahlrahmen
		if( selectRect != null && Math.abs( selectRect.width ) > MIN_SELECTRECT && Math.abs(selectRect.height) > MIN_SELECTRECT ) {
			GC gc = e.gc;
			gc.setForeground( e.display.getSystemColor( SWT.COLOR_BLACK ) );
			gc.drawRectangle( selectRect );
		}
	}

	@Override
	public void mouseDoubleClick( MouseEvent e ) {}

	@SuppressWarnings("unchecked")
	@Override
	public void mouseDown( MouseEvent e ) {
		
		dragStart = new Vector(e.x, e.y);
		
		// Linke Maustaste oder rechte Maustaste
		if( e.button == MOUSE_SELECT_BUTTON1 || e.button == MOUSE_SELECT_BUTTON2 ) {
			
			if( (e.stateMask & SWT.CTRL) == 0 ) {
				// Geklickten Knoten suchen
				final INodeView<C> clickedNode = getNodeFromPosition(e.x, e.y);
				final Collection<INodeView<C>> selectedNodes = nodeSelector.getSelected();
				
				// Geklickter Knoten schon ausgewählt?
				if( selectedNodes.contains( clickedNode ) ) {
					
					// Alle ausgewählten Knoten ziehen
					for( INodeView<C> selectedNode : selectedNodes ) {
						draggedNode.add( selectedNode );
						dragObject.add( selectedNode.getPosition() );
					}
					return;
				}
				
				if( clickedNode != null ) {
					if( (e.stateMask & SWT.SHIFT) != 0 ) {
						if( selectedNodes.size() == 1 && !selectedNodes.contains( clickedNode )) {
							
							// Pfad auswählen
							INodeView<C>[] nodeDisplays = selectedNodes.toArray( new INodeView[0] );
							
							nodeSelector.unselectAll();
							if( !nodeSelector.selectPath( nodeDisplays[0], clickedNode )) {
								return;
							}
						}
					} else if( (e.stateMask & SWT.CTRL) == 0 )
						nodeSelector.unselectAll();
					
					// Geklickten Knoten auswählen
					nodeSelector.select( clickedNode );
					
					// Knoten ziehen
					if( e.button == MOUSE_SELECT_BUTTON1) {
						draggedNode.add( clickedNode );
						dragObject.add( clickedNode.getPosition() );
					}
				} else if( e.button == MOUSE_BORDER_BUTTON ) {
					// Rahmen ziehen
					nodeSelector.unselectAll();
					selectRect = new Rectangle(e.x, e.y, 0, 0);
				}
			} else {
				// STRG gedr�ckt... Ziehen
				mouseDragButtonPressed = true;
				dragGraph = getGraphOffset();
			}
		} else if( e.button == MOUSE_DRAG_BUTTON ) { // mittlere Maustaste
			
			// Graphen ziehen
			mouseDragButtonPressed = true;
			dragGraph = getGraphOffset();
		}
		refreshView();
	}

	@Override
	public void mouseUp( MouseEvent e ) {
		// Linke Maustaste oder rechte Maustaste
		if( (e.button == MOUSE_SELECT_BUTTON1 || e.button == MOUSE_SELECT_BUTTON2) && mouseDragButtonPressed ) {
			mouseDragButtonPressed = false;
		}

		// Auswahlrechteck auswerten
		if( e.button == MOUSE_BORDER_BUTTON ) {
			draggedNode.clear();
			dragObject.clear();
			if( selectRect != null && Math.abs( selectRect.width ) > MIN_SELECTRECT && Math.abs(selectRect.height) > MIN_SELECTRECT) {
				
				nodeSelector.unselectAll();
				
				selectRect.width = e.x - selectRect.x;
				selectRect.height = e.y - selectRect.y;
			
				if( selectRect.width < 0 ) {
					selectRect.x += selectRect.width;
					selectRect.width *= -1;
				}
				if( selectRect.height < 0 ) {
					selectRect.y += selectRect.height;
					selectRect.height *= -1;
				}
				
				if( graphView != null ) {
					
					// Knoten finden
					Collection<INodeView<C>> nodesToSelect = new ArrayList<INodeView<C>>();
					for( INodeView<C> nodeDisplay : graphView.getViewedNodes() ) {
						
						// Knoten in gezeichnete Koordinaten umrechnen
						final Vector pos = nodeDisplay.getPosition()
										.add( getGraphOffset() )
										.mul( zoomFactor );
						final int width = (int)(nodeDisplay.getWidth() * getZoomFactor());
						final int height = (int)(nodeDisplay.getHeight() * getZoomFactor());
						
						if( selectRect.intersects( (int)pos.getX(), (int)pos.getY(), width, height ) ) {
							nodesToSelect.add( nodeDisplay );
						}
					}
					nodeSelector.select( nodesToSelect );
					
					// Verbindungen finden
					
					
					refreshView();
				}
			}	
			selectRect = null;
		}
		else if( e.button == MOUSE_DRAG_BUTTON ) 
			mouseDragButtonPressed = false;
	}

	@Override
	public void mouseMove( MouseEvent e ) {
		
		final Vector distance = new Vector(e.x, e.y).sub( dragStart );
		final Vector distanceRel = distance.div( zoomFactor );
		
		if( !draggedNode.isEmpty() ) {
			// Ziehe grade Knoten
			for( int i = 0; i < draggedNode.size(); i++ ) {
				draggedNode.get( i ).setPosition( dragObject.get( i ).add( distanceRel ) );
			}
			refreshView();
		}
		else if( selectRect != null ) {
			// Ziehe einen Rahmen
			selectRect.width = e.x - selectRect.x;
			selectRect.height = e.y - selectRect.y;
			refreshView();
		}
		else if( mouseDragButtonPressed ) {
			// Ziehe Graphen
			setGraphOffset( dragGraph.add( distanceRel ));
			if( !canvas.isFocusControl() )
				canvas.setFocus();
			
			refreshView();
		}
	}

	@Override
	public void mouseScrolled( MouseEvent e ) {
		if( e.count > 0 ) { // Mausrad nach oben
			zoom(e.x, e.y, 0.1f);
			refreshView();
		} 
		if( e.count < 0 ) {
			zoom(e.x, e.y, -0.1f);
			refreshView();
		}
		
	}

	@Override
	public int getRenderHeight() {
		return canvas.getClientArea().height;
	}

	@Override
	public int getRenderWidth() {
		return canvas.getClientArea().width;
	}

	public Control getCanvas() {
		return canvas;
	}
}
