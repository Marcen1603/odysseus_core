package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.diagram;

import java.util.LinkedList;

import org.eclipse.swt.graphics.GC;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.AbstractLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class DiagramLayer extends AbstractLayer{
	private static final long serialVersionUID = -3981262445120202445L;
	
	private ScreenTransformation transformation = null;
	private ScreenManager screenManager = null;
	private SDFAttribute geometrieSDFAttribute = null;
	private int geometrieAttributeIndex = 0;
	private LinkedList<SDFAttribute> visualizationSDFAttributes = null;
	private LinkedList<Integer> visualizationAttributeIndex = null;
	private DiagramStyle style = null;
	
	private LinkedList<Diagram> diagramList = new LinkedList<>();
	private DiagramLegend legend;
	
	static protected String[] types;
	static{
		types = new String[]{
			SDFSpatialDatatype.SPATIAL_GEOMETRY.getURI(),
			SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION.getURI(),
			SDFSpatialDatatype.SPATIAL_POINT.getURI(),
			SDFSpatialDatatype.SPATIAL_MULTI_POINT.getURI()
		};
	}
	
	public DiagramLayer(LayerConfiguration configuration) {
		super(configuration);
		style = new DiagramStyle(100, false, 200, "PieChart");
	}

	public DiagramStyle getStyle() {
		return style;
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema,
			SDFAttribute attribute) {		
	}
	
	public void init(ScreenManager screenManager, SDFSchema schema,
			SDFAttribute geometrieAttribute,LinkedList<SDFAttribute> visualizationAttributes) {
		if(screenManager != null){
			this.screenManager = screenManager;
			this.transformation = screenManager.getTransformation();
		}
		if(schema != null && geometrieAttribute != null && visualizationAttributes != null){
			this.geometrieSDFAttribute = geometrieAttribute;
			this.geometrieAttributeIndex = schema.indexOf(geometrieAttribute);
			this.visualizationSDFAttributes = visualizationAttributes;
			visualizationAttributeIndex = new LinkedList<>();
			for (int i = 0; i < visualizationAttributes.size(); i++) {
				SDFAttribute visAttribute = visualizationAttributes.get(i);
				visualizationAttributeIndex.add(schema.indexOf(visAttribute));
			}
		}
		if(this.screenManager != null && this.geometrieSDFAttribute != null && this.visualizationSDFAttributes!=null){
			String attributes = "[";
			for(int i=0;i<visualizationSDFAttributes.size();i++){
				SDFAttribute attribute = visualizationSDFAttributes.get(i);
				if(i==visualizationAttributes.size()-1){
					attributes += attribute.getAttributeName();
				}else{
					attributes += attribute.getAttributeName()+",";
				}
			}
			attributes+="]";
			this.name =  this.configuration.getName()+" [Diagram("+this.geometrieSDFAttribute.getAttributeName()+","+attributes+")]";
			this.active = true;
		}
	}


	@Override
	public String[] getSupprtedDatatypes() {
		return types;
	}

	@Override
	public void addTuple(Tuple<?> tuple) {
		Geometry geometry = (Geometry)tuple.getAttribute(geometrieAttributeIndex);
		LinkedList<Integer> valueList = new LinkedList<>();
		for(int i=0; i<visualizationAttributeIndex.size();i++){
			valueList.add((Integer)tuple.getAttribute(visualizationAttributeIndex.get(i)));
		}
		addDiagramToList(geometry, valueList);
	}
	
	private void addDiagramToList(Geometry geometry, LinkedList<Integer> valueList){
		if(geometry instanceof Point){
			Diagram diagram = new Diagram((Point) geometry, valueList);
			addDiagramToList(diagram);
		}else if(geometry instanceof GeometryCollection){
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				addDiagramToList(geometry.getGeometryN(i), valueList);
			}
		}
	}
	
	private void addDiagramToList(Diagram diagram){
		synchronized (diagramList) {
			this.diagramList.offer(diagram);
		}
	}

	@Override
	public void draw(GC gc) {
		synchronized (diagramList) {
			for (Diagram element : diagramList) {
				drawDiagram(element, gc);
			}
		}
	}

	private void drawDiagram(Diagram element, GC gc) {
		drawDiagram(element.getLocation(),element.getValueList(), gc);
	}

	private void drawDiagram(Point location, LinkedList<Integer> valueList, GC gc) {
		int[] uv = transformation.transformCoord(location.getCoordinate(),location.getSRID());
		style.draw(gc, uv, valueList);
	}

	@Override
	public void removeLast() {
		diagramList.removeLast();
	}

	@Override
	public int getTupleCount() {
		return diagramList.size();
	}
	
	public void clean(){
		this.diagramList.clear();
	}

	public DiagramLegend getLegend() {
		return legend;
	}

	public LinkedList<SDFAttribute> getVisualizationSDFAttributes() {
		return visualizationSDFAttributes;
	}
	public LinkedList<Integer> getVisualizationAttributeIndex() {
		return visualizationAttributeIndex;
	}

	public void updateStyle(DiagramStyle style){
		this.style = style;
		screenManager.getDisplay().asyncExec(new Runnable() {	
			public void run() {
				screenManager.getCanvas().redraw();
			}
		});
	}
	

}
