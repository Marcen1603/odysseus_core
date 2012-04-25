

/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.vividsolutions.com
 */
package com.vividsolutions.jts.geom;

import java.util.*;
import java.io.Serializable;
import com.vividsolutions.jts.geom.impl.*;
import com.vividsolutions.jts.geom.util.*;
import com.vividsolutions.jts.util.Assert;

/**
 * Supplies a set of utility methods for building Geometry objects from lists
 * of Coordinates.
 * <p>
 * Note that the factory constructor methods do <b>not</b> change the input coordinates in any way.
 * In particular, they are not rounded to the supplied <tt>PrecisionModel</tt>.
 * It is assumed that input Coordinates meet the given precision.
 *
 *
 * @version 1.7
 */
public class GeometryFactory
    implements Serializable
{
  private static final long serialVersionUID = -6820524753094095635L;
  private PrecisionModel precisionModel;

  private CoordinateSequenceFactory coordinateSequenceFactory;


  public static Point createPointFromInternalCoord(Coordinate coord, Geometry exemplar)
  {
    exemplar.getPrecisionModel().makePrecise(coord);
    return exemplar.getFactory().createPoint(coord);
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having the given
   * PrecisionModel, spatial-reference ID, and CoordinateSequence implementation.
   */
  public GeometryFactory(PrecisionModel precisionModel, int SRID,
                         CoordinateSequenceFactory coordinateSequenceFactory) {
      this.precisionModel = precisionModel;
      this.coordinateSequenceFactory = coordinateSequenceFactory;
      this.SRID = SRID;
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having the given
   * CoordinateSequence implementation, a double-precision floating PrecisionModel and a
   * spatial-reference ID of 0.
   */
  public GeometryFactory(CoordinateSequenceFactory coordinateSequenceFactory) {
    this(new PrecisionModel(), 0, coordinateSequenceFactory);
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having the given
   * {@link PrecisionModel} and the default CoordinateSequence
   * implementation.
   *
   * @param precisionModel the PrecisionModel to use
   */
  public GeometryFactory(PrecisionModel precisionModel) {
    this(precisionModel, 0, getDefaultCoordinateSequenceFactory());
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having the given
   * {@link PrecisionModel} and spatial-reference ID, and the default CoordinateSequence
   * implementation.
   *
   * @param precisionModel the PrecisionModel to use
   * @param SRID the SRID to use
   */
  public GeometryFactory(PrecisionModel precisionModel, int SRID) {
    this(precisionModel, SRID, getDefaultCoordinateSequenceFactory());
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having a floating
   * PrecisionModel and a spatial-reference ID of 0.
   */
  public GeometryFactory() {
    this(new PrecisionModel(), 0);
  }

  private static CoordinateSequenceFactory getDefaultCoordinateSequenceFactory()
  {
    return CoordinateArraySequenceFactory.instance();
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  points  the <code>List</code> of Points to convert
   *@return         the <code>List</code> in array format
   */
  public static Point[] toPointArray(Collection points) {
    Point[] pointArray = new Point[points.size()];
    return (Point[]) points.toArray(pointArray);
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  geometries  the list of <code>Geometry's</code> to convert
   *@return            the <code>List</code> in array format
   */
  public static Geometry[] toGeometryArray(Collection geometries) {
    if (geometries == null) return null;
    Geometry[] geometryArray = new Geometry[geometries.size()];
    return (Geometry[]) geometries.toArray(geometryArray);
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  linearRings  the <code>List</code> of LinearRings to convert
   *@return              the <code>List</code> in array format
   */
  public static LinearRing[] toLinearRingArray(Collection linearRings) {
    LinearRing[] linearRingArray = new LinearRing[linearRings.size()];
    return (LinearRing[]) linearRings.toArray(linearRingArray);
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  lineStrings  the <code>List</code> of LineStrings to convert
   *@return              the <code>List</code> in array format
   */
  public static LineString[] toLineStringArray(Collection lineStrings) {
    LineString[] lineStringArray = new LineString[lineStrings.size()];
    return (LineString[]) lineStrings.toArray(lineStringArray);
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  polygons  the <code>List</code> of Polygons to convert
   *@return           the <code>List</code> in array format
   */
  public static Polygon[] toPolygonArray(Collection polygons) {
    Polygon[] polygonArray = new Polygon[polygons.size()];
    return (Polygon[]) polygons.toArray(polygonArray);
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  multiPolygons  the <code>List</code> of MultiPolygons to convert
   *@return                the <code>List</code> in array format
   */
  public static MultiPolygon[] toMultiPolygonArray(Collection multiPolygons) {
    MultiPolygon[] multiPolygonArray = new MultiPolygon[multiPolygons.size()];
    return (MultiPolygon[]) multiPolygons.toArray(multiPolygonArray);
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  multiLineStrings  the <code>List</code> of MultiLineStrings to convert
   *@return                   the <code>List</code> in array format
   */
  public static MultiLineString[] toMultiLineStringArray(Collection multiLineStrings) {
    MultiLineString[] multiLineStringArray = new MultiLineString[multiLineStrings.size()];
    return (MultiLineString[]) multiLineStrings.toArray(multiLineStringArray);
  }

  /**
   *  Converts the <code>List</code> to an array.
   *
   *@param  multiPoints  the <code>List</code> of MultiPoints to convert
   *@return              the <code>List</code> in array format
   */
  public static MultiPoint[] toMultiPointArray(Collection multiPoints) {
    MultiPoint[] multiPointArray = new MultiPoint[multiPoints.size()];
    return (MultiPoint[]) multiPoints.toArray(multiPointArray);
  }

  /**
   *  If the <code>Envelope</code> is a null <code>Envelope</code>, returns an
   *  empty <code>Point</code>. If the <code>Envelope</code> is a point, returns
   *  a non-empty <code>Point</code>. If the <code>Envelope</code> is a
   *  rectangle, returns a <code>Polygon</code> whose points are (minx, miny),
   *  (maxx, miny), (maxx, maxy), (minx, maxy), (minx, miny).
   *
   *@param  envelope        the <code>Envelope</code> to convert to a <code>Geometry</code>
   *@param  precisionModel  the specification of the grid of allowable points
   *      for the new <code>Geometry</code>
   *@param  SRID            the ID of the Spatial Reference System used by the
   *      <code>Envelope</code>
   *@return                 an empty <code>Point</code> (for null <code>Envelope</code>
   *      s), a <code>Point</code> (when min x = max x and min y = max y) or a
   *      <code>Polygon</code> (in all other cases)
   *@throws  <code>         TopologyException</code> if <code>coordinates</code>
   *      is not a closed linestring, that is, if the first and last coordinates
   *      are not equal
   */
  public Geometry toGeometry(Envelope envelope) {
    if (envelope.isNull()) {
      return createPoint((CoordinateSequence)null);
    }
    if (envelope.getMinX() == envelope.getMaxX() && envelope.getMinY() == envelope.getMaxY()) {
      return createPoint(new Coordinate(envelope.getMinX(), envelope.getMinY()));
    }
    return createPolygon(createLinearRing(new Coordinate[]{
        new Coordinate(envelope.getMinX(), envelope.getMinY()),
        new Coordinate(envelope.getMaxX(), envelope.getMinY()),
        new Coordinate(envelope.getMaxX(), envelope.getMaxY()),
        new Coordinate(envelope.getMinX(), envelope.getMaxY()),
        new Coordinate(envelope.getMinX(), envelope.getMinY())
        }), null);
  }

  /**
   * Returns the PrecisionModel that Geometries created by this factory
   * will be associated with.
   */
  public PrecisionModel getPrecisionModel() {
    return precisionModel;
  }

  /**
   * Creates a Point using the given Coordinate; a null Coordinate will create
   * an empty Geometry.
   */
  public Point createPoint(Coordinate coordinate) {
    return createPoint(coordinate != null ? getCoordinateSequenceFactory().create(new Coordinate[]{coordinate}) : null);
  }

  /**
   * Creates a Point using the given CoordinateSequence; a null or empty
   * CoordinateSequence will create an empty Point.
   */
  public Point createPoint(CoordinateSequence coordinates) {
  	return new Point(coordinates, this);
  }

  /**
   * Creates a MultiLineString using the given LineStrings; a null or empty
   * array will create an empty MultiLineString.
   * @param lineStrings LineStrings, each of which may be empty but not null
   */
  public MultiLineString createMultiLineString(LineString[] lineStrings) {
  	return new MultiLineString(lineStrings, this);
  }

  /**
   * Creates a GeometryCollection using the given Geometries; a null or empty
   * array will create an empty GeometryCollection.
   * @param geometries Geometries, each of which may be empty but not null
   */
  public GeometryCollection createGeometryCollection(Geometry[] geometries) {
  	return new GeometryCollection(geometries, this);
  }



  /**
   * Creates a MultiPolygon using the given Polygons; a null or empty array
   * will create an empty Polygon. The polygons must conform to the
   * assertions specified in the <A
   * HREF="http://www.opengis.org/techno/specs.htm">OpenGIS Simple Features
   * Specification for SQL</A>.
   *
   * @param polygons
   *            Polygons, each of which may be empty but not null
   */
  public MultiPolygon createMultiPolygon(Polygon[] polygons) {
    return new MultiPolygon(polygons, this);
  }

  /**
   * Creates a LinearRing using the given Coordinates; a null or empty array will
   * create an empty LinearRing. The points must form a closed and simple
   * linestring. Consecutive points must not be equal.
   * @param coordinates an array without null elements, or an empty array, or null
   */
  public LinearRing createLinearRing(Coordinate[] coordinates) {
    return createLinearRing(coordinates != null ? getCoordinateSequenceFactory().create(coordinates) : null);
  }

  /**
   * Creates a LinearRing using the given CoordinateSequence; a null or empty CoordinateSequence will
   * create an empty LinearRing. The points must form a closed and simple
   * linestring. Consecutive points must not be equal.
   * @param coordinates a CoordinateSequence possibly empty, or null
   */
  public LinearRing createLinearRing(CoordinateSequence coordinates) {
    return new LinearRing(coordinates, this);
  }

  /**
   * Creates a MultiPoint using the given Points.
   * A null or empty array will create an empty MultiPoint.
   *
   * @param coordinates an array (without null elements), or an empty array, or <code>null</code>
   * @return a MultiPoint object
   */
  public MultiPoint createMultiPoint(Point[] point) {
  	return new MultiPoint(point, this);
  }

  /**
   * Creates a {@link MultiPoint} using the given {@link Coordinate}s.
   * A null or empty array will create an empty MultiPoint.
   *
   * @param coordinates an array (without null elements), or an empty array, or <code>null</code>
   * @return a MultiPoint object
   */
  public MultiPoint createMultiPoint(Coordinate[] coordinates) {
      return createMultiPoint(coordinates != null
                              ? getCoordinateSequenceFactory().create(coordinates)
                              : null);
  }

  /**
   * Creates a MultiPoint using the given CoordinateSequence.
   * A a null or empty CoordinateSequence will create an empty MultiPoint.
   *
   * @param coordinates a CoordinateSequence (possibly empty), or <code>null</code>
   * @return a MultiPoint object
   */
  public MultiPoint createMultiPoint(CoordinateSequence coordinates) {
    if (coordinates == null) {
      return createMultiPoint(new Point[0]);
    }
    Point[] points = new Point[coordinates.size()];
    for (int i = 0; i < coordinates.size(); i++) {
      points[i] = createPoint(coordinates.getCoordinate(i));
    }
    return createMultiPoint(points);
  }

  /**
   * Constructs a <code>Polygon</code> with the given exterior boundary and
   * interior boundaries.
   *
   * @param shell
   *            the outer boundary of the new <code>Polygon</code>, or
   *            <code>null</code> or an empty <code>LinearRing</code> if
   *            the empty geometry is to be created.
   * @param holes
   *            the inner boundaries of the new <code>Polygon</code>, or
   *            <code>null</code> or empty <code>LinearRing</code> s if
   *            the empty geometry is to be created.
   */
  public Polygon createPolygon(LinearRing shell, LinearRing[] holes) {
    return new Polygon(shell, holes, this);
  }

  /**
   *  Build an appropriate <code>Geometry</code>, <code>MultiGeometry</code>, or
   *  <code>GeometryCollection</code> to contain the <code>Geometry</code>s in
   *  it.
   * For example:<br>
   *
   *  <ul>
   *    <li> If <code>geomList</code> contains a single <code>Polygon</code>,
   *    the <code>Polygon</code> is returned.
   *    <li> If <code>geomList</code> contains several <code>Polygon</code>s, a
   *    <code>MultiPolygon</code> is returned.
   *    <li> If <code>geomList</code> contains some <code>Polygon</code>s and
   *    some <code>LineString</code>s, a <code>GeometryCollection</code> is
   *    returned.
   *    <li> If <code>geomList</code> is empty, an empty <code>GeometryCollection</code>
   *    is returned
   *  </ul>
   *
   * Note that this method does not "flatten" Geometries in the input, and hence if
   * any MultiGeometries are contained in the input a GeometryCollection containing
   * them will be returned.
   *
   *@param  geomList  the <code>Geometry</code>s to combine
   *@return           a <code>Geometry</code> of the "smallest", "most
   *      type-specific" class that can contain the elements of <code>geomList</code>
   *      .
   */
  public Geometry buildGeometry(Collection geomList) {
    Class geomClass = null;
    boolean isHeterogeneous = false;
    boolean hasGeometryCollection = false;
    for (Iterator i = geomList.iterator(); i.hasNext(); ) {
      Geometry geom = (Geometry) i.next();
      Class partClass = geom.getClass();
      if (geomClass == null) {
        geomClass = partClass;
      }
      if (partClass != geomClass) {
        isHeterogeneous = true;
      }
      if (geom instanceof GeometryCollection)
        hasGeometryCollection = true;
    }
    // for the empty geometry, return an empty GeometryCollection
    if (geomClass == null) {
      return createGeometryCollection(null);
    }
    if (isHeterogeneous || hasGeometryCollection) {
      return createGeometryCollection(toGeometryArray(geomList));
    }
    // at this point we know the collection is hetereogenous.
    // Determine the type of the result from the first Geometry in the list
    // this should always return a geometry, since otherwise an empty collection would have already been returned
    Geometry geom0 = (Geometry) geomList.iterator().next();
    boolean isCollection = geomList.size() > 1;
    if (isCollection) {
      if (geom0 instanceof Polygon) {
        return createMultiPolygon(toPolygonArray(geomList));
      }
      else if (geom0 instanceof LineString) {
        return createMultiLineString(toLineStringArray(geomList));
      }
      else if (geom0 instanceof Point) {
        return createMultiPoint(toPointArray(geomList));
      }
      Assert.shouldNeverReachHere("Unhandled class: " + geom0.getClass().getName());
    }
    return geom0;
  }

  /**
   * Creates a LineString using the given Coordinates; a null or empty array will
   * create an empty LineString. Consecutive points must not be equal.
   * @param coordinates an array without null elements, or an empty array, or null
   */
  public LineString createLineString(Coordinate[] coordinates) {
    return createLineString(coordinates != null ? getCoordinateSequenceFactory().create(coordinates) : null);
  }
  /**
   * Creates a LineString using the given CoordinateSequence; a null or empty CoordinateSequence will
   * create an empty LineString. Consecutive points must not be equal.
   * @param coordinates a CoordinateSequence possibly empty, or null
   */
  public LineString createLineString(CoordinateSequence coordinates) {
	return new LineString(coordinates, this);
  }

  /**
   * @return a clone of g based on a CoordinateSequence created by this
   * GeometryFactory's CoordinateSequenceFactory
   */
  public Geometry createGeometry(Geometry g)
  {
    // could this be cached to make this more efficient? Or maybe it isn't enough overhead to bother
    GeometryEditor editor = new GeometryEditor(this);
    return editor.edit(g, new GeometryEditor.CoordinateOperation() {
      public Coordinate[] edit(Coordinate[] coordinates, Geometry geometry) {
                  return coordinates;
          }
    });
  }


  public int getSRID() {
    return SRID;
  }

  private int SRID;

  public CoordinateSequenceFactory getCoordinateSequenceFactory() {
    return coordinateSequenceFactory;
  }

}

