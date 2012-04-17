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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * 
 * 
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class StreamMapEditor implements IStreamEditorType {

	private static final Logger LOG = LoggerFactory.getLogger(StreamMapEditor.class);
	
	private static final Color WHITE = Display.getCurrent().getSystemColor(
			SWT.COLOR_WHITE);
	private static final Color BLACK = Display.getCurrent().getSystemColor(
			SWT.COLOR_BLACK);

	private Canvas viewer;
	private SDFSchema schema;
	
	private Map<Integer, Layer> spatialDataIndex = new TreeMap<Integer, Layer>();
	private Map<Integer, Color> colors = new TreeMap<Integer, Color>();
	private MapTransformation transformation = null;
	private Rectangle rect = null;

	private int maxTuplesCount = 0;
	
	protected LinkedList<Tuple<?>> tuples = new LinkedList<Tuple<?>>();
	protected Runnable update;

	public StreamMapEditor(int maxTuples) {
		transformation = new MapTransformation();
		setRect(null);
		setMaxTuplesCount(maxTuples);
	}

	/**
	 * 
	 * Receives the stream elements. 
	 * 
	 * @param element - incoming streaming element
	 * @param port - incoming port of the streaming element
	 * 
	 * 
	 * 
	 */
	/*
	 * (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener#streamElementRecieved(java.lang.Object, int)
	 * 
	 * Start reading here! 
	 * 
	 */
	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: StreamMap is only for relational tuple!");
			return;
		}
		for (Integer key : getSpatialDataIndex().keySet()) {
			getSpatialDataIndex().get(key).addGeometry(
					(Geometry) ((Tuple<?>) element).getAttribute(key));
		}
		
		tuples.add(0, (Tuple<?>) element);
		if (tuples.size() > getMaxTuplesCount()) {
			tuples.remove(tuples.size() - 1);
			for (Layer layer : getSpatialDataIndex().values()) {
				layer.removeLast();
			}
		}
		
		if (update == null && hasCanvasViewer()
				&& !getCanvasViewer().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay()
					.asyncExec(update = new Runnable() {
						@Override
						public void run() {
							if (!getCanvasViewer().isDisposed())
								getCanvasViewer().redraw();
							update = null;
						}
					});
		}
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?>[] sources = editorInput.getStreamConnection().getSources()
				.toArray(new ISource<?>[0]);
		setSchema(sources[0].getOutputSchema());
	}

	@Override
	public void createPartControl(Composite parent) {
		if (hasSchema() && getSchema().size() > 0) {
			setCanvasViewer(createCanvas(parent));
		} else {
			LOG.debug("Operator provides no schema.");
			Label label = new Label(parent, SWT.NONE);
			label.setText("Operator provides no schema");
		}
	}

	@Override
	public void setFocus() {
		if (hasCanvasViewer())
			getCanvasViewer().setFocus();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
		
	}
	
	public final SDFSchema getSchema() {
		return schema;
	}

	public final int getMaxTuplesCount() {
		return maxTuplesCount;
	}

	protected final boolean hasSchema() {
		return getSchema() != null;
	}

	protected IContentProvider createContentProvider() {
		return ArrayContentProvider.getInstance();
	}

	protected Canvas createCanvas(Composite parent) {
		Canvas canvasViewer = new Canvas(parent, SWT.NONE);
		canvasViewer.setBackground(WHITE);
		canvasViewer.addPaintListener(new GeometryPaintListener(this));
		transformation.updateMapSize(canvasViewer.getClientArea());
		canvasViewer.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				transformation.updateMapSize(viewer.getClientArea());
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});

		canvasViewer.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				transformation.update(getRect());
				setRect(null);

				if (hasCanvasViewer() && !getCanvasViewer().isDisposed()) {
					PlatformUI.getWorkbench().getDisplay()
							.asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!getCanvasViewer().isDisposed())
										getCanvasViewer().redraw();
								}
							});
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
				setRect(new Rectangle(e.x, e.y, 0, 0));
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				LOG.error("Mouse Double Click is not implemented");
			}
		});

		canvasViewer.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				// TODO Auto-generated method stub
				if (getRect() != null) {
					getRect().width = e.x - getRect().x;
					getRect().height = e.y - getRect().y;
					if (hasCanvasViewer() && !getCanvasViewer().isDisposed()) {
						PlatformUI.getWorkbench().getDisplay()
								.asyncExec(new Runnable() {
									@Override
									public void run() {
										if (!getCanvasViewer().isDisposed())
											getCanvasViewer().redraw();
									}
								});
					}
				}
			}
		});
		canvasViewer.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == '+')
					transformation.zoomin();
				if (e.character == '-')
					transformation.zoomout();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP)
					transformation.panNorth();
				if (e.keyCode == SWT.ARROW_DOWN)
					transformation.panSouth();
				if (e.keyCode == SWT.ARROW_LEFT)
					transformation.panWest();
				if (e.keyCode == SWT.ARROW_RIGHT)
					transformation.panEast();
			}
		});
		return canvasViewer;
	}

	protected final Canvas getCanvasViewer() {
		return viewer;
	}

	protected final boolean hasCanvasViewer() {
		return getCanvasViewer() != null;
	}

	private void setCanvasViewer(Canvas viewer) {
		if(viewer != null){
			this.viewer = viewer; 	
		}
		else{
			LOG.error("Canvas Viewer is null.");
		}
	}

	/**
	 * 
	 * The setSchema method 
	 * 
	 * @param schema - the streaming schema
	 */
	private void setSchema(SDFSchema schema) {
			this.schema = schema;	
			for (int i = 0; i < schema.size(); i++) {
				if (schema.getAttribute(i).getDatatype() instanceof SDFSpatialDatatype) {
					SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) schema
							.getAttribute(i).getDatatype();

					Style style = null;
					if (spatialDatatype.isPoint()) {
						style = new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1,BLACK, ColorManager.getInstance().randomColor());
					} else if (spatialDatatype.isLineString()) {
						style = new LineStyle(1, ColorManager.getInstance().randomColor());
					} else if (spatialDatatype.isPolygon()) {
						style = new PolygonStyle(1, ColorManager.getInstance().randomColor(), null);
					}
					
					if (style != null) {
						spatialDataIndex.put(i,new Layer(transformation, schema.getAttribute(i), style));
					} else {
						throw new RuntimeException("Style for Spatialtype is not available or not implemented!");
					}

				}
			}
	}

	/*
	 * Maybe it is better to return a SDFSchema
	 */
	@Deprecated
	public Map<Integer, Layer> computeSpatialOutputSchema(SDFSchema inputSchema){
		Map<Integer, Layer> spatialDataIndex = new TreeMap<Integer, Layer>();
		for (int i = 0; i < schema.size(); i++) {
			if (schema.getAttribute(i).getDatatype() instanceof SDFSpatialDatatype) {
				SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) schema
						.getAttribute(i).getDatatype();

				Style style = null;
				if (spatialDatatype.isPoint()) {
					style = new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1,BLACK, ColorManager.getInstance().randomColor());
				} else if (spatialDatatype.isLineString()) {
					style = new LineStyle(1, ColorManager.getInstance().randomColor());
				} else if (spatialDatatype.isPolygon()) {
					style = new PolygonStyle(1, ColorManager.getInstance().randomColor(), null);
				}
				
				if (style != null) {
					spatialDataIndex.put(i,new Layer(transformation, schema.getAttribute(i), style));
				} else {
					throw new RuntimeException("Style for Spatialtype is not available or not implemented!");
				}

			}
		}
		return spatialDataIndex;
	}
	
	private void setMaxTuplesCount(int maxTuples) {
		if (maxTuples > 0)
			this.maxTuplesCount = maxTuples;
		else
			this.maxTuplesCount = Integer.MAX_VALUE;
	}

	@Override
	public void initToolbar(ToolBar toolbar) {
		
	}

	public Map<Integer, Layer> getSpatialDataIndex() {
		return spatialDataIndex;
	}

	public void setSpatialDataIndex(Map<Integer, Layer> spatialDataIndex) {
		this.spatialDataIndex = spatialDataIndex;
	}

	public Map<Integer, Color> getColors() {
		return colors;
	}

	public void setColors(Map<Integer, Color> colors) {
		this.colors = colors;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
}
