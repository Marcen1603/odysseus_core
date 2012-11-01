package de.uniol.inf.is.odysseus.rcp.viewer.stream.thematicalmap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.AbstractLayer;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class MapWizardEditor{// extends StreamMapEditor{
//	protected static final Logger LOG = LoggerFactory.getLogger(MapWizardEditor.class);
//	
//	protected Map<Integer, AbstractLayer> spatialDataIndex = new TreeMap<Integer, AbstractLayer>();
//	
//	LinkedList<Integer> spatialDatatypes;
//	LinkedList<Integer> integerDatatypes;
//	
//	public MapWizardEditor() {
//		super(Integer.MAX_VALUE);
//	}
//	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
//		List<ISubscription<? extends ISource<Object>>> subs = editorInput.getStreamConnection().getSubscriptions();
//		// TODO: Adapt to multiple sources
//		setSchema(subs.get(0).getSchema());
//	}
//	public void streamElementRecieved(Object element, int port) {
//		if (!(element instanceof Tuple<?>)) {
//			LOG.error("Warning: StreamMap is only for spatial relational tuple!");
//			return;
//		}
//		// LOG.info("Received Element: " + element.toString());
//		for (Integer key : spatialDataIndex.keySet()) {
//			if(spatialDataIndex.get(key) instanceof ChoroplethLayer){
//				ChoroplethLayer layer = ((ChoroplethLayer)spatialDataIndex.get(key));
//				Geometry geometry = (Geometry) ((Tuple<?>) element).getAttribute(layer.getGeoStreamPosition());
//				Integer value = (Integer) ((Tuple<?>) element).getAttribute(layer.getValueStreamPosition());
//				layer.addGeometryAndValue(geometry,value);
//			}
//		}
//
////		tuples.addFirst((Tuple<?>) element);
////		// LOG.debug("Tuples: " + tuples.size());
////
////		if (tuples.size() > getMaxTuplesCount()) {
////			tuples.removeLast();
////			for (VectorLayer layer : spatialDataIndex.values()) {
////				layer.removeLast();
////			}
////		}
//
//		if (update == null && screenManager.hasCanvasViewer() && !screenManager.getCanvas().isDisposed()) {
//			PlatformUI.getWorkbench().getDisplay().asyncExec(update = new Runnable() {
//				@Override
//				public void run() {
//					if (!screenManager.getCanvas().isDisposed())
//						screenManager.getCanvas().redraw();
//					update = null;
//				}
//			});
//		}
//
//	}
//	private void setSchema(SDFSchema schema) {
//		this.schema = schema;
//
//		spatialDatatypes = new LinkedList<>();
//		integerDatatypes = new LinkedList<>();
//		
//		for (int i = 0; i < schema.size(); i++) {
//			LOG.info(schema.getAttribute(i).getDatatype().toString());
//			if (schema.getAttribute(i).getDatatype() instanceof SDFSpatialDatatype) {
//				spatialDatatypes.add(i);
//			}else if(schema.getAttribute(i).getDatatype().isInteger()){
//				integerDatatypes.add(i);
//			}
//		}
//		int position = 0;
//		for(int i=0;i<spatialDatatypes.size();i++){
//			for(int j=0;j<integerDatatypes.size();j++){
//				addChoroplethLayer("ChoroplethLayer "+schema.getAttribute(spatialDatatypes.get(i))+"/"+schema.getAttribute(integerDatatypes.get(j)), schema.getAttribute(spatialDatatypes.get(i)), position, spatialDatatypes.get(i), integerDatatypes.get(j));
//				position++;
//			}
//		}
//	}
//	public void createPartControl(Composite parent) {
//		super.createPartControl(parent);
//		initLegend();
//	}
//	public void initLegend() {
//		
//		
//		Canvas canvas = screenManager.getCanvas();
//		
//		
//		Composite com = new Composite(parent, SWT.BORDER);
//		com.setLayout(new GridLayout(2,false));
//		canvas.setParent(com);
//		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		
//		
//		ScrolledComposite scroll = new ScrolledComposite(com, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
//		scroll.setExpandVertical(true);
//		scroll.setExpandHorizontal(true);
//		
//		
//		scroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
//		
//		
//		LegendViewer legendViewer = new LegendViewer(scroll, SWT.NONE);
//		scroll.setContent(legendViewer.getMainComposite());
//		
//	}
//	public void addChoroplethLayer(String name, SDFAttribute attribute, int layerPosition, int geoStreamPosition, int valueStreamPosition) {
//		ChoroplethLayer layer = new ChoroplethLayer(name, transformation, attribute, geoStreamPosition, valueStreamPosition);
//		spatialDataIndex.put(layerPosition, layer);
//		layerOrder.add(layer);
//	}


}
