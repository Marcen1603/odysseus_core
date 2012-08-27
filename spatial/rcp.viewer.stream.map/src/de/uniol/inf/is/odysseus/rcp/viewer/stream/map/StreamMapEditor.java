/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator.ViewerStreamMapPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.PropertyTitleDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 *  
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class StreamMapEditor implements IStreamEditorType  {

	protected static final Logger LOG = LoggerFactory.getLogger(StreamMapEditor.class);

	protected SDFSchema schema;

	protected LinkedList<ILayer> layerOrder = new LinkedList<ILayer>();

	protected ScreenTransformation transformation;
	protected ScreenManager screenManager;

	protected int maxTuplesCount = Integer.MAX_VALUE;

	protected Map<Integer, VectorLayer> spatialDataIndex = new TreeMap<Integer, VectorLayer>();
	protected LinkedList<Tuple<?>> tuples = new LinkedList<Tuple<?>>();
	protected Runnable update;

	protected Composite parent;
	
	protected boolean reactangleZoom = false;
	
	public StreamMapEditor(int maxTuples) {
		LOG.debug("Create Stream Map Editor");
		transformation = new ScreenTransformation();
		screenManager = new ScreenManager(transformation, this);
		setMaxTuplesCount(maxTuples);
	}

	public StreamMapEditor() {
		this(10);
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: StreamMap is only for spatial relational tuple!");
			return;
		}
		//LOG.info("Received Element: " + element.toString());
		for (Integer key : spatialDataIndex.keySet()) {	
			spatialDataIndex.get(key).addGeometry((Geometry)((Tuple<?>) element).getAttribute(key));	
		}


		tuples.addFirst((Tuple<?>) element);
		//LOG.debug("Tuples: " + tuples.size());
		
		if (tuples.size() > getMaxTuplesCount()) {
			tuples.removeLast();
			for (VectorLayer layer : spatialDataIndex.values()) {
				layer.removeLast();
			}
		}

		if (update == null && screenManager.hasCanvasViewer()
				&& !screenManager.getCanvas().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay()
					.asyncExec(update = new Runnable() {
						@Override
						public void run() {
							if (!screenManager.getCanvas().isDisposed())
								screenManager.getCanvas().redraw();
							update = null;
						}
					});
		}
		
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		List<ISubscription<? extends ISource<Object>>> subs = editorInput.getStreamConnection().getSubscriptions();
		// TODO: Adapt to multiple sources
		setSchema(subs.get(0).getSchema());
	}

	@Override
	public void createPartControl(Composite parent) {
		setParent(parent);
		
		if (hasSchema() && getSchema().size() > 0) {
			screenManager.setCanvasViewer(screenManager.createCanvas(parent));
		} else {
			LOG.debug("Operator provides no schema.");
			Label label = new Label(parent, SWT.NONE);
			label.setText("Operator provides no schema");
		}
		
		//Create Map Background

		RasterLayer map = new RasterLayer(screenManager, 0, "Raster Basic");
		layerOrder.addFirst(map);
		
		BasicLayer basic = new BasicLayer(screenManager);
		layerOrder.addFirst(basic);
		
	}

	@Override
	public void setFocus() {
//		if (screenManager.hasCanvasViewer())
//			screenManager.getCanvas().setFocus();
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
				SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) schema.getAttribute(i).getDatatype();

				Style style = getStyle(spatialDatatype);
				
				if (style != null) {
					addVectorLayer( schema.getAttribute(i),i,style);
				} else {
					throw new RuntimeException("Style for Spatialtype is not available or not implemented: " + spatialDatatype.getQualName().toString());
				}

			}
		}
	}

	public void addVectorLayer(SDFAttribute attribute, int position, Style style){
		VectorLayer layer = new VectorLayer(transformation, attribute, style);
		spatialDataIndex.put(position, layer);
		layerOrder.add(layer);
	}
	
	public void removeVectorLayer(int position){
		spatialDataIndex.remove(position);
	}
	
	public Style getStyle(SDFSpatialDatatype spatialDatatype){
		Style style = null;
		
		if (spatialDatatype.isPoint()) {
			style = new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1,ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor());
		} else if (spatialDatatype.isLineString()) {
			style = new LineStyle(1, ColorManager.getInstance()
					.randomColor());
		} else if (spatialDatatype.isPolygon()) {
			style = new PolygonStyle(1, ColorManager.getInstance().randomColor(), null);
		} else if (spatialDatatype.isMultiPoint()) {
			style = new CollectionStyle();
			style.addStyle(new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1, ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor()));
		} else if (spatialDatatype.isMultiLineString()) {
			style = new CollectionStyle();
			style.addStyle(new LineStyle(1, ColorManager.getInstance().randomColor()));
		} else if (spatialDatatype.isMultiPolygon()) {
			style = new CollectionStyle();
			style.addStyle(new PolygonStyle(1, ColorManager.getInstance().randomColor(), null));
		} else if (spatialDatatype.isSpatial()) {
			style = new CollectionStyle();
			style.addStyle(new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1, ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor()));
			style.addStyle(new LineStyle(1, ColorManager.getInstance().randomColor()));
			style.addStyle(new PolygonStyle(1, ColorManager.getInstance().randomColor(), null));
		}
		
		return style;
	}
	
	
	
	private void setMaxTuplesCount(int maxTuples) {
		if (maxTuples > 0)
			this.maxTuplesCount = maxTuples;
		else
			this.maxTuplesCount = Integer.MAX_VALUE;
	}
	
	@Override
	public void initToolbar(final ToolBar toolbar) {
		//final Label toolbarLabel = new Label(toolbar.getParent(), SWT.NONE);
		
		/* break */
		//ToolItem toolbarBreak = new ToolItem(toolbar, SWT.BREAK);
		
		/* blank button  16 */
//		ToolItem buttom_16 = new ToolItem(toolbar, SWT.PUSH);	
//		buttom_16.setImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("blank_16"));
		
		/* Filter button */
//		ToolItem filter = new ToolItem(toolbar, SWT.PUSH);
//		filter.setImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("filter_16"));
//		filter.setToolTipText("Filer");
//		
//		filter.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				PropertyWindow window = new PropertyWindow(PlatformUI.getWorkbench().getDisplay(), "Filter");				
//				window.show();
//				
////				if( !window.isCanceled()) {
////					getParent().layout();
////				}
//			}
//		});
		
		/* Change between rectangle and click zoom button */
		final ToolItem magnifier_rectangle = new ToolItem(toolbar, SWT.PUSH);
		magnifier_rectangle.setImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("magnifier_rectangle_16"));
		magnifier_rectangle.setDisabledImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("magnifier_rectangle_de_16"));
		magnifier_rectangle.setToolTipText("Magnifier");
		
		final ToolItem magnifier_zoom = new ToolItem(toolbar, SWT.PUSH);
		magnifier_zoom.setImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("magnifier_zoom_16"));
		magnifier_zoom.setDisabledImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("magnifier_zoom_de_16"));
		magnifier_zoom.setToolTipText("Magnifier");
		
		magnifier_rectangle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
					magnifier_rectangle.setEnabled(false);
					reactangleZoom = true;
					magnifier_zoom.setEnabled(true);
			}
		}); 
		
		magnifier_zoom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					magnifier_rectangle.setEnabled(true);
					reactangleZoom = false;
					magnifier_zoom.setEnabled(false);
			}
		});
		
		/* Edit Layer */	
		ToolItem layerOrder = new ToolItem(toolbar, SWT.PUSH);
		layerOrder.setImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("layers_16"));
		layerOrder.setToolTipText("Add a new Layer.");
		
		layerOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				PropertyTitleDialog dialog = new PropertyTitleDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),layerOrder, schema	);
