package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;

import java.util.LinkedList;

import org.eclipse.swt.graphics.GC;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.AbstractLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class LocationLayer extends AbstractLayer {
	private static final long serialVersionUID = 7162809826415077940L;
	
	private ScreenTransformation transformation = null;
	private ScreenManager screenManager = null;
	private SDFAttribute geometrieSDFAttribute = null;
	private int geometrieAttributeIndex = 0;
	private SDFAttribute visualizationSDFAttribute = null;
	private int visualizationAttributeIndex = 0;
	private Style style = null;
	
	private LinkedList<Location> locationList = new LinkedList<Location>();
	private LocationLegend legend;

	static protected String[] types;
	static{
		types = new String[]{
			SDFSpatialDatatype.SPATIAL_GEOMETRY.getURI(),
			SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION.getURI(),
			SDFSpatialDatatype.SPATIAL_POINT.getURI(),
			SDFSpatialDatatype.SPATIAL_MULTI_POINT.getURI()
		};
	}
	
	public LocationLayer(LayerConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema,
			SDFAttribute attribute) {
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
			this.name =  this.configuration.getName()+" [Location("+this.geometrieSDFAttribute.getAttributeName()+","+this.visualizationSDFAttribute.getAttributeName()+")]";
			this.active = true;
		}
	}
	
	private Style initStyle(){
		if(configuration.getStyle() == null){
			SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) geometrieSDFAttribute.getDatatype();
			if (spatialDatatype.isPoint()) {
				style = new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1,ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor());
			} else if (spatialDatatype.isMultiPoint()) {
				style = new CollectionStyle();
				style.addStyle(new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1, ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor()));
			} else if (spatialDatatype.isSpatial()) {
				style = new CollectionStyle();
				style.addStyle(new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1, ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor()));
				style.addStyle(new LineStyle(1, ColorManager.getInstance().randomColor()));
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
		Location location = new Location((Geometry)tuple.getAttribute(geometrieAttributeIndex),(Integer)tuple.getAttribute(visualizationAttributeIndex));
		synchronized (locationList) {
			this.locationList.offer(location);
		}
	}
	
	@Override
	public void removeLast() {
		locationList.removeLast();
	}

	@Override
	public int getTupleCount() {
		return locationList.size();
	}

	@Override
	public void draw(GC gc) {
		synchronized (locationList) {
			if(legend==null){
				legend = new LocationLegend(visualizationSDFAttribute.getQualName());
			}
			for (Location location : locationList) {
				drawLocation(location, gc);
			}
		}
	}

	private void drawLocation(Location location, GC gc) {
		if (location.getLocation() instanceof Point) {
			drawLocationPoint((Point)location.getLocation(), location.getValue(), gc);
		} else if (location.getLocation() instanceof GeometryCollection) {
			drawLocationPointCollection((GeometryCollection)location.getLocation(), location.getValue(), gc);
		} 
	}

	private void drawLocationPointCollection(GeometryCollection geometryCollection,
			Integer value, GC gc) {
		for(int i=0;i<geometryCollection.getNumGeometries();i++){
			drawLocation(new Location(geometryCollection.getGeometryN(i), value), gc);
		}
	}

	private void drawLocationPoint(Point location, Integer value, GC gc) {
		int[] uv = transformation.transformCoord(location.getCoordinate(),location.getSRID());
		Style style = legend.getStyleForValue(value);
		style.draw(gc, uv);
	}
	
	public void clean(){
		synchronized (locationList) {
			this.locationList.clear();
		}
	}

	public LocationLegend getLegend() {
		return legend;
	}
	
	public void updateLegend(LocationLegend legend){
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
