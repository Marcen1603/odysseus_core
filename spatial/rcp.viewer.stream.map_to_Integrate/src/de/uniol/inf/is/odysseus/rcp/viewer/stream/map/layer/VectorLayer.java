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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.util.LinkedList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class VectorLayer extends AbstractLayer{

	private static final long serialVersionUID = 2904911648287160846L;

	static protected String[] types;
	static{
		types = new String[]{
			SDFSpatialDatatype.SPATIAL_GEOMETRY.getURI(),
			SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION.getURI(),
			SDFSpatialDatatype.SPATIAL_POINT.getURI(),
			SDFSpatialDatatype.SPATIAL_MULTI_POINT.getURI(),
			SDFSpatialDatatype.SPATIAL_LINE_STRING.getURI(),
			SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING.getURI(),
			SDFSpatialDatatype.SPATIAL_POLYGON.getURI(),
			SDFSpatialDatatype.SPATIAL_MULTI_POLYGON.getURI()
		};
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(VectorLayer.class);
	
	private LinkedList<Geometry> geometries = new LinkedList<Geometry>();
	private ScreenTransformation transformation = null;
	private ScreenManager screenManager = null;
	private SDFAttribute sdfAttribute = null;
	private Style style = null;
	private int idx = 0;

    public VectorLayer() {
	    super(new LayerConfiguration("Default"));
    }
	
    public VectorLayer(LayerConfiguration configuration) {
	    super(configuration);
    }

	
	@Override
    public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
		if(screenManager != null){
			this.screenManager = screenManager;
			this.transformation = screenManager.getTransformation();
		}
		if(schema != null && attribute != null){
			this.sdfAttribute = attribute;
			this.idx = schema.indexOf(attribute);
		}
		if(this.screenManager != null && this.sdfAttribute != null){
			this.style = initStyle();
			this.name =  this.configuration.getName()+" [Vector("+this.sdfAttribute.getAttributeName()+")]";
			this.active = true;
		}
    }
	
	private Style initStyle(){
		if(configuration.getStyle() == null){
			SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) sdfAttribute.getDatatype();
			if (spatialDatatype.isPoint()) {
				style = new PointStyle(PointStyle.SHAPE.CIRCLE, 5, 1,ColorManager.getInstance().randomColor(), ColorManager.getInstance().randomColor());
			} else if (spatialDatatype.isLineString()) {
				style = new LineStyle(1, ColorManager.getInstance().randomColor());
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
		}	
		
		return style;
	}
	
	public void setStyle(Style style) {
		this.style = style;
	}

	public Style getStyle() {
		return style;
	}
	
	public void draw(GC gc) {
		synchronized (geometries) {
			for (Geometry geometry : geometries) {
				drawGeometry(geometry, gc);
			}
		}
	}
	
	private void drawGeometry(Geometry geometry, GC gc) {
		if (geometry instanceof Point) {
			drawPoint((Point) geometry, gc);
		} else if (geometry instanceof LineString) {
			drawLineString((LineString) geometry, gc);
		} else if (geometry instanceof Polygon) {
			drawPolygon((Polygon) geometry, gc);
		} else if (geometry instanceof GeometryCollection) {
			drawGeometryCollection((GeometryCollection) geometry, gc);
		}
	}

	private void drawGeometryCollection(GeometryCollection geometryCollection, GC gc) {
		for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
			drawGeometry(geometryCollection.getGeometryN(i), gc);
		}

	}

	private void drawPoint(Point point, GC gc) {
		int[] uv = transformation.transformCoord(point.getCoordinate(),point.getSRID());
		this.style.setActiveStyle(point);
		this.style.draw(gc, uv);
	}

	private void drawLineString(LineString lineString, GC gc) {
		int i = 0;
		int[] path = new int[lineString.getNumPoints()
				+ lineString.getNumPoints()];
		for (Coordinate coord : lineString.getCoordinates()) {
			int[] uv = transformation.transformCoord(coord,lineString.getSRID());
			path[i++] = uv[0];
			path[i++] = uv[1];
		}
		
		this.style.setActiveStyle(lineString);
		
		
		this.style.draw(gc, path);	
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

	private void drawPolygon(Polygon polygon, GC gc) {
		int[][] list = new int[polygon.getNumInteriorRing() + 1][];
		list[0] = drawLinearRing(polygon.getExteriorRing(), gc);
		for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
			list[n + 1] = drawLinearRing(polygon.getInteriorRingN(n), gc);
		}
		this.style.setActiveStyle(polygon);
		

		
		this.style.draw(gc, list);	

	}
	
	public void addGeometry(Geometry geometry) {
		synchronized (geometries) {
			this.geometries.offer(geometry);
		}
	}

	public void removeLast() {
		LOG.debug("(REMOVE)Current Geometries:" + geometries.size());
		synchronized (geometries) {
			this.geometries.poll();
		}
	}
	
	public void clean() {
//		LOG.debug("(CLEAN)Current Geometries:" + geometries.size());
		synchronized (geometries) {
			this.geometries.clear();
		}
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public SDFAttribute getNativeAttribute() {
		return this.sdfAttribute;
	}
	
	@Override
    public String[] getSupprtedDatatypes() {
		return types;
    }

	@Override
    public void addTuple(Tuple<?> tuple) {
		Geometry geometry = (Geometry) tuple.getAttribute(idx);	
		synchronized (geometries) {
			this.geometries.offer(geometry);
		}
    }

	@Override
    public int getTupleCount() {
	    // TODO Auto-generated method stub
	    return 1000;
    }

	@Override
	public void setConfiguration(LayerConfiguration configuration){
		this.configuration = configuration;
		if(configuration.getStyle() != null)
			this.setStyleByConfig(configuration.getStyle());
	}	
	
	private void setStyleByConfig(PersestentStyle style){
		Color lineColor = null;
		Color fillColor = null;
		int lineWidth = 1;
		
		if(style.getLineColor() != null)
			lineColor = new Color(PlatformUI.getWorkbench().getDisplay(), style.getLineColor());
		if(style.getFillColor() != null)
			fillColor = new Color(PlatformUI.getWorkbench().getDisplay(), style.getFillColor());
		
		 lineWidth = style.getLineWidth();	
		
		if (style.getType().equals("point")) {
			this.style = new PointStyle(PointStyle.SHAPE.CIRCLE, 5, lineWidth,lineColor, fillColor);
		} else if (style.getType().equals("line")) {
			this.style = new LineStyle(lineWidth, lineColor);
		} else if (style.getType().equals("polygon")) {
			this.style = new PolygonStyle(1, lineColor, fillColor);
		} else if (style.getType().equals("collection")) {
			this.style = new CollectionStyle();
			for(PersestentStyle substyle : style.getSubstyles()){
				Color sublineColor = null;
				Color subfillColor = null;
				
				if(substyle.getLineColor() != null)
					sublineColor = new Color(PlatformUI.getWorkbench().getDisplay(), substyle.getLineColor());
				
				if(substyle.getFillColor() != null)
					subfillColor = new Color(PlatformUI.getWorkbench().getDisplay(), substyle.getFillColor());
				
				int sublineWidth = style.getLineWidth();	
				if (substyle.getType().equals("point")) {
					this.style.addStyle(new PointStyle(PointStyle.SHAPE.CIRCLE, 5, sublineWidth,sublineColor, subfillColor));
				} else if (substyle.getType().equals("line")) {
					this.style.addStyle(new LineStyle(sublineWidth, sublineColor));
				} else if (substyle.getType().equals("polygon")) {
					this.style.addStyle(new PolygonStyle(sublineWidth, sublineColor, subfillColor));
				}	
			}			
		}
		
	}
	
	
}
