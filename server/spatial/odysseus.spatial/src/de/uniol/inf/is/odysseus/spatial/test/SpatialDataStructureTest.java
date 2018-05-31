package de.uniol.inf.is.odysseus.spatial.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.FastQuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.GeoHashSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.ISpatioTemporalDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.NaiveSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;

/**
 * Tests a spatial data structure.
 * 
 * Hint: Coordinates are Lat/Lng
 * 
 * @author Tobias Brandt
 *
 */
public class SpatialDataStructureTest {

	private static final String DATA_STRUCTURE_NAME = "test";
	private static final int GEOMETRY_POSITION = 0;

	private ISpatioTemporalDataStructure dataStructureNaive;
	private ISpatioTemporalDataStructure dataStructureFastQuadTree;
	private ISpatioTemporalDataStructure dataStructureGeoHash;
	private GeometryFactory factory;

	private TimeInterval testSearchInterval = new TimeInterval(new PointInTime(5000), new PointInTime(5100));
	private TimeInterval testWrongTime = new TimeInterval(new PointInTime(6000), new PointInTime(6100));

	// For kNN test
	private Point kNNTestCenter;
	private List<Point> neighbors;

	// For range test
	private Point rangeTestCenter;
	private List<Point> rangeNeighbors;
	private double range = 2100; // m

	// For bounding box test
	private List<Point> polygon;
	private List<Point> correctQueryResults;

