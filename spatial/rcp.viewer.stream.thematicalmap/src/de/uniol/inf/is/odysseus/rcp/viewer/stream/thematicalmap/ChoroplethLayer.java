package de.uniol.inf.is.odysseus.rcp.viewer.stream.thematicalmap;

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
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.AbstractLayer;

public class ChoroplethLayer extends AbstractLayer {
	// private static final Logger LOG =
	// LoggerFactory.getLogger(ChoroplethLayer.class);
	private LinkedList<GeometryAndValue> geometryAndValueList = null;
	private ScreenTransformation transformation = null;
	private SDFAttribute sdfAttribute = null;
	private Legend legend = null;

	private int geoStreamPosition;
	private int valueStreamPosition;

	public int getGeoStreamPosition() {
		return geoStreamPosition;
	}

	public int getValueStreamPosition() {
		return valueStreamPosition;
	}

	public ChoroplethLayer(ScreenTransformation transformation,
			SDFAttribute sdfAttribute, int geoStreamPosition,
			int valueStreamPosition) {
		// FIXME:
		super(null);
		this.geometryAndValueList = new LinkedList<GeometryAndValue>();
		this.transformation = transformation;
		this.sdfAttribute = sdfAttribute;
		this.name = sdfAttribute.getAttributeName();
		this.geoStreamPosition = geoStreamPosition;
		this.valueStreamPosition = valueStreamPosition;
		this.legend = new Legend();
	}

	public ChoroplethLayer(String name, ScreenTransformation transformation,
			SDFAttribute sdfAttribute, int geoStreamPosition,
			int valueStreamPosition) {
		this(transformation, sdfAttribute, geoStreamPosition,
				valueStreamPosition);
		this.name = name;
	}

	public void draw(GC gc) {
		synchronized (geometryAndValueList) {
			for (GeometryAndValue geometryAndValue : geometryAndValueList) {
				drawGeometryAndValue(geometryAndValue, gc);
			}
		}
	}

	private void drawGeometryAndValue(GeometryAndValue geometryAndValue, GC gc) {
		if (geometryAndValue.getGeometry() instanceof Polygon) {
			drawPolygonAndValue((Polygon) geometryAndValue.getGeometry(),
					geometryAndValue.getValue(), gc);
		} else if (geometryAndValue.getGeometry() instanceof GeometryCollection) {
			drawGeometryCollectionAndValue(
					(GeometryCollection) geometryAndValue.getGeometry(),
					geometryAndValue.getValue(), gc);
		}
	}

	private void drawGeometryCollectionAndValue(
			GeometryCollection geometryCollection, Integer value, GC gc) {
		for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
			drawGeometryAndValue(
					new GeometryAndValue(geometryCollection.getGeometryN(i),
							value), gc);
		}

	}

	private void drawPolygonAndValue(Polygon polygon, Integer value, GC gc) {
		int[][] list = new int[polygon.getNumInteriorRing() + 1][];
		list[0] = drawLinearRing(polygon.getExteriorRing(), gc);
		for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
			list[n + 1] = drawLinearRing(polygon.getInteriorRingN(n), gc);
		}
		// this.style.setActiveStyle(polygon);
		// this.style.draw(gc, list);
		legend.getStyleForValue(value).draw(gc, list);

	}

	private int[] drawLinearRing(LineString lineString, GC gc) {
		int i = 0;
		int[] path = new int[lineString.getNumPoints()
				+ lineString.getNumPoints()];
		for (Coordinate coord : lineString.getCoordinates()) {
			int[] uv = transformation.transformCoord(coord,
					lineString.getSRID());
			path[i++] = uv[0];
			path[i++] = uv[1];
		}
		return path;
	}

	public void addGeometryAndValue(Geometry geometry, Integer value) {
		synchronized (geometryAndValueList) {
			this.geometryAndValueList.offer(new GeometryAndValue(geometry,
					value));
		}
	}

	public SDFAttribute getNativeAttribute() {
		return this.sdfAttribute;
	}

	class GeometryAndValue {
		private Geometry geometry;
		private Integer value;

		public GeometryAndValue(Geometry geometry, Integer value) {
			super();
			this.geometry = geometry;
			this.value = value;
		}

		public Geometry getGeometry() {
			return geometry;
		}

		public void setGeometry(Geometry geometry) {
			this.geometry = geometry;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema,
			SDFAttribute attribute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getSupprtedDatatypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTuple(Tuple<?> tuple) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTupleCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
