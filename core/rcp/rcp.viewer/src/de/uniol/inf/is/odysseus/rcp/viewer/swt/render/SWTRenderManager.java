/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.viewer.swt.render;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.position.INodePositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.render.IRenderManager;
import de.uniol.inf.is.odysseus.rcp.viewer.render.impl.RenderRange;
import de.uniol.inf.is.odysseus.rcp.viewer.select.ISelectListener;
import de.uniol.inf.is.odysseus.rcp.viewer.select.ISelector;
import de.uniol.inf.is.odysseus.rcp.viewer.select.impl.GraphSelector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public final class SWTRenderManager<C> implements ISelectListener<INodeView<C>>, PaintListener, MouseListener, MouseMoveListener, MouseWheelListener, IRenderManager<C> {

	private static final Logger logger = LoggerFactory.getLogger(SWTRenderManager.class);

	private static final int MIN_SELECTRECT = 5;
	private static final int SCROLL_SPEED = 20;

	// 1 = linke Maustaste
	// 2 = Mittlere Maustaste
	// 3 = rechte Maustaste
	private static final int MOUSE_SELECT_BUTTON1 = 1;
	private static final int MOUSE_SELECT_BUTTON2 = 3;
	private static final int MOUSE_DRAG_BUTTON = 2;
	private static final int MOUSE_BORDER_BUTTON = 1;

	private final Composite canvas;

	private final GraphSelector<C> nodeSelector = new GraphSelector<C>();
	private final GraphSelector<C> highlightSelector = new GraphSelector<C>();
	private Vector dragStart = new Vector(0, 0);

	private INodePositioner<C> nodePositioner;

	private ArrayList<INodeView<C>> draggedNode = new ArrayList<INodeView<C>>();
	private ArrayList<Vector> dragObject = new ArrayList<Vector>();
	private Vector dragGraph = new Vector(0, 0);

	private boolean mouseDragButtonPressed = false;

	private Rectangle selectRect;

	private SWTSymbolRenderer<C> renderer;
	private IGraphView<C> graphView;
	private Vector shift = new Vector(0, 0);
	private float zoomFactor = 1.0f;

	public SWTRenderManager(Composite comp, INodePositioner<C> nodePositioner) {

		if (nodePositioner == null)
			throw new IllegalArgumentException("device is null");

		this.renderer = new SWTSymbolRenderer<C>();
		this.nodePositioner = nodePositioner;
		comp.setLayout(new FillLayout());
		this.canvas = new Composite(comp, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		//this.canvas.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		this.canvas.setBackground(Display.findDisplay(Thread.currentThread()).getSystemColor(SWT.COLOR_WHITE));
		this.canvas.setSize(1000, 1000);

		nodeSelector.addSelectListener(this);
		highlightSelector.addSelectListener(this);

		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == SWT.ARROW_DOWN)
					setGraphOffset(getGraphOffset().sub(0, SCROLL_SPEED));
				if (arg0.keyCode == SWT.ARROW_UP)
					setGraphOffset(getGraphOffset().add(0, SCROLL_SPEED));
				if (arg0.keyCode == SWT.ARROW_LEFT)
					setGraphOffset(getGraphOffset().add(SCROLL_SPEED, 0));
				if (arg0.keyCode == SWT.ARROW_RIGHT)
					setGraphOffset(getGraphOffset().sub(SCROLL_SPEED, 0));
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

		});

		canvas.addPaintListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
		canvas.addMouseWheelListener(this);
	}
	
	public void dispose() {
		renderer.dispose();
	}

	public void setUpdateInterval(int interval) {
		if (interval < 10)
			interval = 10;
		// updateInterval = interval;
	}

	public int getUpdateInterval() {
		// return updateInterval;
		return 0;
	}

	@Override
	public GraphSelector<C> getSelector() {
		return nodeSelector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#resetPositions
	 * ()
	 */
	@Override
	public void resetPositions() {
		if (getDisplayedGraph() != null) {
			final int width = canvas.getClientArea().width;
			final int height = canvas.getClientArea().height;

			nodePositioner.positionize(graphView, width, height);
			resetGraphOffset();
			refreshView();
		}
	}

	private INodeView<C> getNodeFromPosition(int x, int y) {
		if (graphView != null) {
			for (INodeView<C> node : graphView.getViewedNodes()) {
				if (!node.isVisible())
					continue;

				final Vector realPos = node.getPosition().add(shift).mul(zoomFactor);
				final int realWidth = (int) (node.getWidth() * zoomFactor);
				final int realHeight = (int) (node.getHeight() * zoomFactor);

				if (x >= realPos.getX() && y >= realPos.getY() && x <= realPos.getX() + realWidth && y <= realPos.getY() + realHeight) {
					return node;
				}
			}
		}
		return null;
	}
	
	private Collection<INodeView<C>> getNodesInRect( Rectangle rect1 ) {
		Rectangle rect = new Rectangle(rect1.x, rect1.y, rect1.width, rect1.height);
		Collection<INodeView<C>> nodes = new ArrayList<INodeView<C>>();
		if (rect != null && Math.abs(rect.width) > MIN_SELECTRECT && Math.abs(rect.height) > MIN_SELECTRECT) {

			if (rect.width < 0) {
				rect.x += rect.width;
				rect.width *= -1;
			}
			if (rect.height < 0) {
				rect.y += rect.height;
				rect.height *= -1;
			}

			if (graphView != null) {

				// Knoten finden
				for (INodeView<C> nodeDisplay : graphView.getViewedNodes()) {

					// Knoten in gezeichnete Koordinaten umrechnen
					final Vector pos = nodeDisplay.getPosition().add(getGraphOffset()).mul(zoomFactor);
					final int width = (int) (nodeDisplay.getWidth() * getZoomFactor());
					final int height = (int) (nodeDisplay.getHeight() * getZoomFactor());

					if (rect.intersects((int) pos.getX(), (int) pos.getY(), width, height)) {
						nodes.add(nodeDisplay);
					}
				}
			}
		}
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#getZoomFactor()
	 */
	@Override
	public float getZoomFactor() {
		return zoomFactor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#setZoomFactor
	 * (float)
	 */
	@Override
	public void setZoomFactor(float zoomFactor) {
		if (zoomFactor < 0.2f)
			zoomFactor = 0.2f;
		if (zoomFactor > 4.0f)
			zoomFactor = 4.0f;

		if (zoomFactor != this.zoomFactor) {
			this.zoomFactor = zoomFactor;
			refreshView();
			logger.debug("Zoomfactor set:" + zoomFactor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#resetZoom()
	 */
	@Override
	public void resetZoom() {
		setZoomFactor(1.0f);
		refreshView();
	}

	@Override
	public void zoom(float offsetZoom) {
		zoom((int) (canvas.getClientArea().width / zoomFactor / 2), (int) (canvas.getClientArea().height / zoomFactor / 2), offsetZoom);
	}

	@Override
	public void zoom(int centerX, int centerY, float offsetZoom) {

		final int width = canvas.getClientArea().width;
		final int height = canvas.getClientArea().height;

		final float oldWidth = (width / zoomFactor);
		final float oldHeight = (height / zoomFactor);

		setZoomFactor(zoomFactor + offsetZoom);

		final float widthPerc = (float) centerX / width;
		final float heightPerc = (float) centerY / height;

		final float newWidth = (width / zoomFactor);
		final float newHeight = (height / zoomFactor);

		final float xMove = (oldWidth - newWidth) * widthPerc;
		final float yMove = (oldHeight - newHeight) * heightPerc;
		setGraphOffset(shift.sub((int) xMove, (int) yMove));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#setDisplayedGraph
	 * (de.uniol.inf.is.odysseus.core.server.viewer.view.graph.IGraphView)
	 */
	@Override
	public void setDisplayedGraph(IGraphView<C> graph) {
		nodeSelector.unselectAll();
		if (this.graphView != graph) {
			this.graphView = graph;
			refreshView();
			logger.debug("Displayed graph changed: " + graphView.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#getDisplayedGraph
	 * ()
	 */
	@Override
	public IGraphView<C> getDisplayedGraph() {
		return graphView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#resetGraphOffset
	 * ()
	 */
	@Override
	public void resetGraphOffset() {
		setGraphOffset(new Vector(0, 0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.viewer.swt.render.IRenderManager#getGraphOffset
	 * ()
	 */
	@Override
	public Vector getGraphOffset() {
		return shift;
	}

	@Override
	public void setGraphOffset(Vector offset) {
		shift = offset;
		refreshView();
		// logger.debug( "Displayed graph changed in position: " + offset.getX()
		// + " " + offset.getY() );
	}
	
	

	@Override
	public void refreshView() {
		if (canvas.isDisposed())
			return;

		canvas.redraw();
	}

	@Override
	public void paintControl(PaintEvent e) {
		
		renderer.setGC(e.gc);

		RenderRange range = new RenderRange();
		range.x = e.x;
		range.y = e.y;
		range.width = e.width;
		range.height = e.height;
		renderer.render(graphView, highlightSelector.getSelected(), nodeSelector.getSelected(), zoomFactor, range, shift);

		// Auswahlrahmen
		if (selectRect != null && Math.abs(selectRect.width) > MIN_SELECTRECT && Math.abs(selectRect.height) > MIN_SELECTRECT) {
			GC gc = e.gc;
			gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(selectRect);
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mouseDown(MouseEvent e) {

		dragStart = new Vector(e.x, e.y);

		// Linke Maustaste oder rechte Maustaste
		if (e.button == MOUSE_SELECT_BUTTON1 || e.button == MOUSE_SELECT_BUTTON2) {

			// STRG nicht gedrückt?
			if ((e.stateMask & SWT.CTRL) == 0) {
				// Geklickten Knoten suchen
				final INodeView<C> clickedNode = getNodeFromPosition(e.x, e.y);
				final Collection<INodeView<C>> selectedNodes = nodeSelector.getSelected();

				// Geklickter Knoten schon ausgewählt?
				if (selectedNodes.contains(clickedNode) && e.button == MOUSE_SELECT_BUTTON1) {

					// Alle ausgewählten Knoten ziehen
					for (INodeView<C> selectedNode : selectedNodes) {
						draggedNode.add(selectedNode);
						dragObject.add(selectedNode.getPosition());
					}
					return;
				}

				// Wurde zuvor bereits ein Knoten ausgewählt?
				if (clickedNode != null) {

					// und SHIFT gedrückt (für Pfad-auswahl)?
					if ((e.stateMask & SWT.SHIFT) != 0) {
						if (selectedNodes.size() == 1 && !selectedNodes.contains(clickedNode)) {

							// Pfad auswählen
							INodeView<C>[] nodeDisplays = selectedNodes.toArray(new INodeView[0]);

							nodeSelector.unselectAll();
							if (!nodeSelector.selectPath(nodeDisplays[0], clickedNode)) {
								return;
							}
						}
					} else if ((e.stateMask & SWT.CTRL) == 0)
						nodeSelector.unselectAll();

					// Geklickten Knoten auswählen
					nodeSelector.select(clickedNode);

					// Knoten ziehen
					if (e.button == MOUSE_SELECT_BUTTON1) {
						draggedNode.add(clickedNode);
						dragObject.add(clickedNode.getPosition());
					}
				} else if (e.button == MOUSE_BORDER_BUTTON) {
					// Rahmen ziehen
					nodeSelector.unselectAll();
					selectRect = new Rectangle(e.x, e.y, 0, 0);
				}
			} else {
				// STRG gedr�ckt... Ziehen
				mouseDragButtonPressed = true;
				dragGraph = getGraphOffset();
			}
		} else if (e.button == MOUSE_DRAG_BUTTON) { // mittlere Maustaste

			// Graphen ziehen
			mouseDragButtonPressed = true;
			dragGraph = getGraphOffset();
		}
		// refreshView();
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// Linke Maustaste oder rechte Maustaste
		if ((e.button == MOUSE_SELECT_BUTTON1 || e.button == MOUSE_SELECT_BUTTON2) && mouseDragButtonPressed) {
			mouseDragButtonPressed = false;
		}

		// Auswahlrechteck auswerten
		if (e.button == MOUSE_BORDER_BUTTON) {
			draggedNode.clear();
			dragObject.clear();
			
			if (selectRect != null && Math.abs(selectRect.width) > MIN_SELECTRECT && Math.abs(selectRect.height) > MIN_SELECTRECT) {
				nodeSelector.unselectAll();
				Collection<INodeView<C>> nodesInRect = getNodesInRect(selectRect);
				nodeSelector.select(nodesInRect);
			}
			selectRect = null;

		} else if (e.button == MOUSE_DRAG_BUTTON)
			mouseDragButtonPressed = false;
	}

	@Override
	public void mouseMove(MouseEvent e) {

		final Vector distance = new Vector(e.x, e.y).sub(dragStart);
		final Vector distanceRel = distance.div(zoomFactor);

		if (!draggedNode.isEmpty()) {
			// Ziehe grade Knoten
			for (int i = 0; i < draggedNode.size(); i++) {
				draggedNode.get(i).setPosition(dragObject.get(i).add(distanceRel));
			}
			refreshView();

		} else if (selectRect != null) {
			// Ziehe einen Rahmen, neue Position bestimmen
			selectRect.width = e.x - selectRect.x;
			selectRect.height = e.y - selectRect.y;
			
			// Bereits beinhaltete Knoten hervorheben
			Collection<INodeView<C>> nodesInRect = getNodesInRect(selectRect);
			highlightSelector.unselectAll();
			highlightSelector.select(nodesInRect);

		} else if (mouseDragButtonPressed) {
			// Ziehe Graphen
			setGraphOffset(dragGraph.add(distanceRel));
			if (!canvas.isFocusControl())
				canvas.setFocus();

			refreshView();
		} else {
			INodeView<C> node = getNodeFromPosition(e.x, e.y);
			highlightSelector.unselectAll();
			if (node != null) 
				highlightSelector.select(node);
			
		}
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		if (e.count > 0) { // Mausrad nach oben
			zoom(e.x, e.y, 0.1f);
			refreshView();
		}
		if (e.count < 0) {
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

	@Override
	public void selectObject(ISelector<INodeView<C>> sender, Collection<? extends INodeView<C>> selected) {
		refreshView();
	}

	@Override
	public void unselectObject(ISelector<INodeView<C>> sender, Collection<? extends INodeView<C>> unselected) {
		refreshView();
	}
}
