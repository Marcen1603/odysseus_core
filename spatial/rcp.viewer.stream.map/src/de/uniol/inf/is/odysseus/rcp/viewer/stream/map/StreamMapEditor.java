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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.Layer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.MapLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.ImageStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
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

	private SDFSchema schema;

	private LinkedList<Layer> layerOrder = new LinkedList<Layer>();

	private ScreenTransformation transformation;
	private ScreenManager screenManager;

	private int maxTuplesCount = Integer.MAX_VALUE;

	protected Map<Integer, VectorLayer> spatialDataIndex = new TreeMap<Integer, VectorLayer>();
	protected LinkedList<Tuple<?>> tuples = new LinkedList<Tuple<?>>();
	protected Runnable update;

	public StreamMapEditor(int maxTuples) {
		LOG.debug("Create Stream Map Editor");
		transformation = new ScreenTransformation();
		screenManager = new ScreenManager(transformation, this);
		setMaxTuplesCount(maxTuples);
		
		//Create Map Background 
		layerOrder.add(new MapLayer(transformation, new ImageStyle()));
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: StreamMap is only for spatial relational tuple!");
			return;
		}
		//LOG.info("Received Element: " + element.toString());
		
		for (Integer key : spatialDataIndex.keySet()) {
				
			if( !(((Tuple<?>) element).getAttribute(key) instanceof ArrayList) ){
				spatialDataIndex.get(key).addGeometry((Geometry)((Tuple<?>) element).getAttribute(key));	
			}
			else{
				//spatialDataIndex.get(key).addGeometry((GeometryCollection)((Tuple<?>) element).getAttribute(key));	
				for(Geometry g: (List<Geometry>)((Tuple<?>) element).getAttribute(key)){
					//LOG.info(g.toString());
					spatialDataIndex.get(key).addGeometry(g);	
				}
				
				
			}
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
				&& !screenManager.getCanvasViewer().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay()
					.asyncExec(update = new Runnable() {
						@Override
						public void run() {
							if (!screenManager.getCanvasViewer().isDisposed())
								screenManager.getCanvasViewer().redraw();
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
		if (hasSchema() && getSchema().size() > 0) {
			screenManager.setCanvasViewer(screenManager.createCanvas(parent));
		} else {
			LOG.debug("Operator provides no schema.");
			Label label = new Label(parent, SWT.NONE);
			label.setText("Operator provides no schema");
		}
	}

	@Override
	public void setFocus() {
		if (screenManager.hasCanvasViewer())
			screenManager.getCanvasViewer().setFocus();
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
	 * @param schema
	 *            - the streaming schema
	 */
	private void setSchema(SDFSchema schema) {
		this.schema = schema;
		for (int i = 0; i < schema.size(); i++) {
			if (schema.getAttribute(i).getDatatype() instanceof SDFSpatialDatatype) {
				SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) schema.getAttribute(i).getDatatype();

				Style style = null;
				
				if (spatialDatatype.isPoint()) {
					style = new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1,ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor());
				} else if (spatialDatatype.isLineString()) {
					style = new LineStyle(1, ColorManager.getInstance()
							.randomColor());
				} else if (spatialDatatype.isPolygon()) {
					style = new PolygonStyle(1, ColorManager.getInstance().randomColor(), null);
				} else if (spatialDatatype.isMultiPoint()) {
					style = new CollectionStyle(1, ColorManager.getInstance().randomColor(), null);
					style.addStyle(new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1, ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor()));
				} else if (spatialDatatype.isMultiLineString()) {
					style = new CollectionStyle(1, ColorManager.getInstance().randomColor(), null);
					style.addStyle(new LineStyle(1, ColorManager.getInstance().randomColor()));
				} else if (spatialDatatype.isMultiPolygon()) {
					style = new CollectionStyle(1, ColorManager.getInstance().randomColor(), null);
					style.addStyle(new PolygonStyle(1, ColorManager.getInstance().randomColor(), null));
				} else if (spatialDatatype.isGeometryCollection()) {
					style = new CollectionStyle(1, ColorManager.getInstance().randomColor(), null);
					style.addStyle(new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1, ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor()));
					style.addStyle(new LineStyle(1, ColorManager.getInstance().randomColor()));
					style.addStyle(new PolygonStyle(1, ColorManager.getInstance().randomColor(), null));
				}
				
				if (style != null) {
					VectorLayer layer = new VectorLayer(transformation, schema.getAttribute(i), style);
					spatialDataIndex.put(i, layer);
					layerOrder.add(layer);
				} else {
					throw new RuntimeException("Style for Spatialtype is not available or not implemented!");
				}

			}
		}
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

	public LinkedList<Layer> getLayerOrder() {
		return layerOrder;
	}

	public void setLayerOrder(LinkedList<Layer> layerOrder) {
		this.layerOrder = layerOrder;
	}
	
	public ScreenManager getScreenManager(){
		return screenManager;
		
	}

}