	@Before
	public void setUp() {

		// Create the list for the kNN test
		neighbors = new ArrayList<Point>(5);

		// Create the list for the range test
		rangeNeighbors = new ArrayList<Point>();

		// Create the lists for the query bounding box test
		polygon = new ArrayList<>();
		correctQueryResults = new ArrayList<>();

		// Create the data structure that needs to be tested
		try {
			dataStructureNaive = new NaiveSTDataStructure(DATA_STRUCTURE_NAME, GEOMETRY_POSITION);
			dataStructureFastQuadTree = new FastQuadTreeSTDataStructure(DATA_STRUCTURE_NAME, GEOMETRY_POSITION);
			dataStructureGeoHash = new GeoHashSTDataStructure(DATA_STRUCTURE_NAME, GEOMETRY_POSITION);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		// Create test data
		factory = new GeometryFactory();
		fillCoordinateList();
	}

	private void fillCoordinateList() {
		fillNeighbourTestData();
		fillRangeTestData();
		fillQueryBoundingBoxTestData();
	}

	private Tuple<ITimeInterval> createTuple(Point point, long start, long end) {
		Tuple<ITimeInterval> tuple = new Tuple<ITimeInterval>(1, false);
		GeometryWrapper wrapper = new GeometryWrapper(point);
		tuple.setAttribute(GEOMETRY_POSITION, wrapper);

		ITimeInterval metadata = new TimeInterval(new PointInTime(start), new PointInTime(end));
		tuple.setMetadata(metadata);

		return tuple;
	}

	private void fillNeighbourTestData() {
		// The point for which we search the kNNs
		kNNTestCenter = factory.createPoint(new Coordinate(10.0, 10.0));

		// Add a point exactly at the center (should be in the results from a
		// distance point of view) but make it timely way before to test if it
		// gets removed by the data structure automatically
		Coordinate tooEarlyCoord = new Coordinate(10.0, 10.0);

		// Generate points which are the kNNs
		Coordinate coord1 = new Coordinate(10.0, 10.1);
		Coordinate coord2 = new Coordinate(10.1, 10.1);
		Coordinate coord3 = new Coordinate(10.2, 10.2);
		Coordinate coord4 = new Coordinate(10.3, 10.3);
		Coordinate coord5 = new Coordinate(10.4, 10.4);

		// Fill the extra list for the neighbors
		neighbors.add(factory.createPoint(coord1));
		neighbors.add(factory.createPoint(coord2));
		neighbors.add(factory.createPoint(coord3));
		neighbors.add(factory.createPoint(coord4));
		neighbors.add(factory.createPoint(coord5));

		// Add the coordinate which is timely before at the beginning
		Tuple<ITimeInterval> tuple = createTuple(factory.createPoint(tooEarlyCoord), 0, 10);

		// And add the neighbors to the normal list as well
		for (Point point : neighbors) {
			tuple = createTuple(point, testSearchInterval.getStart().getMainPoint(),
					testSearchInterval.getEnd().getMainPoint());
			addToAll(tuple);
		}
	}

	private void addToAll(Tuple<ITimeInterval> tuple) {
		dataStructureNaive.add(tuple);
		dataStructureFastQuadTree.add(tuple);
		dataStructureGeoHash.add(tuple);
	}

	private void fillRangeTestData() {
		// The center to search a certain range. This is the center of Oldenburg
		// downtown.
		rangeTestCenter = factory.createPoint(new Coordinate(53.140206, 8.213074));

		// Generate points which are in the range
		// Coordinates captured with http://www.gps-coordinates.net/
		// Distances calculated with
		// http://www.sunearthtools.com/tools/distance.php
		Coordinate coord1 = new Coordinate(53.14, 8.215091); // about 136.5m
		Coordinate coord2 = new Coordinate(53.140284, 8.217452); // about 292.2m
		Coordinate coord3 = new Coordinate(53.139872, 8.220799); // about 516.8m
		Coordinate coord4 = new Coordinate(53.140052, 8.224275); // about 747.5m
		Coordinate coord5 = new Coordinate(53.14188,
				8.243115); /* about 2.0129km */
		// Huntebruecke, about 4.3577 km
		Coordinate coord6 = new Coordinate(53.153488, 8.274529);
		// Elsfleth, about 19.5791 km
		Coordinate coord7 = new Coordinate(53.238099, 8.457241);

		// Fill the extra list for the correct range result (as we do a 2.1 km
		// range, we do not include the Huntebruecke and Elsfleth coordinates)
		rangeNeighbors.add(factory.createPoint(coord1));
		rangeNeighbors.add(factory.createPoint(coord3));
		rangeNeighbors.add(factory.createPoint(coord4));
		rangeNeighbors.add(factory.createPoint(coord5));

		// And add the range neighbors to the normal list as well
		for (Point point : rangeNeighbors) {
			Tuple<ITimeInterval> tuple = createTuple(point, testSearchInterval.getStart().getMainPoint(),
					testSearchInterval.getEnd().getMainPoint());
			addToAll(tuple);
		}

		// Add the coordinates which are not within the correct result

		// Huntebruecke (too far away)
		Tuple<ITimeInterval> tuple = createTuple(factory.createPoint(coord6),
				testSearchInterval.getStart().getMainPoint(), testSearchInterval.getEnd().getMainPoint());
		addToAll(tuple);

		// Elsfleth (too far away)
		tuple = createTuple(factory.createPoint(coord7), testSearchInterval.getStart().getMainPoint(),
				testSearchInterval.getEnd().getMainPoint());
		addToAll(tuple);

		// Coord 2 (in range, but wrong time)
		tuple = createTuple(factory.createPoint(coord2), testWrongTime.getStart().getMainPoint(),
				testWrongTime.getEnd().getMainPoint());
		addToAll(tuple);
	}

	private void fillQueryBoundingBoxTestData() {

		/*
		 * Polygon for Oldenburg university main campus, created with
		 * http://www.gmapgis.com/ 8.17698,53.14588,0.0 8.1778,53.14731,0.0
		 * 8.17795,53.1477,0.0 8.18007,53.14886,0.0 8.18078,53.14941,0.0
		 * 8.18267,53.1484,0.0 8.18666,53.14632,0.0 8.18569,53.14512,0.0
		 */

		polygon.add(factory.createPoint(new Coordinate(53.14588, 8.17698)));
		polygon.add(factory.createPoint(new Coordinate(53.14731, 8.1778)));
		polygon.add(factory.createPoint(new Coordinate(53.1477, 8.17795)));
		polygon.add(factory.createPoint(new Coordinate(53.14886, 8.18007)));
		polygon.add(factory.createPoint(new Coordinate(53.14941, 8.18078)));
		polygon.add(factory.createPoint(new Coordinate(53.1484, 8.18267)));
		polygon.add(factory.createPoint(new Coordinate(53.14632, 8.18666)));
		polygon.add(factory.createPoint(new Coordinate(53.14512, 8.18569)));

		// Points within that polygon
		Coordinate coord1 = new Coordinate(53.147491, 8.18217); // A14
		Coordinate coord2 = new Coordinate(53.147118, 8.181505); // A1 - A4
		Coordinate coord3 = new Coordinate(53.147568, 8.179927); // Bib
		Coordinate coord4 = new Coordinate(53.146642, 8.178651); // Gym

		// Points that a slightly outside of the polygon
		Coordinate noCoord1 = new Coordinate(53.149711, 8.181891); // Combi
		Coordinate noCoord2 = new Coordinate(53.150425, 8.180614); // V-buildings

		// Add the correct points to the compare list
		correctQueryResults.add(factory.createPoint(coord1));
		correctQueryResults.add(factory.createPoint(coord2));
		correctQueryResults.add(factory.createPoint(coord3));

		// Add correct result points to the whole data set
		for (Point point : correctQueryResults) {
			Tuple<ITimeInterval> tuple = createTuple(point, testSearchInterval.getStart().getMainPoint(),
					testSearchInterval.getEnd().getMainPoint());
			addToAll(tuple);
		}

		// Add wrong point to total data set
		
		// Combi (too far away)
		Tuple<ITimeInterval> tuple = createTuple(factory.createPoint(noCoord1),
				testSearchInterval.getStart().getMainPoint(), testSearchInterval.getEnd().getMainPoint());
		addToAll(tuple);
		
		// V-buildings (too far away)
		tuple = createTuple(factory.createPoint(noCoord2), testSearchInterval.getStart().getMainPoint(),
				testSearchInterval.getEnd().getMainPoint());
		addToAll(tuple);
		
		// Gym (wrong time)
		tuple = createTuple(factory.createPoint(coord4), testWrongTime.getStart().getMainPoint(),
				testWrongTime.getEnd().getMainPoint());
		addToAll(tuple);
	}

	/**
	 * Tests whether the kNN method from the data structure works correctly. We
	 * know the kNNs for the 5 nearest points.
	 */
	@Test
	public void testkNNNaive() {
		int k = neighbors.size();
		List<Tuple<ITimeInterval>> kNN = dataStructureNaive.queryKNN(kNNTestCenter, k, testSearchInterval);
		assertEqualList(kNN, neighbors);
	}

	/**
	 * Tests whether the kNN method from the data structure works correctly. We
	 * know the kNNs for the 5 nearest points.
	 */
	@Test
	public void testkNNFastQuadTree() {
		int k = neighbors.size();
		List<Tuple<ITimeInterval>> kNN = dataStructureFastQuadTree.queryKNN(kNNTestCenter, k, testSearchInterval);
		assertEqualList(kNN, neighbors);
	}

	/**
	 * Tests whether the kNN method from the data structure works correctly. We
	 * know the kNNs for the 5 nearest points.
	 */
	@Test
	public void testkNNGeoHash() {
		int k = neighbors.size();
		List<Tuple<ITimeInterval>> kNN = dataStructureGeoHash.queryKNN(kNNTestCenter, k, testSearchInterval);
		assertEqualList(kNN, neighbors);
	}

	/**
	 * Tests whether the range method from the data structure works correctly.
	 * We know the neighbors in 5,000m range
	 */
	@Test
	public void testRangeNaive() {
		List<Tuple<ITimeInterval>> rangeResult = dataStructureNaive.queryCircle(rangeTestCenter, range,
				testSearchInterval);
		assertEqualList(rangeResult, rangeNeighbors);
	}

	/**
	 * Tests whether the range method from the data structure works correctly.
	 * We know the neighbors in 5,000m range
	 */
	@Test
	public void testRangeFastQuadTree() {
		List<Tuple<ITimeInterval>> rangeResult = dataStructureFastQuadTree.queryCircle(rangeTestCenter, range,
				testSearchInterval);
		assertEqualList(rangeResult, rangeNeighbors);
	}

	/**
	 * Tests whether the range method from the data structure works correctly.
	 * We know the neighbors in 5,000m range
	 */
	@Test
	public void testRangeGeoHash() {
		List<Tuple<ITimeInterval>> rangeResult = dataStructureGeoHash.queryCircle(rangeTestCenter, range,
				testSearchInterval);
		assertEqualList(rangeResult, rangeNeighbors);
	}

	@Test
	public void testBoundingBoxQueryNaive() {
		List<Tuple<ITimeInterval>> boundingBoxResult = dataStructureNaive.queryBoundingBox(polygon, testSearchInterval);
		assertEqualList(boundingBoxResult, correctQueryResults);
	}

	@Test
	public void testBoundingBoxQueryFastQuadTree() {
		List<Tuple<ITimeInterval>> boundingBoxResult = dataStructureFastQuadTree.queryBoundingBox(polygon,
				testSearchInterval);
		assertEqualList(boundingBoxResult, correctQueryResults);
	}

	@Test
	public void testBoundingBoxQueryGeoHash() {
		List<Tuple<ITimeInterval>> boundingBoxResult = dataStructureGeoHash.queryBoundingBox(polygon,
				testSearchInterval);
		assertEqualList(boundingBoxResult, correctQueryResults);
	}

	/**
	 * Tests if the two list are "equal". Therefore, the length is tested and it
	 * is tested if the points in the result list (the tuples) are on the same
	 * position as the points in the correct solution. The order does not need
	 * to be the same.
	 * 
	 * @param queryResult
	 *            The result of the query from the data structure
	 * @param correctSolution
	 *            The points which we know are correct
	 */
	@Test
	private void assertEqualList(List<Tuple<ITimeInterval>> queryResult, List<Point> correctSolution) {
		// Copy the list with the original neighbors that need to be found
		List<Point> correctSolutionCopy = new ArrayList<>(correctSolution);

		// Both lists need to have the same size
		assertEquals(correctSolutionCopy.size(), queryResult.size());

		// The lists need to be the same (but the order can be different)
		for (int i = 0; i < queryResult.size(); i++) {

			// The neighbor that was found
			Tuple<?> tuple = queryResult.get(i);
			GeometryWrapper geometry = tuple.getAttribute(GEOMETRY_POSITION);

			// Search, if there is this geometry in the list with the correct
			// neighbors
			boolean found = false;
			for (Point point : correctSolutionCopy) {
				if (point.equals(geometry.getGeometry())) {
					// We found the corresponding neighbor
					found = true;
					break;
				}
			}

			if (found) {
				// Remove the element from the list
				correctSolutionCopy.remove(geometry.getGeometry());
			}

			// Was the element found (if not, it is not a correct neighbor)
			assertTrue(found);

		}

		// Now all elements should have been found. The copy of the neighbors
		// list should be empty
		assertEquals(correctSolutionCopy.size(), 0);
	}

}