//				dialog.create();
//				if (dialog.open() == Window.OK) {
//					LayerConfiguration config = dialog.getLayerConfiguration();
//					if(config.isRaster()){
//						config.getName();
//						RasterLayer newLayer = new RasterLayer(screenManager, 0, config.getName());
//						layerOrder.addLast(newLayer);
//						screenManager.redraw();
//					}
//					
//				} 
			}
		});
		
		/* Add Layer */
		ToolItem addLayer = new ToolItem(toolbar, SWT.PUSH);
		addLayer.setImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("layers_plus_16"));
		addLayer.setToolTipText("Add a new Layer.");
		
		addLayer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PropertyTitleDialog dialog = new PropertyTitleDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),layerOrder, schema	);
				dialog.create();
				if (dialog.open() == Window.OK) {
					LayerConfiguration config = dialog.getLayerConfiguration();
					if(config.isRaster()){
						config.getName();
						RasterLayer newLayer = new RasterLayer(screenManager, 0, config.getName());
						layerOrder.addLast(newLayer);
						screenManager.redraw();
					}
					
				} 
			}
		});
		

		
		
		/* Dummy Buttom */
//		ToolItem dummyButton2 = new ToolItem(toolbar, SWT.PUSH);
//		dummyButton2.setImage(ViewerStreamMapPlugIn.getDefault().getImageRegistry().get("dummy_16") );
//		dummyButton2.setToolTipText("Dummy Button");
//		
//		dummyButton2.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				LayerPropertyDialog window = new LayerPropertyDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), layerOrder);
////				window.create();
//				window.open();
//
//				
//				if( !window.close()) {
//					getParent().layout();
//				}
//				
//			}
//		});
		
		
		
	}

	public final Composite getParent() {
		return parent;
	}
	
	private void setParent(Composite parent) {
		this.parent = parent;
	}


	public LinkedList<ILayer> getLayerOrder() {
		return layerOrder;
	}

	public void setLayerOrder(LinkedList<ILayer> layerOrder) {
		this.layerOrder = layerOrder;
	}
	
	public ScreenManager getScreenManager(){
		return screenManager;
		
	}

	public boolean isRectangleZoom() {
		return reactangleZoom;
	}
	
}
