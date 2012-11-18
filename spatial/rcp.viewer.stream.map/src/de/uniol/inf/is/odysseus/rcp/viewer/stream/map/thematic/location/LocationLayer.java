package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;

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
			this.name =  this.configuration.getName()+" [Location("+this.geometrieSDFAttribute.getAttributeName()+","+this.visualizationSDFAttribute.getAttributeName()+")]";
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
		Integer value = (Integer)tuple.getAttribute(visualizationAttributeIndex);
		addPointToList(geometry, value);
	}
	
	private void addPointToList(Geometry geometry, Integer value){
		if(geometry instanceof Point){
			Location location = new Location((Point) geometry, value);
			addLocationToList(location);
		}else if(geometry instanceof GeometryCollection){
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				addPointToList(geometry.getGeometryN(i), value);
			}
		}
	}
	
	private void addLocationToList(Location location){
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
		drawLocation(location.getLocation(), location.getValue(), gc);
	}

	private void drawLocation(Point location, Integer value, GC gc) {
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
