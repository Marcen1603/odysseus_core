package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import java.util.LinkedList;

import org.eclipse.swt.graphics.GC;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.AbstractLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class ChoroplethLayer extends AbstractLayer{
	private static final long serialVersionUID = 7347374438979210810L;

//	private static final Logger LOG = LoggerFactory.getLogger(ChoroplethLayer.class);
	
	private ScreenTransformation transformation = null;
	private ScreenManager screenManager = null;
	private SDFAttribute geometrieSDFAttribute = null;
	private int geometrieAttributeIndex = 0;
	private SDFAttribute visualizationSDFAttribute = null;
	private int visualizationAttributeIndex = 0;
	private Style style = null;
	
	private LinkedList<Choropleth> choroplethList = new LinkedList<Choropleth>();
	private ChoroplethLegend legend;
	
	
	static protected String[] types;
	static{
		types = new String[]{
			SDFSpatialDatatype.SPATIAL_GEOMETRY.getURI(),
			SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION.getURI(),
			SDFSpatialDatatype.SPATIAL_POLYGON.getURI(),
			SDFSpatialDatatype.SPATIAL_MULTI_POLYGON.getURI()
		};
	}
	
	public ChoroplethLayer(LayerConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
//		UNUSED
		
		//		if(screenManager != null){
//			this.screenManager = screenManager;
//			this.transformation = screenManager.getTransformation();
//		}
//		if(schema != null && attribute != null){
//			this.sdfAttribute = attribute;
//			this.idx = schema.indexOf(attribute);
//		}
//		if(this.screenManager != null && this.sdfAttribute != null){
//			this.style = initStyle();
//			this.name =  this.sdfAttribute.getAttributeName();
//			this.active = true;
//		}
	}
	public void init(ScreenManager screenManager, SDFSchema schema,
			SDFAttribute geometrieAttribute,SDFAttribute visualizationAttribute) {
		if(screenManager != null){
			this.screenManager = screenManager;
			this.transformation = screenManager.getTransformation();
		}
		if(schema != null && geometrieAttribute != null && visualizationAttribute != null){
			this.geometrieSDFAttribute = geometrieAttribute;
			this.geometrieAttributeIndex = schema.indexOf(geometrieAttribute);
			this.visualizationSDFAttribute = visualizationAttribute;
			this.visualizationAttributeIndex = schema.indexOf(visualizationAttribute);
		}
		if(this.screenManager != null && this.geometrieSDFAttribute != null && this.visualizationSDFAttribute!=null){
			this.style = initStyle();
			this.name =  this.configuration.getName()+" [Choropleth("+this.geometrieSDFAttribute.getAttributeName()+","+this.visualizationSDFAttribute.getAttributeName()+")]";
			this.active = true;
		}
	}
	private Style initStyle(){
		if(configuration.getStyle() == null){
			SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) geometrieSDFAttribute.getDatatype();
			if (spatialDatatype.isPolygon()) {
				style = new PolygonStyle(1, ColorManager.getInstance().randomColor(), null);
			} else if (spatialDatatype.isMultiPolygon()) {
				style = new CollectionStyle();
				style.addStyle(new PolygonStyle(1, ColorManager.getInstance().randomColor(), null));
			} else if (spatialDatatype.isSpatial()) {
				style = new CollectionStyle();
				style.addStyle(new PolygonStyle(1, ColorManager.getInstance().randomColor(), null));
			}
		}	
		return style;
	}

	@Override
	public String[] getSupprtedDatatypes() {
		return types;
	}

	@Override
	public void addTuple(Tuple<?> tuple) {
		Geometry geometry = (Geometry)tuple.getAttribute(geometrieAttributeIndex);
		Integer value = (Integer)tuple.getAttribute(visualizationAttributeIndex);
		addChoroplethToList(geometry, value);
	}
	
	private void addChoroplethToList(Geometry geometry, Integer value){
		if(geometry instanceof Polygon){
			Choropleth choropleth = new Choropleth((Polygon) geometry, value);
			addChoroplethToList(choropleth);
		}else if(geometry instanceof GeometryCollection){
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				addChoroplethToList(geometry.getGeometryN(i), value);
			}
		}
	}
	
	private void addChoroplethToList(Choropleth choropleth){
		synchronized (choroplethList) {
			this.choroplethList.offer(choropleth);
		}
	}
	

	@Override
	public void removeLast() {
		choroplethList.removeLast();
	}

	@Override
	public int getTupleCount() {
		return choroplethList.size();
	}
	
	
	@Override
	public void draw(GC gc) {
		synchronized (choroplethList) {
			if(legend==null){
				legend = new ChoroplethLegend(visualizationSDFAttribute.getQualName());
			}
			for (Choropleth choropleth : choroplethList) {
				drawChoropleth(choropleth, gc);
			}
		}
	}

	private void drawChoropleth(Choropleth choropleth, GC gc) {
		drawChoropleth(choropleth.getPolygon(), choropleth.getValue(), gc);
	}
	
	private void drawChoropleth(Polygon polygon, Integer value, GC gc) {
		int[][] list = new int[polygon.getNumInteriorRing() + 1][];
		list[0] = drawLinearRing(polygon.getExteriorRing(), gc);
		for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
			list[n + 1] = drawLinearRing(polygon.getInteriorRingN(n), gc);
		}
		
		Style style = legend.getStyleForValue(value);
		style.draw(gc, list);	

	}
	private int[] drawLinearRing(LineString lineString, GC gc) {
		int i = 0;
		int[] path = new int[lineString.getNumPoints()
				+ lineString.getNumPoints()];
		for (Coordinate coord : lineString.getCoordinates()) {
			int[] uv = transformation.transformCoord(coord,lineString.getSRID());
			path[i++] = uv[0];
			path[i++] = uv[1];
		}
		return path;
	}
	public void clean() {
//		LOG.debug("(CLEAN)Current Choroplethes:" + choroplethList.size());
		synchronized (choroplethList) {
			this.choroplethList.clear();
		}
	}

	public ChoroplethLegend getLegend() {
		return legend;
	}

	public void updateLegend(ChoroplethLegend legend) {
		this.legend = legend;
		screenManager.getDisplay().asyncExec(new Runnable() {	
			public void run() {
				screenManager.getCanvas().redraw();
			}
		});
	}

	public SDFAttribute getVisualizationSDFAttribute() {
		return visualizationSDFAttribute;
	}
	

}
