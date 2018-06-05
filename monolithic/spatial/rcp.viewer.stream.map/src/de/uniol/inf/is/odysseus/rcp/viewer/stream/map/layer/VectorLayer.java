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

import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.VectorLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style.PersistentStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style.STYLE;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class VectorLayer extends AbstractLayer<VectorLayerConfiguration> {

	private static final long serialVersionUID = 2904911648287160846L;

	static protected String[] types;
	static {
		types = new String[] { SDFSpatialDatatype.SPATIAL_GEOMETRY.getURI(), SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION.getURI(),
				SDFSpatialDatatype.SPATIAL_POINT.getURI(), SDFSpatialDatatype.SPATIAL_MULTI_POINT.getURI(), SDFSpatialDatatype.SPATIAL_LINE_STRING.getURI(),
				SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING.getURI(), SDFSpatialDatatype.SPATIAL_POLYGON.getURI(),
				SDFSpatialDatatype.SPATIAL_MULTI_POLYGON.getURI() };
	}

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(VectorLayer.class);

	// private HashMap<Tuple<? extends ITimeInterval>, DataSet> dataSets = new
	// HashMap<Tuple<? extends ITimeInterval>, DataSet>();
	// LinkedList<DataSet> dataSets = new LinkedList<DataSet>();
	// private com.vividsolutions.jts.index.quadtree.Quadtree tree = new
	// Quadtree();
	// private Envelope env = new Envelope();
	private ScreenTransformation transformation = null;
	private ScreenManager screenManager = null;
	private SDFAttribute sdfAttribute = null;
	private Style style = null;
	private int idx = 0;

	private LayerUpdater layerUpdater;

	public VectorLayer() {
		super(new VectorLayerConfiguration("Default"));
	}

	public VectorLayer(VectorLayerConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
		if (screenManager != null) {
			this.screenManager = screenManager;
			this.transformation = screenManager.getTransformation();
		}
		if (this.screenManager != null && this.sdfAttribute != null) {
			this.style = initStyle();
			// this.name = this.sdfAttribute.getAttributeName();
			this.active = true;
		}
		if (schema != null && attribute != null) {
			this.sdfAttribute = attribute;
			this.style.init(schema);
			this.idx = schema.indexOf(attribute);
		}
	}

	private Style initStyle() {
		if (configuration.getStyle() == null) {
			SDFSpatialDatatype spatialDatatype = (SDFSpatialDatatype) sdfAttribute.getDatatype();
			if (spatialDatatype.isPoint()) {
				style = new PointStyle();
			} else if (spatialDatatype.isLineString()) {
				style = new LineStyle();
			} else if (spatialDatatype.isPolygon()) {
				style = new PolygonStyle();
			} else if (spatialDatatype.isMultiPoint()) {
				style = new CollectionStyle();
				style.addStyle(new PointStyle());
			} else if (spatialDatatype.isMultiLineString()) {
				style = new CollectionStyle();
				style.addStyle(new LineStyle());
			} else if (spatialDatatype.isMultiPolygon()) {
				style = new CollectionStyle();
				style.addStyle(new PolygonStyle());
			} else if (spatialDatatype.isSpatial()) {
				style = new CollectionStyle();
				style.addStyle(new PointStyle());
				style.addStyle(new LineStyle());
				style.addStyle(new PolygonStyle());
			}
		}

		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	@Override
	public Style getStyle() {
		return style;
	}

	@Override
	public void draw(GC gc) {
		if (screenManager != null) {
			Envelope world = screenManager.getViewportWorldCoord();
			List<?> result = this.layerUpdater.query(world, idx);
			for (Object obj : result) {
				DataSet dataSet = (DataSet) obj;
				drawGeometry(dataSet.getGeometry(), gc, dataSet.getTuple());
			}
		}
		// synchronized (geometries) {
		//
		// for (Geometry geometry : geometries) {
		// drawGeometry(geometry, gc);
		// }
		// }
	}

	private void drawGeometry(Geometry geometry, GC gc, Tuple<?> tuple) {
		if (geometry instanceof Point) {
			drawPoint((Point) geometry, gc, tuple);
		} else if (geometry instanceof LineString) {
			drawLineString((LineString) geometry, gc, tuple);
		} else if (geometry instanceof Polygon) {
			drawPolygon((Polygon) geometry, gc, tuple);
		} else if (geometry instanceof GeometryCollection) {
			drawGeometryCollection((GeometryCollection) geometry, gc, tuple);
		}
	}

	private void drawGeometryCollection(GeometryCollection geometryCollection, GC gc, Tuple<?> tuple) {
		for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
			drawGeometry(geometryCollection.getGeometryN(i), gc, tuple);
		}

	}

	private void drawPoint(Point point, GC gc, Tuple<?> tuple) {
		int[] uv = transformation.transformCoord(point.getCoordinate(), point.getSRID());
		this.style.setActiveStyle(STYLE.POINT);
		this.style.draw(gc, uv, tuple);
	}

	private void drawLineString(LineString lineString, GC gc, Tuple<?> tuple) {
		int i = 0;
		int[] path = new int[lineString.getNumPoints() + lineString.getNumPoints()];
		for (Coordinate coord : lineString.getCoordinates()) {
			int[] uv = transformation.transformCoord(coord, lineString.getSRID());
			path[i++] = uv[0];
			path[i++] = uv[1];
		}

		this.style.setActiveStyle(STYLE.LINESTRING);

		this.style.draw(gc, path, tuple);
	}

	private int[] drawLinearRing(LineString lineString) {
		int i = 0;
		int[] path = new int[lineString.getNumPoints() + lineString.getNumPoints()];
		for (Coordinate coord : lineString.getCoordinates()) {
			int[] uv = transformation.transformCoord(coord, lineString.getSRID());
			path[i++] = uv[0];
			path[i++] = uv[1];
		}
		return path;
	}

	private void drawPolygon(Polygon polygon, GC gc, Tuple<?> tuple) {
		int[][] list = new int[polygon.getNumInteriorRing() + 1][];
		list[0] = drawLinearRing(polygon.getExteriorRing());
		for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
			list[n + 1] = drawLinearRing(polygon.getInteriorRingN(n));
		}
		this.style.setActiveStyle(STYLE.POLYGON);

		this.style.draw(gc, list, tuple);

	}

	// public void addGeometry(Geometry geometry) {
	// synchronized (geometries) {
	// this.geometries.offer(geometry);
	// }
	// }

	// public void clean() {
	// LOG.debug("(CLEAN)Current Geometries:" + dataSets.size());
	// synchronized (dataSets) {
	// this.dataSets.clear();
	// }
	// }

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getComplexName() {
		// TODO Auto-generated method stub
		return this.name + " (" + sdfAttribute.getAttributeName() + ")";
	}

	public SDFAttribute getNativeAttribute() {
		return this.sdfAttribute;
	}

	@Override
	public String[] getSupprtedDatatypes() {
		return types;
	}

	@Override
	public int getTupleCount() {
		// TODO Auto-generated method stub
		return this.layerUpdater.getViewSize();
	}

	@Override
	public void setConfiguration(LayerConfiguration configuration) {
		if (configuration instanceof VectorLayerConfiguration) {
			setVectorLayerConfiguration((VectorLayerConfiguration) configuration);
		}
	}

	public void setVectorLayerConfiguration(VectorLayerConfiguration configuration) {
		this.configuration = configuration;
		this.name = configuration.getName();
		if (configuration.getStyle() != null)
			this.setStyle(Style.setStyleByConfig(configuration.getStyle()));
	}

	@Override
	public VectorLayerConfiguration getConfiguration() {
		if (this.style != null)
			this.configuration.setStyle(new PersistentStyle(getStyle()));
		return this.configuration;
	}

	@Override
	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return this.layerUpdater.getEnvelope(idx);
	}

	@Override
	public void setLayerUpdater(LayerUpdater layerUpdater) {
		this.layerUpdater = layerUpdater;

	}

}
